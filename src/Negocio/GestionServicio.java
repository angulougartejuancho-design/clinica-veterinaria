/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.ServicioDAO;
import Exception.ValidationException;
import modelo.ConsultaGeneral;
import modelo.Procedimiento;
import modelo.Servicio;
import modelo.Vacunacion;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Anyel
 */
public class GestionServicio {
    
private final ServicioDAO servicioDAO;

    private final Map<Integer, Servicio> cacheServicios;

    public GestionServicio() {
        servicioDAO = new ServicioDAO();
        cacheServicios = new LinkedHashMap<>();
    }

    public Servicio crearConsultaGeneral(
            String nombre,
            String descripcion,
            double costoBase,
            String motivoTipico)
            throws ValidationException {

        validarDatosComunes(nombre, descripcion, costoBase);

        return new ConsultaGeneral(
                nombre.trim(),
                descripcion.trim(),
                costoBase,
                motivoTipico == null
                        ? ""
                        : motivoTipico.trim()
        );
    }

    public Servicio crearVacunacion(
            String nombre,
            String descripcion,
            double costoBase,
            String tipoVacuna)
            throws ValidationException {

        validarDatosComunes(nombre, descripcion, costoBase);

        if (tipoVacuna == null
                || tipoVacuna.trim().isEmpty()) {

            throw new ValidationException(
                    "Debe indicar el tipo de vacuna."
            );
        }

        return new Vacunacion(
                nombre.trim(),
                descripcion.trim(),
                costoBase,
                tipoVacuna.trim()
        );
    }

    public Servicio crearProcedimiento(
            String nombre,
            String descripcion,
            double costoBase,
            int duracionMinutos)
            throws ValidationException {

        validarDatosComunes(nombre, descripcion, costoBase);

        if (duracionMinutos <= 0) {
            throw new ValidationException(
                    "La duración del procedimiento debe "
                            + "ser mayor que cero minutos."
            );
        }

        return new Procedimiento(
                nombre.trim(),
                descripcion.trim(),
                costoBase,
                duracionMinutos
        );
    }

    public void registrar(Servicio servicio)
            throws ValidationException, SQLException {

        if (servicio == null) {
            throw new ValidationException(
                    "Debe indicar los datos del servicio."
            );
        }

        servicioDAO.insertar(servicio);

        cacheServicios.put(servicio.getId(), servicio);
    }

    public boolean actualizar(Servicio servicio)
            throws ValidationException, SQLException {

        if (servicio == null || servicio.getId() <= 0) {

            throw new ValidationException(
                    "Debe seleccionar un servicio válido "
                            + "para actualizar."
            );
        }

        validarDatosComunes(
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getCostoBase()
        );

        boolean actualizado =
                servicioDAO.actualizar(servicio);

        if (actualizado) {
            cacheServicios.put(
                    servicio.getId(),
                    servicio
            );
        }

        return actualizado;
    }

    public boolean eliminar(int idServicio)
            throws ValidationException, SQLException {

        if (idServicio <= 0) {
            throw new ValidationException(
                    "Debe seleccionar un servicio válido."
            );
        }

        boolean eliminado =
                servicioDAO.eliminar(idServicio);

        if (eliminado) {
            cacheServicios.remove(idServicio);
        }

        return eliminado;
    }

    public List<Servicio> listar() throws SQLException {

        List<Servicio> servicios = servicioDAO.listar();

        cacheServicios.clear();

        for (Servicio servicio : servicios) {
            cacheServicios.put(servicio.getId(), servicio);
        }

        return servicios;
    }

    
    public Servicio buscarPorId(int idServicio)
            throws ValidationException, SQLException {

        if (idServicio <= 0) {
            throw new ValidationException(
                    "El ID del servicio debe ser "
                            + "mayor que cero."
            );
        }

        if (cacheServicios.containsKey(idServicio)) {
            return cacheServicios.get(idServicio);
        }

        Servicio servicio =
                servicioDAO.buscarPorId(idServicio);

        if (servicio != null) {
            cacheServicios.put(idServicio, servicio);
        }

        return servicio;
    }

   
    public double calcularTotal(List<Servicio> servicios) {

        double total = 0.0;

        if (servicios == null) {
            return total;
        }

        for (Servicio servicio : servicios) {
            total += servicio.calcularPrecio();
        }

        return total;
    }

    private void validarDatosComunes(
            String nombre,
            String descripcion,
            double costoBase)
            throws ValidationException {

        if (nombre == null
                || nombre.trim().isEmpty()) {

            throw new ValidationException(
                    "El nombre del servicio es obligatorio."
            );
        }

        if (nombre.trim().length() > 100) {
            throw new ValidationException(
                    "El nombre no puede superar los "
                            + "100 caracteres."
            );
        }

        if (descripcion == null
                || descripcion.trim().isEmpty()) {

            throw new ValidationException(
                    "La descripción del servicio es "
                            + "obligatoria."
            );
        }

        if (costoBase <= 0) {
            throw new ValidationException(
                    "El costo base debe ser mayor que cero."
            );
        }
    }
}

