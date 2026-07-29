/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Daryelin
 */
public enum EstadoCita {
    PROGRAMADA("Programada"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    COMPLETADA("Completada");

    private final String descripcion;

    EstadoCita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Convierte el valor almacenado en la base de datos
     * en un EstadoCita.
     *
     * Ejemplo:
     * "PROGRAMADA" -> EstadoCita.PROGRAMADA
     */
    public static EstadoCita desdeTexto(String texto) {

        if (texto == null || texto.isBlank()) {
            return PROGRAMADA;
        }

        return EstadoCita.valueOf(
                texto.trim().toUpperCase()
        );
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
