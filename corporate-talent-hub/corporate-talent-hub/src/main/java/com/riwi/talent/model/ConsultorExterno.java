package com.riwi.talent.model;

public final class ConsultorExterno extends Persona {
    private double tarifaDiaria;

    public ConsultorExterno(String nombre, int edad, double tarifaDiaria) {
        super(nombre, edad);
        this.tarifaDiaria = tarifaDiaria;
    }

    public double getTarifaDiaria() { return tarifaDiaria; }
}
