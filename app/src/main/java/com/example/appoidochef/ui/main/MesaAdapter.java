package com.example.appoidochef.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appoidochef.R;
import com.example.appoidochef.data.model.MesaAPI;

import java.util.List;

/**
 * Adaptador para mostrar una lista de mesas en un RecyclerView.
 * Implementa el patrón ViewHolder para mejor rendimiento.
 * Incluye funcionalidad de click listener para cada mesa.
 */
public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.ViewHolder> {

    /**
     * Interfaz para manejar eventos de click en las mesas
     */
    public interface OnMesaClickListener {
        /**
         * Método llamado cuando se hace click en una mesa
         * @param mesa Mesa seleccionada
         */
        void onMesaClick(MesaAPI mesa);
    }

    // Contexto de la aplicación
    private final Context context;
    // Lista de mesas a mostrar
    private final List<MesaAPI> mesas;
    // Listener para clicks en mesas
    private final OnMesaClickListener listener;

    /**
     * Constructor del adaptador
     * @param context Contexto de la aplicación
     * @param mesas Lista de mesas a mostrar
     * @param listener Listener para eventos de click
     */
    public MesaAdapter(Context context, List<MesaAPI> mesas, OnMesaClickListener listener) {
        this.context = context;
        this.mesas = mesas;
        this.listener = listener;
    }

    /**
     * Crea nuevos ViewHolders cuando el RecyclerView lo necesite
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item de mesa
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mesa, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Vincula los datos de la mesa con las views del ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener mesa en la posición actual
        MesaAPI mesa = mesas.get(position);

        // Configurar número de mesa
        holder.txtNumeroMesa.setText("Mesa " + mesa.getNumero());

        // Configurar click listener en la CardView
        holder.cardMesa.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMesaClick(mesa);
            }
        });
    }

    /**
     * Retorna el número total de mesas en la lista
     */
    @Override
    public int getItemCount() {
        return mesas != null ? mesas.size() : 0;
    }

    /**
     * ViewHolder que contiene las views para cada item del RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroMesa;
        CardView cardMesa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Obtener referencias a las views del layout
            txtNumeroMesa = itemView.findViewById(R.id.txtNumeroMesa);
            cardMesa = itemView.findViewById(R.id.cardMesa);
        }
    }
}