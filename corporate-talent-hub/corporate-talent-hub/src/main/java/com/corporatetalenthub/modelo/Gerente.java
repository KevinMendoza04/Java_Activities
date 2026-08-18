package com.corporatetalenthub.modelo;

public final class Gerente extends Empleado {
    private double presupuestoMensual;

    public Gerente(byte nivelAcceso, short anioIngreso, int idEmpleado,
            long numeroDocumento, float puntajeTest, double salarioBase,
            char tipoContrato, boolean esActivo, String nombre, int edad,
            int idSede, double bonoMensual, double presupuestoMensual) {
        super(nivelAcceso, anioIngreso, idEmpleado, numeroDocumento, puntajeTest,
                salarioBase, tipoContrato, esActivo, nombre, edad, idSede, bonoMensual);
        this.presupuestoMensual = presupuestoMensual;
    }

    public double getPresupuestoMensual() { return presupuestoMensual; }
}