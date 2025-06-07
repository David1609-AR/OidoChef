package com.example.appoidochef.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;
import com.example.appoidochef.data.model.PedidoAPI;

import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<PedidoAPI> pedidos = new ArrayList<>();

    public void setPedidos(List<PedidoAPI> nuevosPedidos) {
        this.pedidos = nuevosPedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        PedidoAPI pedido = pedidos.get(position);

        holder.textMesa.setText("🪑 Mesa " + pedido.getMesaId());
        holder.textPersonas.setText("👥 Personas: " + pedido.getNumPersonas());

        StringBuilder productos = new StringBuilder();
        for (ItemPedidoAPI item : pedido.getItems()) {
            productos.append("🍽️ ")
                    .append(item.getNombreProducto())
                    .append(" x")
                    .append(item.getCantidad())
                    .append("\n");
        }

        holder.textProductos.setText(productos.toString().trim());
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textMesa, textPersonas, textProductos;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            textMesa = itemView.findViewById(R.id.txtMesaPedido);       // corregido
            textPersonas = itemView.findViewById(R.id.txtPersonasPedido); // corregido
            textProductos = itemView.findViewById(R.id.txtProductosPedido); // corregido
        }
    }
}
