package com.mycompany.gestionproductos;

import com.mycompany.gestionproductos.controller.ProductoController;
import com.mycompany.gestionproductos.model.ProductoFisico;
import com.mycompany.gestionproductos.model.ProductoDigital;
import com.mycompany.gestionproductos.view.VentanaPrincipal;

public class GestionProductos {

    public static void main(String[] args) {
        // Esta es la ÚNICA libreta (controller) de toda la aplicación.
        ProductoController controller = new ProductoController();
        registrarProductosDeEjemplo(controller);

        // Se la pasamos a VentanaPrincipal por el constructor.
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal(controller).setVisible(true));
    }

    private static void registrarProductosDeEjemplo(ProductoController controller) {
        ProductoFisico fisico1 = new ProductoFisico(
                "F001", "Teclado Mecánico", 150000, 20, true, 1.2, 8000);
        ProductoFisico fisico2 = new ProductoFisico(
                "F002", "Monitor 27 pulgadas", 900000, 8, true, 12.5, 25000);

        ProductoDigital digital1 = new ProductoDigital(
                "D001", "Curso de Java", 80000, 100, true, 2.3, "PDF+Video");
        ProductoDigital digital2 = new ProductoDigital(
                "D002", "Software de Diseño", 350000, 50, true, 6.8, "EXE");

        controller.registrarProducto(fisico1);
        controller.registrarProducto(fisico2);
        controller.registrarProducto(digital1);
        controller.registrarProducto(digital2);
    }
}