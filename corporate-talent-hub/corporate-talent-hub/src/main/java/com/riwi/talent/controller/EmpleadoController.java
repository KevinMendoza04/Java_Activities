package com.riwi.talent.controller;

import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpleadoDAO;
import com.riwi.talent.model.EmpleadoDAOImpl;

import java.util.List;
import java.util.Optional;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 3 — Controlador MVC: EmpleadoController
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * RESPONSABILIDAD EN EL PATRÓN MVC:
 * ──────────────────────────────────────────────────────────────────────────
 *   Model      → Entidades + DAO  (com.riwi.talent.model)
 *   View       → I/O de consola   (com.riwi.talent.view)
 *   Controller → ESTE ARCHIVO     (com.riwi.talent.controller)
 *
 * El Controller actúa como MEDIADOR entre Vista y Modelo:
 *   1. Recibe datos ya capturados por la Vista (sin usar Scanner aquí).
 *   2. Aplica las reglas de negocio o validaciones de flujo que sean necesarias.
 *   3. Delega la persistencia al DAO (Model).
 *   4. Devuelve resultados a la Vista para su presentación.
 *
 * Por qué esta separación importa (Legacy vs Modern):
 * ──────────────────────────────────────────────────────────────────────────
 *   JAVA 8 Legacy: era común mezclar Scanner, lógica SQL y System.out.println
 *   en un solo método main o clase "Dios". El resultado era código difícil
 *   de mantener, probar y escalar.
 *
 *   JAVA 17/21 LTS: la combinación MVC + Records + try-with-resources permite
 *   que cada capa sea independiente: la Vista puede cambiar (consola → web)
 *   sin tocar el Controller; el Controller puede cambiar de base de datos
 *   (H2 → PostgreSQL) sin tocar la Vista.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class EmpleadoController {

    // El Controller depende de la INTERFAZ, no de la implementación concreta.
    // Esto aplica el principio de inversión de dependencias (SOLID - D):
    // si mañana cambia la BD, basta con cambiar la instancia aquí.
    private final EmpleadoDAO dao;

    public EmpleadoController() {
        this.dao = new EmpleadoDAOImpl();
    }

    // Constructor alternativo que permite inyectar cualquier implementación
    // (útil para pruebas unitarias con mocks sin necesidad de BD real).
    public EmpleadoController(EmpleadoDAO dao) {
        this.dao = dao;
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo empleado en la base de datos.
     * El Controller valida que el ID no sea negativo antes de delegar al DAO.
     *
     * @return mensaje de resultado para que la Vista lo muestre
     */
    public String registrarEmpleado(Empleado empleado) {
        if (empleado.getIdEmpleado() <= 0) {
            return "ERROR: El ID del empleado debe ser un numero positivo.";
        }
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            return "ERROR: El nombre del empleado no puede estar vacio.";
        }

        // Verificar si ya existe un empleado con ese ID
        Optional<Empleado> existente = dao.buscarPorId(empleado.getIdEmpleado());
        if (existente.isPresent()) {
            return "ERROR: Ya existe un empleado con ID " + empleado.getIdEmpleado();
        }

        boolean exito = dao.insertar(empleado);
        return exito
                ? "Empleado '" + empleado.getNombre() + "' registrado correctamente."
                : "ERROR: No se pudo registrar el empleado.";
    }

    // ─── READ ────────────────────────────────────────────────────────────────

    /**
     * Devuelve la lista completa de empleados desde la BD.
     * La Vista decide cómo presentar el resultado.
     */
    public List<Empleado> obtenerTodos() {
        return dao.listar();
    }

    /**
     * Busca un empleado por ID.
     * Devuelve Optional para forzar a la Vista a manejar el caso "no encontrado"
     * sin riesgo de NullPointerException.
     */
    public Optional<Empleado> buscarPorId(int idEmpleado) {
        if (idEmpleado <= 0) {
            return Optional.empty();
        }
        return dao.buscarPorId(idEmpleado);
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    /**
     * Actualiza los datos de un empleado existente.
     * Solo actualiza si el empleado ya existe en la BD.
     *
     * @return mensaje de resultado para la Vista
     */
    public String actualizarEmpleado(Empleado empleado) {
        Optional<Empleado> existente = dao.buscarPorId(empleado.getIdEmpleado());
        if (existente.isEmpty()) {
            return "ERROR: No se encontro ningun empleado con ID " + empleado.getIdEmpleado();
        }

        boolean exito = dao.actualizar(empleado);
        return exito
                ? "Empleado ID " + empleado.getIdEmpleado() + " actualizado correctamente."
                : "ERROR: No se pudo actualizar el empleado.";
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    /**
     * Elimina un empleado por ID.
     * Verifica su existencia antes de intentar el borrado.
     *
     * @return mensaje de resultado para la Vista
     */
    public String eliminarEmpleado(int idEmpleado) {
        Optional<Empleado> existente = dao.buscarPorId(idEmpleado);
        if (existente.isEmpty()) {
            return "ERROR: No se encontro ningun empleado con ID " + idEmpleado;
        }

        boolean exito = dao.eliminar(idEmpleado);
        return exito
                ? "Empleado ID " + idEmpleado + " eliminado correctamente."
                : "ERROR: No se pudo eliminar el empleado.";
    }
}
