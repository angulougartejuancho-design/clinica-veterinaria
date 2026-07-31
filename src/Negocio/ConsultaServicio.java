/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.ConsultaDAO;
import Exception.ValidationException;
import modelo.Consulta;
import modelo.Mascota;
import modelo.Veterinario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


/**
 *
 * @author Anyel
 */
public class ConsultaServicio {
    
 private final ConsultaDAO consultaDAO;

    private static final int LONGITUD_MINIMA_TEXTO = 5;
    private static final int LONGITUD_MAXIMA_TEXTO = 500;

    public ConsultaServicio() {
        consultaDAO = new ConsultaDAO();
    }

    public void registrar(Consulta consulta)
            throws ValidationException, SQLException {

        validarConsulta(consulta);

        prepararDatos(consulta);

        consultaDAO.insertar(consulta);
    }

    public boolean actualizar(Consulta consulta)
            throws ValidationException, SQLException {

        if (consulta == null
                || consulta.getId() <= 0) {

            throw new ValidationException(
                    "Debe seleccionar una consulta válida "
                            + "para actualizar."
            );
        }

        validarConsulta(consulta);

        Consulta consultaExistente =
                consultaDAO.buscarPorId(consulta.getId());

        if (consultaExistente == null) {
            throw new ValidationException(
                    "La consulta seleccionada no existe."
            );
        }

        prepararDatos(consulta);

        return consultaDAO.actualizar(consulta);
    }

    public boolean eliminar(int idConsulta)
            throws ValidationException, SQLException {

        if (idConsulta <= 0) {
            throw new ValidationException(
                    "Debe seleccionar una consulta válida."
            );
        }

        Consulta consulta =
                consultaDAO.buscarPorId(idConsulta);

        if (consulta == null) {
            throw new ValidationException(
                    "La consulta seleccionada no existe."
            );
        }

        return consultaDAO.eliminar(idConsulta);
    }

    public List<Consulta> listar() throws SQLException {
        return consultaDAO.listar();
    }

    public List<Consulta> listarPorMascota(int idMascota)
            throws ValidationException, SQLException {

        if (idMascota <= 0) {
            throw new ValidationException(
                    "Debe seleccionar una mascota válida."
            );
        }

        return consultaDAO.listarPorMascota(idMascota);
    }

    public Consulta buscarPorId(int idConsulta)
            throws ValidationException, SQLException {

        if (idConsulta <= 0) {
            throw new ValidationException(
                    "El ID de la consulta debe ser "
                            + "mayor que cero."
            );
        }

        return consultaDAO.buscarPorId(idConsulta);
    }

    private void validarConsulta(Consulta consulta)
            throws ValidationException {

        if (consulta == null) {
            throw new ValidationException(
                    "Los datos de la consulta son "
                            + "obligatorios."
            );
        }

        validarMascota(consulta.getMascota());
        validarVeterinario(consulta.getVeterinario());
        validarFecha(consulta.getFecha());

        validarTexto(
                consulta.getDiagnostico(),
                "El diagnóstico"
        );

        validarTexto(
                consulta.getTratamiento(),
                "El tratamiento"
        );

        if (consulta.getObservaciones() != null
                && consulta.getObservaciones().trim()
                        .length() > LONGITUD_MAXIMA_TEXTO) {

            throw new ValidationException(
                    "Las observaciones no pueden superar "
                            + "los "
                            + LONGITUD_MAXIMA_TEXTO
                            + " caracteres."
            );
        }
    }

    private void validarMascota(Mascota mascota)
            throws ValidationException {

        if (mascota == null || mascota.getId() <= 0) {
            throw new ValidationException(
                    "Debe seleccionar una mascota válida."
            );
        }
    }

    private void validarVeterinario(Veterinario veterinario)
            throws ValidationException {

        if (veterinario == null
                || veterinario.getId() <= 0) {

            throw new ValidationException(
                    "Debe seleccionar un veterinario válido."
            );
        }
    }

    private void validarFecha(LocalDate fecha)
            throws ValidationException {

        if (fecha == null) {
            throw new ValidationException(
                    "Debe ingresar la fecha de la consulta."
            );
        }

        if (fecha.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "La fecha de la consulta no puede "
                            + "ser futura."
            );
        }
    }

    private void validarTexto(
            String texto,
            String nombreCampo)
            throws ValidationException {

        String textoLimpio = limpiarTexto(texto);

        if (textoLimpio == null) {
            throw new ValidationException(
                    nombreCampo + " es obligatorio."
            );
        }

        if (textoLimpio.length() < LONGITUD_MINIMA_TEXTO) {
            throw new ValidationException(
                    nombreCampo
                            + " debe contener al menos "
                            + LONGITUD_MINIMA_TEXTO
                            + " caracteres."
            );
        }

        if (textoLimpio.length() > LONGITUD_MAXIMA_TEXTO) {
            throw new ValidationException(
                    nombreCampo
                            + " no puede superar los "
                            + LONGITUD_MAXIMA_TEXTO
                            + " caracteres."
            );
        }
    }

    private void prepararDatos(Consulta consulta) {

        consulta.setDiagnostico(
                consulta.getDiagnostico().trim()
        );

        consulta.setTratamiento(
                consulta.getTratamiento().trim()
        );

        String observaciones = consulta.getObservaciones();

        consulta.setObservaciones(
                observaciones == null
                        ? ""
                        : observaciones.trim()
        );
    }

    private String limpiarTexto(String texto) {

        if (texto == null) {
            return null;
        }

        String textoLimpio = texto.trim();

        return textoLimpio.isEmpty() ? null : textoLimpio;
    }
}

