/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Anyel
 */
public class Consulta {
    
  private int id;
    private Mascota mascota;
    private Veterinario veterinario;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    public Consulta(
            int id,
            Mascota mascota,
            Veterinario veterinario,
            LocalDate fecha,
            String diagnostico,
            String tratamiento,
            String observaciones) {

        this.id = id;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
    }

    public Consulta(
            Mascota mascota,
            Veterinario veterinario,
            LocalDate fecha,
            String diagnostico,
            String tratamiento,
            String observaciones) {

        this(
                0,
                mascota,
                veterinario,
                fecha,
                diagnostico,
                tratamiento,
                observaciones
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean perteneceALaMascota(int idMascota) {
        return mascota != null
                && mascota.getId() == idMascota;
    }

    @Override
    public String toString() {

        String nombreMascota =
                mascota != null
                        ? mascota.getNombre()
                        : "Sin mascota";

        return fecha
                + " - "
                + nombreMascota
                + ": "
                + diagnostico;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Consulta otraConsulta)) {
            return false;
        }

        if (id > 0 && otraConsulta.id > 0) {
            return id == otraConsulta.id;
        }

        return Objects.equals(mascota, otraConsulta.mascota)
                && Objects.equals(fecha, otraConsulta.fecha)
                && Objects.equals(
                        diagnostico,
                        otraConsulta.diagnostico
                );
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }

        return Objects.hash(mascota, fecha, diagnostico);
    }
}

