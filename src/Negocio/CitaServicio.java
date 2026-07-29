/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;
import Datos.CitaDAO;
import Exception.CitaNoDisponibleException;
import modelo.Cita;
import modelo.EstadoCita;
import modelo.Mascota;
import modelo.Veterinario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 *
 * @author Daryelin
 */
public class CitaServicio {
    private final CitaDAO citaDAO;

    /**
     * Hora mínima permitida para registrar citas.
     */
    private static final LocalTime HORA_APERTURA =
            LocalTime.of(8, 0);

    /**
     * Hora máxima permitida para registrar citas.
     */
    private static final LocalTime HORA_CIERRE =
            LocalTime.of(18, 0);

    /**
     * Constructor principal.
     */
    public CitaServicio() {
        citaDAO = new CitaDAO();
    }

    /**
     * Registra una cita después de validar sus datos.
     *
     * @param cita cita que se desea registrar
     * @throws IllegalArgumentException cuando los datos son inválidos
     * @throws CitaNoDisponibleException cuando el horario ya está ocupado
     * @throws SQLException cuando ocurre un error en la base de datos
     */
    public void registrar(Cita cita)
            throws SQLException,
            CitaNoDisponibleException {

        validarCita(cita);

        boolean horarioOcupado =
                citaDAO.existeHorarioOcupado(
                        cita.getVeterinario().getId(),
                        cita.getFecha(),
                        cita.getHora(),
                        0
                );

        if (horarioOcupado) {
            throw new CitaNoDisponibleException(
                    "El veterinario ya tiene una cita "
                            + "programada para esa fecha y hora."
            );
        }

        prepararDatos(cita);

        citaDAO.insertar(cita);
    }

    /**
     * Actualiza una cita existente.
     *
     * El ID de la cita actual se excluye de la validación
     * para evitar que choque contra su propio horario.
     *
     * @param cita cita modificada
     * @return true si la cita fue actualizada
     */
    public boolean actualizar(Cita cita)
            throws SQLException,
            CitaNoDisponibleException {

        if (cita == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una cita."
            );
        }

        if (cita.getId() <= 0) {
            throw new IllegalArgumentException(
                    "La cita seleccionada no posee un ID válido."
            );
        }

        validarCita(cita);

        Cita citaExistente =
                citaDAO.buscarPorId(cita.getId());

        if (citaExistente == null) {
            throw new IllegalArgumentException(
                    "La cita seleccionada no existe."
            );
        }

        boolean horarioOcupado =
                citaDAO.existeHorarioOcupado(
                        cita.getVeterinario().getId(),
                        cita.getFecha(),
                        cita.getHora(),
                        cita.getId()
                );

        if (horarioOcupado) {
            throw new CitaNoDisponibleException(
                    "El veterinario ya tiene otra cita "
                            + "registrada para esa fecha y hora."
            );
        }

        prepararDatos(cita);

