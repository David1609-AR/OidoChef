package com.example.appoidochef.data.model;

public class ItemPedido {
    private ProductoAPI producto;
    private int cantidad;

    public ItemPedido() {}

    public ItemPedido(ProductoAPI producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoAPI getProducto() {
        return producto;
    }

    public void setProducto(ProductoAPI producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto != null ? producto.getPrecio() * cantidad : 0;
    }

}
