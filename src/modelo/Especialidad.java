/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Daryelin
 */
public enum Especialidad {
    MEDICINA_GENERAL("Medicina general"),
    CIRUGIA("Cirugía"),
    DERMATOLOGIA("Dermatología"),
    ODONTOLOGIA("Odontología"),
    ANIMALES_EXOTICOS("Animales exóticos"),
    MEDICINA_INTERNA("Medicina interna");

    private final String descripcion;

    Especialidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
