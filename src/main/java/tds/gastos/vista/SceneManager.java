package tds.gastos.vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tds.gastos.App;

/**
 * Se encarga de cargar las vistas FXML y de la navegación entre ellas.
 */
public class SceneManager {

    private Stage stage;
    private Scene escenaActual;

    public void inicializar(Stage stage) {
        this.stage = stage;
        stage.setTitle("Gestión de Gastos");
    }

    public void showVentanaPrincipal() {
        cargarYMostrar("VentanaPrincipal");
    }

    private void cargarYMostrar(String fxml) {
        try {
            Parent root = cargarFXML(fxml);
            if (escenaActual == null) {
                // Primera vez: se crea la escena y se muestra la ventana
                escenaActual = new Scene(root);
                stage.setScene(escenaActual);
                stage.show();
            } else {
                // Las siguientes veces solo se cambia el contenido
                escenaActual.setRoot(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido cargar " + fxml, e);
        }
    }

    private Parent cargarFXML(String fxml) throws IOException {
        // Busca el fichero en src/main/resources/tds/gastos/
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }
}