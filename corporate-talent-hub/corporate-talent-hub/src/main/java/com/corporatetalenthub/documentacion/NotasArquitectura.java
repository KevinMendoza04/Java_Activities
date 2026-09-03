package com.corporatetalenthub.documentacion;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * Notas de arquitectura — Corporate Talent Hub
 * Java 8 (Legacy) vs Java 17/21 (LTS moderno)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ── 1. REPRESENTACIÓN DE DATOS ───────────────────────────────────────────
 *
 * Java 8 (Legacy):
 *   Los datos se representaban con clases POJO (Plain Old Java Object).
 *   Cada clase necesitaba: campos privados, constructor, getters, setters,
 *   equals(), hashCode() y toString() escritos a mano → ~50 líneas por entidad.
 *   Riesgo: olvidar un campo en equals() o hashCode() introduce bugs silenciosos.
 *
 * Java 17/21 (LTS moderno) — Records:
 *   Un Record declara sus componentes en una sola línea; el compilador genera
 *   todo el boilerplate automáticamente.
 *   La inmutabilidad está garantizada por diseño: no existen setters.
 *   Uso correcto: DTOs de solo lectura (ResultSet → EmpleadoReport).
 *   Cuando se necesita mutabilidad (Empleado con bono actualizable), se
 *   sigue usando la clase tradicional — no existe una solución única.
 *
 * ── 2. PERSISTENCIA JDBC ─────────────────────────────────────────────────
 *
 * Java 8 (Legacy) — Cierre manual en finally:
 *   Connection conn = null;
 *   try { conn = ... } finally { if (conn != null) conn.close(); }
 *   Problema: si close() lanza una excepción, los recursos posteriores
 *   no se cierran → Memory Leak (la conexión TCP con la BD queda abierta).
 *   El Garbage Collector NO cierra conexiones JDBC; solo recupera la
 *   memoria del objeto Java. El socket de red permanece abierto hasta
 *   que el servidor BD lo expire por timeout.
 *
 * Java 17/21 (LTS moderno) — try-with-resources:
 *   try (Connection c = ...; PreparedStatement ps = ...; ResultSet rs = ...) { }
 *   El compilador inserta el cierre garantizado en orden inverso (rs → ps → c).
 *   Si el bloque y el close() lanzan excepciones simultáneamente, Java conserva
 *   ambas (Suppressed Exceptions), facilitando el diagnóstico.
 *   Ventaja clave: es imposible olvidar cerrar un recurso.
 *
 * ── 3. SEGURIDAD — SQL Injection ─────────────────────────────────────────
 *
 * Statement (NUNCA en producción):
 *   String sql = "SELECT * FROM empleados WHERE nombre = '" + nombre + "'";
 *   Si nombre = "' OR '1'='1", devuelve todos los registros.
 *   Si nombre = "'; DROP TABLE empleados; --", destruye la tabla.
 *
 * PreparedStatement (obligatorio):
 *   "SELECT * FROM empleados WHERE nombre = ?"
 *   El driver separa SQL de datos en el protocolo de red: los valores son
 *   siempre datos, nunca código SQL ejecutable. Además, la consulta se
 *   pre-compila, mejorando el rendimiento en ejecuciones repetidas.
 *
 * ── 4. PATRÓN MVC — Separación de responsabilidades ──────────────────────
 *
 * Java 8 (Legacy anti-patrón):
 *   Era común encontrar Scanner, lógica SQL y System.out.println en el
 *   mismo método main. Imposible de mantener, probar o escalar.
 *
 * Java 17/21 con MVC:
 *   Model      (com.riwi.talent.model):
 *     Entidades (Empleado, Persona, Records), DAO (interfaz + impl), DatabaseConnection.
 *     No sabe que existe una consola ni un usuario.
 *
 *   Controller (com.riwi.talent.controller):
 *     Orquesta el flujo: valida entrada, coordina con el DAO, devuelve mensajes.
 *     No contiene Scanner ni System.out.println.
 *     Depende de la interfaz EmpleadoDAO (no de la implementación concreta):
 *     permite cambiar H2 por PostgreSQL sin modificar el Controller.
 *
 *   View       (com.riwi.talent.view):
 *     Único punto de I/O con el usuario (Scanner, System.out).
 *     No contiene lógica de negocio ni acceso directo a datos.
 *     Cambiar la interfaz (consola → Swing → REST) solo afecta esta capa.
 *
 * ── 5. RECORDS + JDBC MODERNO vs POJO JAVA 8 ─────────────────────────────
 *
 * Flujo con POJO (Java 8):
 *   ResultSet → new EmpleadoPOJO() → setId() → setNombre() → ... (mutable)
 *   Cualquier capa podría modificar el objeto en tránsito sin querer.
 *
 * Flujo con Record (Java 17/21):
 *   ResultSet → new EmpleadoReport(id, nombre, salario, ...) (inmutable)
 *   Imposible corromper el objeto en tránsito: no existen setters.
 *   El Controller recibe datos seguros y la Vista los presenta directamente.
 *   Agregar un campo al reporte requiere editar solo la firma del Record,
 *   no 4 lugares distintos como con el POJO.
 *
 * ── 6. JVM, HEAP Y GARBAGE COLLECTOR ─────────────────────────────────────
 *
 *   javac compila el código fuente a bytecode (.class).
 *   La JVM carga y ejecuta ese bytecode en cualquier sistema operativo.
 *   Los objetos creados con new se alojan en el Heap.
 *   Cuando un objeto deja de ser alcanzable desde referencias activas,
 *   el Garbage Collector recupera su memoria automáticamente.
 *   El programador NO puede garantizar el instante exacto de recolección,
 *   por eso try-with-resources es indispensable para recursos externos
 *   como conexiones JDBC: el GC no los cierra, solo libera la memoria Java.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public final class NotasArquitectura {

    private NotasArquitectura() {
        // Clase de documentación técnica: no debe instanciarse.
    }
}
