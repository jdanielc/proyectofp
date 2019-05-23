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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getImagenDetalle() {
        return imagenDetalle;
    }

    public void setImagenDetalle(int imagenDetalle) {
        this.imagenDetalle = imagenDetalle;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private String nombre;
    private String info;
    private int imagenId;
    private int ID;
    private String descripcion;
    private int upvotes;
    private String user;
    private int imagenDetalle;

    public alimentoVo(int ID, String nombre, String info, int imagenId, String descripcion, int upvotes,  String user, int imagenDetalle){
        this.nombre = nombre;
        this.info = info;
        this.imagenId = imagenId;
        this.ID = ID;
        this.descripcion = descripcion;
        this.upvotes = upvotes;
        this.user = user;
        this.imagenDetalle = imagenDetalle;
    }

    public  alimentoVo(){}
}
