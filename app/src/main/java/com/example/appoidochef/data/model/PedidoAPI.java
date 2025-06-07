package com.example.appoidochef.data.model;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.List;

public class PedidoAPI implements Serializable {

    private int mesaId;
    private int numeroMesa;

    private int numPersonas;
    private MesaAPI mesa;
    private List<ItemPedidoAPI> items;

    public PedidoAPI() {}

    public PedidoAPI(int mesaId, int numPersonas, List<ItemPedidoAPI> items) {
        this.mesaId = mesaId;
        this.numPersonas = numPersonas;
        this.items = items;
    }

    public PedidoAPI(int mesaId, List<ItemPedidoAPI> items) {
        this(mesaId, 1, items); // por defecto 1 persona
    }

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public List<ItemPedidoAPI> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoAPI> items) {
        this.items = items;
    }

    public MesaAPI getMesa() {
        return mesa;
    }

    public void setMesa(MesaAPI mesa) {
        this.mesa = mesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
