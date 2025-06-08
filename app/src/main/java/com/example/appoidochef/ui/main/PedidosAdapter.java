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

/**
 * Adaptador para mostrar una lista de pedidos en un RecyclerView.
 * Muestra información de mesa, número de personas y productos solicitados.
 */
public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    // Lista de pedidos a mostrar (inicializada vacía para evitar null)
    private List<PedidoAPI> pedidos = new ArrayList<>();

    /**
     * Actualiza la lista de pedidos y notifica al RecyclerView del cambio
     * @param nuevosPedidos Nueva lista de pedidos
     */
    public void setPedidos(List<PedidoAPI> nuevosPedidos) {
        this.pedidos = nuevosPedidos;
        notifyDataSetChanged(); // Notifica que todos los datos cambiaron
    }

    /**
     * Crea nuevos ViewHolders cuando el RecyclerView lo necesite
     */
    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item de pedido
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(vista);
    }

    /**
     * Vincula los datos del pedido con las views del ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        // Obtener pedido en la posición actual
        PedidoAPI pedido = pedidos.get(position);

        // Configurar texto de mesa con emoji
        holder.textMesa.setText("🪑 Mesa " + pedido.getMesaId());

        // Configurar texto de personas con emoji
        holder.textPersonas.setText("👥 Personas: " + pedido.getNumPersonas());

        // Construir cadena de productos con formato
        StringBuilder productos = new StringBuilder();
        for (ItemPedidoAPI item : pedido.getItems()) {
            productos.append("🍽️ ")               // Emoji para productos
                    .append(item.getNombreProducto())
                    .append(" x")
                    .append(item.getCantidad())   // Cantidad del producto
                    .append("\n");                // Salto de línea
        }

        // Establecer texto de productos (eliminando último salto de línea si existe)
        holder.textProductos.setText(productos.toString().trim());
    }

    /**
     * Retorna el número total de pedidos en la lista
     */
    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    /**
     * ViewHolder que contiene las views para cada item del RecyclerView
     */
    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textMesa, textPersonas, textProductos;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Obtener referencias a las views del layout
            textMesa = itemView.findViewById(R.id.txtMesaPedido);
            textPersonas = itemView.findViewById(R.id.txtPersonasPedido);
            textProductos = itemView.findViewById(R.id.txtProductosPedido);
        }
    }
}