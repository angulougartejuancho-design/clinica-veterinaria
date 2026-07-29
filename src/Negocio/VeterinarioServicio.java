/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.VeterinarioDAO;
import modelo.Especialidad;
import modelo.Veterinario;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Daryelin
 */
public class VeterinarioServicio {
     private final VeterinarioDAO veterinarioDAO;

    /*
     * Expresión regular sencilla para validar correos.
     */
    private static final Pattern PATRON_EMAIL =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            );

    /**
     * Constructor principal.
     */
    public VeterinarioServicio() {
        veterinarioDAO = new VeterinarioDAO();
    }

    /**
     * Registra un veterinario después de validar sus datos.
     *
     * @param veterinario veterinario que se desea registrar
     * @throws IllegalArgumentException si los datos no son válidos
     * @throws SQLException si ocurre un error con la base de datos
     */
    public void registrar(Veterinario veterinario)
            throws SQLException {

        validarVeterinario(veterinario);

        String email = limpiarTexto(
                veterinario.getEmail()
        );

        /*
         * En la base de datos el email permite valores nulos.
         * Solo se verifica duplicado cuando se ingresó un correo.
         */
        if (email != null
                && veterinarioDAO.existeEmail(email, 0)) {

            throw new IllegalArgumentException(
                    "Ya existe un veterinario registrado "
                            + "con ese correo electrónico."
            );
        }

        prepararDatos(veterinario);

        veterinarioDAO.insertar(veterinario);
    }

    /**
     * Actualiza los datos de un veterinario existente.
     *
     * @param veterinario veterinario modificado
     * @return true si fue actualizado
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws SQLException si ocurre un error JDBC
     */
    public boolean actualizar(Veterinario veterinario)
            throws SQLException {

        if (veterinario == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un veterinario."
            );
        }

        if (veterinario.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El veterinario no posee un ID válido."
            );
        }

        validarVeterinario(veterinario);

        String email = limpiarTexto(
                veterinario.getEmail()
        );

        if (email != null
                && veterinarioDAO.existeEmail(
                        email,
                        veterinario.getId()
                )) {

            throw new IllegalArgumentException(
                    "Ya existe otro veterinario registrado "
                            + "con ese correo electrónico."
            );
        }

        prepararDatos(veterinario);

        return veterinarioDAO.actualizar(veterinario);
    }

    /**
     * Elimina un veterinario mediante su ID.
     *
     * La base de datos impedirá eliminarlo cuando tenga
     * citas relacionadas debido a ON DELETE RESTRICT.
     *
     * @param idVeterinario identificador del veterinario
     * @return true si fue eliminado
     * @throws IllegalArgumentException si el ID es inválido
     * @throws SQLException si ocurre un error JDBC
     */
    public boolean eliminar(int idVeterinario)
            throws SQLException {

        if (idVeterinario <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un veterinario válido."
            );
        }

        Veterinario veterinario =
                veterinarioDAO.buscarPorId(idVeterinario);

        if (veterinario == null) {
            throw new IllegalArgumentException(
                    "El veterinario seleccionado no existe."
            );
        }

        return veterinarioDAO.eliminar(idVeterinario);
    }

    /**
     * Obtiene todos los veterinarios registrados.
     */
    public List<Veterinario> listar()
            throws SQLException {

        return veterinarioDAO.listar();
    }

    /**
     * Busca un veterinario mediante su ID.
     */
    public Veterinario buscarPorId(int idVeterinario)
            throws SQLException {

        if (idVeterinario <= 0) {
            throw new IllegalArgumentException(
                    "El ID del veterinario debe ser mayor que cero."
            );
        }

        return veterinarioDAO.buscarPorId(idVeterinario);
    }

    /**
     * Busca un veterinario mediante su correo electrónico.
     */
    public Veterinario buscarPorEmail(String email)
            throws SQLException {

        String emailLimpio = limpiarTexto(email);

        if (emailLimpio == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar un correo electrónico."
            );
        }

        if (!esEmailValido(emailLimpio)) {
            throw new IllegalArgumentException(
                    "El formato del correo electrónico no es válido."
            );
        }

        return veterinarioDAO.buscarPorEmail(emailLimpio);
    }

    /**
     * Valida los datos principales del veterinario.
     */
    private void validarVeterinario(
            Veterinario veterinario) {

        if (veterinario == null) {
            throw new IllegalArgumentException(
                    "Los datos del veterinario son obligatorios."
            );
        }

        String nombre = limpiarTexto(
                veterinario.getNombre()
        );

        String telefono = limpiarTexto(
                veterinario.getTelefono()
        );

        String email = limpiarTexto(
                veterinario.getEmail()
        );

        Especialidad especialidad =
                veterinario.getEspecialidad();

        if (nombre == null) {
            throw new IllegalArgumentException(
                    "El nombre del veterinario es obligatorio."
            );
        }

        if (nombre.length() < 3) {
            throw new IllegalArgumentException(
                    "El nombre debe contener al menos 3 caracteres."
            );
        }

        if (nombre.length() > 100) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar los 100 caracteres."
            );
        }

        if (especialidad == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una especialidad."
            );
        }

        /*
         * El teléfono puede quedar vacío porque la columna
         * permite NULL, pero si se escribe debe ser válido.
         */
        if (telefono != null) {

            if (telefono.length() > 20) {
                throw new IllegalArgumentException(
                        "El teléfono no puede superar los 20 caracteres."
                );
            }

            if (!telefono.matches(
                    "[0-9+()\\-\\s]{7,20}"
            )) {

                throw new IllegalArgumentException(
                        "El teléfono contiene caracteres no válidos."
                );
            }
        }

        /*
         * El correo también es opcional según la base de datos.
         */
        if (email != null) {

            if (email.length() > 100) {
                throw new IllegalArgumentException(
                        "El correo no puede superar los 100 caracteres."
                );
            }

            if (!esEmailValido(email)) {
                throw new IllegalArgumentException(
                        "El formato del correo electrónico no es válido."
                );
            }
        }
    }

    /**
     * Limpia los textos antes de guardarlos.
     *
     * Los textos vacíos se convierten en null para que
     * MySQL pueda almacenarlos correctamente en campos opcionales.
     */
    private void prepararDatos(
            Veterinario veterinario) {

        veterinario.setNombre(
                veterinario.getNombre().trim()
        );

        veterinario.setTelefono(
                limpiarTexto(
                        veterinario.getTelefono()
                )
        );

        String emailLimpio =
                limpiarTexto(
                        veterinario.getEmail()
                );

        if (emailLimpio != null) {
            emailLimpio =
                    emailLimpio.toLowerCase();
        }

        veterinario.setEmail(emailLimpio);
    }

    /**
     * Comprueba el formato del correo.
     */
    private boolean esEmailValido(String email) {

        return PATRON_EMAIL
                .matcher(email)
                .matches();
    }

    /**
     * Elimina espacios innecesarios.
     *
     * @return null cuando el texto está vacío
     */
    private String limpiarTexto(String texto) {

        if (texto == null) {
            return null;
        }

        String textoLimpio = texto.trim();

        if (textoLimpio.isEmpty()) {
            return null;
        }

        return textoLimpio;
    }
}
