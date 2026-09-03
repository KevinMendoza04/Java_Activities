package com.riwi.talent.model;

public final class Desarrollador extends Empleado {
    private String lenguajePrincipal;

    public Desarrollador(byte nivelAcceso, short anioIngreso, int idEmpleado,
            long numeroDocumento, float puntajeTest, double salarioBase,
            char tipoContrato, boolean esActivo, String nombre, int edad,
            int idSede, double bonoMensual, String lenguajePrincipal) {
        super(nivelAcceso, anioIngreso, idEmpleado, numeroDocumento, puntajeTest,
                salarioBase, tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
        this.lenguajePrincipal = lenguajePrincipal;
    }

    public String getLenguajePrincipal() { return lenguajePrincipal; }
}
