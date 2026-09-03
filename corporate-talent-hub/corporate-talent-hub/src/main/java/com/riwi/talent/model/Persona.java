package com.riwi.talent.model;

/**
 * Clase base sellada (sealed): solo Empleado y ConsultorExterno pueden heredar.
 * Esto protege el diseño del dominio — nadie externo puede crear
 * subclases inesperadas de Persona.
 */
public sealed class Persona permits Empleado, ConsultorExterno {
    protected String nombre;
    protected int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public int getEdad()     { return edad; }
}
