/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.ConsultaGeneral;
import modelo.Procedimiento;
import modelo.Servicio;
import modelo.TipoServicio;
import modelo.Vacunacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Anyel
 */
public class ServicioDAO {
    public void insertar(Servicio servicio)
            throws SQLException {

        String sql =
                "INSERT INTO servicios "
                + "(tipo_servicio, nombre, descripcion, "
                + "costo_base, detalle_texto, "
                + "duracion_minutos) "
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

            completarSentencia(sentencia, servicio);

            sentencia.executeUpdate();

            try (
                    ResultSet clavesGeneradas =
                            sentencia.getGeneratedKeys()
            ) {

                if (clavesGeneradas.next()) {
                    servicio.setId(
                            clavesGeneradas.getInt(1)
                    );
                }
            }
        }
    }

    public List<Servicio> listar() throws SQLException {

        List<Servicio> servicios = new ArrayList<>();

        String sql =
                "SELECT * FROM servicios "
                + "ORDER BY id_servicio DESC";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {
                servicios.add(crearServicio(resultado));
            }
        }

        return servicios;
    }

    public Servicio buscarPorId(int idServicio)
            throws SQLException {

        String sql =
                "SELECT * FROM servicios "
                + "WHERE id_servicio = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idServicio);

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return crearServicio(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Servicio servicio)
            throws SQLException {

        String sql =
                "UPDATE servicios "
                + "SET tipo_servicio = ?, "
                + "nombre = ?, "
                + "descripcion = ?, "
                + "costo_base = ?, "
                + "detalle_texto = ?, "
                + "duracion_minutos = ? "
                + "WHERE id_servicio = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            int siguientePosicion =
                    completarSentencia(sentencia, servicio);

            sentencia.setInt(
                    siguientePosicion,
                    servicio.getId()
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idServicio)
            throws SQLException {

        String sql =
                "DELETE FROM servicios "
                + "WHERE id_servicio = ?";

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setInt(1, idServicio);

            return sentencia.executeUpdate() > 0;
        }
    }

  
    private int completarSentencia(
            PreparedStatement sentencia,
            Servicio servicio)
            throws SQLException {

        sentencia.setString(
                1,
                servicio.getTipo().name()
        );

        sentencia.setString(2, servicio.getNombre());
        sentencia.setString(3, servicio.getDescripcion());
        sentencia.setDouble(4, servicio.getCostoBase());

        if (servicio instanceof Vacunacion vacunacion) {

            sentencia.setString(
                    5,
                    vacunacion.getTipoVacuna()
            );

            sentencia.setNull(6, Types.INTEGER);

        } else if (servicio
                instanceof Procedimiento procedimiento) {

            sentencia.setNull(5, Types.VARCHAR);

            sentencia.setInt(
                    6,
                    procedimiento.getDuracionMinutos()
            );

        } else if (servicio
                instanceof ConsultaGeneral consultaGeneral) {

            sentencia.setString(
                    5,
                    consultaGeneral.getMotivoTipico()
            );

            sentencia.setNull(6, Types.INTEGER);

        } else {

            sentencia.setNull(5, Types.VARCHAR);
            sentencia.setNull(6, Types.INTEGER);
        }

        return 7;
    }

    private Servicio crearServicio(ResultSet resultado)
            throws SQLException {

        TipoServicio tipo =
                TipoServicio.desdeTexto(
                        resultado.getString(
                                "tipo_servicio"
                        )
                );

        int id = resultado.getInt("id_servicio");
        String nombre = resultado.getString("nombre");
        String descripcion =
                resultado.getString("descripcion");
        double costoBase =
                resultado.getDouble("costo_base");
        String detalleTexto =
                resultado.getString("detalle_texto");
        int duracionMinutos =
                resultado.getInt("duracion_minutos");

        return switch (tipo) {

            case VACUNACION -> new Vacunacion(
                    id,
                    nombre,
                    descripcion,
                    costoBase,
                    detalleTexto
            );

            case PROCEDIMIENTO -> new Procedimiento(
                    id,
                    nombre,
                    descripcion,
                    costoBase,
                    duracionMinutos
            );

            case CONSULTA_GENERAL -> new ConsultaGeneral(
                    id,
                    nombre,
                    descripcion,
                    costoBase,
                    detalleTexto
            );
        };
    }
}


