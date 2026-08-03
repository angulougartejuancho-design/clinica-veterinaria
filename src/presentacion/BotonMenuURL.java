/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author PC
 */
public class BotonMenuURL extends PanelRedondeado{
    
     private final Color colorNormal;
    private final Color colorHover;
    private final Color colorActivo;

    private final JLabel lblTexto;

    private boolean activo;

    /**
     * Constructor.
     */
    public BotonMenuURL(
            String urlIcono,
            String textoBoton,
            Color colorNormal,
            Color colorHover,
            Color colorActivo) {

        super(18, colorNormal);

        this.colorNormal = colorNormal;
        this.colorHover = colorHover;
        this.colorActivo = colorActivo;
        this.activo = false;

        setLayout(new BorderLayout(12, 0));

        setOpaque(false);

        setBorder(
                new EmptyBorder(
                        8,
                        15,
                        8,
                        15
                )
        );

        setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        setPreferredSize(
                new Dimension(
                        220,
                        50
                )
        );

        setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        /*
         * Icono.
         */
        IconoURL icono =
                new IconoURL(
                        urlIcono,
                        24,
                        24,
                        "+"
                );

        /*
         * Texto.
         */
        lblTexto =
                new JLabel(
                        textoBoton
                );

        lblTexto.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        lblTexto.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblTexto.setForeground(
                Color.WHITE
        );

        add(
                icono,
                BorderLayout.WEST
        );

        add(
                lblTexto,
                BorderLayout.CENTER
        );

        configurarEventosMouse();
    }

    /**
     * Eventos del mouse.
     */
    private void configurarEventosMouse() {

        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        if (!activo) {

                            setColorFondo(
                                    colorHover
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        actualizarColor();
                    }

                    @Override
                    public void mousePressed(
                            MouseEvent e) {

                        if (!activo) {

                            setColorFondo(
                                    colorActivo
                            );
                        }
                    }

                    @Override
                    public void mouseReleased(
                            MouseEvent e) {

                        actualizarColor();
                    }
                }
        );
    }

    /**
     * Marca el botón como activo.
     */
    public void setActivo(
            boolean activo) {

        this.activo = activo;

        actualizarColor();
    }

    public boolean isActivo() {

        return activo;
    }

    /**
     * Actualiza el color del botón.
     */
    private void actualizarColor() {

        if (activo) {

            setColorFondo(
                    colorActivo
            );

        } else {

            setColorFondo(
                    colorNormal
            );
        }

        repaint();
    }

    /**
     * Cambia el texto mostrado.
     */
    public void setTexto(
            String texto) {

        lblTexto.setText(texto);
    }

    /**
     * Obtiene el texto.
     */
    public String getTexto() {

        return lblTexto.getText();
    }
    
}
