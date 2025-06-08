package com.example.appoidochef.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.Producto;

import java.util.List;

/**
 * Adaptador para mostrar una lista de productos en un RecyclerView.
 * Implementa patrón ViewHolder para mejor rendimiento.
 * Incluye funcionalidad de click listener para cada producto.
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    // Lista de productos a mostrar
    private List<Producto> productos;

    // Listener para manejar clicks en productos
    private OnProductoClickListener listener;

    /**
     * Interfaz para manejar eventos de click en productos
     */
    public interface OnProductoClickListener {
        /**
         * Método llamado cuando se hace click en un producto
         * @param producto Producto seleccionado
         */
        void onProductoClick(Producto producto);
    }

    /**
     * Constructor del adaptador
     * @param productos Lista inicial de productos
     * @param listener Listener para clicks en productos
     */
    public ProductoAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    /**
     * Actualiza la lista de productos y notifica al RecyclerView
     * @param nuevosProductos Nueva lista de productos
     */
    public void actualizarLista(List<Producto> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged(); // Notifica que los datos cambiaron
    }

    /**
     * Crea nuevos ViewHolders cuando el RecyclerView lo necesite
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item de producto
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(vista);
    }

    /**
     * Vincula los datos del producto con las views del ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener producto en la posición actual
        Producto producto = productos.get(position);

        // Establecer valores en las views
        holder.nombre.setText(producto.getNombre());
        holder.precio.setText(String.format("%.2f €", producto.getPrecio()));

        // Configurar click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductoClick(producto);
            }
        });
    }

    /**
     * Retorna el número total de items en la lista
     */
    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }

    /**
     * Clase ViewHolder que contiene las views para cada item del RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Obtener referencias a las views
            nombre = itemView.findViewById(R.id.tvNombreProducto);
            precio = itemView.findViewById(R.id.tvPrecioProducto);
        }
    }
}