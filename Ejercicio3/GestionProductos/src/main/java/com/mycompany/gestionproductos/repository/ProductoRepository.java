package com.mycompany.gestionproductos.repository;

import com.mycompany.gestionproductos.model.Producto;
import java.util.ArrayList;

public class ProductoRepository {

    private ArrayList<Producto> productos = new ArrayList<>();

    public void guardar(Producto producto) {
        productos.add(producto);
    }

    public ArrayList<Producto> listarTodos() {
        return productos;
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    public boolean eliminarPorCodigo(String codigo) {
        Producto producto = buscarPorCodigo(codigo);
        if (producto != null) {
            productos.remove(producto);
            return true;
        }
        return false;
    }

    public boolean existeCodigo(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }
}