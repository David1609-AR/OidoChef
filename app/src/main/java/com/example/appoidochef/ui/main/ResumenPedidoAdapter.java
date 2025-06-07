package com.example.appoidochef.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;

import java.util.List;

public class ResumenPedidoAdapter extends RecyclerView.Adapter<ResumenPedidoAdapter.ViewHolder> {

    private List<ItemPedidoAPI> items;

    public ResumenPedidoAdapter(List<ItemPedidoAPI> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreProducto, textCantidad;

        public ViewHolder(View itemView) {
            super(itemView);
            textNombreProducto = itemView.findViewById(R.id.textNombreProducto);
            textCantidad = itemView.findViewById(R.id.textCantidad);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resumen_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPedidoAPI item = items.get(position);
        holder.textNombreProducto.setText(item.getNombreProducto());
        holder.textCantidad.setText("Cantidad: " + item.getCantidad());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
