package com.example.appoidochef.util;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.util.CarritoAdapter;
import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.PedidoAPI;
import com.example.appoidochef.data.websocket.WebSocketManager;

import java.util.List;

/**
 * Actividad que muestra el carrito de compras y permite enviar pedidos.
 * Gestiona la visualización de productos, cálculo del total y envío a cocina.
 */
public class CarritoActivity extends AppCompatActivity {

    // Componentes de la UI
    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter carritoAdapter;
    private Button btnEnviarPedido;
    private TextView txtMesaCarrito, txtTotalPedido;

    // Datos de la mesa
    private int mesaId;

    // Handler para operaciones con retraso
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Inicializar vistas
        inicializarVistas();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Manejar evento de enviar pedido
        configurarBotonEnviar();
    }

    /**
     * Inicializa y vincula los componentes de la interfaz de usuario.
     */
    private void inicializarVistas() {
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
        btnEnviarPedido = findViewById(R.id.btnEnviarPedido);
        txtMesaCarrito = findViewById(R.id.txtMesaCarrito);
        txtTotalPedido = findViewById(R.id.txtTotalPedido);
    }

    /**
     * Configura el RecyclerView con su adaptador y layout manager.
     */
    private void configurarRecyclerView() {
        // Usar LinearLayoutManager para disposición vertical
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));

        // Obtener ID de mesa del intent
        mesaId = getIntent().getIntExtra("mesaId", -1);
        if (mesaId == -1) {
            mostrarErrorMesaInvalida();
            return;
        }

        // Mostrar número de mesa
        txtMesaCarrito.setText("Mesa: " + mesaId);

        // Obtener carrito para esta mesa
        List<ItemPedidoAPI> carrito = CarritoManager.getCarrito(mesaId);

        // Configurar adaptador con listener para eliminar items
        carritoAdapter = new CarritoAdapter(carrito, item -> {
            CarritoManager.eliminarProducto(mesaId, item.getProductoId());
            carritoAdapter.notifyDataSetChanged();
            actualizarTotal();
        });

        recyclerViewCarrito.setAdapter(carritoAdapter);
        actualizarTotal();
    }

    /**
     * Muestra error cuando el ID de mesa es inválido y finaliza la actividad.
     */
    private void mostrarErrorMesaInvalida() {
        Toast.makeText(this, "❌ ID de mesa inválido", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Configura el botón para enviar pedidos a cocina.
     */
    private void configurarBotonEnviar() {
        btnEnviarPedido.setOnClickListener(v -> {
            List<ItemPedidoAPI> carritoActual = CarritoManager.getCarrito(mesaId);

            // Validar carrito no vacío
            if (carritoActual.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear objeto PedidoAPI
            PedidoAPI pedido = new PedidoAPI(mesaId, 1, carritoActual);

            // Obtener instancia del WebSocketManager
            WebSocketManager wsManager = WebSocketManager.getInstance();

            // Intentar enviar pedido
            if (wsManager.isConnected()) {
                enviarPedidoExitoso(wsManager, pedido);
            } else {
                manejarConexionWebSocket(wsManager, pedido);
            }
        });
    }

    /**
     * Maneja el envío exitoso del pedido.
     */
    private void enviarPedidoExitoso(WebSocketManager wsManager, PedidoAPI pedido) {
        wsManager.sendOrderWhenReady(pedido);
        Toast.makeText(this, "✅ Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
        CarritoManager.limpiarCarrito(mesaId);
        finish();
    }

    /**
     * Maneja la conexión WebSocket cuando no está disponible.
     */
    private void manejarConexionWebSocket(WebSocketManager wsManager, PedidoAPI pedido) {
        Toast.makeText(this, "🔄 Esperando conexión WebSocket...", Toast.LENGTH_SHORT).show();
        wsManager.connect();

        // Intentar enviar después de 1.5 segundos
        handler.postDelayed(() -> {
            if (wsManager.isConnected()) {
                enviarPedidoExitoso(wsManager, pedido);
            } else {
                Toast.makeText(this, "❌ Error de conexión con WebSocket", Toast.LENGTH_LONG).show();
            }
        }, 1500);
    }

    /**
     * Calcula y actualiza el total del pedido en la interfaz.
     */
    private void actualizarTotal() {
        double total = 0.0;
        for (ItemPedidoAPI item : CarritoManager.getCarrito(mesaId)) {
            total += item.getCantidad() * item.getPrecio();
        }
        txtTotalPedido.setText(String.format("Total: %.2f€", total));
    }
}