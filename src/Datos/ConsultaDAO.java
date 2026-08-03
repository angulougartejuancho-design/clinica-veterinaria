/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.Consulta;
import modelo.Especialidad;
import modelo.Especie;
import modelo.Mascota;
import modelo.Veterinario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.Normalizer;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anyel
 */
public class ConsultaDAO {
    
public void insertar(Consulta consulta)
            throws SQLException {

        String sql =
                "INSERT INTO consultas "
                + "(id_mascota, id_veterinario, fecha, "
                + "diagnostico, tratamiento, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            sentencia.setInt(
                    1,
                    consulta.getMascota().getId()
            );

            sentencia.setInt(
                    2,
                    consulta.getVeterinario().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(consulta.getFecha())
            );

            sentencia.setString(
                    4,
                    consulta.getDiagnostico()
            );

            sentencia.setString(
                    5,
                    consulta.getTratamiento()
            );

            sentencia.setString(
                    6,
                    consulta.getObservaciones()
            );

            sentencia.executeUpdate();

            try (
                    ResultSet clavesGeneradas =
                            sentencia.getGeneratedKeys()
            ) {

                if (clavesGeneradas.next()) {
                    consulta.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }
        }
    }

    public List<Consulta> listar() throws SQLException {

        List<Consulta> consultas = new ArrayList<>();

        String sql = consultaBase()
                + "ORDER BY co.fecha DESC, co.id_consulta DESC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                consultas.add(crearConsulta(resultado));
            }
        }

        return consultas;
    }

    public List<Consulta> listarPorMascota(int idMascota)
            throws SQLException {

        List<Consulta> consultas = new ArrayList<>();

        String sql = consultaBase()
                + "WHERE co.id_mascota = ? "
                + "ORDER BY co.fecha DESC, co.id_consulta DESC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idMascota);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                while (resultado.next()) {
                    consultas.add(
                            crearConsulta(resultado)
                    );
                }
            }
        }

        return consultas;
    }

    public Consulta buscarPorId(int idConsulta)
            throws SQLException {

        String sql = consultaBase()
                + "WHERE co.id_consulta = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idConsulta);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return crearConsulta(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Consulta consulta)
            throws SQLException {

        String sql =
                "UPDATE consultas "
                + "SET id_mascota = ?, "
                + "id_veterinario = ?, "
                + "fecha = ?, "
                + "diagnostico = ?, "
                + "tratamiento = ?, "
                + "observaciones = ? "
                + "WHERE id_consulta = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    consulta.getMascota().getId()
            );

            sentencia.setInt(
                    2,
                    consulta.getVeterinario().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(consulta.getFecha())
            );

            sentencia.setString(
                    4,
                    consulta.getDiagnostico()
            );

            sentencia.setString(
                    5,
                    consulta.getTratamiento()
            );

            sentencia.setString(
                    6,
                    consulta.getObservaciones()
            );

            sentencia.setInt(
                    7,
                    consulta.getId()
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idConsulta)
            throws SQLException {

        String sql =
                "DELETE FROM consultas "
                + "WHERE id_consulta = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idConsulta);

            return sentencia.executeUpdate() > 0;
        }
    }

    private String consultaBase() {

        return "SELECT "
                + "co.id_consulta, "
                + "co.fecha, "
                + "co.diagnostico, "
                + "co.tratamiento, "
                + "co.observaciones, "
                + "m.id_mascota, "
                + "m.nombre AS nombre_mascota, "
                + "m.especie, "
                + "m.raza, "
                + "m.fecha_nacimiento, "
                + "v.id_veterinario, "
                + "v.nombre AS nombre_veterinario, "
                + "v.especialidad, "
                + "v.telefono AS telefono_veterinario, "
                + "v.email AS email_veterinario "
                + "FROM consultas co "
                + "INNER JOIN mascotas m "
                + "ON co.id_mascota = m.id_mascota "
                + "INNER JOIN veterinarios v "
                + "ON co.id_veterinario = v.id_veterinario ";
    }

    private Consulta crearConsulta(ResultSet resultado)
            throws SQLException {

        Mascota mascota = crearMascota(resultado);
        Veterinario veterinario = crearVeterinario(resultado);

        Date fechaSQL = resultado.getDate("fecha");

        LocalDate fecha =
                fechaSQL != null
                        ? fechaSQL.toLocalDate()
                        : null;

        return new Consulta(
                resultado.getInt("id_consulta"),
                mascota,
                veterinario,
                fecha,
                resultado.getString("diagnostico"),
                resultado.getString("tratamiento"),
                resultado.getString("observaciones")
        );
    }

    private Mascota crearMascota(ResultSet resultado)
            throws SQLException {

        Date fechaNacimientoSQL =
                resultado.getDate("fecha_nacimiento");

        LocalDate fechaNacimiento =
                fechaNacimientoSQL != null
                        ? fechaNacimientoSQL.toLocalDate()
                        : null;

        return new Mascota(
                resultado.getInt("id_mascota"),
                resultado.getString("nombre_mascota"),
                Especie.valueOf(
                        resultado
                                .getString("especie")
                                .toUpperCase()
                ),
                resultado.getString("raza"),
                fechaNacimiento
        );
    }

    private Veterinario crearVeterinario(ResultSet resultado)
            throws SQLException {

        Especialidad especialidad =
                convertirEspecialidad(
                        resultado.getString("especialidad")
                );

        return new Veterinario(
                resultado.getInt("id_veterinario"),
                resultado.getString("nombre_veterinario"),
                resultado.getString("telefono_veterinario"),
                resultado.getString("email_veterinario"),
                especialidad
        );
    }

    private Especialidad convertirEspecialidad(String texto)
            throws SQLException {

        if (texto == null || texto.isBlank()) {
            throw new SQLException(
                    "La especialidad del veterinario "
                            + "no puede estar vacía."
            );
        }

        String textoNormalizado = normalizarTexto(texto);

        for (Especialidad especialidad
                : Especialidad.values()) {

            String nombreNormalizado =
                    normalizarTexto(
                            especialidad.name()
                                    .replace("_", " ")
                    );

            String descripcionNormalizada =
                    normalizarTexto(
                            especialidad.getDescripcion()
                    );

            if (textoNormalizado.equals(nombreNormalizado)
                    || textoNormalizado.equals(
                            descripcionNormalizada
                    )) {

                return especialidad;
            }
        }

        throw new SQLException(
                "La especialidad almacenada no es válida: "
                        + texto
        );
    }

    private String normalizarTexto(String texto) {

        String textoSinTildes =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                ).replaceAll("\\p{M}", "");

        return textoSinTildes
                .trim()
                .replace("_", " ")
                .replaceAll("\\s+", " ")
                .toUpperCase();
    }
}

