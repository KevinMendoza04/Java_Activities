package com.corporatetalenthub.modelo;

public interface Promocionable {
    double calcularBonoAscenso();

    // Método default: permite agregar funcionalidad sin romper clases existentes.
    default void registrarPromocion() {
        System.out.println("Promocion registrada en el sistema.");
    }
}