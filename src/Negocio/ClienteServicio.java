/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.ClienteDAO;
import modelo.Cliente;
import Exception.ValidationException;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Contiene las validaciones y operaciones
 * relacionadas con los clientes.
 *
 * @author angul
 */
public class ClienteServicio {

    private final ClienteDAO clienteDAO;

    private static final Pattern PATRON_EMAIL =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );

    public ClienteServicio() {
        this.clienteDAO = new ClienteDAO();
    }

    public Cliente registrar(
            String nombre,
            String telefono,
            String email,
            String direccion)
            throws ValidationException, SQLException {

        validarDatos(
                nombre,
                telefono,
                email,
                direccion
        );

        Cliente cliente = new Cliente(
                0,
                nombre.trim(),
                telefono.trim(),
                email.trim(),
                direccion.trim()
        );

        clienteDAO.insertar(cliente);

        return cliente;
    }

    public List<Cliente> listar()
            throws SQLException {

        return clienteDAO.listarTodos();
    }

    public Cliente buscarPorId(int idCliente)
            throws ValidationException, SQLException {

        validarId(idCliente);

        Cliente cliente =
                clienteDAO.buscarPorId(idCliente);

        if (cliente == null) {
            throw new ValidationException(
                    "No se encontró el cliente seleccionado."
            );
        }

        return cliente;
    }

    public void actualizar(
            int idCliente,
            String nombre,
            String telefono,
            String email,
            String direccion)
            throws ValidationException, SQLException {

        validarId(idCliente);

        validarDatos(
                nombre,
                telefono,
                email,
                direccion
        );

        Cliente cliente = new Cliente(
                idCliente,
                nombre.trim(),
                telefono.trim(),
                email.trim(),
                direccion.trim()
        );

        clienteDAO.actualizar(cliente);
    }

    public void eliminar(int idCliente)
            throws ValidationException, SQLException {

        validarId(idCliente);

        Cliente cliente =
                clienteDAO.buscarPorId(idCliente);

        if (cliente == null) {
            throw new ValidationException(
                    "El cliente que desea eliminar no existe."
            );
        }

        clienteDAO.eliminar(idCliente);
    }

    private void validarDatos(
            String nombre,
            String telefono,
            String email,
            String direccion)
            throws ValidationException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException(
                    "El nombre del cliente es obligatorio."
            );
        }

        if (nombre.trim().length() < 3) {
            throw new ValidationException(
                    "El nombre debe contener al menos 3 caracteres."
            );
        }

        if (nombre.trim().length() > 100) {
            throw new ValidationException(
                    "El nombre no puede superar los 100 caracteres."
            );
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ValidationException(
                    "El teléfono es obligatorio."
            );
        }

        String telefonoLimpio =
                telefono.replaceAll("[\\s-]", "");

        if (!telefonoLimpio.matches("\\d{8,15}")) {
            throw new ValidationException(
                    "El teléfono debe contener entre 8 y 15 números."
            );
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException(
                    "El correo electrónico es obligatorio."
            );
        }

        if (!PATRON_EMAIL.matcher(email.trim()).matches()) {
            throw new ValidationException(
                    "El correo electrónico no tiene un formato válido."
            );
        }

        if (email.trim().length() > 120) {
            throw new ValidationException(
                    "El correo no puede superar los 120 caracteres."
            );
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ValidationException(
                    "La dirección es obligatoria."
            );
        }

        if (direccion.trim().length() > 200) {
            throw new ValidationException(
                    "La dirección no puede superar los 200 caracteres."
            );
        }
    }

    private void validarId(int idCliente)
            throws ValidationException {

        if (idCliente <= 0) {
            throw new ValidationException(
                    "Debe seleccionar un cliente válido."
            );
        }
    }
}