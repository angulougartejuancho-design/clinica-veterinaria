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

    /**
     * Precio unitario sin IVA.
     */
    private double precioUnitario;

    private int cantidad;

    /**
     * Subtotal de la línea sin IVA.
     */
    private double subtotal;

    /**
     * Constructor para crear un detalle nuevo.
     */
    public DetalleFactura(
            Servicio servicio,
            int cantidad) {

        validarServicio(servicio);
        validarCantidad(cantidad);

        this.idDetalle = 0;
        this.idFactura = 0;
        this.servicio = servicio;

        /*
         * Se guarda el precio antes del IVA,
         * pero incluyendo los recargos propios.
         */
        this.precioUnitario
                = servicio.calcularPrecioSinIVA();

        this.cantidad = cantidad;

        calcularSubtotal();
    }

    /**
     * Constructor completo utilizado al recuperar un detalle desde la base de
     * datos.
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
        this.precioUnitario
                = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    /**
     * Calcula el subtotal sin IVA.
     *
     * subtotal = precioUnitario × cantidad
     */
    public void calcularSubtotal() {

        if (cantidad <= 0
                || precioUnitario < 0) {

            subtotal = 0;
            return;
        }

        subtotal
                = precioUnitario * cantidad;
    }

    /**
     * Calcula el impuesto correspondiente a este detalle.
     */
    public double calcularImpuesto() {

        return subtotal
                * Servicio.getIVA();
    }

    /**
     * Calcula el total del detalle con IVA.
     */
    public double calcularTotalConImpuesto() {

        return subtotal
                + calcularImpuesto();
    }

    public int getIdDetalle() {

        return idDetalle;
    }

    public void setIdDetalle(
            int idDetalle) {

        this.idDetalle = idDetalle;
    }

    public int getIdFactura() {

        return idFactura;
    }

    public void setIdFactura(
            int idFactura) {

        this.idFactura = idFactura;
    }

    public Servicio getServicio() {

        return servicio;
    }

    public void setServicio(
            Servicio servicio) {

        validarServicio(servicio);

        this.servicio = servicio;

        this.precioUnitario
                = servicio.calcularPrecioSinIVA();

        calcularSubtotal();
    }

    public double getPrecioUnitario() {

        return precioUnitario;
    }

    public void setPrecioUnitario(
            double precioUnitario) {

        if (precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo."
            );
        }

        this.precioUnitario
                = precioUnitario;

        calcularSubtotal();
    }

    public int getCantidad() {

        return cantidad;
    }

    public void setCantidad(
            int cantidad) {

        validarCantidad(cantidad);

        this.cantidad = cantidad;

        calcularSubtotal();
    }

    public double getSubtotal() {

        return subtotal;
    }

    public void setSubtotal(
            double subtotal) {

        if (subtotal < 0) {
            throw new IllegalArgumentException(
                    "El subtotal no puede ser negativo."
            );
        }

        this.subtotal = subtotal;
    }

    private void validarServicio(
            Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo."
            );
        }
    }

    private void validarCantidad(
            int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }
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
                + " - Subtotal sin IVA: ₡"
                + String.format(
                        "%,.2f",
                        subtotal
                );
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof DetalleFactura otroDetalle)) {

            return false;
        }

        if (idDetalle > 0
                && otroDetalle.idDetalle > 0) {

            return idDetalle
                    == otroDetalle.idDetalle;
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
