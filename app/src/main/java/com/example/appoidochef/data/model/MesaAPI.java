package com.example.appoidochef.data.model;

public class MesaAPI {
    private int id;
    private int numeroMesa;
    private boolean bloqueada;
    private boolean ocupada;

    // 🔧 Constructor vacío necesario para Gson
    public MesaAPI() {
    }

    // 🔧 Constructor completo (opcional, útil para pruebas)
    public MesaAPI(int id, int numero, boolean bloqueada, boolean ocupada) {
        this.id = id;
        this.numeroMesa = numero;
        this.bloqueada = bloqueada;
        this.ocupada = ocupada;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numeroMesa;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public boolean isOcupada() {
        return ocupada;
    }
}
