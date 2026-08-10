package com.mycompany.gestionproductos.model;

public class ProductoFisico extends Producto {

    private double peso;
    private double costoEnvio;

    public ProductoFisico(String codigo, String nombre, double precioBase,
            int cantidadDisponible, boolean activo, double peso, double costoEnvio) {
        super(codigo, nombre, precioBase, cantidadDisponible, activo);
        this.peso = peso;
        this.costoEnvio = costoEnvio;
    }

    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }
    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    @Override
    public double calcularPrecioFinal() {
        double precioFinal = getPrecioBase() + costoEnvio;
        if (peso > 10) {
            double recargo = getPrecioBase() * 0.08;
            precioFinal += recargo;
        }
        return precioFinal;
    }
}