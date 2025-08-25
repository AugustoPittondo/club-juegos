package edu.uca.club.domain;

import java.time.LocalDate;
import java.util.*;

public class Sesion {
    private final UUID id = UUID.randomUUID();
    private final LocalDate fecha;
    private final UUID juegoId;
    private final Map<UUID, Resultado> resultados = new LinkedHashMap<>();

    public Sesion(LocalDate fecha, UUID juegoId) {
        this.fecha = Objects.requireNonNull(fecha);
        this.juegoId = Objects.requireNonNull(juegoId);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public UUID getJuegoId() {
        return juegoId;
    }

    public Collection<Resultado> getResultados() {
        return resultados.values();
    }

    public void cargarResultado(Resultado r) {
        // Regla de negocio: un socio por sesión
        if (resultados.containsKey(r.getSocioId())) throw new IllegalArgumentException("Socio ya tiene resultado");
        resultados.put(r.getSocioId(), r);
    }
}