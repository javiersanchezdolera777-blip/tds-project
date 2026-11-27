package tds.gastos.adapters.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tds.gastos.adapters.repository.exceptions.ElementoExistenteException;
import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.adapters.repository.exceptions.ErrorPersistenciaException;
import tds.gastos.negocio.modelo.*;

public class CategoriaRepositorySQLiteImpl implements CategoriaRepository {

    private final BaseDatosSQLite baseDatos;

    public CategoriaRepositorySQLiteImpl(BaseDatosSQLite baseDatos) {
        this.baseDatos = baseDatos;
    }

    @Override
    public List<Categoria> getCategorias() throws ErrorPersistenciaException {

        String sql = "SELECT id, nombre, predefinida FROM categoria ORDER BY id";

        List<Categoria> categorias = new ArrayList<>();

        try (Connection conexion = baseDatos.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery(sql)) {

            while (resultado.next()) {
                categorias.add(mapearCategoria(resultado));
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }

        return categorias;
    }

    @Override
    public Categoria findById(Integer id)
            throws ElementoNoEncontradoException, ErrorPersistenciaException {

        String sql = "SELECT id, nombre, predefinida FROM categoria WHERE id = ?";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return mapearCategoria(resultado);
                }
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }

        throw new ElementoNoEncontradoException(
                "No se ha encontrado la categoría con ID " + id
        );
    }

    @Override
    public Categoria findByNombre(String nombre)
            throws ElementoNoEncontradoException, ErrorPersistenciaException {

        if (nombre == null) {
            throw new ElementoNoEncontradoException(
                    "No se ha encontrado una categoría con nombre null"
            );
        }

        String nombreBuscado = nombre.trim();

        return getCategorias().stream()
                .filter(categoria ->
                        categoria.getNombre().equalsIgnoreCase(nombreBuscado))
                .findFirst()
                .orElseThrow(() -> new ElementoNoEncontradoException(
                        "No se ha encontrado la categoría " + nombreBuscado
                ));
    }

    @Override
    public void addCategoria(Categoria categoria)
            throws ElementoExistenteException, ErrorPersistenciaException {

        try {
            findByNombre(categoria.getNombre());

            throw new ElementoExistenteException(
                    "Ya existe una categoría con el nombre "
                            + categoria.getNombre()
            );

        } catch (ElementoNoEncontradoException e) {
            // No existe ninguna categoría con ese nombre.
            // Podemos continuar con la inserción.
        }

        String sql = "INSERT INTO categoria (nombre, predefinida) VALUES (?, ?)";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.isPredefinida() ? 1 : 0);

            int filasInsertadas = statement.executeUpdate();

            if (filasInsertadas != 1) {
                throw new SQLException(
                        "No se ha podido insertar la categoría"
                );
            }

            // Recuperamos el identificador generado por SQLite.
            try (Statement consultaId = conexion.createStatement();
                 ResultSet resultado = consultaId.executeQuery(
                         "SELECT last_insert_rowid()")) {

                if (resultado.next()) {
                    categoria.setId(resultado.getInt(1));
                } else {
                    throw new SQLException(
                            "No se ha podido recuperar el ID de la categoría"
                    );
                }
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }
    }

    private Categoria mapearCategoria(ResultSet resultado) throws SQLException {

        Integer id = resultado.getInt("id");
        String nombre = resultado.getString("nombre");
        boolean predefinida = resultado.getInt("predefinida") != 0;

        return new Categoria(id, nombre, predefinida);
    }
}