package com.riwi.talent.model;

import java.util.List;
import java.util.Optional;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 2 — Interfaz EmpleadoDAO (Data Access Object)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * El patrón DAO desacopla la lógica de negocio del acceso a datos.
 * La capa Controller conoce solo esta interfaz; no importa si mañana
 * cambiamos H2 por PostgreSQL: basta con crear una nueva implementación.
 *
 * Contrato CRUD completo:
 *   C → insertar   (Create)
 *   R → listar / buscarPorId  (Read)
 *   U → actualizar (Update)
 *   D → eliminar   (Delete)
 */
public interface EmpleadoDAO {

    /**
     * Inserta un nuevo empleado en la base de datos.
     *
     * @param empleado objeto a persistir (idEmpleado debe ser único)
     * @return true si la fila fue insertada, false en caso de error
     */
    boolean insertar(Empleado empleado);

    /**
     * Devuelve todos los empleados almacenados.
     *
     * @return lista (posiblemente vacía) de empleados
     */
    List<Empleado> listar();

    /**
     * Busca un empleado por su identificador primario.
     *
     * @param idEmpleado clave primaria
     * @return Optional con el empleado si existe, Optional.empty() si no
     */
    Optional<Empleado> buscarPorId(int idEmpleado);

    /**
     * Actualiza los campos modificables de un empleado existente.
     *
     * @param empleado objeto con los nuevos valores (idEmpleado identifica la fila)
     * @return true si al menos una fila fue actualizada
     */
    boolean actualizar(Empleado empleado);

    /**
     * Elimina el empleado con el identificador dado.
     *
     * @param idEmpleado clave primaria del empleado a eliminar
     * @return true si la fila fue eliminada
     */
    boolean eliminar(int idEmpleado);
}
