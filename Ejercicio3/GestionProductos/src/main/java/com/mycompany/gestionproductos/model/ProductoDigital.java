package com.mycompany.gestionproductos.model;

public class ProductoDigital extends Producto {

    private double tamanioArchivo;
    private String formato;

    public ProductoDigital(String codigo, String nombre, double precioBase,
            int cantidadDisponible, boolean activo, double tamanioArchivo, String formato) {
        super(codigo, nombre, precioBase, cantidadDisponible, activo);
        this.tamanioArchivo = tamanioArchivo;
        this.formato = formato;
    }

    public double getTamanioArchivo() {
        return tamanioArchivo;
    }
    public void setTamanioArchivo(double tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public String getFormato() {
        return formato;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }

    @Override
    public double calcularPrecioFinal() {
        double porcentajeDescuento = (tamanioArchivo > 5) ? 0.05 : 0.10;
        double descuento = getPrecioBase() * porcentajeDescuento;
        return getPrecioBase() - descuento;
    }
}