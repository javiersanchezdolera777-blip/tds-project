package tds.gastos.adapters.repository;

import java.util.List;

import tds.gastos.adapters.repository.exceptions.ElementoNoEncontradoException;
import tds.gastos.adapters.repository.exceptions.ErrorPersistenciaException;
import tds.gastos.negocio.modelo.Gasto;

public interface GastoRepository {

    /**
     * Devuelve una lista con todos los gastos cargados.
     *
     * @return Lista de gastos.
     */
    List<Gasto> getGastos()
    		throws ErrorPersistenciaException;

    /**
     * Busca un gasto por su identificador.
     *
     * @param id Identificador del gasto.
     * @return Gasto encontrado.
     * @throws ElementoNoEncontradoException si no existe.
     */
    Gasto findById(Integer id)
            throws ElementoNoEncontradoException,  ErrorPersistenciaException;

    /**
     * Añade un gasto al repositorio.
     *
     * @param gasto Gasto que se desea añadir.
     * @throws ErrorPersistenciaException si ocurre un error al persistirlo.
     */
    void addGasto(Gasto gasto)
            throws ErrorPersistenciaException;

    /**
     * Elimina un gasto del repositorio.
     * Si ya no existe, no realiza ninguna operación.
     *
     * @param gasto Gasto que se desea eliminar.
     * @throws ErrorPersistenciaException si ocurre un error de persistencia.
     */
    void removeGasto(Gasto gasto)
            throws ErrorPersistenciaException, ElementoNoEncontradoException;

    /**
     * Actualiza un gasto y devuelve la referencia al gasto actualizado.
     *
     * @param gasto Gasto con los nuevos valores.
     * @return Gasto actualizado.
     * @throws ErrorPersistenciaException si ocurre un error
     *         o el gasto no existe.
     */
    Gasto updateGasto(Gasto gasto)
            throws ErrorPersistenciaException, ElementoNoEncontradoException;
}
