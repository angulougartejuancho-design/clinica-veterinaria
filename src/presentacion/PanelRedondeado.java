/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 *
 * @author PC
 */
public class PanelRedondeado extends JPanel{
      
     private int radio;

    /**
     * Color principal del panel.
     */
    private Color colorFondo;

    /**
     * Indica si debe dibujarse una sombra.
     */
    private boolean mostrarSombra;

    /**
     * Intensidad de la sombra.
     */
    private int transparenciaSombra;

    /**
     * Desplazamiento horizontal de la sombra.
     */
    private int desplazamientoSombraX;

    /**
     * Desplazamiento vertical de la sombra.
     */
    private int desplazamientoSombraY;

    public PanelRedondeado(
            int radio,
            Color colorFondo) {

        this.radio = radio;
        this.colorFondo = colorFondo;

        this.mostrarSombra = false;
        this.transparenciaSombra = 24;

        this.desplazamientoSombraX = 4;
        this.desplazamientoSombraY = 5;

        /*
         * Debe permanecer en false para que se vean
         * correctamente las esquinas redondeadas.
         */
        setOpaque(false);
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {

        this.radio =
                Math.max(0, radio);

        repaint();
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(
            Color colorFondo) {

        if (colorFondo == null) {

            throw new IllegalArgumentException(
                    "El color de fondo no puede ser nulo."
            );
        }

        this.colorFondo = colorFondo;

        repaint();
    }

    public void setColorSombra(
            Color color,
            int transparencia) {

        this.transparenciaSombra = transparencia;

        repaint();
    }

    public boolean isMostrarSombra() {
        return mostrarSombra;
    }

    public void setMostrarSombra(
            boolean mostrarSombra) {

        this.mostrarSombra = mostrarSombra;

        repaint();
    }

    public int getTransparenciaSombra() {
        return transparenciaSombra;
    }

    public void setTransparenciaSombra(
            int transparenciaSombra) {

        this.transparenciaSombra =
                Math.max(
                        0,
                        Math.min(
                                255,
                                transparenciaSombra
                        )
                );

        repaint();
    }

    public void setDesplazamientoSombra(
            int desplazamientoX,
            int desplazamientoY) {

        this.desplazamientoSombraX =
                desplazamientoX;

        this.desplazamientoSombraY =
                desplazamientoY;

        repaint();
    }

    @Override
    protected void paintComponent(
            Graphics graphics) {

        Graphics2D g2 =
                (Graphics2D)
                        graphics.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int anchoDisponible =
                Math.max(
                        0,
                        getWidth()
                        - Math.max(
                                6,
                                desplazamientoSombraX + 5
                        )
                );

        int altoDisponible =
                Math.max(
                        0,
                        getHeight()
                        - Math.max(
                                8,
                                desplazamientoSombraY + 6
                        )
                );

        /*
         * Sombra suave debajo del panel.
         */
        if (mostrarSombra) {

            g2.setColor(
                    new Color(
                            35,
                            29,
                            23,
                            transparenciaSombra
                    )
            );

            g2.fillRoundRect(
                    desplazamientoSombraX,
                    desplazamientoSombraY,
                    anchoDisponible,
                    altoDisponible,
                    radio,
                    radio
            );
        }

        /*
         * Fondo principal.
         */
        g2.setColor(colorFondo);

        g2.fillRoundRect(
                0,
                0,
                anchoDisponible,
                altoDisponible,
                radio,
                radio
        );

        g2.dispose();

        /*
         * Permite que Swing pinte los componentes internos
         * después de dibujar el fondo personalizado.
         */
        super.paintComponent(graphics);
    }
    
}
