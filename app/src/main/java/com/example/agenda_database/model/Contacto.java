package com.example.agenda_database.model;

import java.io.Serializable;

public class Contacto implements Serializable {
    private String nombre;
    private String movil;
    private String email;

    public Contacto(String nombre, String movil, String email) {
        this.nombre = nombre;
        this.movil = movil;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMovil() {
        return movil;
    }

    public String getEmail() {
        return email;
    }


}

