package tds.gastos;

import tds.gastos.vista.SceneManager;

/**
 * Guarda los objetos compartidos de la aplicación (controladores, SceneManager...).
 * Se accede desde cualquier sitio con Configuracion.getInstancia().
 */
public abstract class Configuracion {

    private static Configuracion instancia;

    private final SceneManager sceneManager = new SceneManager();

    // Solo se llama desde App al arrancar
    static void setInstancia(Configuracion impl) {
        instancia = impl;
    }

    public static Configuracion getInstancia() {
        return instancia;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    // Aquí irá getControladorGastos() cuando esté el modelo
}