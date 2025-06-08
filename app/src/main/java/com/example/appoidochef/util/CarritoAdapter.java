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

/**
 * Adaptador para el RecyclerView que muestra los items del carrito de compras.
 * Maneja la visualización y las interacciones con los elementos del carrito.
 */
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    // Lista de items del pedido (carrito)
    private final List<ItemPedidoAPI> items;

    // Listener para manejar eventos de clic en los items
    private final OnItemClickListener listener;

    /**
     * Interfaz para definir los eventos de clic en los items.
     */
    public interface OnItemClickListener {
        /**
         * Método llamado cuando se hace clic en el botón eliminar de un item.
         * @param item El item del pedido que se quiere eliminar.
         */
        void onEliminarClick(ItemPedidoAPI item);
    }

    /**
     * Constructor del adaptador.
     * @param items Lista de items del pedido a mostrar.
     * @param listener Listener para manejar eventos de clic.
     */
    public CarritoAdapter(List<ItemPedidoAPI> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /**
     * Método llamado cuando se necesita crear un nuevo ViewHolder.
     * @param parent El ViewGroup padre donde se añadirá la nueva vista.
     * @param viewType El tipo de vista (no utilizado en este caso).
     * @return Un nuevo ViewHolder que contiene la vista del item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item del carrito
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Método llamado para mostrar los datos en la posición especificada.
     * @param holder El ViewHolder que debe actualizarse.
     * @param position La posición del item en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el item en la posición actual
        ItemPedidoAPI item = items.get(position);

        // Asignar los valores del item a las vistas del ViewHolder
        holder.txtNombre.setText(item.getNombreProducto());
        holder.txtCantidad.setText("Cantidad: " + item.getCantidad());
        holder.txtPrecio.setText(String.format("%.2f €", item.getPrecio()));

        // Configurar el listener para el botón eliminar
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(item));
    }

    /**
     * Método que devuelve el número total de items en la lista.
     * @return El número de items en la lista.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Clase ViewHolder que representa cada item del RecyclerView.
     * Contiene referencias a las vistas que se mostrarán para cada item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad, txtPrecio;
        ImageButton btnEliminar;

        /**
         * Constructor del ViewHolder.
         * @param itemView La vista del item.
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Obtener referencias a las vistas del layout
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}