package com.mycompany.gestionproductos.controller;

import com.mycompany.gestionproductos.model.Producto;
import com.mycompany.gestionproductos.service.ProductoService;
import java.util.ArrayList;

public class ProductoController {

    // El controller no habla directo con el repository: siempre pasa por service.
    private ProductoService service = new ProductoService();

    public void registrarProducto(Producto producto) {
        service.registrarProducto(producto);
    }

    public ArrayList<Producto> listarProductos() {
        return service.listarProductos();
    }

    public Producto buscarProducto(String codigo) {
        return service.buscarProducto(codigo);
    }

    public void actualizarProducto(Producto producto) {
        service.actualizarProducto(producto);
    }

    public void eliminarProducto(String codigo) {
        service.eliminarProducto(codigo);
    }

    public int obtenerCantidadProductos() {
        return service.obtenerCantidadProductos();
    }

    public double calcularValorTotalInventario() {
        return service.calcularValorTotalInventario();
    }
}