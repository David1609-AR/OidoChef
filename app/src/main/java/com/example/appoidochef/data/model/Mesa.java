package com.example.appoidochef.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Representa una mesa del restaurante (compatible con backend Spark y Gson).
 */
public class Mesa implements Serializable {

    @SerializedName("idMesa")
    private int idMesa;

    @SerializedName("numeroMesa")
    private int numeroMesa;

    @SerializedName("bloqueada")
    private boolean bloqueada;

    @SerializedName("ocupada")
    private boolean ocupada;

    // 🔧 Constructor vacío requerido por Gson
    public Mesa() {}

    // ✅ Constructor útil para construir mesas desde código (frontend)
    public Mesa(int idMesa, int numeroMesa) {
        this.idMesa = idMesa;
        this.numeroMesa = numeroMesa;
        this.bloqueada = false;
        this.ocupada = false;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    @Override
    public String toString() {
        return "Mesa " + numeroMesa + (bloqueada ? " (Bloqueada)" : "");
    }
}