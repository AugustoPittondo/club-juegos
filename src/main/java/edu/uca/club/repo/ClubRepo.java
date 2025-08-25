package edu.uca.club.repo;

import edu.uca.club.domain.Juego;
import edu.uca.club.domain.Sesion;
import edu.uca.club.domain.Socio;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ClubRepo {
    // “Persistencia” simple
    private final Map<UUID, Socio> socios = new ConcurrentHashMap<>();
    private final Map<UUID, Juego> juegos = new ConcurrentHashMap<>();
    private final Map<UUID, Sesion> sesiones = new ConcurrentHashMap<>();

    public Socio save(Socio s){ socios.put(s.getId(), s); return s; }
    public Juego save(Juego j){ juegos.put(j.getId(), j); return j; }
    public Sesion save(Sesion s){ sesiones.put(s.getId(), s); return s; }

    public Optional<Socio> socio(UUID id){ return Optional.ofNullable(socios.get(id)); }
    public Optional<Juego> juego(UUID id){ return Optional.ofNullable(juegos.get(id)); }
    public Optional<Sesion> sesion(UUID id){ return Optional.ofNullable(sesiones.get(id)); }

    public List<Socio> socios(){ return new ArrayList<>(socios.values()); }
    public List<Juego> juegos(){ return new ArrayList<>(juegos.values()); }
    public List<Sesion> sesiones(){ return new ArrayList<>(sesiones.values()); }

    public List<Sesion> sesionesPorJuegoYMes(UUID juegoId, YearMonth ym){
        return sesiones.values().stream()
                .filter(s -> s.getJuegoId().equals(juegoId))
                .filter(s -> YearMonth.from(s.getFecha()).equals(ym))
                .sorted(Comparator.comparing(Sesion::getFecha))
                .toList();
    }
}