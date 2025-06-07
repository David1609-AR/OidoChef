package com.example.appoidochef.data.model;

import java.io.Serializable;

public class Producto implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
    private boolean tiene_iva;

    public Producto() {}

    public Producto(int id, String nombre, String descripcion, double precio, String categoria, boolean tiene_iva) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.tiene_iva = tiene_iva;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isTieneIva() { return tiene_iva; }
    public void setTieneIva(boolean tiene_iva) { this.tiene_iva = tiene_iva; }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
