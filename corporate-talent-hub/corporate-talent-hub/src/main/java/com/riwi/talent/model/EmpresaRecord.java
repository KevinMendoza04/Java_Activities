package com.riwi.talent.model;

/**
 * Record (Java 16+): reduce la verbosidad al máximo.
 * Java genera automáticamente constructor canónico, accesores (nombre()),
 * equals, hashCode y toString. Sus componentes son INMUTABLES; no se pueden
 * reasignar después de construir el objeto.
 *
 * Comparativa con Java 8:
 * - Java 8: necesitaba una clase con ~40 líneas de boilerplate para representar
 *   estos 3 datos de solo lectura.
 * - Java 17/21: un Record de 1 línea hace exactamente lo mismo, con mayor
 *   claridad de intención (señala "este objeto es un dato inmutable").
 */
public record EmpresaRecord(
        String nombre,
        String nit,
        int anioFundacion) {
}
