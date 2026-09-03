package com.riwi.talent.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 1 — Gestión de conexiones y recursos: Legacy vs Modern
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * JAVA 8 LEGACY — Cierre manual en bloque finally
 * ──────────────────────────────────────────────────────────────────────────
 * En versiones anteriores a Java 7, el desarrollador cerraba cada recurso
 * manualmente dentro de un bloque finally. El patrón era:
 *
 *   Connection conn = null;
 *   PreparedStatement ps = null;
 *   ResultSet rs = null;
 *   try {
 *       conn = DriverManager.getConnection(URL, USER, PASS);
 *       ps   = conn.prepareStatement("SELECT * FROM empleados");
 *       rs   = ps.executeQuery();
 *       // ... procesar resultados ...
 *   } catch (SQLException e) {
 *       e.printStackTrace();
 *   } finally {
 *       // Cierre manual — propenso a errores:
 *       // 1. Si rs.close() lanza otra excepción, ps y conn nunca se cierran.
 *       // 2. Cada recurso requiere su propio try/catch interno.
 *       // 3. El código se vuelve largo y difícil de mantener.
 *       if (rs   != null) { try { rs.close();   } catch (SQLException ignored) {} }
 *       if (ps   != null) { try { ps.close();   } catch (SQLException ignored) {} }
 *       if (conn != null) { try { conn.close();  } catch (SQLException ignored) {} }
 *   }
 *
 * PROBLEMA PRINCIPAL — Memory Leaks (fugas de memoria):
 *   Si una excepción ocurría antes del bloque finally (o dentro de él), los
 *   recursos JDBC quedaban abiertos indefinidamente. Las conexiones a BD son
 *   costosas: un pool de conexiones agotado bloquea toda la aplicación.
 *   El Garbage Collector NO cierra conexiones JDBC automáticamente; solo
 *   recupera la memoria del objeto Java, pero el socket TCP con la BD queda
 *   abierto hasta que el servidor lo expira por timeout.
 *
 * ──────────────────────────────────────────────────────────────────────────
 * JAVA 17/21 MODERN — try-with-resources (implementado en este proyecto)
 * ──────────────────────────────────────────────────────────────────────────
 * Desde Java 7, cualquier clase que implemente AutoCloseable puede declararse
 * en el encabezado del try. El compilador genera automáticamente el bloque
 * finally con el cierre correcto de cada recurso, en orden inverso al de
 * apertura, garantizando que TODOS se cierren incluso si ocurre una excepción.
 *
 * Ventajas concretas frente al enfoque legacy:
 *   1. PREVIENE Memory Leaks: el cierre es garantizado por el compilador,
 *      no depende de que el desarrollador no olvide ningún recurso.
 *   2. Código más corto y legible: elimina decenas de líneas de boilerplate.
 *   3. Cierre en orden inverso: rs → ps → conn, evitando estados inválidos.
 *   4. Supressed Exceptions: si el try y el close lanzan excepciones a la
 *      vez, Java conserva ambas (la original como principal, la del close
 *      como suprimida), facilitando el diagnóstico.
 *
 * Patrón moderno utilizado en EmpleadoDAOImpl:
 *   try (Connection conn = DatabaseConnection.getConnection();
 *        PreparedStatement ps = conn.prepareStatement(SQL);
 *        ResultSet rs = ps.executeQuery()) {
 *       // ... procesar resultados ...
 *   }   // <- conn, ps y rs se cierran automáticamente aquí
 * ═══════════════════════════════════════════════════════════════════════════
 */
public final class DatabaseConnection {

    // URL de H2 en modo archivo: los datos persisten en disco entre ejecuciones.
    // "AUTO_SERVER=TRUE" permite acceso concurrente si se desea.
    private static final String URL  = "jdbc:h2:./talent_hub;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    /** SQL para crear la tabla si no existe (ejecución inicial). */
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS empleados (
                id_empleado      INT PRIMARY KEY,
                nombre           VARCHAR(100) NOT NULL,
                edad             INT,
                nivel_acceso     TINYINT,
                anio_ingreso     SMALLINT,
                numero_documento BIGINT,
                puntaje_test     REAL,
                salario_base     DOUBLE,
                tipo_contrato    CHAR(1),
                es_activo        BOOLEAN,
                id_sede          INT,
                bono_mensual     DOUBLE
            )
            """;

    // Clase de utilidad: no debe instanciarse.
    private DatabaseConnection() {}

    /**
     * Devuelve una nueva conexión JDBC a la base de datos H2.
     *
     * El llamador es responsable de cerrarla — idealmente con try-with-resources,
     * que garantiza el cierre automático (ver comentario de clase).
     *
     * @return Connection lista para usar.
     * @throws SQLException si el driver no está disponible o la URL es incorrecta.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Inicializa el esquema de la base de datos.
     * Se llama una sola vez al arrancar la aplicación (desde App.java).
     *
     * Usa try-with-resources: Connection y Statement se cierran automáticamente
     * al salir del bloque, sin importar si ocurre una excepción.
     */
    public static void inicializarEsquema() {
        // ── MODERN: try-with-resources ───────────────────────────────────────
        // Connection implementa AutoCloseable → el compilador inserta el cierre
        // garantizado, igual que si hubiera un finally explícito pero sin el
        // riesgo de olvidar algún recurso.
        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement()) {

            stmt.execute(CREATE_TABLE_SQL);
            System.out.println("[DB] Esquema inicializado correctamente.");

        } catch (SQLException e) {
            // En producción se usaría un logger (SLF4J/Logback).
            System.err.println("[DB] Error al inicializar el esquema: " + e.getMessage());
        }
        // Al salir del try, stmt.close() y conn.close() son invocados
        // automáticamente por la JVM en orden inverso (stmt primero, conn después).
    }
}
