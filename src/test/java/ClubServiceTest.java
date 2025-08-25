import edu.uca.club.domain.Juego;
import edu.uca.club.domain.Socio;
import edu.uca.club.repo.ClubRepo;
import edu.uca.club.service.ClubService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.testng.Assert.*;

public class ClubServiceTest {

    private ClubRepo repo;
    private ClubService service;

    private UUID juegoCatanId, juegoAzulId;
    private UUID socioAnaId, socioLuisId, socioMoraId;

    @BeforeMethod
    public void setUp() {
        repo = new ClubRepo();
        service = new ClubService(repo);

        // seed básico
        socioAnaId  = repo.save(new Socio("Ana")).getId();
        socioLuisId = repo.save(new Socio("Luis")).getId();
        socioMoraId = repo.save(new Socio("Mora")).getId();

        juegoCatanId = repo.save(new Juego("Catan")).getId();
        juegoAzulId  = repo.save(new Juego("Azul")).getId();
    }

    // ---------- crearSocio ----------

    @Test
    public void crearSocio_ok() {
        var s = service.crearSocio("  Bruno  ");
        assertNotNull(s.getId());
        assertEquals(s.getNombre(), "Bruno");
        assertTrue(repo.socios().stream().anyMatch(x -> x.getNombre().equals("Bruno")));
    }

    @DataProvider
    public Object[][] nombresInvalidos() {
        return new Object[][]{
                {""}, {" "}, {"\t"}, {"A"}, // muy corto
                { "x".repeat(100) } // muy largo
        };
    }

    @Test(dataProvider = "nombresInvalidos", expectedExceptions = IllegalArgumentException.class)
    public void crearSocio_nombreInvalido(String nombre) {
        service.crearSocio(nombre);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void crearSocio_duplicadoCaseInsensitive() {
        service.crearSocio("andres");
        service.crearSocio("  ANDRES "); // debería disparar duplicado
    }

    // ---------- crearJuego ----------

    @Test
    public void crearJuego_ok() {
        var j = service.crearJuego("  Carcassonne  ");
        assertEquals(j.getNombre(), "Carcassonne");
        assertTrue(repo.juegos().stream().anyMatch(x -> x.getNombre().equals("Carcassonne")));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void crearJuego_duplicadoCaseInsensitive() {
        service.crearJuego("Dominion");
        service.crearJuego("  dominion ");
    }

    // ---------- crearSesion ----------

    @Test
    public void crearSesion_ok() {
        var today = LocalDate.now();
        var ses = service.crearSesion(today, juegoCatanId);
        assertEquals(ses.getFecha(), today);
        assertEquals(ses.getJuegoId(), juegoCatanId);
        assertTrue(repo.sesion(ses.getId()).isPresent());
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void crearSesion_juegoNoExiste() {
        service.crearSesion(LocalDate.now(), UUID.randomUUID());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void crearSesion_fechaMuyFutura() {
        var fecha = LocalDate.now().plusDays(45); // >30 días
        service.crearSesion(fecha, juegoCatanId);
    }

    // ---------- cargarResultado ----------

    @Test
    public void cargarResultado_ok() {
        var ses = service.crearSesion(LocalDate.now(), juegoAzulId);
        var actualizado = service.cargarResultado(ses.getId(), socioAnaId, 17);
        assertEquals(actualizado.getResultados().size(), 1);
        assertTrue(actualizado.getResultados().stream().anyMatch(r -> r.getSocioId().equals(socioAnaId) && r.getPuntos()==17));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void cargarResultado_sesionNoExiste() {
        service.cargarResultado(UUID.randomUUID(), socioAnaId, 10);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void cargarResultado_socioNoExiste() {
        var ses = service.crearSesion(LocalDate.now(), juegoCatanId);
        service.cargarResultado(ses.getId(), UUID.randomUUID(), 5);
    }

    @DataProvider
    public Object[][] puntosInvalidos() {
        return new Object[][] { { -101 }, { 101 } };
    }

    @Test(dataProvider = "puntosInvalidos", expectedExceptions = IllegalArgumentException.class)
    public void cargarResultado_puntosFueraDeRango(int puntos) {
        var ses = service.crearSesion(LocalDate.now(), juegoCatanId);
        service.cargarResultado(ses.getId(), socioAnaId, puntos);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void cargarResultado_unResultadoPorSocio() {
        var ses = service.crearSesion(LocalDate.now(), juegoCatanId);
        service.cargarResultado(ses.getId(), socioAnaId, 10);
        service.cargarResultado(ses.getId(), socioAnaId, 15); // debe fallar por duplicado en la misma sesión
    }

    // ---------- rankingMensual ----------

    @Test
    public void rankingMensual_acumulaYOrdena() {
        // Mes objetivo
        var ym = YearMonth.now();

        // Sesión 1 (mismo juego)
        var s1 = service.crearSesion(ym.atDay(3), juegoCatanId);
        service.cargarResultado(s1.getId(), socioAnaId, 10);
        service.cargarResultado(s1.getId(), socioLuisId, 5);

        // Sesión 2 (mismo juego y mes)
        var s2 = service.crearSesion(ym.atDay(20), juegoCatanId);
        service.cargarResultado(s2.getId(), socioAnaId, 7);
        service.cargarResultado(s2.getId(), socioMoraId, 12);

        // Sesión fuera de mes (no debe contar)
        var s3 = service.crearSesion(ym.minusMonths(1).atEndOfMonth(), juegoCatanId);
        service.cargarResultado(s3.getId(), socioAnaId, 99);

        // Sesión de otro juego en el mismo mes (no debe contar)
        var s4 = service.crearSesion(ym.atDay(10), juegoAzulId);
        service.cargarResultado(s4.getId(), socioLuisId, 50);

        var ranking = service.rankingMensual(juegoCatanId, ym);

        // esperado:
        // Ana: 10 + 7 = 17
        // Mora: 12
        // Luis: 5
        assertEquals(ranking.size(), 3);

        // orden desc
        assertEquals(((Number) ranking.get(0).get("puntosTotales")).intValue(), 17);
        assertEquals(ranking.get(0).get("nombre"), "Ana");

        assertEquals(((Number) ranking.get(1).get("puntosTotales")).intValue(), 12);
        assertEquals(ranking.get(1).get("nombre"), "Mora");

        assertEquals(((Number) ranking.get(2).get("puntosTotales")).intValue(), 5);
        assertEquals(ranking.get(2).get("nombre"), "Luis");
    }
}
