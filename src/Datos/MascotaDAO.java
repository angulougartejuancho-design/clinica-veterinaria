/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.Cliente;
import modelo.Especie;
import modelo.Mascota;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author angul
 */
public class MascotaDAO {

  
    public void insertar(
            Mascota mascota,
            int idCliente)
            throws SQLException {

        String sql = """
            INSERT INTO mascotas
                (
                    id_cliente,
                    nombre,
                    especie,
                    raza,
                    fecha_nacimiento
                )
            VALUES
                (?, ?, ?, ?, ?)
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(1, idCliente);
            ps.setString(2, mascota.getNombre());
            ps.setString(
                    3,
                    mascota.getEspecie().name()
            );
            ps.setString(4, mascota.getRaza());

            colocarFecha(
                    ps,
                    5,
                    mascota.getFechaNacimiento()
            );

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    mascota.setId(rs.getInt(1));
                }
            }
        }
    }

    
    public List<Mascota> listarPorCliente(
            int idCliente)
            throws SQLException {

        List<Mascota> mascotas = new ArrayList<>();

        String sql = """
            SELECT
                id_mascota,
                nombre,
                especie,
                raza,
                fecha_nacimiento
            FROM mascotas
            WHERE id_cliente = ?
            ORDER BY nombre
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(
                            crearMascotaDesdeResultSet(rs)
                    );
                }
            }
        }

        return mascotas;
    }

    
    public List<Mascota> listarPorCliente(
            Cliente cliente)
            throws SQLException {

        List<Mascota> mascotas =
                listarPorCliente(cliente.getId());

        for (Mascota mascota : mascotas) {
            mascota.setCliente(cliente);
        }

        return mascotas;
    }

   
    public Mascota buscarPorId(int idMascota)
            throws SQLException {

        String sql = """
            SELECT
                id_mascota,
                nombre,
                especie,
                raza,
                fecha_nacimiento
            FROM mascotas
            WHERE id_mascota = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idMascota);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearMascotaDesdeResultSet(rs);
                }
            }
        }

        return null;
    }

   
    public void actualizar(Mascota mascota)
            throws SQLException {

        String sql = """
            UPDATE mascotas
            SET nombre = ?,
                especie = ?,
                raza = ?,
                fecha_nacimiento = ?
            WHERE id_mascota = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, mascota.getNombre());
            ps.setString(
                    2,
                    mascota.getEspecie().name()
            );
            ps.setString(3, mascota.getRaza());

            colocarFecha(
                    ps,
                    4,
                    mascota.getFechaNacimiento()
            );

            ps.setInt(5, mascota.getId());

            ps.executeUpdate();
        }
    }

   
    public void cambiarCliente(
            int idMascota,
            int idClienteNuevo)
            throws SQLException {

        String sql = """
            UPDATE mascotas
            SET id_cliente = ?
            WHERE id_mascota = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idClienteNuevo);
            ps.setInt(2, idMascota);

            ps.executeUpdate();
        }
    }

   
    public void eliminar(int idMascota)
            throws SQLException {

        String sql = """
            DELETE FROM mascotas
            WHERE id_mascota = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idMascota);
            ps.executeUpdate();
        }
    }

 
    private void colocarFecha(
            PreparedStatement ps,
            int posicion,
            LocalDate fecha)
            throws SQLException {

        if (fecha == null) {
            ps.setNull(posicion, Types.DATE);
        } else {
            ps.setDate(
                    posicion,
                    Date.valueOf(fecha)
            );
        }
    }

 
    private Mascota crearMascotaDesdeResultSet(
            ResultSet rs)
            throws SQLException {

        Date fechaSQL =
                rs.getDate("fecha_nacimiento");

        LocalDate fechaNacimiento =
                fechaSQL != null
                        ? fechaSQL.toLocalDate()
                        : null;

        Especie especie;

        try {
            especie = Especie.valueOf(
                    rs.getString("especie")
            );
        } catch (IllegalArgumentException
                 | NullPointerException excepcion) {

            especie = Especie.OTRO;
        }

        return new Mascota(
                rs.getInt("id_mascota"),
                rs.getString("nombre"),
                especie,
                rs.getString("raza"),
                fechaNacimiento
        );
    }
}