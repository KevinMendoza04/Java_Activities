package com.mycompany.gestionproductos.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precioBase;
    private int cantidadDisponible;
    private boolean activo;

    public Producto(String codigo, String nombre, double precioBase, int cantidadDisponible, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.cantidadDisponible = cantidadDisponible;
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public abstract double calcularPrecioFinal();

    public double calcularValorEnInventario() {
        return calcularPrecioFinal() * cantidadDisponible;
    }

    public String mostrarInformacion() {
        return "Código: " + codigo
                + "\nNombre: " + nombre
                + "\nPrecio base: " + precioBase
                + "\nPrecio final: " + calcularPrecioFinal()
                + "\nCantidad disponible: " + cantidadDisponible
                + "\nActivo: " + activo;
    }
}