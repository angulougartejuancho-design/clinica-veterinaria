/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.FacturaDAO;
import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.EstadoFactura;
import modelo.Factura;
import modelo.Servicio;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author PC
 */
public class FacturaServicio {

    private final FacturaDAO facturaDAO;

    public FacturaServicio() {
        this.facturaDAO = new FacturaDAO();
    }

    /**
     * Crea una factura nueva para un cliente.
     */
    public Factura crearFactura(
            Cliente cliente,
            String observaciones) {

        validarCliente(cliente);

        if (observaciones == null) {
            observaciones = "";
        }

        return new Factura(
                cliente,
                observaciones.trim()
        );
    }

    /**
     * Agrega un servicio a una factura.
     */
    public void agregarServicio(
            Factura factura,
            Servicio servicio,
            int cantidad) {

        validarFacturaExistente(factura);
        validarServicio(servicio);
        validarCantidad(cantidad);

        DetalleFactura detalle
                = new DetalleFactura(
                        servicio,
                        cantidad
                );

        factura.agregarDetalle(detalle);
    }

    /**
     * Elimina un detalle según su posición.
     */
    public boolean eliminarDetalle(
            Factura factura,
            int indice) {

        validarFacturaExistente(factura);

        return factura.eliminarDetalle(indice);
    }

    /**
     * Limpia todos los detalles de la factura.
     */
    public void limpiarDetalles(Factura factura) {

        validarFacturaExistente(factura);

        factura.limpiarDetalles();
    }

    /**
     * Guarda la factura y todos sus detalles.
     */
    public void guardarFactura(Factura factura)
            throws SQLException {

        validarFacturaParaGuardar(factura);

        /*
         * Se recalculan los totales antes de guardar,
         * para evitar almacenar montos desactualizados.
         */
        factura.calcularTotales();

        facturaDAO.insertar(factura);
    }

    /**
     * Obtiene todas las facturas registradas.
     */
    public List<Factura> obtenerFacturas()
            throws SQLException {

        return facturaDAO.listarTodas();
    }

    /**
     * Busca una factura por su ID.
     */
    public Factura buscarFacturaPorId(int idFactura)
            throws SQLException {

        if (idFactura <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la factura debe ser mayor que cero."
            );
        }

        return facturaDAO.buscarPorId(idFactura);
    }

    /**
     * Marca una factura como pagada.
     */
    public boolean marcarComoPagada(int idFactura)
            throws SQLException {

        validarIdFactura(idFactura);

        return facturaDAO.actualizarEstado(
                idFactura,
                EstadoFactura.PAGADA
        );
    }

    /**
     * Anula una factura.
     */
    public boolean anularFactura(int idFactura)
            throws SQLException {

        validarIdFactura(idFactura);

        Factura factura
                = facturaDAO.buscarPorId(idFactura);

        if (factura == null) {
            throw new IllegalArgumentException(
                    "La factura indicada no existe."
            );
        }

        if (factura.getEstado()
                == EstadoFactura.ANULADA) {

            throw new IllegalStateException(
                    "La factura ya se encuentra anulada."
            );
        }

        return facturaDAO.actualizarEstado(
                idFactura,
                EstadoFactura.ANULADA
        );
    }

    /**
     * Devuelve el total actual de una factura.
     */
    public double obtenerTotal(Factura factura) {

        validarFacturaExistente(factura);

        factura.calcularTotales();

        return factura.getTotal();
    }

    /**
     * Devuelve la cantidad total de servicios agregados a una factura.
     */
    public int obtenerCantidadServicios(
            Factura factura) {

        validarFacturaExistente(factura);

        return factura.obtenerCantidadTotalServicios();
    }

    private void validarFacturaParaGuardar(
            Factura factura) {

        validarFacturaExistente(factura);
        validarCliente(factura.getCliente());

        if (!factura.tieneDetalles()) {
            throw new IllegalArgumentException(
                    "Debe agregar al menos un servicio."
            );
        }

        for (DetalleFactura detalle
                : factura.getDetalles()) {

            if (detalle == null) {
                throw new IllegalArgumentException(
                        "La factura contiene un detalle inválido."
                );
            }

            validarServicio(
                    detalle.getServicio()
            );

            validarCantidad(
                    detalle.getCantidad()
            );

            if (detalle.getPrecioUnitario() < 0) {
                throw new IllegalArgumentException(
                        "El precio unitario no puede ser negativo."
                );
            }
        }

        if (factura.getEstado() == null) {
            factura.setEstado(
                    EstadoFactura.PENDIENTE
            );
        }
    }

    private void validarFacturaExistente(
            Factura factura) {

        if (factura == null) {
            throw new IllegalArgumentException(
                    "La factura no puede ser nula."
            );
        }
    }

    private void validarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente."
            );
        }

        if (cliente.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El cliente seleccionado no posee un ID válido."
            );
        }
    }

    private void validarServicio(
            Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un servicio."
            );
        }

        if (servicio.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El servicio seleccionado no posee un ID válido."
            );
        }

        if (servicio.getCostoBase() < 0) {
            throw new IllegalArgumentException(
                    "El costo del servicio no puede ser negativo."
            );
        }
    }

    private void validarCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }
    }

    private void validarIdFactura(int idFactura) {

        if (idFactura <= 0) {
            throw new IllegalArgumentException(
                    "El ID de la factura debe ser mayor que cero."
            );
        }
    }
}
