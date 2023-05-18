package com.grupo3.backgroundapp.CategoriasAdmin.VideojuegosA;

public class VideoJuego {

    private String imagen;
    private String nombre;
    private int vistas;

    public VideoJuego() {
        // Required empty constructor for Firebase Realtime Database
    }

    public VideoJuego(String imagen, String nombre, int vistas) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.vistas = vistas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }
}
