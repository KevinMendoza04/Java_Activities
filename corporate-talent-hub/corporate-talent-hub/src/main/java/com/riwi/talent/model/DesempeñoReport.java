package com.riwi.talent.model;

/**
 * Record inmutable para reportes de desempeño trimestral.
 * Java genera constructor, accesores, equals, hashCode y toString.
 */
public record DesempeñoReport(int idEmpleado, double promedio, String feedback) {}
