package com.example.proyectofinal.general;

public class alimentoGeneral {
    private String nombre;
    private String info;
    private int imagenId;
    private boolean favorito;
    public alimentoGeneral(String nombre, String info, int imagenId, boolean favorito) {
        this.nombre = nombre;
        this.info = info;
        this.imagenId = imagenId;
        this.favorito = favorito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
