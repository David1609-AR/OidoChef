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

public class VerPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);

        recyclerView = findViewById(R.id.recyclerViewPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PedidosAdapter();
        recyclerView.setAdapter(adapter);

        cargarPedidosActivos(); // 🔄 Solo carga pedidos no cerrados
    }

    private void cargarPedidosActivos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<PedidoAPI>> call = apiService.obtenerPedidosActivos();

        call.enqueue(new Callback<List<PedidoAPI>>() {
            @Override
            public void onResponse(Call<List<PedidoAPI>> call, Response<List<PedidoAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPedidos(response.body());
                } else {
                    Toast.makeText(VerPedidosActivity.this, "Error al obtener pedidos", Toast.LENGTH_SHORT).show();
                    Log.e("VerPedidos", "Respuesta no exitosa o vacía");
                }
            }

            @Override
            public void onFailure(Call<List<PedidoAPI>> call, Throwable t) {
                Toast.makeText(VerPedidosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VerPedidos", "Error al conectar con el servidor", t);
            }
        });
    }
}
