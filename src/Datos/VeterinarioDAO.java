/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.Especialidad;
import modelo.Veterinario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.Normalizer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryelin
 */
public class VeterinarioDAO {
     public void insertar(Veterinario veterinario)
            throws SQLException {

        String sql =
                "INSERT INTO veterinarios "
                + "(nombre, especialidad, telefono, email) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            sentencia.setString(
                    1,
                    veterinario.getNombre()
            );

            sentencia.setString(
                    2,
                    veterinario
                            .getEspecialidad()
                            .getDescripcion()
            );

            sentencia.setString(
                    3,
                    veterinario.getTelefono()
            );

            sentencia.setString(
                    4,
                    veterinario.getEmail()
            );

            sentencia.executeUpdate();

            try (
                    ResultSet clavesGeneradas =
                            sentencia.getGeneratedKeys()
            ) {

                if (clavesGeneradas.next()) {
                    veterinario.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }
        }
    }

    /**
     * Consulta todos los veterinarios registrados.
     */
    public List<Veterinario> listar()
            throws SQLException {

        List<Veterinario> veterinarios =
                new ArrayList<>();

        String sql =
                "SELECT "
                + "id_veterinario, "
                + "nombre, "
                + "especialidad, "
                + "telefono, "
                + "email "
                + "FROM veterinarios "
                + "ORDER BY nombre ASC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                veterinarios.add(
                        crearVeterinario(resultado)
                );
            }
        }

        return veterinarios;
    }

    /**
     * Busca un veterinario por su identificador.
     *
     * @return veterinario encontrado o null si no existe
     */
    public Veterinario buscarPorId(
            int idVeterinario)
            throws SQLException {

        String sql =
                "SELECT "
                + "id_veterinario, "
                + "nombre, "
                + "especialidad, "
                + "telefono, "
                + "email "
                + "FROM veterinarios "
                + "WHERE id_veterinario = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    idVeterinario
            );

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return crearVeterinario(resultado);
                }
            }
        }

        return null;
    }

    /**
     * Busca un veterinario mediante su correo.
     */
    public Veterinario buscarPorEmail(
            String email)
            throws SQLException {

        String sql =
                "SELECT "
                + "id_veterinario, "
                + "nombre, "
                + "especialidad, "
                + "telefono, "
                + "email "
                + "FROM veterinarios "
                + "WHERE email = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    email
            );

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return crearVeterinario(resultado);
                }
            }
        }

        return null;
    }

    /**
     * Actualiza los datos de un veterinario.
     */
    public boolean actualizar(
            Veterinario veterinario)
            throws SQLException {

        String sql =
                "UPDATE veterinarios SET "
                + "nombre = ?, "
                + "especialidad = ?, "
                + "telefono = ?, "
                + "email = ? "
                + "WHERE id_veterinario = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    veterinario.getNombre()
            );

            sentencia.setString(
                    2,
                    veterinario
                            .getEspecialidad()
                            .getDescripcion()
            );

            sentencia.setString(
                    3,
                    veterinario.getTelefono()
            );

            sentencia.setString(
                    4,
                    veterinario.getEmail()
            );

            sentencia.setInt(
                    5,
                    veterinario.getId()
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un veterinario mediante su identificador.
     *
     * MySQL impedirá la eliminación cuando tenga citas
     * relacionadas debido a la llave foránea con
     * ON DELETE RESTRICT.
     */
    public boolean eliminar(
            int idVeterinario)
            throws SQLException {

        String sql =
                "DELETE FROM veterinarios "
                + "WHERE id_veterinario = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    idVeterinario
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Comprueba si existe otro veterinario con el mismo email.
     *
     * idVeterinarioExcluir debe ser 0 al registrar.
     * Al actualizar, debe enviarse el ID editado.
     */
    public boolean existeEmail(
            String email,
            int idVeterinarioExcluir)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                + "FROM veterinarios "
                + "WHERE email = ? "
                + "AND id_veterinario <> ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    email
            );

            sentencia.setInt(
                    2,
                    idVeterinarioExcluir
            );

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Construye un objeto Veterinario a partir
     * de una fila del ResultSet.
     */
    private Veterinario crearVeterinario(
            ResultSet resultado)
            throws SQLException {

        return new Veterinario(
                resultado.getInt(
                        "id_veterinario"
                ),
                resultado.getString(
                        "nombre"
                ),
                resultado.getString(
                        "telefono"
                ),
                resultado.getString(
                        "email"
                ),
                convertirEspecialidad(
                        resultado.getString(
                                "especialidad"
                        )
                )
        );
    }

    /**
     * Convierte el texto almacenado en MySQL
     * en una constante del enum Especialidad.
     */
    private Especialidad convertirEspecialidad(
            String texto)
            throws SQLException {

        if (texto == null || texto.isBlank()) {
            throw new SQLException(
                    "La especialidad del veterinario "
                            + "no puede estar vacía."
            );
        }

        String textoNormalizado =
                normalizarTexto(texto);

        for (Especialidad especialidad
                : Especialidad.values()) {

            String nombreNormalizado =
                    normalizarTexto(
                            especialidad
                                    .name()
                                    .replace("_", " ")
                    );

            String descripcionNormalizada =
                    normalizarTexto(
                            especialidad
                                    .getDescripcion()
                    );

            if (textoNormalizado.equals(
                    nombreNormalizado
            )
                    || textoNormalizado.equals(
                            descripcionNormalizada
                    )) {

                return especialidad;
            }
        }

        throw new SQLException(
                "Especialidad no reconocida: "
                        + texto
        );
    }

    /**
     * Elimina tildes y diferencias entre mayúsculas,
     * minúsculas, espacios y guiones bajos.
     */
    private String normalizarTexto(
            String texto) {

        String textoSinTildes =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                ).replaceAll(
                        "\\p{M}",
                        ""
                );

        return textoSinTildes
                .trim()
                .replace("_", " ")
                .replaceAll("\\s+", " ")
                .toUpperCase();
    }
}
