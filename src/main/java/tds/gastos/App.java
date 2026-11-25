package tds.gastos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Configuracion configuracion = new ConfiguracionMemoriaImpl();
        Configuracion.setInstancia(configuracion);

        configuracion.getSceneManager().inicializar(stage);
        configuracion.getSceneManager().showVentanaPrincipal();
    }

    public static void main(String[] args) {
        launch();
    }
}