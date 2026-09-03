package com.corporatetalenthub;

import com.corporatetalenthub.documentacion.NotasArquitectura;
import com.riwi.talent.model.DatabaseConnection;
import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpresaRecord;
import com.riwi.talent.model.Desarrollador;
import com.riwi.talent.model.Gerente;
import com.riwi.talent.model.ConsultorExterno;
import com.riwi.talent.model.Persona;
import com.riwi.talent.model.Promocionable;
import com.riwi.talent.model.DesempeñoReport;
import com.riwi.talent.view.EmpleadoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * Punto de entrada — Corporate Talent Hub
 * Arquitectura: MVC + JDBC + Records + try-with-resources (Java 21 LTS)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Flujo de arranque:
 *   1. Se inicializa el esquema de la base de datos (crea tabla si no existe).
 *   2. Se presenta el menú principal con todas las opciones históricas más
 *      la nueva opción de gestión persistente con BD.
 *   3. Cada opción delega a la capa correspondiente (Vista → Controller → DAO).
 *
 * Separación de responsabilidades (MVC):
 *   - App.java: orquestación inicial únicamente (no Scanner para datos de empleados).
 *   - EmpleadoView: toda la I/O del módulo CRUD vive allí.
 *   - EmpleadoController: lógica de flujo y validaciones.
 *   - EmpleadoDAOImpl: persistencia JDBC con PreparedStatement.
 */
public class App {

