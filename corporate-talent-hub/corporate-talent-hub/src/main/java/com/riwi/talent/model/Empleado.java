package com.riwi.talent.model;

/**
 * Modelo de empleado — compatible con Java 8+ y aprovechado con Java 17/21.
 *
 * JAVA 8 (Legacy): esta clase era la única manera de representar datos:
 * constructor, getters, setters, equals, hashCode y toString escritos a mano.
 * Verboso pero necesario cuando el objeto necesita estado MUTABLE.
 *
 * JAVA 17/21 (LTS moderno): para objetos de solo lectura se prefieren Records
 * (ver EmpleadoReport). Aquí se mantiene la clase completa porque Empleado
 * es mutable (bonoMensual puede actualizarse durante la ejecución).
 *
 * SWITCH EXPRESSION (Java 14+): ver obtenerCategoriaSalarial(). Elimina el
 * riesgo de fall-through del switch tradicional de Java 8.
 */
public sealed class Empleado extends Persona implements Promocionable
        permits Desarrollador, Gerente {

    // Los 8 tipos primitivos de Java:
    private byte   nivelAcceso;
    private short  anioIngreso;
    private int    idEmpleado;
    private long   numeroDocumento;
    private float  puntajeTest;
    private double salarioBase;
    private char   tipoContrato;
    private boolean esActivo;

    // Datos adicionales de negocio
    private int    idSede;
    private double bonoMensual;

    public Empleado(
            byte nivelAcceso, short anioIngreso, int idEmpleado,
            long numeroDocumento, float puntajeTest, double salarioBase,
            char tipoContrato, boolean esActivo, String nombre, int edad,
            int idSede, double bonoMensual) {
        super(nombre, edad);
        this.nivelAcceso      = nivelAcceso;
        this.anioIngreso      = anioIngreso;
        this.idEmpleado       = idEmpleado;
        this.numeroDocumento  = numeroDocumento;
        this.puntajeTest      = puntajeTest;
        this.salarioBase      = salarioBase;
        this.tipoContrato     = tipoContrato;
        this.esActivo         = esActivo;
        this.idSede           = idSede;
        this.bonoMensual      = bonoMensual;
    }

    // ─── Lógica de negocio ───────────────────────────────────────────────────

    public double calcularSalarioFinal() {
        return (salarioBase + (bonoMensual * 1.10)) - (salarioBase * 0.05);
    }

    @Override
    public double calcularBonoAscenso() {
        return salarioBase * 0.15;
    }

    public boolean tieneBonoExtra()    { return idEmpleado % 2 == 0; }

    public boolean validarElegibilidad() {
        return (puntajeTest > 85 && edad < 30) || (idSede == 1 && !esActivo);
    }

    public void actualizarBonoMensual(double incremento) { bonoMensual += incremento; }

    /**
     * Switch Expression (Java 14+): cada rama retorna directamente con ->.
     * No existe riesgo de fall-through (olvidar "break" en Java 8).
     * El compilador exige que el switch sea exhaustivo o tenga default.
     */
    public String obtenerCategoriaSalarial() {
        int rango = (int) (salarioBase / 1_000_000);
        return switch (rango) {
            case 0, 1 -> "Categoria Basica";
            case 2, 3 -> "Categoria Media";
            case 4, 5 -> "Categoria Alta";
            default   -> "Categoria Ejecutiva";
        };
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────

    public int    getIdEmpleado()      { return idEmpleado; }
    public long   getNumeroDocumento() { return numeroDocumento; }
    public float  getPuntajeTest()     { return puntajeTest; }
    public double getSalarioBase()     { return salarioBase; }
    public double getBonoMensual()     { return bonoMensual; }
    public short  getAnioIngreso()     { return anioIngreso; }
    public char   getTipoContrato()    { return tipoContrato; }
    public boolean isEsActivo()        { return esActivo; }
    public int    getIdSede()          { return idSede; }
    public byte   getNivelAcceso()     { return nivelAcceso; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setSalarioBase(double s)     { this.salarioBase = s; }
    public void setBonoMensual(double b)     { this.bonoMensual = b; }
    public void setEsActivo(boolean activo)  { this.esActivo = activo; }

    @Override
    public String toString() {
        return "Empleado{id=" + idEmpleado
                + ", nombre='" + nombre + '\''
                + ", salarioBase=" + salarioBase
                + ", tipoContrato=" + tipoContrato
                + ", esActivo=" + esActivo + '}';
    }
}
