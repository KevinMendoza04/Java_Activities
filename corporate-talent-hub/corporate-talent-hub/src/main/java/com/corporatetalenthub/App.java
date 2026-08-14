package com.corporatetalenthub;

import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.EmpresaRecord;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App {

public static void main(String[] args) {
    String encabezado = """
            
                 CORPORATE TALENT HUB
               Gestión del talento humano
            
            """;
    System.out.println(encabezado);

    Scanner scanner = new Scanner(System.in);
    int opcion = 0;

    do {
        System.out.println("""
                ========= MENU PRINCIPAL =========
                1. Demo de POO basica
                2. Gestionar empleados (menu interactivo)
                3. Gestion dinamica de colecciones
                4. Salir
                ===================================
                """);
        System.out.print("Seleccione una opcion: ");

        try {
            opcion = scanner.nextInt();
        } catch (InputMismatchException excepcion) {
            System.out.println("Entrada invalida: debe ingresar un numero.");
            scanner.nextLine();
            continue;
        }
        scanner.nextLine();

        switch (opcion) {
            case 1 -> ejecutarDemoPoo();
            case 2 -> ejecutarMenuPrincipal(crearEmpleadoDePrueba());
            case 3 -> gestionarColeccionesDinamicas();
            case 4 -> System.out.println("Cerrando la aplicacion...");
            default -> System.out.println("Opcion no valida.");
        }
    } while (opcion != 4);
}

private static void ejecutarDemoPoo() {
    Empleado empleado = crearEmpleadoDePrueba();
    EmpresaRecord empresa = new EmpresaRecord(
            "CodeUp Solutions",
            "900123456-7",
            2015);

    System.out.println("========= DEMO DE POO BASICA =========");
    System.out.println(empleado);
    System.out.println("Empresa: " + empresa.nombre());
    System.out.println("Salario final: " + empleado.calcularSalarioFinal());

    // En vez de imprimir el boolean crudo, se traduce a una frase legible,
    // pero la lógica de negocio (modulo %, && || !) sigue siendo la misma.
    System.out.println(empleado.tieneBonoExtra()
            ? "El ID del empleado es par: tiene bono extra."
            : "El ID del empleado es impar: no tiene bono extra.");

    System.out.println(empleado.validarElegibilidad()
            ? "El empleado SI es elegible segun las reglas de negocio."
            : "El empleado NO es elegible segun las reglas de negocio.");

    if (empleado.tieneBonoExtra()) {
        empleado.actualizarBonoMensual(100_000.0);
        System.out.println("Bono mensual actualizado con +=: " + empleado.getBonoMensual());
    }

    System.out.println("--- Comparacion de referencias (==) ---");
    compararReferencias();

    System.out.println("--- Laboratorio de valores nulos ---");
    ejecutarLaboratorioDeNulos(empleado);
}    
        
        private static void gestionarColeccionesDinamicas() {
    // ===== TASK 1: ArrayList y HashMap (Legacy 8/11) =====
    // ArrayList sustituye los arreglos fijos: crece dinámicamente sin
    // necesidad de declarar un tamaño de antemano.
    ArrayList<Empleado> empleados = new ArrayList<>();
    empleados.add(crearEmpleadoDePrueba());
    empleados.add(new Empleado((byte) 2, (short) 2022, 105, 987654321L, 78.0f,
            2_500_000.0, 'F', true, "Carlos Ruiz", 32, 1, 300_000.0));
    empleados.add(new Empleado((byte) 5, (short) 2020, 110, 123123123L, 95.0f,
            4_000_000.0, 'I', true, "Ana Torres", 26, 2, 600_000.0));
    empleados.add(new Empleado((byte) 1, (short) 2023, 120, 456456456L, 60.0f,
            2_000_000.0, 'T', false, "Luis Pardo", 29, 1, 150_000.0));

    // HashMap<String, Empleado>: búsqueda instantánea por ID (clave única),
    // sin recorrer toda la lista como tocaría hacer con un ArrayList.
    HashMap<String, Empleado> empleadosPorId = new HashMap<>();
    for (Empleado empleado : empleados) {
        empleadosPorId.put(String.valueOf(empleado.getIdEmpleado()), empleado);
    }
    System.out.println("Busqueda instantanea por ID 105: "
            + empleadosPorId.get("105").getNombre());

    // ===== TASK 2: Factory Methods inmutables (Legacy 9/11) =====
    List<String> tecnologias = List.of("Java", "Spring Boot", "PostgreSQL", "Docker");
    Map<String, String> sedes = Map.of("1", "Medellin", "2", "Bogota", "3", "Cali");

    // List.of()/Map.of() crean colecciones INMUTABLES: cualquier intento de
    // .add(), .remove() o .put() lanza UnsupportedOperationException en
    // tiempo de ejecucion. Son mas seguras que ArrayList/HashMap tradicionales
    // para datos de configuracion que no deberian cambiar durante la
    // ejecucion (evitan que un error en otro punto del programa las altere
    // por accidente). La contrapartida es justamente esa inmutabilidad: si
    // genuinamente se necesita una lista que crezca o cambie, List.of() no
    // es la herramienta correcta, ahi se sigue usando ArrayList.
    System.out.println("Tecnologias: " + tecnologias);
    System.out.println("Sedes: " + sedes);

    // ===== TASK 3: Sequenced Collections (Java 21) =====
    // Sintaxis Legacy (Java 8/11): acceso manual por indice.
    Empleado primeroLegacy = empleados.get(0);
    Empleado ultimoLegacy = empleados.get(empleados.size() - 1);
    System.out.println("Primero (legacy): " + primeroLegacy.getNombre());
    System.out.println("Ultimo (legacy): " + ultimoLegacy.getNombre());

    // Sintaxis moderna (Java 21): getFirst()/getLast() evitan errores de
    // indice (por ejemplo, olvidar el "-1" en size()-1, o usar size() solo
    // y obtener IndexOutOfBoundsException). reversed() devuelve una vista
    // en orden inverso de la lista sin necesidad de Collections.reverse()
    // ni de escribir un algoritmo de ordenamiento manual.
    Empleado primeroModerno = empleados.getFirst();
    Empleado ultimoModerno = empleados.getLast();
    System.out.println("Primero (Java 21): " + primeroModerno.getNombre());
    System.out.println("Ultimo (Java 21): " + ultimoModerno.getNombre());

    System.out.println("Empleados en orden inverso (Java 21):");
    for (var empleado : empleados.reversed()) {
        System.out.println("- " + empleado.getNombre());
    }

    // ===== TASK 4: Filtrado avanzado y var =====
    // removeIf recibe una condicion (expresion lambda) y elimina de la
    // propia lista todos los elementos que la cumplan, sin bucle manual
    // ni necesidad de crear una lista nueva.
    empleados.removeIf(empleado -> empleado.getPuntajeTest() < 70);

    System.out.println("Empleados tras el filtrado (puntaje >= 70):");
    for (var empleado : empleados) {
        System.out.println("- " + empleado.getNombre() + " (puntaje: " + empleado.getPuntajeTest() + ")");
    }

    // var (Java 11+) infiere el tipo automaticamente a partir del valor
    // asignado. En Java 8 se habria escrito explicitamente:
    // int totalEmpleados = 0; double sumaSalarios = 0.0; etc.
    // Usar var aqui simplifica la lectura sin perder tipado (sigue siendo
    // fuertemente tipado, solo que el compilador infiere el tipo por ti).
    var totalEmpleados = empleados.size();
    var sumaSalarios = 0.0;
    for (var empleado : empleados) {
        sumaSalarios += empleado.calcularSalarioFinal();
    }
    var promedioSalarios = totalEmpleados == 0 ? 0.0 : sumaSalarios / totalEmpleados;

    System.out.println("========== REPORTE FINAL ==========");
    System.out.println("Total de empleados: " + totalEmpleados);
    System.out.println("Promedio de salarios: " + promedioSalarios);
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
                4. Volver al menu anterior
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
                System.out.println("Volviendo al menu principal...");
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