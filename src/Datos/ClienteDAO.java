/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author angul
 */
public class ClienteDAO {

    
    public void insertar(Cliente cliente)
            throws SQLException {

        String sql = """
            INSERT INTO clientes
                (nombre, telefono, email, direccion)
            VALUES
                (?, ?, ?, ?)
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    
    public List<Cliente> listarTodos()
            throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
            SELECT
                id_cliente,
                nombre,
                telefono,
                email,
                direccion
            FROM clientes
            ORDER BY nombre
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion")
                );

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    
    public Cliente buscarPorId(int idCliente)
            throws SQLException {

        String sql = """
            SELECT
                id_cliente,
                nombre,
                telefono,
                email,
                direccion
            FROM clientes
            WHERE id_cliente = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getString("direccion")
                    );
                }
            }
        }

        return null;
    }

   
    public void actualizar(Cliente cliente)
            throws SQLException {

        String sql = """
            UPDATE clientes
            SET nombre = ?,
                telefono = ?,
                email = ?,
                direccion = ?
            WHERE id_cliente = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getId());

            ps.executeUpdate();
        }
    }

  
    public void eliminar(int idCliente)
            throws SQLException {

        String sql = """
            DELETE FROM clientes
            WHERE id_cliente = ?
            """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }
}