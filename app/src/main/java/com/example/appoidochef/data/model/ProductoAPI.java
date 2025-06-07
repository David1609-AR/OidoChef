package com.example.appoidochef.data.model;

import java.io.Serializable;

public class ProductoAPI implements Serializable {

    private int id;
    private String nombre;
    private double precio;
    private String categoria;

    public ProductoAPI() {}

    public ProductoAPI(int id, String nombre, double precio, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
