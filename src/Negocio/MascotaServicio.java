/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio;

import Datos.ClienteDAO;
import Datos.MascotaDAO;
import Exception.ValidationException;
import modelo.Cliente;
import modelo.Especie;
import modelo.Mascota;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Contiene las validaciones y operaciones
 * relacionadas con las mascotas.
 *
 * @author angul
 */
public class MascotaServicio {

    private final MascotaDAO mascotaDAO;
    private final ClienteDAO clienteDAO;

    public MascotaServicio() {
        this.mascotaDAO = new MascotaDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public Mascota registrar(
            int idCliente,
            String nombre,
            Especie especie,
            String raza,
            LocalDate fechaNacimiento)
            throws ValidationException, SQLException {

        validarIdCliente(idCliente);

        validarDatosMascota(
                nombre,
                especie,
                raza,
                fechaNacimiento
        );

        Cliente cliente =
                clienteDAO.buscarPorId(idCliente);

        if (cliente == null) {
            throw new ValidationException(
                    "El cliente seleccionado no existe."
            );
        }

        Mascota mascota = new Mascota(
                0,
                nombre.trim(),
                especie,
                raza.trim(),
                fechaNacimiento
        );

        mascota.setCliente(cliente);

        mascotaDAO.insertar(
                mascota,
                idCliente
        );

        return mascota;
    }

    public List<Mascota> listarPorCliente(
            int idCliente)
            throws ValidationException, SQLException {

        validarIdCliente(idCliente);

        return mascotaDAO.listarPorCliente(idCliente);
    }

    public Mascota buscarPorId(int idMascota)
            throws ValidationException, SQLException {

        validarIdMascota(idMascota);

        Mascota mascota =
                mascotaDAO.buscarPorId(idMascota);

        if (mascota == null) {
            throw new ValidationException(
                    "No se encontró la mascota seleccionada."
            );
        }

        return mascota;
    }

    public void actualizar(
            int idMascota,
            String nombre,
            Especie especie,
            String raza,
            LocalDate fechaNacimiento)
            throws ValidationException, SQLException {

        validarIdMascota(idMascota);

        validarDatosMascota(
                nombre,
                especie,
                raza,
                fechaNacimiento
        );

        Mascota mascota =
                mascotaDAO.buscarPorId(idMascota);

        if (mascota == null) {
            throw new ValidationException(
                    "La mascota que desea actualizar no existe."
            );
        }

        mascota.setNombre(nombre.trim());
        mascota.setEspecie(especie);
        mascota.setRaza(raza.trim());
        mascota.setFechaNacimiento(fechaNacimiento);

        mascotaDAO.actualizar(mascota);
    }

    public void cambiarCliente(
            int idMascota,
            int idClienteNuevo)
            throws ValidationException, SQLException {

        validarIdMascota(idMascota);
        validarIdCliente(idClienteNuevo);

        Mascota mascota =
                mascotaDAO.buscarPorId(idMascota);

        if (mascota == null) {
            throw new ValidationException(
                    "La mascota seleccionada no existe."
            );
        }

        Cliente clienteNuevo =
                clienteDAO.buscarPorId(idClienteNuevo);

        if (clienteNuevo == null) {
            throw new ValidationException(
                    "El nuevo cliente seleccionado no existe."
            );
        }

        mascotaDAO.cambiarCliente(
                idMascota,
                idClienteNuevo
        );
    }

    public void eliminar(int idMascota)
            throws ValidationException, SQLException {

        validarIdMascota(idMascota);

        Mascota mascota =
                mascotaDAO.buscarPorId(idMascota);

        if (mascota == null) {
            throw new ValidationException(
                    "La mascota que desea eliminar no existe."
            );
        }

        mascotaDAO.eliminar(idMascota);
    }

    private void validarDatosMascota(
            String nombre,
            Especie especie,
            String raza,
            LocalDate fechaNacimiento)
            throws ValidationException {

        if (nombre == null
                || nombre.trim().isEmpty()) {

            throw new ValidationException(
                    "El nombre de la mascota es obligatorio."
            );
        }

        if (nombre.trim().length() < 2) {
            throw new ValidationException(
                    "El nombre debe contener al menos 2 caracteres."
            );
        }

        if (nombre.trim().length() > 100) {
            throw new ValidationException(
                    "El nombre no puede superar los 100 caracteres."
            );
        }

        if (especie == null) {
            throw new ValidationException(
                    "Debe seleccionar una especie."
            );
        }

        if (raza == null
                || raza.trim().isEmpty()) {

            throw new ValidationException(
                    "La raza de la mascota es obligatoria."
            );
        }

        if (raza.trim().length() > 100) {
            throw new ValidationException(
                    "La raza no puede superar los 100 caracteres."
            );
        }

        if (fechaNacimiento == null) {
            throw new ValidationException(
                    "La fecha de nacimiento es obligatoria."
            );
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "La fecha de nacimiento no puede ser futura."
            );
        }
    }

    private void validarIdCliente(int idCliente)
            throws ValidationException {

        if (idCliente <= 0) {
            throw new ValidationException(
                    "Debe seleccionar un cliente válido."
            );
        }
    }

    private void validarIdMascota(int idMascota)
            throws ValidationException {

        if (idMascota <= 0) {
            throw new ValidationException(
                    "Debe seleccionar una mascota válida."
            );
        }
    }
}