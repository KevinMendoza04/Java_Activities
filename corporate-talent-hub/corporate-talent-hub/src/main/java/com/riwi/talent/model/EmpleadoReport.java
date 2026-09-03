package com.riwi.talent.model;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 4 — Record para transferencia de datos de solo lectura
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Un Record (Java 16+) es la forma idiomática de representar un DTO
 * (Data Transfer Object) inmutable. Java genera automáticamente:
 *   - Constructor canónico con todos los parámetros
 *   - Accesores (idEmpleado(), nombre(), salarioBase(), etc.)
 *   - equals() / hashCode() basados en los componentes
 *   - toString() legible
 *
 * ANÁLISIS MODERNO — Records + JDBC vs POJO de Java 8
 * ──────────────────────────────────────────────────────────────────────────
 * JAVA 8 (POJO tradicional):
 *   Para representar estos 6 campos se necesitaban ~50 líneas:
 *   - Declaración de campos privados
 *   - Constructor con asignaciones
 *   - 6 getters
 *   - equals() y hashCode() manuales (propensos a omisión de campos)
 *   - toString() manual
 *   Cualquier cambio (agregar un campo) requería actualizar 4 lugares distintos.
 *
 * JAVA 17/21 (Record):
 *   1 línea de declaración hace exactamente lo mismo.
 *   Agregar un campo solo requiere modificar la firma del record.
 *   La inmutabilidad es garantizada por el compilador: no hay setters
 *   que un compañero pueda invocar accidentalmente y corromper el estado.
 *
 * Combinación Record + JDBC moderno:
 *   - El DAO mapea la fila del ResultSet directamente al constructor del Record.
 *   - El Controller recibe un objeto inmutable y lo pasa a la Vista.
 *   - La Vista accede a los datos con accesores expresivos (nombre(), no getNombre()).
 *   - En ningún momento se puede modificar el objeto en tránsito: más seguro y
 *     fácil de razonar que un POJO mutable que cualquier capa podría alterar.
 *
 * Limitación: si el objeto necesita estado mutable (como Empleado con bono
 * actualizable), se sigue usando la clase tradicional. El Record es ideal
 * para resultados de consultas SELECT de solo lectura, exactamente este caso.
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Mapea los resultados de la consulta SELECT compleja que calcula el
 * salario final y la categoría salarial junto con el estado del empleado.
 */
public record EmpleadoReport(
        int    idEmpleado,
        String nombre,
        double salarioBase,
        double salarioFinal,       // calculado: (salarioBase + bono*1.10) - salarioBase*0.05
        String categoriaSalarial,  // derivado del switch expression en Empleado
        boolean esActivo) {

    /**
     * Representación compacta para logs o debug rápido.
     * Los Records generan un toString() automático, pero este
     * override da un formato más legible para reportes de consola.
     */
    @Override
    public String toString() {
        return "[%d] %s | Base: %,.2f | Final: %,.2f | %s | %s"
                .formatted(idEmpleado, nombre, salarioBase, salarioFinal,
                        categoriaSalarial, esActivo ? "ACTIVO" : "INACTIVO");
    }
}
