package tds.gastos.vista;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controlador de la ventana principal (tabla de gastos).
 */
public class VentanaPrincipalController {

    private static final Logger log = LogManager.getLogger();

    @FXML private TableView<?> tablaGastos;
    @FXML private TableColumn<?, ?> colFecha;
    @FXML private TableColumn<?, ?> colCantidad;
    @FXML private TableColumn<?, ?> colCategoria;
    @FXML private TableColumn<?, ?> colDescripcion;

    // JavaFX lo llama solo después de cargar el FXML
    @FXML
    private void initialize() {
        log.info("Ventana principal cargada");
    }

    @FXML
    private void nuevoGasto() {
        log.info("Nuevo gasto (pendiente)");
    }

    @FXML
    private void editarGasto() {
        log.info("Editar gasto (pendiente)");
    }

    @FXML
    private void borrarGasto() {
        log.info("Borrar gasto (pendiente)");
    }

    @FXML
    private void salir() {
        Platform.exit();
    }
}