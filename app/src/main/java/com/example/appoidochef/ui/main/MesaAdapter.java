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

public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.ViewHolder> {

    public interface OnMesaClickListener {
        void onMesaClick(MesaAPI mesa);
    }

    private final Context context;
    private final List<MesaAPI> mesas;
    private final OnMesaClickListener listener;

    public MesaAdapter(Context context, List<MesaAPI> mesas, OnMesaClickListener listener) {
        this.context = context;
        this.mesas = mesas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MesaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mesa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MesaAdapter.ViewHolder holder, int position) {
        MesaAPI mesa = mesas.get(position);
        holder.txtNumeroMesa.setText("Mesa " + mesa.getNumero());

        holder.cardMesa.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMesaClick(mesa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mesas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroMesa;
        CardView cardMesa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumeroMesa = itemView.findViewById(R.id.txtNumeroMesa);
            cardMesa = itemView.findViewById(R.id.cardMesa);
        }
    }
}
