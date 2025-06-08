package com.example.appoidochef.ui.main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad que muestra los pedidos existentes para una mesa específica.
 * Obtiene los datos del pedido desde una API REST y los muestra en un RecyclerView.
 */
public class PedidoExistenteActivity extends AppCompatActivity {

    // Views
    private TextView textMesaPedido;
    private RecyclerView recyclerPedidosMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_existente);

        // Inicialización de vistas
        inicializarVistas();

        // Obtener ID de mesa del Intent
        int mesaId = obtenerMesaId();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Cargar pedidos de la mesa desde la API
        cargarPedidosMesa(mesaId);
    }

    /**
     * Vincula las vistas con los componentes del layout
     */
    private void inicializarVistas() {
        textMesaPedido = findViewById(R.id.textMesaPedido);
        recyclerPedidosMesa = findViewById(R.id.recyclerPedidosMesa);
    }

    /**
     * Obtiene el ID de mesa pasado como extra en el Intent
     * @return ID de la mesa o -1 si no se proporcionó
     */
    private int obtenerMesaId() {
        int mesaId = getIntent().getIntExtra("mesaId", -1);
        textMesaPedido.setText("Pedidos de la mesa " + mesaId);
        return mesaId;
    }

    /**
     * Configura el RecyclerView con un LinearLayoutManager
     */
    private void configurarRecyclerView() {
        recyclerPedidosMesa.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Realiza la petición a la API para obtener los pedidos de una mesa específica
     * @param mesaId ID de la mesa cuyos pedidos se quieren obtener
     */
    private void cargarPedidosMesa(int mesaId) {
        ApiService apiService = ApiClient.getApiService();
        apiService.obtenerPedidoPorMesa(mesaId).enqueue(new Callback<PedidoAPI>() {
            @Override
            public void onResponse(Call<PedidoAPI> call, Response<PedidoAPI> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si hay respuesta exitosa con datos
                    manejarRespuestaExitosa(response.body());
                } else {
                    // Si la respuesta no contiene datos
                    mostrarMensaje("No hay pedidos registrados");
                }
            }

            @Override
            public void onFailure(Call<PedidoAPI> call, Throwable t) {
                // Si falla la conexión
                mostrarError("Error: " + t.getMessage());
            }
        });
    }

    /**
     * Maneja la respuesta exitosa de la API
     * @param pedido Objeto PedidoAPI con los datos del pedido
     */
    private void manejarRespuestaExitosa(PedidoAPI pedido) {
        List<ItemPedidoAPI> items = pedido.getItems();
        recyclerPedidosMesa.setAdapter(new ResumenPedidoAdapter(items));
    }

    /**
     * Muestra un mensaje Toast al usuario
     * @param mensaje Texto del mensaje a mostrar
     */
    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un mensaje de error Toast al usuario
     * @param mensaje Texto del error a mostrar
     */
    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}