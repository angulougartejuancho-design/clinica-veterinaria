/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Anyel
 */
public class ConsultaGeneral extends Servicio {

    private String motivoTipico;

    public ConsultaGeneral(
            int id,
            String nombre,
            String descripcion,
            double costoBase,
            String motivoTipico) {

        super(id, nombre, descripcion, costoBase);

        this.motivoTipico = motivoTipico;
    }

    public ConsultaGeneral(
            String nombre,
            String descripcion,
            double costoBase,
            String motivoTipico) {

        super(nombre, descripcion, costoBase);

        this.motivoTipico = motivoTipico;
    }

    public String getMotivoTipico() {
        return motivoTipico;
    }

    public void setMotivoTipico(String motivoTipico) {
        this.motivoTipico = motivoTipico;
    }

    @Override
    public double calcularPrecio() {

        double costoBase = getCostoBase();

        return costoBase + (costoBase * IVA);
    }

    @Override
    public TipoServicio getTipo() {
        return TipoServicio.CONSULTA_GENERAL;
    }
}

