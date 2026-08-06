package com.corporatetalenthub;

import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.EmpresaRecord;
import java.util.Scanner;
import java.util.InputMismatchException;

public class App {

    public static void main(String[] args) {
        String encabezado = """
                =====================================
                     CORPORATE TALENT HUB
                   Gestión del talento humano
                =====================================
                """;
        System.out.println(encabezado);

        Empleado empleado = crearEmpleadoDePrueba();
        EmpresaRecord empresa = new EmpresaRecord(
                "CodeUp Solutions",
                "900123456-7",
                2015);

        System.out.println(empleado);
        System.out.println("Empresa: " + empresa.nombre());
        System.out.println("Salario final: " + empleado.calcularSalarioFinal());
        System.out.println("¿ID par con bono extra?: " + empleado.tieneBonoExtra());
        System.out.println("¿Empleado elegible?: " + empleado.validarElegibilidad());

        if (empleado.tieneBonoExtra()) {
            empleado.actualizarBonoMensual(100_000.0);
            System.out.println("Bono actualizado con +=: " + empleado.getBonoMensual());
        }

        compararReferencias();
        ejecutarLaboratorioDeNulos(empleado);
        ejecutarMenuPrincipal(empleado);
    }

    private static Empleado crearEmpleadoDePrueba() {
        return new Empleado(
                (byte) 3,             // byte
                (short) 2024,         // short
                102,                  // int: ID par
                1_023_456_789L,       // long: sufijo L
                92.5f,                // float: sufijo f
                3_000_000.0,          // double
                'I',                  // char: contrato indefinido
                true,                 // boolean
                "Laura Gómez",        // String
                27,
                2,
                500_000.0);
    }

    private static void compararReferencias() {
        Empleado primero = crearEmpleadoDePrueba();
        Empleado segundo = crearEmpleadoDePrueba();
        Empleado aliasDelPrimero = primero;

        System.out.println("primero == segundo: " + (primero == segundo));
        System.out.println("primero == aliasDelPrimero: "
                + (primero == aliasDelPrimero));

        // == no compara los atributos de los objetos: comprueba si ambas variables
        // se refieren exactamente al mismo objeto. primero y segundo se crearon con
        // new por separado; aliasDelPrimero recibió la misma referencia de primero.
        // Conceptualmente los objetos viven en el Heap, pero == no debe entenderse
        // como una comparación manual de direcciones físicas de memoria.
    }

    private static void ejecutarLaboratorioDeNulos(Empleado empleado) {
        empleado.setNombre(null);

        try {
            System.out.println(empleado.getNombre().toUpperCase());
        } catch (NullPointerException excepcion) {
            System.out.println("NPE controlada: " + excepcion.getMessage());
        }

        // Java 8 normalmente informa que ocurrió una NullPointerException y señala
        // la línea mediante el stack trace, pero una expresión encadenada puede hacer
        // difícil reconocer cuál referencia era null.
        // Desde Java 14, Helpful NullPointerExceptions puede indicar que no se pudo
        // invocar toUpperCase() porque el resultado de getNombre() era null.
        // El try/catch es solo para que el laboratorio no detenga toda la aplicación;
        // la solución real es validar el dato o impedir nombres nulos según el dominio.
    }
    
    private static void ejecutarMenuPrincipal(Empleado empleado) {
    Scanner scanner = new Scanner(System.in);
    int opcion = 0;
    // Sintaxis legacy (Java 8): switch tradicional con case : break;
    // Si se olvida un "break", el flujo "cae" (fall-through) al siguiente case
    // sin lanzar ningún error, ejecutando código que no correspondía a esa opción.
    // La sintaxis moderna con -> (ver obtenerCategoriaSalarial en Empleado)
    // elimina ese riesgo por completo.
    do {
        System.out.println("""
                --------- MENU PRINCIPAL ---------
                1. Ver categoria salarial
                2. Registrar calificaciones trimestrales
                3. Capturar nuevo dato de empleado
                4. Salir
                -----------------------------------
                """);
        System.out.print("Seleccione una opcion: ");

        // Java 11+: 'var' infiere el tipo (int) a partir del valor asignado.
        // En Java 8 se habría escrito explícitamente: int opcionIngresada = 0;
        var opcionIngresada = 0;
        try {
            opcionIngresada = scanner.nextInt();
        } catch (InputMismatchException excepcion) {
            // Java 17/21 mejora el nivel de detalle de los mensajes de error
            // (por ejemplo con Helpful NullPointerExceptions), permitiendo
            // diagnosticar más rápido la causa real de una excepción.
            System.out.println("Entrada invalida: debe ingresar un numero.");
            scanner.nextLine();
            continue;
        }
        scanner.nextLine();
        opcion = opcionIngresada;

        switch (opcion) {
            case 1:
                System.out.println("Categoria salarial: " + empleado.obtenerCategoriaSalarial());
                break;
            case 2:
                registrarDesempenio(empleado, scanner);
                break;
            case 3:
                capturarDatoConValidacion(scanner);
                break;
            case 4:
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opcion no valida.");
                break;
        }
    } while (opcion != 4);
}

private static void registrarDesempenio(Empleado empleado, Scanner scanner) {
    // Matriz double[][]: filas = empleados registrados, columnas = 3 trimestres.
    double[][] calificaciones = new double[1][3];

    for (int fila = 0; fila < calificaciones.length; fila++) {
        for (int columna = 0; columna < calificaciones[fila].length; columna++) {
            System.out.print("Calificacion trimestre " + (columna + 1) + " para "
                    + empleado.getNombre() + ": ");
            try {
                calificaciones[fila][columna] = scanner.nextDouble();
            } catch (InputMismatchException excepcion) {
                System.out.println("Valor invalido, se registra 0.0 por defecto.");
                calificaciones[fila][columna] = 0.0;
            }
            scanner.nextLine();
        }
    }

    double suma = 0.0;
    for (int fila = 0; fila < calificaciones.length; fila++) {
        for (int columna = 0; columna < calificaciones[fila].length; columna++) {
            suma += calificaciones[fila][columna];
        }
    }
    double promedio = suma / (calificaciones.length * calificaciones[0].length);

    // Casting explícito de double a int: trunca la parte decimal (no redondea),
    // por lo que el "Puntaje Simplificado" pierde precisión respecto al promedio real.
    int puntajeSimplificado = (int) promedio;
    System.out.println("Promedio de desempenio: " + promedio);
    System.out.println("Puntaje Simplificado (con perdida de precision): " + puntajeSimplificado);

    // Operador ternario: decide el estado de promoción según el promedio.
    String estadoPromocion = (promedio >= 4.0) ? "Promocionable" : "No promocionable";
    System.out.println("Estado de promocion: " + estadoPromocion);
}

private static void capturarDatoConValidacion(Scanner scanner) {
    System.out.print("Ingrese edad del empleado: ");
    // Java 11+: 'var' infiere el tipo (int) automáticamente.
    var edadIngresada = 0;
    try {
        edadIngresada = scanner.nextInt();
    } catch (InputMismatchException excepcion) {
        System.out.println("Entrada invalida: debe ser un numero entero.");
        scanner.nextLine();
        return;
    }
    scanner.nextLine();

    // Validación de rango para el tipo primitivo int.
    if (edadIngresada >= 18 && edadIngresada <= 70) {
        System.out.println("Edad valida: " + edadIngresada);
    } else {
        System.out.println("Edad fuera de rango permitido (18-70).");
    }
}
}