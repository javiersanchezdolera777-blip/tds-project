module es.um.tds.gastos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires com.google.common;

    // FXML necesita acceder a los controladores de las vistas
   // opens tds.gastos.vista to javafx.fxml;

    // Las tablas de JavaFX leen las propiedades del modelo
   // opens tds.gastos.negocio.modelo to javafx.base;

    exports tds.gastos;
}