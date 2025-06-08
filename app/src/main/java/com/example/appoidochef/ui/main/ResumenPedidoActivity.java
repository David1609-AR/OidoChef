package com.example.appoidochef.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.Mesa;
import com.example.appoidochef.data.model.MesaAPI;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;
import com.example.appoidochef.util.CarritoManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad que muestra un resumen del pedido antes de confirmarlo.
 * Permite visualizar los items del pedido y enviarlos al servidor.
 */
public class ResumenPedidoActivity extends AppCompatActivity {

    // Views
    private TextView textMesaResumen;
    private RecyclerView recyclerResumen;
    private Button btnConfirmar;

    // Datos
    private Mesa mesa;
    private List<ItemPedidoAPI> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_pedido);

        // Inicialización de vistas
        inicializarVistas();

        // Obtener datos del intent
        obtenerDatosIntent();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar botón de confirmación
        configurarBotonConfirmar();
    }

    /**
     * Vincula las vistas con sus componentes en el layout
     */
    private void inicializarVistas() {
        textMesaResumen = findViewById(R.id.textMesaResumen);
        recyclerResumen = findViewById(R.id.recyclerResumen);
        btnConfirmar = findViewById(R.id.btnConfirmar);
    }

    /**
     * Obtiene los datos pasados a través del Intent
     */
    private void obtenerDatosIntent() {
        mesa = (Mesa) getIntent().getSerializableExtra("mesa");
        items = (ArrayList<ItemPedidoAPI>) getIntent().getSerializableExtra("items");

        // Mostrar número de mesa
        textMesaResumen.setText("Resumen de: Mesa " + mesa.getNumeroMesa());
    }

    /**
     * Configura el RecyclerView con su adaptador
     */
    private void configurarRecyclerView() {
        recyclerResumen.setLayoutManager(new LinearLayoutManager(this));
        recyclerResumen.setAdapter(new ResumenPedidoAdapter(items));
    }

    /**
     * Configura el comportamiento del botón de confirmación
     */
    private void configurarBotonConfirmar() {
        btnConfirmar.setOnClickListener(v -> confirmarPedido());
    }

    /**
     * Prepara y envía el pedido al servidor
     */
    private void confirmarPedido() {
        // Crear objeto PedidoAPI con los datos necesarios
        PedidoAPI pedido = crearPedido();

        // Obtener instancia del servicio API
        ApiService apiService = ApiClient.getApiService();

        // Realizar llamada al servidor
        apiService.crearPedido(pedido).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    manejarExitoEnvio();
                } else {
                    manejarErrorServidor();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                manejarErrorRed(t);
            }
        });
    }

    /**
     * Crea el objeto PedidoAPI con los datos de la mesa y items
     */
    private PedidoAPI crearPedido() {
        PedidoAPI pedido = new PedidoAPI();
        pedido.setNumeroMesa(mesa.getNumeroMesa());
        pedido.setMesaId(mesa.getIdMesa());
        pedido.setMesa(new MesaAPI(
                mesa.getIdMesa(),
                mesa.getNumeroMesa(),
                mesa.isBloqueada(),
                mesa.isOcupada()
        ));
        pedido.setItems(items);
        return pedido;
    }

    /**
     * Maneja el caso de envío exitoso
     */
    private void manejarExitoEnvio() {
        Toast.makeText(this, "✅ Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
        CarritoManager.limpiarCarrito(mesa.getNumeroMesa());
        finish();
    }

    /**
     * Maneja errores del servidor
     */
    private void manejarErrorServidor() {
        Toast.makeText(this, "❌ Error del servidor", Toast.LENGTH_SHORT).show();
    }

    /**
     * Maneja errores de red
     */
    private void manejarErrorRed(Throwable t) {
        Toast.makeText(this, "❌ Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }
}