        return citaDAO.actualizar(cita);
    }

    /**
     * Modifica únicamente el estado de una cita.
     *
     * @param idCita identificador de la cita
     * @param estado nuevo estado
     * @return true si el estado fue actualizado
     */
    public boolean cambiarEstado(
            int idCita,
            EstadoCita estado)
            throws SQLException {

        if (idCita <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una cita válida."
            );
        }

        if (estado == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un estado."
            );
        }

        Cita cita =
                citaDAO.buscarPorId(idCita);

        if (cita == null) {
            throw new IllegalArgumentException(
                    "La cita seleccionada no existe."
            );
        }

        validarCambioEstado(
                cita.getEstado(),
                estado
        );

        return citaDAO.actualizarEstado(
                idCita,
                estado
        );
    }

    /**
     * Cancela una cita.
     */
    public boolean cancelar(int idCita)
            throws SQLException {

        return cambiarEstado(
                idCita,
                EstadoCita.CANCELADA
        );
    }

    /**
     * Confirma una cita programada.
     */
    public boolean confirmar(int idCita)
            throws SQLException {

        return cambiarEstado(
                idCita,
                EstadoCita.CONFIRMADA
        );
    }

    /**
     * Marca una cita como completada.
     */
    public boolean completar(int idCita)
            throws SQLException {

        return cambiarEstado(
                idCita,
                EstadoCita.COMPLETADA
        );
    }

    /**
     * Elimina una cita mediante su ID.
     *
     * @param idCita identificador de la cita
     * @return true si fue eliminada
     */
    public boolean eliminar(int idCita)
            throws SQLException {

        if (idCita <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una cita válida."
            );
        }

        Cita cita =
                citaDAO.buscarPorId(idCita);

        if (cita == null) {
            throw new IllegalArgumentException(
                    "La cita seleccionada no existe."
            );
        }

        return citaDAO.eliminar(idCita);
    }

    /**
     * Devuelve todas las citas registradas.
     */
    public List<Cita> listar()
            throws SQLException {

        return citaDAO.listar();
    }

    /**
     * Busca una cita mediante su ID.
     */
    public Cita buscarPorId(int idCita)
            throws SQLException {

        if (idCita <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la cita debe ser mayor que cero."
            );
        }

        return citaDAO.buscarPorId(idCita);
    }

    /**
     * Devuelve las citas correspondientes a un veterinario.
     */
    public List<Cita> listarPorVeterinario(
            int idVeterinario)
            throws SQLException {

        if (idVeterinario <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un veterinario válido."
            );
        }

        return citaDAO.listarPorVeterinario(
                idVeterinario
        );
    }

    /**
     * Devuelve las citas correspondientes a una mascota.
     */
    public List<Cita> listarPorMascota(
            int idMascota)
            throws SQLException {

        if (idMascota <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una mascota válida."
            );
        }

        return citaDAO.listarPorMascota(
                idMascota
        );
    }

    /**
     * Valida los datos principales de la cita.
     */
    private void validarCita(Cita cita) {

        if (cita == null) {
            throw new IllegalArgumentException(
                    "Los datos de la cita son obligatorios."
            );
        }

        validarMascota(
                cita.getMascota()
        );

        validarVeterinario(
                cita.getVeterinario()
        );

        validarFecha(
                cita.getFecha()
        );

        validarHora(
                cita.getHora()
        );

        validarMotivo(
                cita.getMotivo()
        );

        if (cita.getEstado() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar el estado de la cita."
            );
        }

        /*
         * No se permite registrar directamente una cita
         * como completada o cancelada.
         */
        if (cita.getId() <= 0
                && cita.getEstado()
                == EstadoCita.COMPLETADA) {

            throw new IllegalArgumentException(
                    "Una cita nueva no puede registrarse "
                            + "directamente como completada."
            );
        }
    }

    /**
     * Valida que la mascota exista y posea un ID válido.
     */
    private void validarMascota(
            Mascota mascota) {

        if (mascota == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una mascota."
            );
        }

        if (mascota.getId() <= 0) {
            throw new IllegalArgumentException(
                    "La mascota seleccionada no posee un ID válido."
            );
        }
    }

    /**
     * Valida que el veterinario exista y posea un ID válido.
     */
    private void validarVeterinario(
            Veterinario veterinario) {

        if (veterinario == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un veterinario."
            );
        }

        if (veterinario.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El veterinario seleccionado no posee un ID válido."
            );
        }
    }

    /**
     * Valida la fecha de la cita.
     */
    private void validarFecha(
            LocalDate fecha) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar la fecha de la cita."
            );
        }

        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se pueden registrar citas "
                            + "en una fecha anterior a hoy."
            );
        }
    }

    /**
     * Valida la hora de la cita.
     */
    private void validarHora(
            LocalTime hora) {

        if (hora == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar la hora de la cita."
            );
        }

        if (hora.isBefore(HORA_APERTURA)
                || hora.isAfter(HORA_CIERRE)) {

            throw new IllegalArgumentException(
                    "La cita debe programarse entre las "
                            + HORA_APERTURA
                            + " y las "
                            + HORA_CIERRE
                            + "."
            );
        }

        /*
         * Las citas solo se permiten en intervalos
         * exactos de 30 minutos.
         */
        if (hora.getMinute() != 0
                && hora.getMinute() != 30) {

            throw new IllegalArgumentException(
                    "La hora de la cita debe estar en "
                            + "intervalos de 30 minutos."
            );
        }

        if (hora.getSecond() != 0
                || hora.getNano() != 0) {

            throw new IllegalArgumentException(
                    "La hora no debe contener segundos."
            );
        }
    }

    /**
     * Valida el motivo de la cita.
     */
    private void validarMotivo(
            String motivo) {

        String motivoLimpio =
                limpiarTexto(motivo);

        /*
         * En MySQL el motivo permite NULL, pero para el
         * funcionamiento del sistema lo solicitaremos.
         */
        if (motivoLimpio == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar el motivo de la cita."
            );
        }

        if (motivoLimpio.length() < 5) {
            throw new IllegalArgumentException(
                    "El motivo debe contener al menos 5 caracteres."
            );
        }

        if (motivoLimpio.length() > 200) {
            throw new IllegalArgumentException(
                    "El motivo no puede superar los 200 caracteres."
            );
        }
    }

    /**
     * Controla las transiciones permitidas entre estados.
     */
    private void validarCambioEstado(
            EstadoCita estadoActual,
            EstadoCita nuevoEstado) {

        if (estadoActual == nuevoEstado) {
            throw new IllegalArgumentException(
                    "La cita ya se encuentra en el estado "
                            + nuevoEstado
                            + "."
            );
        }

        if (estadoActual
                == EstadoCita.CANCELADA) {

            throw new IllegalArgumentException(
                    "Una cita cancelada no puede cambiar de estado."
            );
        }

        if (estadoActual
                == EstadoCita.COMPLETADA) {

            throw new IllegalArgumentException(
                    "Una cita completada no puede cambiar de estado."
            );
        }

        if (estadoActual
                == EstadoCita.PROGRAMADA
                && nuevoEstado
                == EstadoCita.COMPLETADA) {

            throw new IllegalArgumentException(
                    "Primero debe confirmar la cita "
                            + "antes de completarla."
            );
        }

        if (estadoActual
                == EstadoCita.CONFIRMADA
                && nuevoEstado
                == EstadoCita.PROGRAMADA) {

            throw new IllegalArgumentException(
                    "Una cita confirmada no puede volver "
                            + "al estado programada."
            );
        }
    }

    /**
     * Limpia los datos antes de guardarlos.
     */
    private void prepararDatos(Cita cita) {

        cita.setMotivo(
                cita.getMotivo().trim()
        );

        /*
         * Cuando por algún motivo no se haya definido el
         * estado, se utiliza PROGRAMADA como valor inicial.
         */
        if (cita.getEstado() == null) {
            cita.setEstado(
                    EstadoCita.PROGRAMADA
            );
        }
    }

    /**
     * Elimina espacios innecesarios.
     */
    private String limpiarTexto(
            String texto) {

        if (texto == null) {
            return null;
        }

        String textoLimpio =
                texto.trim();

        if (textoLimpio.isEmpty()) {
            return null;
        }

        return textoLimpio;
    }
}
