package com.example.appoidochef.data.model;

import java.io.Serializable;

/**
 * Representa un ítem dentro de un pedido (producto + cantidad).
 */
public class ItemPedidoAPI implements Serializable {

    private int productoId;
    private String nombreProducto;
    private double precio;
    private int cantidad;
    private String categoria;

    public ItemPedidoAPI() {}

    // Constructor desde modelo local (útil si usas Producto como objeto)
    public ItemPedidoAPI(Producto producto, int cantidad) {
        this.productoId = producto.getId();
        this.nombreProducto = producto.getNombre();
        this.precio = producto.getPrecio();
        this.categoria = producto.getCategoria();
        this.cantidad = cantidad;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecioTotal() {
        return precio * cantidad;
    }

    @Override
    public String toString() {
        return cantidad + " x " + nombreProducto + " (€" + precio + ")";
    }
}
