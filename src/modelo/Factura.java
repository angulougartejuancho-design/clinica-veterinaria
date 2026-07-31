/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PC
 */
public class Factura {

    private int idFactura;
    private Cliente cliente;
    private LocalDateTime fecha;

    private double subtotal;
    private double impuesto;
    private double total;

    private EstadoFactura estado;
    private String observaciones;

    private final List<DetalleFactura> detalles;

    /**
     * Constructor para crear una factura nueva.
     */
    public Factura(
            Cliente cliente,
            String observaciones) {

        this.idFactura = 0;
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();

        this.subtotal = 0;
        this.impuesto = 0;
        this.total = 0;

        this.estado = EstadoFactura.PENDIENTE;
        this.observaciones = observaciones;

        this.detalles = new ArrayList<>();
    }

    /**
     * Constructor completo utilizado cuando se recupera una factura desde la
     * base de datos.
     */
    public Factura(
            int idFactura,
            Cliente cliente,
            LocalDateTime fecha,
            double subtotal,
            double impuesto,
            double total,
            EstadoFactura estado,
            String observaciones) {

        this.idFactura = idFactura;
        this.cliente = cliente;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;

        this.detalles = new ArrayList<>();
    }

    /**
     * Agrega un detalle a la factura.
     */
    public void agregarDetalle(DetalleFactura detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle no puede ser nulo."
            );
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        /*
         * Si el servicio ya se encuentra agregado,
         * se aumenta la cantidad.
         */
        for (DetalleFactura detalleActual : detalles) {

            if (detalleActual.getServicio() != null
                    && detalle.getServicio() != null
                    && detalleActual.getServicio().getId()
                    == detalle.getServicio().getId()) {

                int nuevaCantidad
                        = detalleActual.getCantidad()
                        + detalle.getCantidad();

                detalleActual.setCantidad(nuevaCantidad);

                calcularTotales();
                return;
            }
        }

        detalle.setIdFactura(idFactura);
        detalles.add(detalle);

        calcularTotales();
    }

    /**
     * Elimina un detalle según su posición en la lista.
     */
    public boolean eliminarDetalle(int indice) {

        if (indice < 0 || indice >= detalles.size()) {
            return false;
        }

        detalles.remove(indice);
        calcularTotales();

        return true;
    }

    /**
     * Elimina todos los servicios agregados.
     */
    public void limpiarDetalles() {

        detalles.clear();
        calcularTotales();
    }

    /**
     * Calcula los montos de la factura.
     *
     * El precio unitario almacenado en cada detalle será el precio final
     * calculado por el servicio.
     */
    public void calcularTotales() {

        subtotal = 0;

        for (DetalleFactura detalle : detalles) {

            detalle.calcularSubtotal();

            subtotal += detalle.getSubtotal();
        }

        /*
         * Los servicios del proyecto ya calculan el IVA
         * mediante calcularPrecio(). Por eso no debemos
         * agregar nuevamente el 13 % aquí.
         */
        impuesto = 0;
        total = subtotal;
    }

    /**
     * Retorna la cantidad total de unidades facturadas.
     */
    public int obtenerCantidadTotalServicios() {

        int cantidadTotal = 0;

        for (DetalleFactura detalle : detalles) {
            cantidadTotal += detalle.getCantidad();
        }

        return cantidadTotal;
    }

    /**
     * Indica si la factura posee servicios agregados.
     */
    public boolean tieneDetalles() {
        return !detalles.isEmpty();
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {

        this.idFactura = idFactura;

        for (DetalleFactura detalle : detalles) {
            detalle.setIdFactura(idFactura);
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El cliente no puede ser nulo."
            );
        }

        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado no puede ser nulo."
            );
        }

        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Devuelve una lista no modificable.
     */
    public List<DetalleFactura> getDetalles() {

        return Collections.unmodifiableList(
                detalles
        );
    }

    @Override
    public String toString() {

        String nombreCliente
                = cliente == null
                        ? "Cliente no disponible"
                        : cliente.getNombre();

        return "Factura #"
                + idFactura
                + " - "
                + nombreCliente
                + " - ₡"
                + String.format("%.2f", total);
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Factura otraFactura)) {
            return false;
        }

        if (idFactura > 0 && otraFactura.idFactura > 0) {
            return idFactura == otraFactura.idFactura;
        }

        return Objects.equals(cliente, otraFactura.cliente)
                && Objects.equals(fecha, otraFactura.fecha);
    }

    @Override
    public int hashCode() {

        if (idFactura > 0) {
            return Objects.hash(idFactura);
        }

        return Objects.hash(cliente, fecha);
    }
}
