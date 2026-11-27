package tds.gastos.adapters.repository.impl;

import java.util.List;

import tds.gastos.adapters.repository.exceptions.ElementoExistenteException;
import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.adapters.repository.exceptions.ErrorPersistenciaException;
import tds.gastos.negocio.modelo.Categoria;

public interface CategoriaRepository {

    /**
     * Devuelve todas las categorías cargadas.
     *
     * @return Lista de categorías.
     */
    List<Categoria> getCategorias()
    		throws ErrorPersistenciaException;

    /**
     * Busca una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     * @return Categoría encontrada.
     * @throws ElementoNoEncontradoException si no existe.
     * @throws ErrorPersistenciaException 
     */
    Categoria findById(Integer id)
            throws ElementoNoEncontradoException, ErrorPersistenciaException;

    /**
     * Busca una categoría por su nombre.
     *
     * @param nombre Nombre de la categoría.
     * @return Categoría encontrada.
     * @throws ElementoNoEncontradoException si no existe.
     */
    Categoria findByNombre(String nombre)
            throws ElementoNoEncontradoException,
             ErrorPersistenciaException;

    /**
     * Añade una categoría al repositorio.
     *
     * @param categoria Categoría que se desea añadir.
     * @throws ElementoExistenteException si ya existe una categoría equivalente.
     * @throws ErrorPersistenciaException si ocurre un error al persistirla.
     */
    void addCategoria(Categoria categoria)
            throws ElementoExistenteException, ErrorPersistenciaException;
}
