/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Anyel
 */
public class Vacunacion extends Servicio {

    private static final double RECARGO_CADENA_FRIO = 3500.0;

    private String tipoVacuna;

    public Vacunacion(
            int id,
            String nombre,
            String descripcion,
            double costoBase,
            String tipoVacuna) {

        super(id, nombre, descripcion, costoBase);

        this.tipoVacuna = tipoVacuna;
    }

    public Vacunacion(
            String nombre,
            String descripcion,
            double costoBase,
            String tipoVacuna) {

        super(nombre, descripcion, costoBase);

        this.tipoVacuna = tipoVacuna;
    }

    public String getTipoVacuna() {
        return tipoVacuna;
    }

    public void setTipoVacuna(String tipoVacuna) {
        this.tipoVacuna = tipoVacuna;
    }

    public static double getRecargoCadenaFrio() {
        return RECARGO_CADENA_FRIO;
    }

    @Override
    public double calcularPrecio() {

        double subtotal =
                getCostoBase() + RECARGO_CADENA_FRIO;

        return subtotal + (subtotal * IVA);
    }

    @Override
    public TipoServicio getTipo() {
        return TipoServicio.VACUNACION;
    }
}
