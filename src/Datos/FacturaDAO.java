/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.EstadoFactura;
import modelo.Factura;
import modelo.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
public class FacturaDAO {

    /**
     * Guarda una factura junto con todos sus detalles.
     *
     * Si ocurre un error al guardar la factura o alguno de sus detalles, se
     * ejecuta rollback.
     */
    public void insertar(Factura factura)
            throws SQLException {

        String sqlFactura = """
            INSERT INTO facturas
                (
                    id_cliente,
                    fecha,
                    subtotal,
                    impuesto,
                    total,
                    estado,
                    observaciones
                )
            VALUES
                (?, ?, ?, ?, ?, ?, ?)
            """;

        String sqlDetalle = """
            INSERT INTO detalle_factura
                (
                    id_factura,
                    id_servicio,
                    precio_unitario,
                    cantidad,
                    subtotal
                )
            VALUES
                (?, ?, ?, ?, ?)
            """;

        Connection conexion = null;

        try {

            conexion = ConexionBD.obtenerConexion();

            /*
             * Desactivamos el autocommit para controlar
             * manualmente la transacción.
             */
            conexion.setAutoCommit(false);

            /*
             * Primero se inserta el encabezado
             * de la factura.
             */
            try (PreparedStatement sentenciaFactura
                    = conexion.prepareStatement(
                            sqlFactura,
                            Statement.RETURN_GENERATED_KEYS
                    )) {

                        sentenciaFactura.setInt(
                                1,
                                factura.getCliente().getId()
                        );

                        sentenciaFactura.setTimestamp(
                                2,
                                Timestamp.valueOf(factura.getFecha())
                        );

                        sentenciaFactura.setDouble(
                                3,
                                factura.getSubtotal()
                        );

                        sentenciaFactura.setDouble(
                                4,
                                factura.getImpuesto()
                        );

                        sentenciaFactura.setDouble(
                                5,
                                factura.getTotal()
                        );

                        sentenciaFactura.setString(
                                6,
                                factura.getEstado().name()
                        );

                        sentenciaFactura.setString(
                                7,
                                factura.getObservaciones()
                        );

                        int filasInsertadas
                                = sentenciaFactura.executeUpdate();

                        if (filasInsertadas == 0) {
                            throw new SQLException(
                                    "No se pudo registrar la factura."
                            );
                        }

                        /*
                 * Recuperamos el ID generado por MySQL.
                         */
                        try (ResultSet clavesGeneradas
                                = sentenciaFactura.getGeneratedKeys()) {

                            if (clavesGeneradas.next()) {

                                int idFactura
                                        = clavesGeneradas.getInt(1);

                                factura.setIdFactura(idFactura);

                            } else {

                                throw new SQLException(
                                        "No se pudo obtener el ID "
                                        + "de la factura."
                                );
                            }
                        }
                    }

                    /*
             * Después se insertan todos los detalles
             * utilizando la misma conexión.
                     */
                    try (PreparedStatement sentenciaDetalle
                            = conexion.prepareStatement(sqlDetalle)) {

                        for (DetalleFactura detalle
                                : factura.getDetalles()) {

                            sentenciaDetalle.setInt(
                                    1,
                                    factura.getIdFactura()
                            );

                            sentenciaDetalle.setInt(
                                    2,
                                    detalle.getServicio().getId()
                            );

                            sentenciaDetalle.setDouble(
                                    3,
                                    detalle.getPrecioUnitario()
                            );

                            sentenciaDetalle.setInt(
                                    4,
                                    detalle.getCantidad()
                            );

                            sentenciaDetalle.setDouble(
                                    5,
                                    detalle.getSubtotal()
                            );

                            /*
                     * Se agrega el INSERT al lote.
                             */
                            sentenciaDetalle.addBatch();
                        }

                        sentenciaDetalle.executeBatch();
                    }

                    /*
             * Si todo salió correctamente,
             * confirmamos la transacción.
                     */
                    conexion.commit();

        } catch (SQLException ex) {

            /*
             * Si algo falla, se revierten todos
             * los cambios realizados.
             */
            if (conexion != null) {

                try {
                    conexion.rollback();

                } catch (SQLException errorRollback) {

                    ex.addSuppressed(errorRollback);
                }
            }

            throw ex;

        } finally {

            if (conexion != null) {

                try {
                    conexion.setAutoCommit(true);
                    conexion.close();

                } catch (SQLException ex) {
                    System.err.println(
                            "Error al cerrar la conexión: "
                            + ex.getMessage()
                    );
                }
            }
        }
    }

