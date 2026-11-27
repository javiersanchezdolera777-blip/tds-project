package tds.gastos.adapters.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tds.gastos.adapters.repository.exceptions.ElementoExistenteException;
import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.negocio.modelo.Categoria;


class CategoriaRepositorySQLiteImplTest {
	@TempDir
	Path directorioTemporal;

    private BaseDatosSQLite baseDatos;
    private CategoriaRepositorySQLiteImpl repositorio;
    private String url;

    
    
    @BeforeEach
    void setUp() throws Exception {

        url = "jdbc:sqlite:"
                + directorioTemporal.resolve("test.db");

        baseDatos = new BaseDatosSQLite(url);
        repositorio = new CategoriaRepositorySQLiteImpl(baseDatos);
    }

    @Test
    void getCategoriasInicialmenteDevuelveListaVacia() throws Exception {

        List<Categoria> categorias = repositorio.getCategorias();

        assertTrue(categorias.isEmpty());
    }

    @Test
    void addCategoriaGuardaCategoriaYAsignaIdentificador() throws Exception {

        Categoria categoria = new Categoria(null, "Alimentación", false);

        repositorio.addCategoria(categoria);

        assertNotNull(categoria.getId());

        Categoria recuperada = repositorio.findById(categoria.getId());

        assertEquals(categoria.getNombre(), recuperada.getNombre());
        assertEquals(categoria.isPredefinida(), recuperada.isPredefinida());
    }

    @Test
    void findByIdLanzaExcepcionCuandoNoExiste() {

        assertThrows(
                ElementoNoEncontradoException.class,
                () -> repositorio.findById(-1)
        );
    }

    @Test
    void findByNombreIgnoraMayusculasYEspacios() throws Exception {

        Categoria categoria = new Categoria(null, "Alimentación", false);

        repositorio.addCategoria(categoria);

        Categoria recuperada = repositorio.findByNombre("  ALIMENTACIÓN  ");

        assertEquals(categoria.getId(), recuperada.getId());
    }

    @Test
    void addCategoriaImpideInsertarNombresDuplicados() throws Exception {

        Categoria primera = new Categoria(null, "Transporte", false);

        repositorio.addCategoria(primera);

        Categoria duplicada = new Categoria(null, "TRANSPORTE", false);

        assertThrows(
                ElementoExistenteException.class,
                () -> repositorio.addCategoria(duplicada)
        );
    }

    @Test
    void getCategoriasRecuperaTodasLasCategorias() throws Exception {

        repositorio.addCategoria(
                new Categoria(null, "Alimentación", false)
        );

        repositorio.addCategoria(
                new Categoria(null, "Transporte", false)
        );

        repositorio.addCategoria(
                new Categoria(null, "Entretenimiento", true)
        );

        List<Categoria> categorias = repositorio.getCategorias();

        assertEquals(3, categorias.size());
    }

    @Test
    void categoriasSiguenDisponiblesEnUnaNuevaInstanciaDelRepositorio()
            throws Exception {

        Categoria categoria = new Categoria(null, "Deporte", false);

        repositorio.addCategoria(categoria);

        CategoriaRepositorySQLiteImpl nuevoRepositorio =
                new CategoriaRepositorySQLiteImpl(new BaseDatosSQLite(url));

        Categoria recuperada =
                nuevoRepositorio.findById(categoria.getId());

        assertEquals(categoria.getNombre(), recuperada.getNombre());
    }
}