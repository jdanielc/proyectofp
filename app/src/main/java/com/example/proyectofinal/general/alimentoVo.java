package com.example.proyectofinal.general;

import java.io.Serializable;

public class alimentoVo implements Serializable {

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getImagenDetalle() {
        return imagenDetalle;
    }

    public void setImagenDetalle(int imagenDetalle) {
        this.imagenDetalle = imagenDetalle;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    private String nombre;
    private String info;
    private int imagenId;

    private String descripcion;
    private int upvotes;
    private int downvotes;
    private int user;
    private int imagenDetalle;
    private boolean favorito;

    public alimentoVo(String nombre, String info, int imagenId, String descripcion, int upvotes, int downvotes, int user, int imagenDetalle, boolean favorito){
        this.nombre = nombre;
        this.info = info;
        this.imagenId = imagenId;

        this.descripcion = descripcion;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.user = user;
        this.imagenDetalle = imagenDetalle;
        this.favorito = favorito;
    }
}