    /**
     * Lista todas las facturas registradas.
     *
     * Este método recupera el encabezado de cada factura, junto con los datos
     * básicos del cliente.
     */
    public List<Factura> listarTodas()
            throws SQLException {

        List<Factura> facturas = new ArrayList<>();

        String sql = """
            SELECT
                f.id_factura,
                f.fecha,
                f.subtotal,
                f.impuesto,
                f.total,
                f.estado,
                f.observaciones,

                c.id_cliente,
                c.nombre,
                c.telefono,
                c.email,
                c.direccion

            FROM facturas f

            INNER JOIN clientes c
                ON f.id_cliente = c.id_cliente

            ORDER BY f.id_factura DESC
            """;

        try (Connection conexion
                = ConexionBD.obtenerConexion(); PreparedStatement sentencia
                = conexion.prepareStatement(sql); ResultSet resultado
                = sentencia.executeQuery()) {

            while (resultado.next()) {

                Cliente cliente = crearCliente(resultado);

                Factura factura = crearFactura(
                        resultado,
                        cliente
                );

                facturas.add(factura);
            }
        }

        return facturas;
    }

    /**
     * Busca una factura por su ID.
     *
     * También carga todos sus detalles.
     */
    public Factura buscarPorId(int idFactura)
            throws SQLException {

        String sql = """
            SELECT
                f.id_factura,
                f.fecha,
                f.subtotal,
                f.impuesto,
                f.total,
                f.estado,
                f.observaciones,

                c.id_cliente,
                c.nombre,
                c.telefono,
                c.email,
                c.direccion

            FROM facturas f

            INNER JOIN clientes c
                ON f.id_cliente = c.id_cliente

            WHERE f.id_factura = ?
            """;

        try (Connection conexion
                = ConexionBD.obtenerConexion(); PreparedStatement sentencia
                = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idFactura);

            try (ResultSet resultado
                    = sentencia.executeQuery()) {

                if (resultado.next()) {

                    Cliente cliente
                            = crearCliente(resultado);

                    Factura factura
                            = crearFactura(
                                    resultado,
                                    cliente
                            );

                    cargarDetalles(
                            conexion,
                            factura
                    );

                    return factura;
                }
            }
        }

        return null;
    }

    /**
     * Carga los detalles de una factura utilizando la misma conexión JDBC.
     */
    private void cargarDetalles(
            Connection conexion,
            Factura factura)
            throws SQLException {

        String sql = """
            SELECT
                id_detalle,
                id_factura,
                id_servicio,
                precio_unitario,
                cantidad,
                subtotal

            FROM detalle_factura

            WHERE id_factura = ?

            ORDER BY id_detalle
            """;

        ServicioDAO servicioDAO
                = new ServicioDAO();

        try (PreparedStatement sentencia
                = conexion.prepareStatement(sql)) {

            sentencia.setInt(
                    1,
                    factura.getIdFactura()
            );

            try (ResultSet resultado
                    = sentencia.executeQuery()) {

                while (resultado.next()) {

                    int idServicio
                            = resultado.getInt(
                                    "id_servicio"
                            );

                    Servicio servicio
                            = servicioDAO.buscarPorId(
                                    idServicio
                            );

                    DetalleFactura detalle
                            = new DetalleFactura(
                                    resultado.getInt(
                                            "id_detalle"
                                    ),
                                    resultado.getInt(
                                            "id_factura"
                                    ),
                                    servicio,
                                    resultado.getDouble(
                                            "precio_unitario"
                                    ),
                                    resultado.getInt(
                                            "cantidad"
                                    ),
                                    resultado.getDouble(
                                            "subtotal"
                                    )
                            );

                    factura.agregarDetalle(detalle);
                }
            }
        }
    }

    /**
     * Cambia el estado de una factura.
     *
     * Ejemplos: PENDIENTE, PAGADA o ANULADA.
     */
    public boolean actualizarEstado(
            int idFactura,
            EstadoFactura estado)
            throws SQLException {

        String sql = """
            UPDATE facturas
            SET estado = ?
            WHERE id_factura = ?
            """;

        try (Connection conexion
                = ConexionBD.obtenerConexion(); PreparedStatement sentencia
                = conexion.prepareStatement(sql)) {

            sentencia.setString(
                    1,
                    estado.name()
            );

            sentencia.setInt(
                    2,
                    idFactura
            );

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Construye un objeto Cliente desde un ResultSet.
     */
    private Cliente crearCliente(
            ResultSet resultado)
            throws SQLException {

        return new Cliente(
                resultado.getInt("id_cliente"),
                resultado.getString("nombre"),
                resultado.getString("telefono"),
                resultado.getString("email"),
                resultado.getString("direccion")
        );
    }

    /**
     * Construye un objeto Factura desde un ResultSet.
     */
    private Factura crearFactura(
            ResultSet resultado,
            Cliente cliente)
            throws SQLException {

        Timestamp fechaSQL
                = resultado.getTimestamp("fecha");

        return new Factura(
                resultado.getInt("id_factura"),
                cliente,
                fechaSQL.toLocalDateTime(),
                resultado.getDouble("subtotal"),
                resultado.getDouble("impuesto"),
                resultado.getDouble("total"),
                EstadoFactura.valueOf(
                        resultado.getString("estado")
                ),
                resultado.getString("observaciones")
        );
    }
}