    public static void main(String[] args) {

        // ── 1. Inicializar BD antes de presentar el menú ─────────────────────
        // try-with-resources aplicado en DatabaseConnection.inicializarEsquema()
        // garantiza que la Connection se cierra aunque el CREATE TABLE falle.
        DatabaseConnection.inicializarEsquema();

        // ── 2. Encabezado con Text Block (Java 15+) ──────────────────────────
        String encabezado = """
                
                     CORPORATE TALENT HUB
                   Gestión del talento humano
                   Arquitectura: MVC + JDBC (Java 21 LTS)
                
                """;
        System.out.println(encabezado);

        // Un único Scanner para toda la aplicación evita conflictos con System.in
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("""
                    ========= MENU PRINCIPAL =========
                    1. Demo de POO basica
                    2. Gestionar empleados en memoria (menu interactivo)
                    3. Gestion dinamica de colecciones
                    4. Arquitectura POO avanzada
                    5. Gestionar empleados en BD (CRUD persistente)
                    6. Salir
                    ===================================
                    """);
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida: debe ingresar un numero.");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            // Switch expression moderno: sin fall-through, sin break.
            switch (opcion) {
                case 1 -> ejecutarDemoPoo();
                case 2 -> ejecutarMenuPrincipal(crearEmpleadoDePrueba(), scanner);
                case 3 -> gestionarColeccionesDinamicas();
                case 4 -> demoArquitecturaPooAvanzada();
                case 5 -> {
                    // La Vista recibe el Scanner compartido: un único objeto sobre System.in
                    EmpleadoView view = new EmpleadoView(scanner);
                    view.mostrarMenuCrud();
                }
                case 6 -> System.out.println("Cerrando la aplicacion...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    // ─── Demos heredados (sin cambios de lógica, solo paquete actualizado) ───

    private static void ejecutarDemoPoo() {
        Empleado empleado = crearEmpleadoDePrueba();
        EmpresaRecord empresa = new EmpresaRecord("CodeUp Solutions", "900123456-7", 2015);

        System.out.println("========= DEMO DE POO BASICA =========");
        System.out.println(empleado);
        System.out.println("Empresa: " + empresa.nombre());
        System.out.println("Salario final: " + empleado.calcularSalarioFinal());

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

    private static void demoArquitecturaPooAvanzada() {
        Persona dev = new Desarrollador((byte) 3, (short) 2024, 201, 1111111111L,
                92.5f, 3_500_000, 'I', true, "Juan Code", 28, 1, 500_000, "Java");
        Persona gerente = new Gerente((byte) 5, (short) 2023, 202, 2222222222L,
                88.0f, 5_000_000, 'I', true, "Maria Lead", 35, 2, 800_000, 2_000_000);
        Persona consultor = new ConsultorExterno("Pedro External", 40, 250_000);

        System.out.println("========= ARQUITECTURA POO AVANZADA =========");
        procesarPersona(dev);
        procesarPersona(gerente);
        procesarPersona(consultor);

        if (dev instanceof Empleado emp) {
            DesempeñoReport report = new DesempeñoReport(emp.getIdEmpleado(), 92.5, "Excelente desempeno");
            System.out.println("Reporte: " + report);
        }
    }

    private static void procesarPersona(Persona p) {
        if (p instanceof Desarrollador des) {
            System.out.println(des.getNombre() + " (Dev) - Lenguaje: " + des.getLenguajePrincipal());
            System.out.println("  Bono ascenso: " + des.calcularBonoAscenso());
        } else if (p instanceof Gerente ger) {
            System.out.println(ger.getNombre() + " (Gerente) - Presupuesto: " + ger.getPresupuestoMensual());
            System.out.println("  Bono ascenso: " + ger.calcularBonoAscenso());
        } else if (p instanceof ConsultorExterno cons) {
            System.out.println(cons.getNombre() + " (Consultor) - Tarifa: $" + cons.getTarifaDiaria() + "/dia");
        }
    }

    private static void gestionarColeccionesDinamicas() {
        ArrayList<Empleado> empleados = new ArrayList<>();
        empleados.add(crearEmpleadoDePrueba());
        empleados.add(new Empleado((byte) 2, (short) 2022, 105, 987654321L, 78.0f,
                2_500_000.0, 'F', true, "Carlos Ruiz", 32, 1, 300_000.0));
        empleados.add(new Empleado((byte) 5, (short) 2020, 110, 123123123L, 95.0f,
                4_000_000.0, 'I', true, "Ana Torres", 26, 2, 600_000.0));
        empleados.add(new Empleado((byte) 1, (short) 2023, 120, 456456456L, 60.0f,
                2_000_000.0, 'T', false, "Luis Pardo", 29, 1, 150_000.0));

        HashMap<String, Empleado> empleadosPorId = new HashMap<>();
        for (Empleado empleado : empleados) {
            empleadosPorId.put(String.valueOf(empleado.getIdEmpleado()), empleado);
        }
        System.out.println("Busqueda por ID 105: " + empleadosPorId.get("105").getNombre());

        List<String> tecnologias = List.of("Java", "Spring Boot", "PostgreSQL", "Docker");
        Map<String, String> sedes = Map.of("1", "Medellin", "2", "Bogota", "3", "Cali");
        System.out.println("Tecnologias: " + tecnologias);
        System.out.println("Sedes: " + sedes);

        Empleado primeroModerno = empleados.getFirst();
        Empleado ultimoModerno  = empleados.getLast();
        System.out.println("Primero (Java 21): " + primeroModerno.getNombre());
        System.out.println("Ultimo (Java 21): "  + ultimoModerno.getNombre());

        empleados.removeIf(e -> e.getPuntajeTest() < 70);
        System.out.println("Empleados tras filtrado (puntaje >= 70):");
        for (var e : empleados) {
            System.out.println("- " + e.getNombre() + " (puntaje: " + e.getPuntajeTest() + ")");
        }

        var total   = empleados.size();
        var suma    = 0.0;
        for (var e : empleados) suma += e.calcularSalarioFinal();
        var promedio = total == 0 ? 0.0 : suma / total;

        System.out.println("Total empleados: " + total);
        System.out.println("Promedio de salarios: " + promedio);
    }

    private static Empleado crearEmpleadoDePrueba() {
        return new Empleado(
                (byte) 3, (short) 2024, 102, 1_023_456_789L,
                92.5f, 3_000_000.0, 'I', true,
                "Laura Gomez", 27, 2, 500_000.0);
    }

    private static void compararReferencias() {
        Empleado primero = crearEmpleadoDePrueba();
        Empleado segundo = crearEmpleadoDePrueba();
        Empleado alias   = primero;
        System.out.println("primero == segundo: "      + (primero == segundo));
        System.out.println("primero == alias: "        + (primero == alias));
    }

    private static void ejecutarLaboratorioDeNulos(Empleado empleado) {
        empleado.setNombre(null);
        try {
            System.out.println(empleado.getNombre().toUpperCase());
        } catch (NullPointerException e) {
            System.out.println("NPE controlada: " + e.getMessage());
        }
    }

    private static void ejecutarMenuPrincipal(Empleado empleado, Scanner scanner) {
        int opcion = 0;
        do {
            System.out.println("""
                    --------- MENU EN MEMORIA ---------
                    1. Ver categoria salarial
                    2. Registrar calificaciones trimestrales
                    3. Capturar nuevo dato de empleado
                    4. Volver al menu anterior
                    -----------------------------------
                    """);
            System.out.print("Seleccione una opcion: ");
            var opcionIngresada = 0;
            try {
                opcionIngresada = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida: debe ingresar un numero.");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            opcion = opcionIngresada;

            switch (opcion) {
                case 1 -> System.out.println("Categoria salarial: " + empleado.obtenerCategoriaSalarial());
                case 2 -> registrarDesempenio(empleado, scanner);
                case 3 -> capturarDatoConValidacion(scanner);
                case 4 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 4);
    }

    private static void registrarDesempenio(Empleado empleado, Scanner scanner) {
        double[][] calificaciones = new double[1][3];
        for (int f = 0; f < calificaciones.length; f++) {
            for (int c = 0; c < calificaciones[f].length; c++) {
                System.out.print("Calificacion trimestre " + (c + 1) + " para "
                        + empleado.getNombre() + ": ");
                try {
                    calificaciones[f][c] = scanner.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Valor invalido, se registra 0.0.");
                    calificaciones[f][c] = 0.0;
                }
                scanner.nextLine();
            }
        }
        double suma = 0.0;
        for (double[] fila : calificaciones)
            for (double v : fila) suma += v;
        double promedio = suma / (calificaciones.length * calificaciones[0].length);
        int puntajeSimplificado = (int) promedio;
        System.out.println("Promedio: " + promedio);
        System.out.println("Puntaje simplificado (truncado): " + puntajeSimplificado);
        System.out.println("Estado: " + (promedio >= 4.0 ? "Promocionable" : "No promocionable"));
    }

    private static void capturarDatoConValidacion(Scanner scanner) {
        System.out.print("Ingrese edad del empleado: ");
        var edad = 0;
        try {
            edad = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada invalida: debe ser un entero.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if (edad >= 18 && edad <= 70)
            System.out.println("Edad valida: " + edad);
        else
            System.out.println("Edad fuera de rango (18-70).");
    }
}
