package com.example.appoidochef.ui.main;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.MesaAPI;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;
import com.example.appoidochef.data.websocket.WebSocketManager;
import com.example.appoidochef.data.websocket.WebSocketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "canal_pedidos";
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private RecyclerView recyclerViewMesas;
    private MesaAdapter mesaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        iniciarServicioWebSocket();
                    } else {
                        Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        checkAndRequestNotificationPermission();

        recyclerViewMesas = findViewById(R.id.recyclerViewMesas);
        recyclerViewMesas.setLayoutManager(new GridLayoutManager(this, 2));

        cargarMesas();
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                iniciarServicioWebSocket();
            }
        } else {
            iniciarServicioWebSocket();
        }
    }
    private void iniciarServicioWebSocket() {
        crearCanalNotificaciones();

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        WebSocketManager.getInstance().init(getApplicationContext());
        WebSocketManager.getInstance().connect();

        // ✅ REGISTRA EL LISTENER
        WebSocketManager.getInstance().addListener(this::handleIncomingMessage);
    }


    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Pedidos Cocina",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notificaciones de estado de pedidos");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void cargarMesas() {
        ApiService apiService = ApiClient.getApiService();
        apiService.obtenerMesas().enqueue(new Callback<List<MesaAPI>>() {
            @Override
            public void onResponse(Call<List<MesaAPI>> call, Response<List<MesaAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mesaAdapter = new MesaAdapter(MainActivity.this, response.body(), mesaSeleccionada -> {
                        Intent intent = new Intent(MainActivity.this, ProductoActivity.class);
                        intent.putExtra("mesaId", mesaSeleccionada.getId());
                        intent.putExtra("mesaNombre", String.valueOf(mesaSeleccionada.getNumero()));
                        startActivity(intent);
                    });
                    recyclerViewMesas.setAdapter(mesaAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar mesas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MesaAPI>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNotificacionProductoListo(JSONObject json) {
        String nombre = json.optString("nombre", "Producto");
        int cantidad = json.optInt("cantidad", 1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.cocinalista)
                .setContentTitle("Producto Listo")
                .setContentText(nombre + " - Cantidad: " + cantidad)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter("MENSAJE_WEBSOCKET"), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, new IntentFilter("MENSAJE_WEBSOCKET"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mensaje = intent.getStringExtra("mensaje");
            handleIncomingMessage(mensaje); // ya tienes esta función
        }
    };


    private void handleIncomingMessage(String message) {
        runOnUiThread(() -> {
            try {
                JSONObject json = new JSONObject(message);
                String tipo = json.optString("tipo", "");

                if ("productoListo".equals(tipo)) {
                    mostrarNotificacionProductoListo(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}