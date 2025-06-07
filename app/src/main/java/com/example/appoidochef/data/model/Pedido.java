package com.example.appoidochef.data.model;

import com.example.appoidochef.data.model.ItemPedido;

import java.util.List;

public class Pedido {
    private int mesaId;
    private List<ItemPedido> items;

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }
}
