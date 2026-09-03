package com.riwi.talent.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 2 — Implementación física de EmpleadoDAO
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * SEGURIDAD — PreparedStatement vs Statement (SQL Injection)
 * ──────────────────────────────────────────────────────────────────────────
 * Un ataque de SQL Injection ocurre cuando un valor ingresado por el usuario
 * contiene fragmentos SQL que se concatenan directamente en la consulta.
 * Ejemplo inseguro con Statement (NUNCA usar):
 *
 *   String sql = "SELECT * FROM empleados WHERE nombre = '" + nombre + "'";
 *   // Si nombre = "' OR '1'='1", la consulta devuelve TODOS los registros.
 *   // Si nombre = "'; DROP TABLE empleados; --", borra la tabla completa.
 *
 * Con PreparedStatement el driver separa el SQL de los datos:
 *   - El SQL se compila una sola vez con marcadores de posición (?).
 *   - Los valores se asignan con setXxx(): son tratados siempre como datos,
 *     NUNCA como código SQL, sin importar su contenido.
 *   - Adicionalmente, la consulta pre-compilada se ejecuta más rápido en
 *     consultas repetidas porque el motor no re-parsea el SQL cada vez.
 *
 * GESTIÓN DE RECURSOS — try-with-resources (Java 7+, obligatorio en LTS)
 * ──────────────────────────────────────────────────────────────────────────
 * Cada método que abre Connection, PreparedStatement o ResultSet los declara
 * en el encabezado del try. Al salir del bloque (normal o por excepción),
 * la JVM llama a close() en orden inverso, garantizando que NUNCA queden
 * conexiones abiertas (ver explicación completa en DatabaseConnection.java).
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class EmpleadoDAOImpl implements EmpleadoDAO {

    // ─── SQL pre-compiladas (constantes) ────────────────────────────────────
    // Definirlas como constantes evita errores tipográficos y facilita
    // el mantenimiento: si cambia la tabla, se edita un solo lugar.

    private static final String SQL_INSERTAR = """
            INSERT INTO empleados
              (id_empleado, nombre, edad, nivel_acceso, anio_ingreso,
               numero_documento, puntaje_test, salario_base, tipo_contrato,
               es_activo, id_sede, bono_mensual)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_LISTAR = """
            SELECT id_empleado, nombre, edad, nivel_acceso, anio_ingreso,
                   numero_documento, puntaje_test, salario_base, tipo_contrato,
                   es_activo, id_sede, bono_mensual
            FROM empleados
            ORDER BY id_empleado
            """;

    private static final String SQL_BUSCAR_POR_ID = """
            SELECT id_empleado, nombre, edad, nivel_acceso, anio_ingreso,
                   numero_documento, puntaje_test, salario_base, tipo_contrato,
                   es_activo, id_sede, bono_mensual
            FROM empleados
            WHERE id_empleado = ?
            """;

    private static final String SQL_ACTUALIZAR = """
            UPDATE empleados
               SET nombre        = ?,
                   salario_base  = ?,
                   bono_mensual  = ?,
                   es_activo     = ?
             WHERE id_empleado   = ?
            """;

    private static final String SQL_ELIMINAR = """
            DELETE FROM empleados WHERE id_empleado = ?
            """;

    // ─── Create ─────────────────────────────────────────────────────────────

    /**
     * C — Inserta un empleado usando PreparedStatement.
     *
     * Cada ? es reemplazado por su valor correspondiente mediante setXxx().
     * El driver escapa caracteres especiales automáticamente: SQL Injection
     * es imposible porque los datos NUNCA se concatenan al texto SQL.
     */
    @Override
    public boolean insertar(Empleado emp) {
        // try-with-resources: Connection y PreparedStatement se cierran solos.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERTAR)) {

            // Asignación posicional: índice 1 = primera ?, índice 2 = segunda ?, etc.
            ps.setInt(1,    emp.getIdEmpleado());
            ps.setString(2, emp.getNombre());
            ps.setInt(3,    emp.getEdad());
            ps.setByte(4,   emp.getNivelAcceso());
            ps.setShort(5,  emp.getAnioIngreso());
            ps.setLong(6,   emp.getNumeroDocumento());
            ps.setFloat(7,  emp.getPuntajeTest());
            ps.setDouble(8, emp.getSalarioBase());
            ps.setString(9, String.valueOf(emp.getTipoContrato()));
            ps.setBoolean(10, emp.isEsActivo());
            ps.setInt(11,   emp.getIdSede());
            ps.setDouble(12, emp.getBonoMensual());

            // executeUpdate() devuelve el número de filas afectadas.
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error al insertar empleado: " + e.getMessage());
            return false;
        }
        // Al salir del bloque try: ps.close() → conn.close() (orden inverso).
    }

    // ─── Read (lista completa) ───────────────────────────────────────────────

    /**
     * R — Lista todos los empleados.
     *
     * ResultSet también es AutoCloseable: se declara dentro del try-with-resources
     * para garantizar su cierre aunque ocurra una excepción mientras se itera.
     */
    @Override
    public List<Empleado> listar() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {          // ResultSet incluido

            while (rs.next()) {
                empleados.add(mapearFila(rs));
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error al listar empleados: " + e.getMessage());
        }

        return empleados;
    }

    // ─── Read (por ID) ───────────────────────────────────────────────────────

    /**
     * R — Busca un empleado por ID.
     *
     * Devuelve Optional para evitar NullPointerException en la capa Controller:
     * obliga al llamador a manejar explícitamente el caso "no encontrado".
     * Esta práctica es idiomática en Java 8+ y sigue siendo la recomendada en LTS.
     */
    @Override
    public Optional<Empleado> buscarPorId(int idEmpleado) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, idEmpleado);   // Único parámetro: la clave primaria

            try (ResultSet rs = ps.executeQuery()) {
                // ResultSet dentro del try anidado: se cierra antes que ps y conn.
                if (rs.next()) {
                    return Optional.of(mapearFila(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error al buscar empleado: " + e.getMessage());
        }

        return Optional.empty();
    }

    // ─── Update ─────────────────────────────────────────────────────────────

    /**
     * U — Actualiza nombre, salario, bono y estado activo de un empleado.
     *
     * La cláusula WHERE id_empleado = ? garantiza que solo se modifica la fila
     * exacta; sin ella, un bug silencioso actualizaría toda la tabla.
     * PreparedStatement hace que ese ? sea siempre tratado como dato, nunca
     * como un fragmento SQL que pueda alterar la lógica de la cláusula WHERE.
     */
    @Override
    public boolean actualizar(Empleado emp) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1,  emp.getNombre());
            ps.setDouble(2,  emp.getSalarioBase());
            ps.setDouble(3,  emp.getBonoMensual());
            ps.setBoolean(4, emp.isEsActivo());
            ps.setInt(5,     emp.getIdEmpleado());   // WHERE

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    // ─── Delete ─────────────────────────────────────────────────────────────

    /**
     * D — Elimina un empleado por su ID.
     *
     * Solo el ID es necesario; el PreparedStatement garantiza que no puede
     * inyectarse código adicional independientemente del valor recibido.
     */
    @Override
    public boolean eliminar(int idEmpleado) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, idEmpleado);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    // ─── Método auxiliar de mapeo ────────────────────────────────────────────

    /**
     * Convierte una fila del ResultSet en un objeto Empleado.
     *
     * Centralizar el mapeo aquí evita duplicar la lectura de columnas en cada
     * método READ, reduciendo el riesgo de inconsistencias (si cambia un nombre
     * de columna, se actualiza en un solo lugar).
     *
     * TASK 4: Este método es el equivalente manual al mapeo automático que
     * ofrecen ORMs como Hibernate. Los Records (EmpleadoReport) complementan
     * este enfoque para consultas de solo lectura (ver EmpleadoReport.java).
     *
     * @param rs ResultSet posicionado en la fila a leer
     * @return Empleado construido con los datos de esa fila
     */
    private Empleado mapearFila(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getByte("nivel_acceso"),
            rs.getShort("anio_ingreso"),
            rs.getInt("id_empleado"),
            rs.getLong("numero_documento"),
            rs.getFloat("puntaje_test"),
            rs.getDouble("salario_base"),
            rs.getString("tipo_contrato").charAt(0),
            rs.getBoolean("es_activo"),
            rs.getString("nombre"),
            rs.getInt("edad"),
            rs.getInt("id_sede"),
            rs.getDouble("bono_mensual")
        );
    }
}
