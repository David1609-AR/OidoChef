package com.example.appoidochef.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.ItemPedidoAPI;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private final List<ItemPedidoAPI> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEliminarClick(ItemPedidoAPI item);
    }

    public CarritoAdapter(List<ItemPedidoAPI> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPedidoAPI item = items.get(position);
        holder.txtNombre.setText(item.getNombreProducto());
        holder.txtCantidad.setText("Cantidad: " + item.getCantidad());
        holder.txtPrecio.setText(String.format("%.2f €", item.getPrecio()));

        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad, txtPrecio;
        ImageButton btnEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
