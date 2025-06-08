package com.example.appoidochef.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para visualizar los pedidos activos (no cerrados) del sistema.
 * Se conecta al backend mediante Retrofit para obtener la lista de pedidos.
 */
public class VerPedidosActivity extends AppCompatActivity {

    // Componentes de la UI
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);

        // Configuración inicial del RecyclerView
        configurarRecyclerView();

        // Carga los pedidos activos del servidor
        cargarPedidosActivos();
    }

    /**
     * Configura el RecyclerView con su LayoutManager y Adapter.
     */
    private void configurarRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewPedidos);

        // Usa LinearLayoutManager para disposición vertical de items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crea e asigna el adaptador personalizado
        adapter = new PedidosAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * Realiza la petición al servidor para obtener los pedidos activos.
     * Solo carga pedidos con estado no cerrado (activos).
     */
    private void cargarPedidosActivos() {
        // Crea instancia del servicio API usando Retrofit
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Prepara la llamada para obtener pedidos activos
        Call<List<PedidoAPI>> call = apiService.obtenerPedidosActivos();

        // Ejecuta la llamada asíncrona
        call.enqueue(new Callback<List<PedidoAPI>>() {
            @Override
            public void onResponse(Call<List<PedidoAPI>> call, Response<List<PedidoAPI>> response) {
                // Maneja respuesta exitosa del servidor
                if (response.isSuccessful() && response.body() != null) {
                    // Actualiza el adaptador con los nuevos datos
                    adapter.setPedidos(response.body());
                } else {
                    // Muestra error si la respuesta no contiene datos
                    mostrarError("Error al obtener pedidos");
                    Log.e("VerPedidos", "Respuesta no exitosa o vacía");
                }
            }

            @Override
            public void onFailure(Call<List<PedidoAPI>> call, Throwable t) {
                // Maneja errores de conexión
                mostrarError("Error de red: " + t.getMessage());
                Log.e("VerPedidos", "Error al conectar con el servidor", t);
            }
        });
    }

    /**
     * Muestra un mensaje de error al usuario mediante Toast.
     * @param mensaje Texto del error a mostrar
     */
    private void mostrarError(String mensaje) {
        Toast.makeText(VerPedidosActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
}