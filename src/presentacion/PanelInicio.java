/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;
/**
 *
 * @author PC
 */
public class PanelInicio extends JPanel {
    
     private static final Color FONDO =
            new Color(255, 251, 244);

    private static final Color VERDE_OSCURO =
            new Color(0, 82, 67);

    private static final Color MARRON =
            new Color(101, 63, 27);

    private static final Color TEXTO =
            new Color(52, 42, 32);

    private static final String IMAGEN_BANNER =
            "https://images.unsplash.com/"
            + "photo-1548199973-03cce0bbc87b"
            + "?auto=format&fit=crop&w=1800&q=90";

    private static final String IMAGEN_CASTRACION =
            "https://images.unsplash.com/"
            + "photo-1583511655857-d19b40a7a54e"
            + "?auto=format&fit=crop&w=900&q=90";

    private static final String IMAGEN_SINTOMAS =
            "https://images.unsplash.com/"
            + "photo-1537151608828-ea2b11777ee8"
            + "?auto=format&fit=crop&w=900&q=90";

    private static final String IMAGEN_VETERINARIO =
            "https://images.unsplash.com/"
            + "photo-1628009368231-7bb7cfcb0def"
            + "?auto=format&fit=crop&w=900&q=90";

    private static final String IMAGEN_RAZAS =
            "https://images.unsplash.com/"
            + "photo-1548199973-03cce0bbc87b"
            + "?auto=format&fit=crop&w=900&q=90";

    private static final String IMAGEN_ALIMENTACION =
            "https://images.unsplash.com/"
            + "photo-1558788353-f76d92427f16"
            + "?auto=format&fit=crop&w=900&q=90";

    private static final String IMAGEN_VACUNACION =
            "https://images.unsplash.com/"
            + "photo-1601758063890-1167f394febb"
            + "?auto=format&fit=crop&w=900&q=90";

    /*
     * Iconos PNG remotos.
     */
    private static final String ICONO_TIJERAS =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "2702.png";

    private static final String ICONO_ALERTA =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "26A0.png";

    private static final String ICONO_MEDICO =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "1FA7A.png";

    private static final String ICONO_HUELLA =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "1F43E.png";

    private static final String ICONO_COMIDA =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "1F35A.png";

    private static final String ICONO_VACUNA =
            "https://cdn.jsdelivr.net/npm/"
            + "openmoji@15.0.0/color/618x618/"
            + "1F489.png";

    public PanelInicio(
            MainFrame ventanaPrincipal) {

        configurarPanel();
        construirInterfaz();
    }

    private void configurarPanel() {

        setLayout(new BorderLayout());
        setBackground(FONDO);
    }

    private void construirInterfaz() {

        JPanel contenido =
                new JPanel();

        contenido.setBackground(FONDO);

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setBorder(
                new EmptyBorder(
                        18,
                        28,
                        28,
                        28
                )
        );

        JPanel banner =
                crearBanner();

        banner.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JPanel tituloSeccion =
                crearTituloSeccion();

        tituloSeccion.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JPanel tarjetas =
                crearTarjetas();

        tarjetas.setAlignmentX(
                LEFT_ALIGNMENT
        );

        contenido.add(banner);
        contenido.add(
                Box.createVerticalStrut(19)
        );

        contenido.add(tituloSeccion);
        contenido.add(
                Box.createVerticalStrut(15)
        );

        contenido.add(tarjetas);
        contenido.add(
                Box.createVerticalStrut(25)
        );

        JScrollPane scroll =
                new JScrollPane(contenido);

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(FONDO);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(18);

        add(
                scroll,
                BorderLayout.CENTER
        );
    }

