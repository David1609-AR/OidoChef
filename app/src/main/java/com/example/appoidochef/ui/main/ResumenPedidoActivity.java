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

public class ResumenPedidoActivity extends AppCompatActivity {

    private TextView textMesaResumen;
    private RecyclerView recyclerResumen;
    private Button btnConfirmar;

    private Mesa mesa;
    private List<ItemPedidoAPI> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_pedido);

        textMesaResumen = findViewById(R.id.textMesaResumen);
        recyclerResumen = findViewById(R.id.recyclerResumen);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        mesa = (Mesa) getIntent().getSerializableExtra("mesa");
        items = (ArrayList<ItemPedidoAPI>) getIntent().getSerializableExtra("items");

        textMesaResumen.setText("Resumen de: Mesa " + mesa.getNumeroMesa());

        recyclerResumen.setLayoutManager(new LinearLayoutManager(this));
        recyclerResumen.setAdapter(new ResumenPedidoAdapter(items));

        btnConfirmar.setOnClickListener(v -> {
            PedidoAPI pedido = new PedidoAPI();

            // Asegúrate de que estos métodos existen en tu clase PedidoAPI
            pedido.setNumeroMesa(mesa.getNumeroMesa());      // para mostrar en backend o visualización
            pedido.setMesaId(mesa.getIdMesa());              // si tu backend usa idMesa internamente
            pedido.setMesa(new MesaAPI(
                    mesa.getIdMesa(),
                    mesa.getNumeroMesa(),
                    mesa.isBloqueada(),
                    mesa.isOcupada()
            ));
            pedido.setItems(items);

            ApiService apiService = ApiClient.getApiService();
            apiService.crearPedido(pedido).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ResumenPedidoActivity.this, "✅ Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                        CarritoManager.limpiarCarrito(mesa.getNumeroMesa());
                        finish();
                    } else {
                        Toast.makeText(ResumenPedidoActivity.this, "❌ Error del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ResumenPedidoActivity.this, "❌ Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
