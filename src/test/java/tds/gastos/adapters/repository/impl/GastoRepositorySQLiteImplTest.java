package tds.gastos.adapters.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.negocio.modelo.Categoria;
import tds.gastos.negocio.modelo.Gasto;

class GastoRepositorySQLiteImplTest {

    @TempDir
    Path directorioTemporal;

    private BaseDatosSQLite baseDatos;
    private CategoriaRepositorySQLiteImpl categoriaRepository;
    private GastoRepositorySQLiteImpl gastoRepository;

    private Categoria categoria;

    @BeforeEach
    void setUp() throws Exception {

        String url = "jdbc:sqlite:"
                + directorioTemporal.resolve("test.db");

        baseDatos = new BaseDatosSQLite(url);

        categoriaRepository =
                new CategoriaRepositorySQLiteImpl(baseDatos);

        gastoRepository =
                new GastoRepositorySQLiteImpl(baseDatos);

        categoria = new Categoria(null, "Alimentación", false);

        categoriaRepository.addCategoria(categoria);
    }

    private Gasto crearGasto() {

        return new Gasto(
                25.50,
                LocalDateTime.of(2026, 11, 20, 14, 30),
                categoria,
                "Compra semanal"
        );
    }

    @Test
    void getGastosInicialmenteDevuelveListaVacia() throws Exception {

        List<Gasto> gastos = gastoRepository.getGastos();

        assertTrue(gastos.isEmpty());
    }

    @Test
    void addGastoGuardaGastoYAsignaIdentificador() throws Exception {

        Gasto gasto = crearGasto();

        gastoRepository.addGasto(gasto);

        assertNotNull(gasto.getId());

        Gasto recuperado = gastoRepository.findById(gasto.getId());

        assertEquals(gasto.getImporte(), recuperado.getImporte());
        assertEquals(gasto.getFecha(), recuperado.getFecha());
        assertEquals(gasto.getDescripcion(), recuperado.getDescripcion());
        assertEquals(gasto.getCategoria().getId(),
                recuperado.getCategoria().getId());
    }

    @Test
    void findByIdLanzaExcepcionCuandoGastoNoExiste() {

        assertThrows(
                ElementoNoEncontradoException.class,
                () -> gastoRepository.findById(-1)
        );
    }

    @Test
    void getGastosRecuperaTodosLosGastos() throws Exception {

        Gasto gasto1 = crearGasto();

        Gasto gasto2 = new Gasto(
                10.00,
                LocalDateTime.of(2026, 11, 21, 10, 0),
                categoria,
                "Café"
        );

        gastoRepository.addGasto(gasto1);
        gastoRepository.addGasto(gasto2);

        List<Gasto> gastos = gastoRepository.getGastos();

        assertEquals(2, gastos.size());
    }

    @Test
    void updateGastoModificaSusDatos() throws Exception {

        Gasto gasto = crearGasto();

        gastoRepository.addGasto(gasto);

        gasto.setImporte(40.00);
        gasto.setFecha(LocalDateTime.of(2026, 11, 22, 18, 45));
        gasto.setDescripcion("Compra modificada");

        Gasto actualizado = gastoRepository.updateGasto(gasto);

        assertEquals(gasto.getId(), actualizado.getId());

        Gasto recuperado = gastoRepository.findById(gasto.getId());

        assertEquals(40.00, recuperado.getImporte());
        assertEquals(
                LocalDateTime.of(2026, 11, 22, 18, 45),
                recuperado.getFecha()
        );
        assertEquals("Compra modificada", recuperado.getDescripcion());
    }

    @Test
    void updateGastoLanzaExcepcionCuandoNoExiste() {

        Gasto gastoInexistente = new Gasto(
                999,
                15.00,
                LocalDateTime.of(2026, 11, 20, 12, 0),
                categoria,
                "Gasto inexistente"
        );

        assertThrows(
                ElementoNoEncontradoException.class,
                () -> gastoRepository.updateGasto(gastoInexistente)
        );
    }

    @Test
    void removeGastoEliminaElGasto() throws Exception {

        Gasto gasto = crearGasto();

        gastoRepository.addGasto(gasto);

        gastoRepository.removeGasto(gasto);

        assertThrows(
                ElementoNoEncontradoException.class,
                () -> gastoRepository.findById(gasto.getId())
        );
    }

    @Test
    void removeGastoLanzaExcepcionCuandoNoExiste() {

        Gasto gastoInexistente = new Gasto(
                999,
                15.00,
                LocalDateTime.of(2026, 11, 20, 12, 0),
                categoria,
                "Gasto inexistente"
        );

        assertThrows(
                ElementoNoEncontradoException.class,
                () -> gastoRepository.removeGasto(gastoInexistente)
        );
    }

    @Test
    void gastoPersisteAlCrearOtraInstanciaDelRepositorio() throws Exception {

        Gasto gasto = crearGasto();

        gastoRepository.addGasto(gasto);

        GastoRepositorySQLiteImpl nuevoRepositorio =
                new GastoRepositorySQLiteImpl(
                        new BaseDatosSQLite(
                                "jdbc:sqlite:"
                                        + directorioTemporal.resolve("test.db")
                        )
                );

        Gasto recuperado =
                nuevoRepositorio.findById(gasto.getId());

        assertEquals(gasto.getId(), recuperado.getId());
        assertEquals(gasto.getImporte(), recuperado.getImporte());
        assertEquals(gasto.getFecha(), recuperado.getFecha());
        assertEquals(gasto.getDescripcion(), recuperado.getDescripcion());
        assertEquals(gasto.getCategoria().getId(),
                recuperado.getCategoria().getId());
    }
}