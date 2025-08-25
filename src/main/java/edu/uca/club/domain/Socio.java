package edu.uca.club.domain;

import java.util.Objects;
import java.util.UUID;

public class Socio {
    private final UUID id;
    private String nombre;

    public Socio(String nombre) {
        this.id = UUID.randomUUID();
        this.nombre = Objects.requireNonNull(nombre);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = Objects.requireNonNull(n);
    }
}