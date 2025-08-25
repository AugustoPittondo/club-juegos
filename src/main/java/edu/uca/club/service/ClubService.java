package edu.uca.club.service;

import edu.uca.club.domain.Juego;
import edu.uca.club.domain.Sesion;
import edu.uca.club.domain.Socio;
import edu.uca.club.repo.ClubRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class ClubService {
    private final ClubRepo repo;
    public ClubService(ClubRepo repo){ this.repo = repo; }

    public Socio crearSocio(String nombre) {
        // TODO: validar no vacío, trim, longitud razonable, normalización
        // TODO: prohibir duplicados exactos por nombre
        throw new UnsupportedOperationException("TODO");
    }

    public Juego crearJuego(String nombre) {
        // TODO: validaciones similares
        throw new UnsupportedOperationException("TODO");
    }

    public Sesion crearSesion(LocalDate fecha, UUID juegoId) {
        // TODO: validar que el juego exista
        // TODO: no permitir fecha futura excesiva (p.ej. > 30 días)
        throw new UnsupportedOperationException("TODO");
    }

    public Sesion cargarResultado(UUID sesionId, UUID socioId, int puntos) {
        // TODO: validar sesión/socio existen
        // TODO: validar puntos en rango (-100..100)
        // TODO: invocar Sesion.cargarResultado con regla "un socio por sesión"
        throw new UnsupportedOperationException("TODO");
    }

    public List<Map<String,Object>> rankingMensual(UUID juegoId, YearMonth mes) {
        // Debe devolver: [{socioId, nombre, puntosTotales}], orden desc por puntos
        // Sugerencia:
        // - Obtener sesiones del repo.sesionesPorJuegoYMes
        // - Acumular por socioId
        // - Mapear a estructura con nombre de socio
        throw new UnsupportedOperationException("TODO");
    }
}