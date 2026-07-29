/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;
/**
 *
 * @author Daryelin
 */
public class Veterinario extends Persona{
     private Especialidad especialidad;

    /**
     * Constructor completo.
     *
     * Se utiliza tanto para registrar como para recuperar
     * veterinarios desde la base de datos.
     *
     * @param id identificador del veterinario
     * @param nombre nombre completo
     * @param telefono número telefónico
     * @param email correo electrónico
     * @param especialidad especialidad del veterinario
     */
    public Veterinario(
            int id,
            String nombre,
            String telefono,
            String email,
            Especialidad especialidad) {

        super(id, nombre, telefono, email);

        this.especialidad = especialidad;
    }

    /**
     * Constructor para registrar un veterinario nuevo.
     *
     * El ID será generado automáticamente por MySQL.
     */
    public Veterinario(
            String nombre,
            String telefono,
            String email,
            Especialidad especialidad) {

        this(
                0,
                nombre,
                telefono,
                email,
                especialidad
        );
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(
            Especialidad especialidad) {

        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return getNombre()
                + " - "
                + especialidad;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Veterinario otroVeterinario)) {
            return false;
        }

        if (getId() > 0 && otroVeterinario.getId() > 0) {
            return getId() == otroVeterinario.getId();
        }

        return Objects.equals(
                getEmail(),
                otroVeterinario.getEmail()
        );
    }

    @Override
    public int hashCode() {
        if (getId() > 0) {
            return Objects.hash(getId());
        }

        return Objects.hash(getEmail());
    }
}
