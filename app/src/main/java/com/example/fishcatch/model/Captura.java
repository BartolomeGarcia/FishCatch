package com.example.fishcatch.model;

public class Captura {
    private int id;
    private int idEspecie;
    private String nombreEspecie;
    private double peso;
    private double tamanno;
    private String fecha; // "dd/MM/yyyy"
    private String hora;
    private String comentario;
    private String fotoUri;
    private Double latitud;
    private Double longitud;
    private Double temperatura;

    public Captura() {
    }

    public Captura(int id, int idEspecie, String nombreEspecie, double peso, double tamanno, String fecha, String hora, String comentario, String fotoUri, Double latitud, Double longitud, Double temperatura) {
        this.id = id;
        this.idEspecie = idEspecie;
        this.nombreEspecie = nombreEspecie;
        this.peso = peso;
        this.tamanno = tamanno;
        this.fecha = fecha;
        this.hora = hora;
        this.comentario = comentario;
        this.fotoUri = fotoUri;
        this.latitud = latitud;
        this.longitud = longitud;
        this.temperatura = temperatura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(int idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTamanno() {
        return tamanno;
    }

    public void setTamanno(double tamanno) {
        this.tamanno = tamanno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    @Override
    public String toString() {
        return "Captura{" +
                "id=" + id +
                ", idEspecie=" + idEspecie +
                ", nombreEspecie='" + nombreEspecie + '\'' +
                ", peso=" + peso +
                ", tamanno=" + tamanno +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", comentario='" + comentario + '\'' +
                ", fotoUri='" + fotoUri + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", temperatura=" + temperatura +
                '}';
    }

    //Métodos para usarlos en MainActivityDetalleCaptura
    public boolean tieneUbicacion() {
        return latitud != null && longitud != null && latitud != 0 && longitud != 0;
    }

    public boolean tieneImagen() {
        return fotoUri != null && !fotoUri.isEmpty();
    }
}
