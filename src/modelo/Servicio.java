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

    
     //Porcentaje de impuesto aplicado sobre el precio de cualquier servicio es una constante compartida por todas las subclases
   
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

        this(0, nombre, descripcion, costoBase);
    }

    public static int getTotalServiciosCreados() {
        return totalServiciosCreados;
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

   
    public abstract double calcularPrecio();


    public abstract TipoServicio getTipo();

    @Override
    public String toString() {
        return nombre
                + " ("
                + getTipo()
                + ") - ₡"
                + String.format(
                        "%.2f",
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

        return Objects.equals(nombre, otroServicio.nombre)
                && getTipo() == otroServicio.getTipo();
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }

        return Objects.hash(nombre, getTipo());
    }
}
