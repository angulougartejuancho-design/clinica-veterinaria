/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/**
 *
 * @author PC
 */
public class PanelScrollable  extends JPanel implements Scrollable{
      @Override
    public Dimension getPreferredScrollableViewportSize() {

        return getPreferredSize();
    }

    /**
     * Cantidad de desplazamiento al utilizar
     * la rueda del mouse o las flechas.
     */
    @Override
    public int getScrollableUnitIncrement(
            Rectangle rectanguloVisible,
            int orientacion,
            int direccion) {

        return 18;
    }

    /**
     * Cantidad de desplazamiento al avanzar
     * una página completa.
     */
    @Override
    public int getScrollableBlockIncrement(
            Rectangle rectanguloVisible,
            int orientacion,
            int direccion) {

        if (orientacion
                == SwingConstants.VERTICAL) {

            return Math.max(
                    60,
                    rectanguloVisible.height - 60
            );
        }

        return Math.max(
                60,
                rectanguloVisible.width - 60
        );
    }

    /**
     * Indica que el panel debe adoptar siempre
     * todo el ancho del viewport.
     *
     * Esto evita la barra horizontal.
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {

        return true;
    }

    /**
     * Permite que el panel crezca verticalmente
     * según el contenido.
     *
     * Si el contenido supera el alto disponible,
     * aparecerá únicamente la barra vertical.
     */
    @Override
    public boolean getScrollableTracksViewportHeight() {

        return false;
    }
}