    private JPanel crearBanner() {

        PanelRedondeado contenedor =
                new PanelRedondeado(
                        30,
                        new Color(255, 242, 217)
                );

        contenedor.setLayout(
                new BorderLayout()
        );

        contenedor.setPreferredSize(
                new Dimension(1050, 270)
        );

        contenedor.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        270
                )
        );

        JPanel textos =
                new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        textos.setBorder(
                new EmptyBorder(
                        35,
                        38,
                        25,
                        20
                )
        );

        JLabel titulo =
                new JLabel(
                        "<html>"
                        + "CUIDAMOS A QUIENES<br>"
                        + "<span style='color:#8B572A;'>"
                        + "FORMAN PARTE DE<br>"
                        + "TU FAMILIA"
                        + "</span>"
                        + "</html>"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        31
                )
        );

        titulo.setForeground(
                VERDE_OSCURO
        );

        JLabel descripcion =
                new JLabel(
                        "<html>"
                        + "Gestión integral de clientes, "
                        + "mascotas,<br>"
                        + "citas, consultas y servicios "
                        + "veterinarios."
                        + "</html>"
                );

        descripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        descripcion.setForeground(TEXTO);

        textos.add(titulo);
        textos.add(
                Box.createVerticalStrut(14)
        );

        textos.add(descripcion);

        PanelImagenURL imagen =
                new PanelImagenURL(
                        IMAGEN_BANNER
                );

        imagen.setPreferredSize(
                new Dimension(600, 270)
        );

        imagen.setTransparencia(0);

        contenedor.add(
                textos,
                BorderLayout.WEST
        );

        contenedor.add(
                imagen,
                BorderLayout.CENTER
        );

        return contenedor;
    }

    private JPanel crearTituloSeccion() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(13, 0)
                );

        panel.setOpaque(false);

        JLabel huella =
                new JLabel(
                        "🐾",
                        SwingConstants.CENTER
                );

        huella.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        29
                )
        );

        huella.setForeground(MARRON);

        JPanel textos =
                new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo =
                new JLabel(
                        "GUÍA PARA EL CUIDADO "
                        + "DE TU MASCOTA"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        titulo.setForeground(
                new Color(70, 50, 31)
        );

        JLabel descripcion =
                new JLabel(
                        "Consejos, prevención y bienestar "
                        + "para una vida más saludable."
                );

        descripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        descripcion.setForeground(
                new Color(94, 76, 60)
        );

        textos.add(titulo);
        textos.add(
                Box.createVerticalStrut(3)
        );

        textos.add(descripcion);

        panel.add(
                huella,
                BorderLayout.WEST
        );

        panel.add(
                textos,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel crearTarjetas() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                20,
                                18
                        )
                );

        panel.setOpaque(false);

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_CASTRACION,
                        ICONO_TIJERAS,
                        "¿Por qué castrar?",
                        "La esterilización ayuda a prevenir "
                        + "camadas no planificadas y puede "
                        + "reducir algunos problemas de salud "
                        + "y comportamiento.",
                        new Color(49, 135, 84),
                        new Color(244, 249, 236)
                )
        );

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_SINTOMAS,
                        ICONO_ALERTA,
                        "Síntomas de alerta",
                        "La falta de apetito, vómitos, "
                        + "decaimiento o dificultad para "
                        + "respirar pueden ser señales de "
                        + "atención veterinaria.",
                        new Color(237, 146, 17),
                        new Color(255, 248, 230)
                )
        );

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_VETERINARIO,
                        ICONO_MEDICO,
                        "¿Cuándo acudir al veterinario?",
                        "Los controles preventivos permiten "
                        + "detectar enfermedades antes de "
                        + "que se conviertan en problemas "
                        + "graves.",
                        new Color(48, 123, 193),
                        new Color(239, 247, 253)
                )
        );

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_RAZAS,
                        ICONO_HUELLA,
                        "Razas y necesidades",
                        "La actividad física, alimentación "
                        + "y cuidado del pelaje pueden variar "
                        + "según la raza, edad y tamaño.",
                        new Color(125, 74, 193),
                        new Color(248, 241, 254)
                )
        );

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_ALIMENTACION,
                        ICONO_COMIDA,
                        "Alimentación adecuada",
                        "Una dieta equilibrada debe considerar "
                        + "el peso, la edad, la actividad y las "
                        + "condiciones médicas.",
                        new Color(242, 105, 22),
                        new Color(255, 244, 232)
                )
        );

        panel.add(
                new TarjetaConsejoImagen(
                        IMAGEN_VACUNACION,
                        ICONO_VACUNA,
                        "Vacunación y prevención",
                        "Mantener al día las vacunas y los "
                        + "tratamientos antiparasitarios "
                        + "protege a toda la familia.",
                        new Color(236, 93, 114),
                        new Color(255, 241, 244)
                )
        );

        panel.setPreferredSize(
                new Dimension(1050, 515)
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        515
                )
        );

        return panel;
    }
    
}
