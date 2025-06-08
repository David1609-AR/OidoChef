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

/**
 * Adaptador para mostrar un resumen de los items de un pedido en un RecyclerView.
 * Muestra el nombre del producto y la cantidad solicitada.
 */
public class ResumenPedidoAdapter extends RecyclerView.Adapter<ResumenPedidoAdapter.ViewHolder> {

    // Lista de items del pedido a mostrar
    private List<ItemPedidoAPI> items;

    /**
     * Constructor del adaptador.
     * @param items Lista de items del pedido a mostrar
     */
    public ResumenPedidoAdapter(List<ItemPedidoAPI> items) {
        this.items = items;
    }

    /**
     * Clase ViewHolder que contiene las vistas para cada item de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Views para mostrar los datos del producto
        TextView textNombreProducto, textCantidad;

        /**
         * Constructor del ViewHolder.
         * @param itemView Vista del item
         */
        public ViewHolder(View itemView) {
            super(itemView);
            // Obtener referencias a las vistas del layout
            textNombreProducto = itemView.findViewById(R.id.textNombreProducto);
            textCantidad = itemView.findViewById(R.id.textCantidad);
        }
    }

    /**
     * Crea nuevos ViewHolders cuando sea necesario.
     * @param parent Vista padre (RecyclerView)
     * @param viewType Tipo de vista (no utilizado en este caso)
     * @return Nuevo ViewHolder inicializado
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resumen_pedido, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Actualiza los contenidos de un ViewHolder existente.
     * @param holder ViewHolder a actualizar
     * @param position Posición del item en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el item en la posición actual
        ItemPedidoAPI item = items.get(position);

        // Establecer los valores en las vistas
        holder.textNombreProducto.setText(item.getNombreProducto());
        holder.textCantidad.setText("Cantidad: " + item.getCantidad());
    }

    /**
     * Devuelve el número total de items en la lista.
     * @return Número total de items
     */
    @Override
    public int getItemCount() {
        return items.size();
    }
}