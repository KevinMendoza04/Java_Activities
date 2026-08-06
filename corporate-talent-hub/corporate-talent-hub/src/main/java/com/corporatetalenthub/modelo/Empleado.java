package com.corporatetalenthub.modelo;

/**
 * Modelo tradicional compatible con la sintaxis de Java 8.
 *
 * Esta clase es más verbosa que un Record porque declara campos, constructor,
 * getters, setter y métodos explícitamente. Esa verbosidad es útil cuando el
 * objeto necesita estado mutable, como bonoMensual o nombre.
 */
public class Empleado {

    // Los 8 tipos primitivos requeridos:
    private byte nivelAcceso;
    private short anioIngreso;
    private int idEmpleado;
    private long numeroDocumento;
    private float puntajeTest;
    private double salarioBase;
    private char tipoContrato;
    private boolean esActivo;

    // String no es primitivo: es una clase y esta variable guarda una referencia.
    private String nombre;

    // Datos adicionales necesarios para las reglas de negocio.
    private int edad;
    private int idSede;
    private double bonoMensual;

    public Empleado(
            byte nivelAcceso,
            short anioIngreso,
            int idEmpleado,
            long numeroDocumento,
            float puntajeTest,
            double salarioBase,
            char tipoContrato,
            boolean esActivo,
            String nombre,
            int edad,
            int idSede,
            double bonoMensual) {
        this.nivelAcceso = nivelAcceso;
        this.anioIngreso = anioIngreso;
        this.idEmpleado = idEmpleado;
        this.numeroDocumento = numeroDocumento;
        this.puntajeTest = puntajeTest;
        this.salarioBase = salarioBase;
        this.tipoContrato = tipoContrato;
        this.esActivo = esActivo;
        this.nombre = nombre;
        this.edad = edad;
        this.idSede = idSede;
        this.bonoMensual = bonoMensual;
    }

    public double calcularSalarioFinal() {
        // Orden: paréntesis internos -> multiplicaciones -> suma -> resta.
        // 1. bonoMensual * 1.10
        // 2. salarioBase + resultado anterior
        // 3. salarioBase * 0.05
        // 4. resta de ambos resultados
        return (salarioBase + (bonoMensual * 1.10))
                - (salarioBase * 0.05);
    }

    public boolean tieneBonoExtra() {
        // % se evalúa antes que ==. Un residuo cero indica que el ID es par.
        return idEmpleado % 2 == 0;
    }

    public boolean validarElegibilidad() {
        // Precedencia: primero !, después && y finalmente ||.
        // Los paréntesis hacen explícitos los dos grupos de condiciones.
        return (puntajeTest > 85 && edad < 30)
                || (idSede == 1 && !esActivo);
    }

    public void actualizarBonoMensual(double incremento) {
        // Equivale a: bonoMensual = bonoMensual + incremento;
        bonoMensual += incremento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public double getBonoMensual() {
        return bonoMensual;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "nivelAcceso=" + nivelAcceso +
                ", anioIngreso=" + anioIngreso +
                ", idEmpleado=" + idEmpleado +
                ", numeroDocumento=" + numeroDocumento +
                ", puntajeTest=" + puntajeTest +
                ", salarioBase=" + salarioBase +
                ", tipoContrato=" + tipoContrato +
                ", esActivo=" + esActivo +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", idSede=" + idSede +
                ", bonoMensual=" + bonoMensual +
                '}';
    }
    
    public String obtenerCategoriaSalarial() {
    // Java 17/21: Switch Expression con sintaxis de flecha ->
    // Ventaja: cada rama retorna directamente, no hay riesgo de fall-through
    // (no existe "olvidar el break" porque no hay break), el compilador exige
    // que el switch sea exhaustivo (o tenga default), y el código es más breve.
    // En Java 8, un switch clásico con case: break; requiere escribir "break;"
    // en cada case; si se olvida, la ejecución "cae" (fall-through) al siguiente
    // case sin ningún aviso del compilador, generando errores silenciosos.
    int rango = (int) (salarioBase / 1_000_000);
    return switch (rango) {
        case 0, 1 -> "Categoría Básica";
        case 2, 3 -> "Categoría Media";
        case 4, 5 -> "Categoría Alta";
        default -> "Categoría Ejecutiva";
    };
}
}