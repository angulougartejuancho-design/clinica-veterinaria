/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;

/**
 *
 * @author PC
 */
public class DetalleFactura {

    private int idDetalle;
    private int idFactura;
    private Servicio servicio;
    private double precioUnitario;
    private int cantidad;
    private double subtotal;

    /**
     * Constructor para crear un detalle nuevo.
     *
     * El precio unitario se obtiene usando calcularPrecio() del servicio.
     */
    public DetalleFactura(
            Servicio servicio,
            int cantidad) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.idDetalle = 0;
        this.idFactura = 0;
        this.servicio = servicio;
        this.precioUnitario = servicio.calcularPrecio();
        this.cantidad = cantidad;

        calcularSubtotal();
    }

    /**
     * Constructor completo.
     *
     * Se utilizará al recuperar detalles desde la base de datos.
     */
    public DetalleFactura(
            int idDetalle,
            int idFactura,
            Servicio servicio,
            double precioUnitario,
            int cantidad,
            double subtotal) {

        this.idDetalle = idDetalle;
        this.idFactura = idFactura;
        this.servicio = servicio;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    /**
     * Calcula el subtotal del detalle.
     *
     * subtotal = precioUnitario * cantidad
     */
    public void calcularSubtotal() {

        if (cantidad <= 0 || precioUnitario < 0) {
            subtotal = 0;
            return;
        }

        subtotal = precioUnitario * cantidad;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo."
            );
        }

        this.servicio = servicio;
        this.precioUnitario = servicio.calcularPrecio();

        calcularSubtotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {

        if (precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo."
            );
        }

        this.precioUnitario = precioUnitario;

        calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.cantidad = cantidad;

        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {

        if (subtotal < 0) {
            throw new IllegalArgumentException(
                    "El subtotal no puede ser negativo."
            );
        }

        this.subtotal = subtotal;
    }

    @Override
    public String toString() {

        String nombreServicio
                = servicio == null
                        ? "Servicio no disponible"
                        : servicio.getNombre();

        return nombreServicio
                + " - Cantidad: "
                + cantidad
                + " - Subtotal: ₡"
                + String.format("%.2f", subtotal);
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof DetalleFactura otroDetalle)) {
            return false;
        }

        if (idDetalle > 0 && otroDetalle.idDetalle > 0) {
            return idDetalle == otroDetalle.idDetalle;
        }

        return Objects.equals(
                servicio,
                otroDetalle.servicio
        );
    }

    @Override
    public int hashCode() {

        if (idDetalle > 0) {
            return Objects.hash(idDetalle);
        }

        return Objects.hash(servicio);
    }
}
