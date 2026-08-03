/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Cursor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author PC
 */
public class TarjetaConsejoImagen extends PanelRedondeado{
    
    private final Color colorOriginal;
    private final Color colorHover;

    public TarjetaConsejoImagen(
            String urlImagen,
            String urlIcono,
            String titulo,
            String descripcion,
            Color colorTitulo,
            Color colorFondo) {

        super(28, colorFondo);

        this.colorOriginal = colorFondo;

        this.colorHover =
                colorFondo.brighter();

        setMostrarSombra(true);

        configurarPanel();

        construirContenido(
                urlImagen,
                urlIcono,
                titulo,
                descripcion,
                colorTitulo
        );

        configurarHover();
    }

    /**
     * Configuración inicial.
     */
    private void configurarPanel() {

        setLayout(
                new BorderLayout()
        );

        setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        setPreferredSize(
                new Dimension(
                        310,
                        260
                )
        );

        setMinimumSize(
                new Dimension(
                        250,
                        250
                )
        );
    }

    /**
     * Construye el contenido visual.
     */
    private void construirContenido(
            String urlImagen,
            String urlIcono,
            String titulo,
            String descripcion,
            Color colorTitulo) {

        /*
         * Imagen superior.
         */
        PanelImagenURL imagen =
                new PanelImagenURL(urlImagen);

        imagen.setPreferredSize(
                new Dimension(
                        300,
                        135
                )
        );

        imagen.setTransparencia(0);

        add(
                imagen,
                BorderLayout.NORTH
        );

        /*
         * Parte inferior.
         */
        JPanel contenido =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        contenido.setOpaque(false);

        contenido.setBorder(
                new EmptyBorder(
                        15,
                        16,
                        16,
                        16
                )
        );

        /*
         * Círculo del icono.
         */
        PanelRedondeado circulo =
                new PanelRedondeado(
                        60,
                        colorTitulo
                );

        circulo.setLayout(
                new BorderLayout()
        );

        circulo.setPreferredSize(
                new Dimension(
                        60,
                        60
                )
        );

        IconoURL icono =
                new IconoURL(
                        urlIcono,
                        32,
                        32,
                        "★"
                );

        circulo.add(
                icono,
                BorderLayout.CENTER
        );

        /*
         * Zona de textos.
         */
        JPanel textos =
                new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "<html><b>"
                        + titulo
                        + "</b></html>"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lblTitulo.setForeground(
                colorTitulo
        );

        JLabel lblDescripcion =
                new JLabel(
                        "<html><div style='width:205px;'>"
                        + descripcion
                        + "</div></html>"
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblDescripcion.setForeground(
                new Color(
                        72,
                        64,
                        55
                )
        );

        textos.add(lblTitulo);

        textos.add(
                Box.createVerticalStrut(8)
        );

        textos.add(lblDescripcion);

        contenido.add(
                circulo,
                BorderLayout.WEST
        );

        contenido.add(
                textos,
                BorderLayout.CENTER
        );

        add(
                contenido,
                BorderLayout.CENTER
        );
    }

    /**
     * Efecto Hover.
     */
    private void configurarHover() {

        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        setColorFondo(
                                colorHover
                        );

                        setTransparenciaSombra(
                                45
                        );

                        repaint();
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        setColorFondo(
                                colorOriginal
                        );

                        setTransparenciaSombra(
                                24
                        );

                        repaint();
                    }
                }
        );
    }
}
