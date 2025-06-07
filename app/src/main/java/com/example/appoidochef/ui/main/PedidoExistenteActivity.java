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

public class PedidoExistenteActivity extends AppCompatActivity {

    private TextView textMesaPedido;
    private RecyclerView recyclerPedidosMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_existente);

        textMesaPedido = findViewById(R.id.textMesaPedido);
        recyclerPedidosMesa = findViewById(R.id.recyclerPedidosMesa);
        recyclerPedidosMesa.setLayoutManager(new LinearLayoutManager(this));

        int mesaId = getIntent().getIntExtra("mesaId", -1);
        textMesaPedido.setText("Pedidos de la mesa " + mesaId);

        ApiService apiService = ApiClient.getApiService();
        apiService.obtenerPedidoPorMesa(mesaId).enqueue(new Callback<PedidoAPI>() {
            @Override
            public void onResponse(Call<PedidoAPI> call, Response<PedidoAPI> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemPedidoAPI> items = response.body().getItems();
                    recyclerPedidosMesa.setAdapter(new ResumenPedidoAdapter(items));
                } else {
                    Toast.makeText(PedidoExistenteActivity.this, "No hay pedidos registrados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PedidoAPI> call, Throwable t) {
                Toast.makeText(PedidoExistenteActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
