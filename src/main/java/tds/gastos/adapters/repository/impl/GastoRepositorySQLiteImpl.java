package tds.gastos.adapters.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;

import tds.gastos.adapters.repository.GastoRepository;
import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.adapters.repository.exceptions.ErrorPersistenciaException;
import tds.gastos.negocio.modelo.Categoria;
import tds.gastos.negocio.modelo.Gasto;

public class GastoRepositorySQLiteImpl implements GastoRepository {

    private final BaseDatosSQLite baseDatos;

    private static final String CONSULTA_GASTOS =
            "SELECT g.id AS gasto_id, g.importe, g.fecha, "
            + "g.descripcion, c.id AS categoria_id, "
            + "c.nombre AS categoria_nombre, "
            + "c.predefinida AS categoria_predefinida "
            + "FROM gasto g "
            + "INNER JOIN categoria c ON g.categoria_id = c.id ";

    public GastoRepositorySQLiteImpl(BaseDatosSQLite baseDatos) {
        this.baseDatos = baseDatos;
    }

    @Override
    public List<Gasto> getGastos() throws ErrorPersistenciaException {

        String sql = CONSULTA_GASTOS + "ORDER BY g.id";

        List<Gasto> gastos = new ArrayList<>();

        try (Connection conexion = baseDatos.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery(sql)) {

            while (resultado.next()) {
                gastos.add(mapearGasto(resultado));
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }

        return gastos;
    }

    @Override
    public Gasto findById(Integer id)
            throws ElementoNoEncontradoException,
                   ErrorPersistenciaException {

        String sql = CONSULTA_GASTOS + "WHERE g.id = ?";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return mapearGasto(resultado);
                }
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }

        throw new ElementoNoEncontradoException(
                "No se ha encontrado el gasto con ID " + id
        );
    }

    @Override
    public void addGasto(Gasto gasto)
            throws ErrorPersistenciaException {

        if (gasto.getCategoria().getId() == null) {
            throw new IllegalArgumentException(
                    "No se puede registrar un gasto con una categoría sin persistir"
            );
        }

        String sql = "INSERT INTO gasto "
                + "(importe, fecha, descripcion, categoria_id) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDouble(1, gasto.getImporte());
            statement.setString(2, gasto.getFecha().toString());
            statement.setString(3, gasto.getDescripcion());
            statement.setInt(4, gasto.getCategoria().getId());

            int filasInsertadas = statement.executeUpdate();

            if (filasInsertadas != 1) {
                throw new SQLException(
                        "No se ha podido insertar el gasto"
                );
            }

            try (Statement consultaId = conexion.createStatement();
                 ResultSet resultado = consultaId.executeQuery(
                         "SELECT last_insert_rowid()")) {

                if (resultado.next()) {
                    gasto.setId(resultado.getInt(1));
                } else {
                    throw new SQLException(
                            "No se ha podido recuperar el ID del gasto"
                    );
                }
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }
    }

    @Override
    public void removeGasto(Gasto gasto)
            throws ElementoNoEncontradoException,
                   ErrorPersistenciaException {

        if (gasto.getId() == null) {
            throw new ElementoNoEncontradoException(
                    "No se puede eliminar un gasto sin identificador"
            );
        }

        String sql = "DELETE FROM gasto WHERE id = ?";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, gasto.getId());

            int filasEliminadas = statement.executeUpdate();

            if (filasEliminadas == 0) {
                throw new ElementoNoEncontradoException(
                        "No se ha encontrado el gasto con ID " + gasto.getId()
                );
            }

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }
    }

    @Override
    public Gasto updateGasto(Gasto gasto)
            throws ElementoNoEncontradoException,
                   ErrorPersistenciaException {

        if (gasto.getId() == null) {
            throw new ElementoNoEncontradoException(
                    "No se puede actualizar un gasto sin identificador"
            );
        }

        if (gasto.getCategoria().getId() == null) {
            throw new IllegalArgumentException(
                    "El gasto debe tener una categoría persistida"
            );
        }

        String sql = "UPDATE gasto SET "
                + "importe = ?, "
                + "fecha = ?, "
                + "descripcion = ?, "
                + "categoria_id = ? "
                + "WHERE id = ?";

        try (Connection conexion = baseDatos.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDouble(1, gasto.getImporte());
            statement.setString(2, gasto.getFecha().toString());
            statement.setString(3, gasto.getDescripcion());
            statement.setInt(4, gasto.getCategoria().getId());
            statement.setInt(5, gasto.getId());

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas == 0) {
                throw new ElementoNoEncontradoException(
                        "No se ha encontrado el gasto con ID " + gasto.getId()
                );
            }

            return gasto;

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }
    }

    private Gasto mapearGasto(ResultSet resultado) throws SQLException {

        Integer idGasto = resultado.getInt("gasto_id");
        double importe = resultado.getDouble("importe");
        String fechaTexto = resultado.getString("fecha");
        String descripcion = resultado.getString("descripcion");

        Integer idCategoria = resultado.getInt("categoria_id");
        String nombreCategoria = resultado.getString("categoria_nombre");
        boolean predefinida =
                resultado.getInt("categoria_predefinida") != 0;

        LocalDateTime fecha;

        try {
            fecha = LocalDateTime.parse(fechaTexto);
        } catch (DateTimeParseException e) {
            throw new SQLException(
                    "La fecha almacenada del gasto no tiene un formato válido",
                    e
            );
        }

        Categoria categoria = new Categoria(
                idCategoria,
                nombreCategoria,
                predefinida
        );

        return new Gasto(
                idGasto,
                importe,
                fecha,
                categoria,
                descripcion
        );
    }
}