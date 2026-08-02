/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Anyel
 */
public class Procedimiento extends Servicio {

    private static final double TARIFA_POR_MINUTO = 450.0;

    private int duracionMinutos;

    public Procedimiento(
            int id,
            String nombre,
            String descripcion,
            double costoBase,
            int duracionMinutos) {

        super(
                id,
                nombre,
                descripcion,
                costoBase
        );

        this.duracionMinutos
                = duracionMinutos;
    }

    public Procedimiento(
            String nombre,
            String descripcion,
            double costoBase,
            int duracionMinutos) {

        super(
                nombre,
                descripcion,
                costoBase
        );

        this.duracionMinutos
                = duracionMinutos;
    }

    public int getDuracionMinutos() {

        return duracionMinutos;
    }

    public void setDuracionMinutos(
            int duracionMinutos) {

        if (duracionMinutos < 0) {
            throw new IllegalArgumentException(
                    "La duración no puede ser negativa."
            );
        }

        this.duracionMinutos
                = duracionMinutos;
    }

    public static double
            getTarifaPorMinuto() {

        return TARIFA_POR_MINUTO;
    }

    /**
     * Calcula el valor del procedimiento antes del IVA.
     *
     * Precio sin IVA = costo base + duración por tarifa por minuto.
     */
    @Override
    public double calcularPrecioSinIVA() {

        return getCostoBase()
                + (duracionMinutos
                * TARIFA_POR_MINUTO);
    }

    @Override
    public TipoServicio getTipo() {

        return TipoServicio.PROCEDIMIENTO;
    }
}
