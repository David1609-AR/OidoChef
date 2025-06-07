package com.example.appoidochef.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.Mesa;
import com.example.appoidochef.data.model.Producto;
import com.example.appoidochef.data.remote.ApiClient;
import com.example.appoidochef.data.remote.ApiService;
import com.example.appoidochef.util.CarritoManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {

    private TextView textMesa;
    private RecyclerView recyclerViewCategorias, recyclerViewProductos;
    private Button btnEnviarPedido, btnVerPedidosExistentes;

    private Mesa mesaSeleccionada;

    private CategoriaAdapter categoriaAdapter;
    private ProductoAdapter productoAdapter;

    private List<Producto> todosLosProductos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Referencias de vista
        textMesa = findViewById(R.id.textMesa);
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        btnEnviarPedido = findViewById(R.id.btnEnviarPedido);
        btnVerPedidosExistentes = findViewById(R.id.btnVerPedidosExistentes);

        // Configurar layout vertical para ambas listas
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this)); // Productos en vertical

        // Obtener datos de la mesa
        Intent intent = getIntent();
        int mesaId = intent.getIntExtra("mesaId", -1);
        String mesaNombre = intent.getStringExtra("mesaNombre");

        int numeroMesa;
        try {
            numeroMesa = Integer.parseInt(mesaNombre);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de mesa inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mesaSeleccionada = new Mesa(mesaId, numeroMesa);
        textMesa.setText("Mesa " + numeroMesa);

        // Obtener productos del servidor
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    todosLosProductos = response.body();
                    inicializarVista();
                } else {
                    Toast.makeText(ProductoActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(ProductoActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ver pedidos anteriores
        btnVerPedidosExistentes.setOnClickListener(v -> {
            Intent intentPedidos = new Intent(this, PedidoExistenteActivity.class);
            intentPedidos.putExtra("mesaId", mesaSeleccionada.getIdMesa());
            startActivity(intentPedidos);
        });

        // Enviar pedido
        btnEnviarPedido.setOnClickListener(v -> {
            List<ItemPedidoAPI> carrito = CarritoManager.getCarrito(mesaSeleccionada.getNumeroMesa());
            if (carrito.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resumenIntent = new Intent(this, ResumenPedidoActivity.class);
            resumenIntent.putExtra("mesa", mesaSeleccionada);
            resumenIntent.putExtra("items", new ArrayList<>(carrito));
            startActivity(resumenIntent);
        });
    }

    private void inicializarVista() {
        Set<String> categoriasSet = new HashSet<>();
        for (Producto p : todosLosProductos) {
            categoriasSet.add(p.getCategoria());
        }

        List<String> categorias = new ArrayList<>(categoriasSet);

        if (categorias.isEmpty()) {
            Toast.makeText(this, "No hay categorías disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        // Adaptador de categorías
        categoriaAdapter = new CategoriaAdapter(categorias, categoria -> {
            List<Producto> filtrados = filtrarPorCategoria(categoria);
            productoAdapter.actualizarLista(filtrados);
        });
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Mostrar productos de la primera categoría
        List<Producto> iniciales = filtrarPorCategoria(categorias.get(0));
        productoAdapter = new ProductoAdapter(iniciales, producto -> {
            CarritoManager.agregarProducto(mesaSeleccionada.getNumeroMesa(), producto);
            Toast.makeText(this, producto.getNombre() + " añadido al carrito", Toast.LENGTH_SHORT).show();
        });
        recyclerViewProductos.setAdapter(productoAdapter);
    }

    private List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : todosLosProductos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }
}
