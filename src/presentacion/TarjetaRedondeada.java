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
public class TarjetaRedondeada  extends JPanel{
    private final int radio;
    private Color colorFondo;

    public TarjetaRedondeada(
            int radio,
            Color colorFondo) {

        this.radio = radio;
        this.colorFondo = colorFondo;

        setOpaque(false);
    }

    public void setColorFondo(
            Color colorFondo) {

        this.colorFondo = colorFondo;
        repaint();
    }

    @Override
    protected void paintComponent(
            Graphics graphics) {

        Graphics2D g2 =
                (Graphics2D) graphics.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(colorFondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                radio,
                radio
        );

        g2.dispose();

        super.paintComponent(graphics);
    }
}
