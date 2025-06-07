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

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter carritoAdapter;
    private Button btnEnviarPedido;
    private TextView txtMesaCarrito, txtTotalPedido;
    private int mesaId;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito);
        btnEnviarPedido = findViewById(R.id.btnEnviarPedido);
        txtMesaCarrito = findViewById(R.id.txtMesaCarrito);
        txtTotalPedido = findViewById(R.id.txtTotalPedido);

        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));

        mesaId = getIntent().getIntExtra("mesaId", -1);
        if (mesaId == -1) {
            Toast.makeText(this, "❌ ID de mesa inválido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        txtMesaCarrito.setText("Mesa: " + mesaId);

        List<ItemPedidoAPI> carrito = CarritoManager.getCarrito(mesaId);
        carritoAdapter = new CarritoAdapter(carrito, item -> {
            CarritoManager.eliminarProducto(mesaId, item.getProductoId());
            carritoAdapter.notifyDataSetChanged();
            actualizarTotal();
        });

        recyclerViewCarrito.setAdapter(carritoAdapter);
        actualizarTotal();

        btnEnviarPedido.setOnClickListener(v -> {
            List<ItemPedidoAPI> carritoActual = CarritoManager.getCarrito(mesaId);
            if (carritoActual.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            PedidoAPI pedido = new PedidoAPI(mesaId, 1, carritoActual);

            WebSocketManager wsManager = WebSocketManager.getInstance();

            if (wsManager.isConnected()) {
                wsManager.sendOrderWhenReady(pedido);
                Toast.makeText(this, "✅ Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                CarritoManager.limpiarCarrito(mesaId);
                finish();
            } else {
                Toast.makeText(this, "🔄 Esperando conexión WebSocket...", Toast.LENGTH_SHORT).show();
                wsManager.connect();
                handler.postDelayed(() -> {
                    if (wsManager.isConnected()) {
                        wsManager.sendOrderWhenReady(pedido);
                        Toast.makeText(this, "✅ Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                        CarritoManager.limpiarCarrito(mesaId);
                        finish();
                    } else {
                        Toast.makeText(this, "❌ Error de conexión con WebSocket", Toast.LENGTH_LONG).show();
                    }
                }, 1500);
            }
        });
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (ItemPedidoAPI item : CarritoManager.getCarrito(mesaId)) {
            total += item.getCantidad() * item.getPrecio();
        }
        txtTotalPedido.setText(String.format("Total: %.2f€", total));
    }
}
