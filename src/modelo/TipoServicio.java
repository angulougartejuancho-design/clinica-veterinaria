/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Anyel
 */
public enum TipoServicio {
    CONSULTA_GENERAL("Consulta general"),
    VACUNACION("Vacunación"),
    PROCEDIMIENTO("Procedimiento");

    private final String descripcion;

    TipoServicio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoServicio desdeTexto(String texto) {

        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de servicio no puede estar vacío."
            );
        }

        return TipoServicio.valueOf(
                texto.trim().toUpperCase()
        );
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
