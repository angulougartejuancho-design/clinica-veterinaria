/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
/**
 *
 * @author Daryelin
 */
public class Cita {
     private int id;
    private Mascota mascota;
    private Veterinario veterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private EstadoCita estado;
    
    public Cita(
            int id,
            Mascota mascota,
            Veterinario veterinario,
            LocalDate fecha,
            LocalTime hora,
            String motivo,
            EstadoCita estado) {

        this.id = id;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }
    
    public Cita(
            Mascota mascota,
            Veterinario veterinario,
            LocalDate fecha,
            LocalTime hora,
            String motivo,
            EstadoCita estado) {

        this(
                0,
                mascota,
                veterinario,
                fecha,
                hora,
                motivo,
                estado
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

    public void setVeterinario(
            Veterinario veterinario) {

        this.veterinario = veterinario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public boolean perteneceAlVeterinario(
            int idVeterinario) {

        return veterinario != null
                && veterinario.getId() == idVeterinario;
    }


    public boolean perteneceALaMascota(
            int idMascota) {

        return mascota != null
                && mascota.getId() == idMascota;
    }

    
    public boolean coincideConHorario(
            LocalDate fecha,
            LocalTime hora) {

        return Objects.equals(this.fecha, fecha)
                && Objects.equals(this.hora, hora);
    }

    @Override
    public String toString() {
        String nombreMascota =
                mascota != null
                        ? mascota.getNombre()
                        : "Sin mascota";

        String nombreVeterinario =
                veterinario != null
                        ? veterinario.getNombre()
                        : "Sin veterinario";

        return fecha
                + " "
                + hora
                + " - "
                + nombreMascota
                + " con "
                + nombreVeterinario;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Cita otraCita)) {
            return false;
        }


        if (id > 0 && otraCita.id > 0) {
            return id == otraCita.id;
        }

       
        return Objects.equals(
                veterinario,
                otraCita.veterinario
        )
                && Objects.equals(
                        fecha,
                        otraCita.fecha
                )
                && Objects.equals(
                        hora,
                        otraCita.hora
                );
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }

        return Objects.hash(
                veterinario,
                fecha,
                hora
        );
    }
}
