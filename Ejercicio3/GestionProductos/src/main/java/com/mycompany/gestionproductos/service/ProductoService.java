package com.mycompany.gestionproductos.service;

import com.mycompany.gestionproductos.model.Producto;
import com.mycompany.gestionproductos.repository.ProductoRepository;
import java.util.ArrayList;

public class ProductoService {

    private ProductoRepository repository = new ProductoRepository();

    public void registrarProducto(Producto producto) {
        if (producto.getCodigo() == null || producto.getCodigo().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (producto.getPrecioBase() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        if (producto.getCantidadDisponible() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
        }
        if (repository.existeCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con ese código");
        }
        repository.guardar(producto);
    }

    public ArrayList<Producto> listarProductos() {
        return repository.listarTodos();
    }

    public Producto buscarProducto(String codigo) {
        Producto producto = repository.buscarPorCodigo(codigo);
        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto con ese código");
        }
        return producto;
    }

    public void actualizarProducto(Producto producto) {
        buscarProducto(producto.getCodigo());
        repository.eliminarPorCodigo(producto.getCodigo());
        repository.guardar(producto);
    }

    public void eliminarProducto(String codigo) {
        boolean eliminado = repository.eliminarPorCodigo(codigo);
        if (!eliminado) {
            throw new IllegalArgumentException("No existe un producto con ese código");
        }
    }

    public int obtenerCantidadProductos() {
        return repository.listarTodos().size();
    }

    public double calcularValorTotalInventario() {
        double total = 0;
       for (Producto producto : repository.listarTodos()) {
            total += producto.calcularValorEnInventario();
        }
        return total;
    }
}