package edu.uca.club.domain;

import java.util.UUID;

public class Resultado {
    private final UUID socioId;
    private final int puntos;

    public Resultado(UUID socioId, int puntos) {
        this.socioId = socioId;
        this.puntos = puntos;
    }

    public UUID getSocioId() {
        return socioId;
    }

    public int getPuntos() {
        return puntos;
    }
}