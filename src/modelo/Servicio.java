/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;

/**
 *
 * @author Anyel
 */
public abstract class Servicio {

    protected static final double IVA = 0.13;

    private static int totalServiciosCreados = 0;

    private int id;
    private String nombre;
    private String descripcion;
    private double costoBase;

    public Servicio(
            int id,
            String nombre,
            String descripcion,
            double costoBase) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costoBase = costoBase;

        Servicio.totalServiciosCreados++;
    }

    public Servicio(
            String nombre,
            String descripcion,
            double costoBase) {

        this(
                0,
                nombre,
                descripcion,
                costoBase
        );
    }

    /**
     * Calcula el precio del servicio antes de aplicar IVA.
     *
     * Cada subclase debe incluir aquí sus recargos propios.
     *
     * @return precio sin IVA
     */
    public abstract double calcularPrecioSinIVA();

    /**
     * Calcula únicamente el impuesto correspondiente al servicio.
     *
     * @return monto del IVA
     */
    public double calcularImpuesto() {

        return calcularPrecioSinIVA() * IVA;
    }

    /**
     * Calcula el precio final con IVA incluido.
     *
     * @return precio total del servicio
     */
    public double calcularPrecio() {

        return calcularPrecioSinIVA()
                + calcularImpuesto();
    }

    public abstract TipoServicio getTipo();

    public static int getTotalServiciosCreados() {
        return totalServiciosCreados;
    }

    public static double getIVA() {
        return IVA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion = descripcion;
    }

    public double getCostoBase() {

        return costoBase;
    }

    public void setCostoBase(
            double costoBase) {

        if (costoBase < 0) {
            throw new IllegalArgumentException(
                    "El costo base no puede ser negativo."
            );
        }

        this.costoBase = costoBase;
    }

    @Override
    public String toString() {

        return nombre
                + " ("
                + getTipo()
                + ") - ₡"
                + String.format(
                        "%,.2f",
                        calcularPrecio()
                );
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Servicio otroServicio)) {

            return false;
        }

        if (id > 0 && otroServicio.id > 0) {

            return id == otroServicio.id;
        }

        return Objects.equals(
                nombre,
                otroServicio.nombre
        ) && getTipo()
                == otroServicio.getTipo();
    }

    @Override
    public int hashCode() {

        if (id > 0) {
            return Objects.hash(id);
        }

        return Objects.hash(
                nombre,
                getTipo()
        );
    }
}
