package edu.uca.club.api;

import edu.uca.club.service.ClubService;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ClubApi {
    record NuevoSocio(@NotBlank String nombre){}
    record NuevoJuego(@NotBlank String nombre){}
    record NuevaSesion(@NotNull LocalDate fecha, @NotNull UUID juegoId){}
    record CargarResultado(@NotNull UUID sesionId, @NotNull UUID socioId, @Min(-100) @Max(100) int puntos){}

    private final ClubService service;
    public ClubApi(ClubService service){ this.service = service; }

    @PostMapping("/socios") public ResponseEntity<?> crearSocio(@RequestBody NuevoSocio req){
        return ResponseEntity.ok(service.crearSocio(req.nombre()));
    }
    @PostMapping("/juegos") public ResponseEntity<?> crearJuego(@RequestBody NuevoJuego req){
        return ResponseEntity.ok(service.crearJuego(req.nombre()));
    }
    @PostMapping("/sesiones") public ResponseEntity<?> crearSesion(@RequestBody NuevaSesion req){
        return ResponseEntity.ok(service.crearSesion(req.fecha(), req.juegoId()));
    }
    @PostMapping("/resultados") public ResponseEntity<?> cargarResultado(@RequestBody CargarResultado req){
        return ResponseEntity.ok(service.cargarResultado(req.sesionId(), req.socioId(), req.puntos()));
    }
    @GetMapping("/ranking")
    public ResponseEntity<?> ranking(@RequestParam UUID juegoId, @RequestParam String ym){
        var yearMonth = YearMonth.parse(ym); // "2025-08"
        return ResponseEntity.ok(service.rankingMensual(juegoId, yearMonth));
    }
}
