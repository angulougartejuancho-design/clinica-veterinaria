/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;
import modelo.Cita;
import modelo.Especialidad;
import modelo.Especie;
import modelo.EstadoCita;
import modelo.Mascota;
import modelo.Veterinario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Daryelin
 */
public class CitaDAO {
     public void insertar(Cita cita) throws SQLException {

        String sql =
                "INSERT INTO citas "
                + "(id_mascota, id_veterinario, fecha, hora, motivo, estado) "
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
                    cita.getMascota().getId()
            );

            sentencia.setInt(
                    2,
                    cita.getVeterinario().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(cita.getFecha())
            );

            sentencia.setTime(
                    4,
                    Time.valueOf(cita.getHora())
            );

            sentencia.setString(
                    5,
                    cita.getMotivo()
            );

            sentencia.setString(
                    6,
                    cita.getEstado().name()
            );

            sentencia.executeUpdate();

            try (
                    ResultSet clavesGeneradas =
                            sentencia.getGeneratedKeys()
            ) {

                if (clavesGeneradas.next()) {
                    cita.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }
        }
    }

    public List<Cita> listar() throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql =
                "SELECT "
                + "c.id_cita, "
                + "c.fecha, "
                + "c.hora, "
                + "c.motivo, "
                + "c.estado, "
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
                + "FROM citas c "
                + "INNER JOIN mascotas m "
                + "ON c.id_mascota = m.id_mascota "
                + "INNER JOIN veterinarios v "
                + "ON c.id_veterinario = v.id_veterinario "
                + "ORDER BY c.fecha ASC, c.hora ASC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                citas.add(
                        crearCita(resultado)
                );
            }
        }

        return citas;
    }

    public Cita buscarPorId(int idCita)
            throws SQLException {

        String sql =
                "SELECT "
                + "c.id_cita, "
                + "c.fecha, "
                + "c.hora, "
                + "c.motivo, "
                + "c.estado, "
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
                + "FROM citas c "
                + "INNER JOIN mascotas m "
                + "ON c.id_mascota = m.id_mascota "
                + "INNER JOIN veterinarios v "
                + "ON c.id_veterinario = v.id_veterinario "
                + "WHERE c.id_cita = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idCita);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return crearCita(resultado);
                }
            }
        }

        return null;
    }

    public List<Cita> listarPorVeterinario(
            int idVeterinario)
            throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql =
                "SELECT "
                + "c.id_cita, "
                + "c.fecha, "
                + "c.hora, "
                + "c.motivo, "
                + "c.estado, "
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
                + "FROM citas c "
                + "INNER JOIN mascotas m "
                + "ON c.id_mascota = m.id_mascota "
                + "INNER JOIN veterinarios v "
                + "ON c.id_veterinario = v.id_veterinario "
                + "WHERE c.id_veterinario = ? "
                + "ORDER BY c.fecha ASC, c.hora ASC";

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

                while (resultado.next()) {
                    citas.add(
                            crearCita(resultado)
                    );
                }
            }
        }

        return citas;
    }

    public List<Cita> listarPorMascota(
            int idMascota)
            throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql =
                "SELECT "
                + "c.id_cita, "
                + "c.fecha, "
                + "c.hora, "
                + "c.motivo, "
                + "c.estado, "
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
                + "FROM citas c "
                + "INNER JOIN mascotas m "
                + "ON c.id_mascota = m.id_mascota "
                + "INNER JOIN veterinarios v "
                + "ON c.id_veterinario = v.id_veterinario "
                + "WHERE c.id_mascota = ? "
                + "ORDER BY c.fecha ASC, c.hora ASC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    idMascota
            );

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                while (resultado.next()) {
                    citas.add(
                            crearCita(resultado)
                    );
                }
            }
        }

        return citas;
    }

    public boolean actualizar(Cita cita)
            throws SQLException {

        String sql =
                "UPDATE citas SET "
                + "id_mascota = ?, "
                + "id_veterinario = ?, "
                + "fecha = ?, "
                + "hora = ?, "
                + "motivo = ?, "
                + "estado = ? "
                + "WHERE id_cita = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    cita.getMascota().getId()
            );

            sentencia.setInt(
                    2,
                    cita.getVeterinario().getId()
            );

            sentencia.setDate(
                    3,
                    Date.valueOf(cita.getFecha())
            );

            sentencia.setTime(
                    4,
                    Time.valueOf(cita.getHora())
            );

            sentencia.setString(
                    5,
                    cita.getMotivo()
            );

            sentencia.setString(
                    6,
                    cita.getEstado().name()
            );

            sentencia.setInt(
                    7,
                    cita.getId()
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(
            int idCita,
            EstadoCita estado)
            throws SQLException {

        String sql =
                "UPDATE citas "
                + "SET estado = ? "
                + "WHERE id_cita = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    estado.name()
            );

            sentencia.setInt(
                    2,
                    idCita
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCita)
            throws SQLException {

        String sql =
                "DELETE FROM citas "
                + "WHERE id_cita = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(
                    1,
                    idCita
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean existeHorarioOcupado(
            int idVeterinario,
            LocalDate fecha,
            LocalTime hora,
            int idCitaExcluir)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                + "FROM citas "
                + "WHERE id_veterinario = ? "
                + "AND fecha = ? "
                + "AND hora = ? "
                + "AND id_cita <> ?";

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

            sentencia.setDate(
                    2,
                    Date.valueOf(fecha)
            );

            sentencia.setTime(
                    3,
                    Time.valueOf(hora)
            );

            sentencia.setInt(
                    4,
                    idCitaExcluir
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
    
    private Cita crearCita(
            ResultSet resultado)
            throws SQLException {

        Mascota mascota =
                crearMascota(resultado);

        Veterinario veterinario =
                crearVeterinario(resultado);

        Date fechaSQL =
                resultado.getDate("fecha");

        Time horaSQL =
                resultado.getTime("hora");

        LocalDate fecha =
                fechaSQL != null
                        ? fechaSQL.toLocalDate()
                        : null;

        LocalTime hora =
                horaSQL != null
                        ? horaSQL.toLocalTime()
                        : null;

        EstadoCita estado =
                EstadoCita.desdeTexto(
                        resultado.getString("estado")
                );

        return new Cita(
                resultado.getInt("id_cita"),
                mascota,
                veterinario,
                fecha,
                hora,
                resultado.getString("motivo"),
                estado
        );
    }

    private Mascota crearMascota(
            ResultSet resultado)
            throws SQLException {

        Date fechaNacimientoSQL =
                resultado.getDate(
                        "fecha_nacimiento"
                );

        LocalDate fechaNacimiento =
                fechaNacimientoSQL != null
                        ? fechaNacimientoSQL.toLocalDate()
                        : null;

        return new Mascota(
                resultado.getInt("id_mascota"),
                resultado.getString(
                        "nombre_mascota"
                ),
                Especie.valueOf(
                        resultado
                                .getString("especie")
                                .toUpperCase()
                ),
                resultado.getString("raza"),
                fechaNacimiento
        );
    }

    private Veterinario crearVeterinario(
            ResultSet resultado)
            throws SQLException {

        Especialidad especialidad =
                convertirEspecialidad(
                        resultado.getString(
                                "especialidad"
                        )
                );

        return new Veterinario(
                resultado.getInt(
                        "id_veterinario"
                ),
                resultado.getString(
                        "nombre_veterinario"
                ),
                resultado.getString(
                        "telefono_veterinario"
                ),
                resultado.getString(
                        "email_veterinario"
                ),
                especialidad
        );
    }


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
                            especialidad.name()
                                    .replace("_", " ")
                    );

            String descripcionNormalizada =
                    normalizarTexto(
                            especialidad.getDescripcion()
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
                "La especialidad almacenada no es válida: "
                        + texto
        );
    }


    private String normalizarTexto(String texto) {

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
