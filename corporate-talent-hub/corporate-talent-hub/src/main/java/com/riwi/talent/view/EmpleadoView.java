package com.riwi.talent.view;

import com.riwi.talent.controller.EmpleadoController;
import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpleadoReport;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * TASK 3 — Vista MVC: EmpleadoView
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * RESPONSABILIDAD ÚNICA DE LA VISTA:
 *   - Toda interacción con el usuario (Scanner, System.out) ocurre AQUÍ.
 *   - No contiene lógica de negocio ni acceso a datos.
 *   - Captura datos del usuario → los envía al Controller.
 *   - Recibe resultados del Controller → los formatea y muestra.
 *
 * Regla de oro MVC:
 *   La Vista NUNCA habla directamente con el DAO ni con la BD.
 *   Solo habla con el Controller.
 *
 * SEPARACIÓN vs JAVA 8 LEGACY:
 *   En el App.java original, Scanner y lógica de datos convivían en el
 *   mismo método. Con MVC, cambiar la interfaz (de consola a Swing, JavaFX
 *   o REST) solo requiere reescribir esta clase; el Controller y el Model
 *   permanecen intactos.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class EmpleadoView {

    // Scanner es el ÚNICO punto de entrada de datos del usuario en toda la app.
    // Se instancia aquí y se pasa como parámetro cuando es necesario,
    // evitando múltiples instancias sobre System.in (causa de bugs en Java).
    private final Scanner scanner;
    private final EmpleadoController controller;

    public EmpleadoView(Scanner scanner) {
        this.scanner    = scanner;
        this.controller = new EmpleadoController();
    }

    // ─── Menú principal del módulo CRUD ─────────────────────────────────────

    /**
     * Bucle principal del módulo de gestión de empleados.
     * Muestra el menú y delega cada opción a un método específico de la Vista.
     */
    public void mostrarMenuCrud() {
        int opcion = 0;
        do {
            // Text Block (Java 15+): multilinea legible sin concatenación.
            System.out.println("""
                    
                    ══════════════════════════════════════
                      GESTIÓN DE EMPLEADOS  (Base de Datos)
                    ══════════════════════════════════════
                      1. Registrar nuevo empleado
                      2. Listar todos los empleados
                      3. Buscar empleado por ID
                      4. Actualizar empleado
                      5. Eliminar empleado
                      6. Reporte consolidado (Records + BD)
                      7. Volver al menu principal
                    ──────────────────────────────────────
                    """);
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida: ingrese un numero del 1 al 7.");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            // Switch expression moderno (Java 14+): sin fall-through, sin break.
            switch (opcion) {
                case 1 -> capturarYRegistrar();
                case 2 -> mostrarListado();
                case 3 -> capturarYBuscar();
                case 4 -> capturarYActualizar();
                case 5 -> capturarYEliminar();
                case 6 -> mostrarReporteConsolidado();
                case 7 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 7);
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────

    /**
     * Captura los datos del nuevo empleado por consola y llama al Controller.
     * La Vista no valida reglas de negocio; solo verifica que la entrada sea
     * del tipo correcto (int, double, etc.).
     */
    private void capturarYRegistrar() {
        System.out.println("\n--- REGISTRAR NUEVO EMPLEADO ---");

        System.out.print("ID empleado       : ");
        int id = leerEntero();

        System.out.print("Nombre            : ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Edad              : ");
        int edad = leerEntero();

        System.out.print("Salario base      : ");
        double salario = leerDecimal();

        System.out.print("Bono mensual      : ");
        double bono = leerDecimal();

        System.out.print("Tipo contrato (I/F/T): ");
        char tipo = scanner.nextLine().trim().toUpperCase().charAt(0);

        System.out.print("Activo (true/false): ");
        boolean activo = Boolean.parseBoolean(scanner.nextLine().trim());

        System.out.print("ID sede           : ");
        int sede = leerEntero();

        // Construye el objeto Empleado con valores por defecto para los campos
        // menos relevantes en la captura básica (nivel, año, doc, puntaje).
        Empleado nuevo = new Empleado(
                (byte) 1, (short) 2024, id, 0L, 0.0f,
                salario, tipo, activo, nombre, edad, sede, bono);

        // La Vista solo muestra el resultado; el Controller decide si es éxito o error.
        String resultado = controller.registrarEmpleado(nuevo);
        System.out.println("\n>> " + resultado);
    }

    // ─── READ (lista) ────────────────────────────────────────────────────────

    /**
     * Solicita la lista al Controller y la formatea para mostrar en consola.
     */
    private void mostrarListado() {
        List<Empleado> empleados = controller.obtenerTodos();

        if (empleados.isEmpty()) {
            System.out.println("\nNo hay empleados registrados en la base de datos.");
            return;
        }

        // Text Block como plantilla de encabezado de tabla
        System.out.println("""
                
                ┌─────┬──────────────────────────┬──────┬─────────────────┬─────────┐
                │ ID  │ Nombre                   │ Edad │  Salario Base   │ Activo  │
                ├─────┼──────────────────────────┼──────┼─────────────────┼─────────┤""");

        for (Empleado emp : empleados) {
            System.out.printf("│ %-3d │ %-24s │ %-4d │ %,15.2f │ %-7s │%n",
                    emp.getIdEmpleado(),
                    emp.getNombre(),
                    emp.getEdad(),
                    emp.getSalarioBase(),
                    emp.isEsActivo() ? "Si" : "No");
        }
        System.out.println("└─────┴──────────────────────────┴──────┴─────────────────┴─────────┘");
        System.out.println("Total: " + empleados.size() + " empleado(s).");
    }

    // ─── READ (por ID) ───────────────────────────────────────────────────────

    private void capturarYBuscar() {
        System.out.print("\nIngrese el ID del empleado a buscar: ");
        int id = leerEntero();

        Optional<Empleado> resultado = controller.buscarPorId(id);

        // Optional obliga a manejar explícitamente el caso "no encontrado".
        if (resultado.isPresent()) {
            Empleado emp = resultado.get();
            System.out.printf("""
                    
                    Empleado encontrado:
                      ID            : %d
                      Nombre        : %s
                      Edad          : %d
                      Salario base  : %,.2f
                      Bono mensual  : %,.2f
                      Tipo contrato : %c
                      Activo        : %s
                      Sede          : %d
                    """,
                    emp.getIdEmpleado(), emp.getNombre(), emp.getEdad(),
                    emp.getSalarioBase(), emp.getBonoMensual(),
                    emp.getTipoContrato(), emp.isEsActivo() ? "Si" : "No",
                    emp.getIdSede());
        } else {
            System.out.println("\nNo se encontro ningun empleado con ID " + id);
        }
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    private void capturarYActualizar() {
        System.out.println("\n--- ACTUALIZAR EMPLEADO ---");
        System.out.print("ID del empleado a actualizar: ");
        int id = leerEntero();

        // Primero verifica que exista (feedback inmediato al usuario).
        Optional<Empleado> existente = controller.buscarPorId(id);
        if (existente.isEmpty()) {
            System.out.println("No existe un empleado con ID " + id);
            return;
        }

        Empleado actual = existente.get();
        System.out.println("Empleado actual: " + actual.getNombre()
                + " | Salario: " + actual.getSalarioBase()
                + " | Bono: " + actual.getBonoMensual());

        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isBlank()) nombre = actual.getNombre();

        System.out.print("Nuevo salario base (0 para mantener): ");
        double salario = leerDecimal();
        if (salario == 0) salario = actual.getSalarioBase();

        System.out.print("Nuevo bono mensual (0 para mantener): ");
        double bono = leerDecimal();
        if (bono == 0) bono = actual.getBonoMensual();

        System.out.print("Activo (true/false): ");
        boolean activo = Boolean.parseBoolean(scanner.nextLine().trim());

        // Crea un nuevo objeto Empleado con los datos modificados.
        Empleado modificado = new Empleado(
                actual.getNivelAcceso(), actual.getAnioIngreso(), id,
                actual.getNumeroDocumento(), actual.getPuntajeTest(),
                salario, actual.getTipoContrato(), activo,
                nombre, actual.getEdad(), actual.getIdSede(), bono);

        System.out.println("\n>> " + controller.actualizarEmpleado(modificado));
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    private void capturarYEliminar() {
        System.out.print("\nID del empleado a eliminar: ");
        int id = leerEntero();

        // Confirmación antes de eliminar — buena práctica UX en operaciones destructivas.
        System.out.print("¿Confirma la eliminacion del empleado ID " + id + "? (s/n): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();

        if (!confirmacion.equals("s")) {
            System.out.println("Operacion cancelada.");
            return;
        }

        System.out.println("\n>> " + controller.eliminarEmpleado(id));
    }

    // ─── REPORTE (TASK 4 — integrado aquí para presentación en consola) ──────

    /**
     * Solicita el reporte al Controller y lo presenta usando Text Block.
     * El formato legible es responsabilidad de la Vista; los datos vienen
     * del Controller a través de EmpleadoReport (Record).
     */
    private void mostrarReporteConsolidado() {
        List<Empleado> empleados = controller.obtenerTodos();

        if (empleados.isEmpty()) {
            System.out.println("\nNo hay datos en la BD para generar el reporte.");
            return;
        }

        // Cálculos agregados en el Controller/Vista (sin lógica en el Model)
        double totalSalarios  = 0;
        double totalBonos     = 0;
        int    totalActivos   = 0;

        StringBuilder filas = new StringBuilder();
        for (Empleado emp : empleados) {
            // Mapear a Record para transferencia inmutable (TASK 4)
            EmpleadoReport rep = new EmpleadoReport(
                    emp.getIdEmpleado(),
                    emp.getNombre(),
                    emp.getSalarioBase(),
                    emp.calcularSalarioFinal(),
                    emp.obtenerCategoriaSalarial(),
                    emp.isEsActivo());

            totalSalarios += rep.salarioBase();
            totalBonos    += emp.getBonoMensual();
            if (rep.esActivo()) totalActivos++;

            filas.append(String.format(
                    "  %-4d | %-22s | %,12.2f | %-18s | %s%n",
                    rep.idEmpleado(), rep.nombre(),
                    rep.salarioFinal(), rep.categoriaSalarial(),
                    rep.esActivo() ? "ACTIVO" : "INACTIVO"));
        }

        // Text Block (Java 15+): formato de reporte legible sin concatenaciones.
        // TASK 4: demuestra cómo Records + Text Blocks mejoran la presentación de datos
        // frente al String.format() encadenado que se usaba en Java 8.
        String reporte = """
                
                ╔══════════════════════════════════════════════════════════════════╗
                ║           REPORTE CONSOLIDADO — CORPORATE TALENT HUB            ║
                ╠══════════════════════════════════════════════════════════════════╣
                ║  ID   │ Nombre                 │  Salario Final │ Categoría          │ Estado
                ╠══════════════════════════════════════════════════════════════════╣
                %s╠══════════════════════════════════════════════════════════════════╣
                ║  Total empleados : %-6d                                        ║
                ║  Empleados activos: %-5d                                        ║
                ║  Suma salarios base: $%,14.2f                              ║
                ║  Suma bonos mensuales: $%,12.2f                            ║
                ╚══════════════════════════════════════════════════════════════════╝
                """.formatted(
                        filas,
                        empleados.size(),
                        totalActivos,
                        totalSalarios,
                        totalBonos);

        System.out.println(reporte);
    }

    // ─── Helpers de lectura segura ───────────────────────────────────────────

    /** Lee un entero desde la consola, reintentando si la entrada es inválida. */
    private int leerEntero() {
        while (true) {
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.print("Valor invalido, ingrese un numero entero: ");
            }
        }
    }

    /** Lee un double desde la consola, reintentando si la entrada es inválida. */
    private double leerDecimal() {
        while (true) {
            try {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.print("Valor invalido, ingrese un numero decimal: ");
            }
        }
    }
}
