package tds.gastos.adapters.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import tds.gastos.adapters.repository.exceptions.ErrorPersistenciaException;

public class BaseDatosSQLite {

    private static final String URL_PREDETERMINADA =
            "jdbc:sqlite:gestion-gastos.db";

    private final String url;

    public BaseDatosSQLite() throws ErrorPersistenciaException {
        this(URL_PREDETERMINADA);
    }

    public BaseDatosSQLite(String url) throws ErrorPersistenciaException {

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "La URL de la base de datos no puede estar vacía"
            );
        }

        this.url = url;
        crearTablas();
    }

    public Connection getConnection() throws SQLException {

        Connection conexion = DriverManager.getConnection(url);

        try (Statement statement = conexion.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");

            return conexion;

        } catch (SQLException e) {

            try {
                conexion.close();
            } catch (SQLException errorCierre) {
                e.addSuppressed(errorCierre);
            }

            throw e;
        }
    }

    private void crearTablas() throws ErrorPersistenciaException {

        String sqlCategoria = "CREATE TABLE IF NOT EXISTS categoria ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "predefinida INTEGER NOT NULL"
                + ")";

        String sqlGasto = "CREATE TABLE IF NOT EXISTS gasto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "importe REAL NOT NULL, "
                + "fecha TEXT NOT NULL, "
                + "descripcion TEXT NOT NULL, "
                + "categoria_id INTEGER NOT NULL, "
                + "FOREIGN KEY (categoria_id) REFERENCES categoria(id)"
                + ")";

        try (Connection conexion = getConnection();
             Statement statement = conexion.createStatement()) {

            statement.execute(sqlCategoria);
            statement.execute(sqlGasto);

        } catch (SQLException e) {
            throw new ErrorPersistenciaException(e);
        }
    }
}