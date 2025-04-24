package com.example.fishcatch.model;

public class Especie {
    private int id;
    private String nombreEspecie;

    public Especie(int id, String nombreEspecie) {
        this.id = id;
        this.nombreEspecie = nombreEspecie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    @Override
    public String toString() {
        return "Especie{" +
                "id=" + id +
                ", nombreEspecie='" + nombreEspecie + '\'' +
                '}';
    }
}
