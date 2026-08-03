/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
    private static final Color VERDE_MENU
                = new Color(0, 84, 69);

        private static final Color VERDE_HOVER
                = new Color(16, 110, 88);

        private static final Color VERDE_ACTIVO
                = new Color(55, 137, 101);

        private static final Color FONDO
                = new Color(255, 251, 244);

        private static final String IMAGEN_MENU
                = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?auto=format&fit=crop&w=700&q=90";

        private static final String ICONO_INICIO
                = "https://img.icons8.com/ios-filled/100/ffffff/home.png";

        private static final String ICONO_CLIENTES
                = "https://img.icons8.com/ios-filled/100/ffffff/user.png";

        private static final String ICONO_MASCOTAS
                = "https://img.icons8.com/ios-filled/100/ffffff/dog-paw-print.png";

        private static final String ICONO_VETERINARIOS
                = "https://img.icons8.com/ios-filled/100/ffffff/stethoscope.png";

        private static final String ICONO_CITAS
                = "https://img.icons8.com/ios-filled/100/ffffff/calendar--v1.png";

        private static final String ICONO_CONSULTAS
                = "https://img.icons8.com/ios-filled/100/ffffff/medical-doctor.png";

        private static final String ICONO_SERVICIOS
                = "https://img.icons8.com/ios-filled/100/ffffff/medical-bag.png";

        private static final String ICONO_FACTURACION
                = "https://img.icons8.com/ios-filled/100/ffffff/bill.png";

        private CardLayout administradorPaneles;
        private JPanel contenedorPaneles;

        private JLabel lblTituloActual;

        private final Map<String, BotonMenuURL> botonesMenu;

        public MainFrame() {

            botonesMenu = new LinkedHashMap<>();

            configurarVentana();
            construirInterfaz();

            mostrarPanel("INICIO");
        }

        private void configurarVentana() {

            setTitle("Clínica Veterinaria");

            setSize(1400, 850);

            setMinimumSize(
                    new Dimension(1100, 700)
            );

            setExtendedState(
                    JFrame.MAXIMIZED_BOTH
            );

            setLocationRelativeTo(null);

            setDefaultCloseOperation(EXIT_ON_CLOSE);

            setLayout(new BorderLayout());
        }

        private void construirInterfaz() {

            add(
                    crearMenuLateral(),
                    BorderLayout.WEST
            );

            add(
                    crearZonaPrincipal(),
                    BorderLayout.CENTER
            );
        }

        private JPanel crearMenuLateral() {

            JPanel menu = new JPanel();

            menu.setLayout(
                    new BoxLayout(
                            menu,
                            BoxLayout.Y_AXIS
                    )
            );

            menu.setBackground(VERDE_MENU);

            menu.setPreferredSize(
                    new Dimension(270, 0)
            );

            menu.setBorder(
                    new EmptyBorder(
                            20,
                            18,
                            18,
                            18
                    )
            );

            JPanel marca = crearMarca();

            marca.setAlignmentX(LEFT_ALIGNMENT);

            menu.add(marca);

            menu.add(Box.createVerticalStrut(25));

            agregarBotonMenu(
                    menu,
                    "INICIO",
                    ICONO_INICIO,
                    "Inicio"
            );

            agregarBotonMenu(
                    menu,
                    "CLIENTES",
                    ICONO_CLIENTES,
                    "Clientes"
            );

            agregarBotonMenu(
                    menu,
                    "MASCOTAS",
                    ICONO_MASCOTAS,
                    "Mascotas"
            );

            agregarBotonMenu(
                    menu,
                    "VETERINARIOS",
                    ICONO_VETERINARIOS,
                    "Veterinarios"
            );

            agregarBotonMenu(
                    menu,
                    "CITAS",
                    ICONO_CITAS,
                    "Citas"
            );

            agregarBotonMenu(
                    menu,
                    "CONSULTAS",
                    ICONO_CONSULTAS,
                    "Consultas"
            );

            agregarBotonMenu(
                    menu,
                    "SERVICIOS",
                    ICONO_SERVICIOS,
                    "Servicios"
            );

            agregarBotonMenu(
                    menu,
                    "FACTURACION",
                    ICONO_FACTURACION,
                    "Facturación"
            );

            menu.add(Box.createVerticalGlue());

            PanelImagenURL imagen
                    = new PanelImagenURL(IMAGEN_MENU);

            imagen.setPreferredSize(
                    new Dimension(
                            230,
                            220
                    )
            );

            imagen.setMaximumSize(
                    new Dimension(
                            Integer.MAX_VALUE,
                            220
                    )
            );

            imagen.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            menu.add(imagen);

            menu.add(Box.createVerticalStrut(12));

            JLabel lblVersion
                    = new JLabel(
                            "<html>"
                            + "<b>Sistema Veterinario</b><br>"
                            + "Versión 1.0"
                            + "</html>"
                    );

            lblVersion.setForeground(
                    new Color(
                            220,
                            240,
                            231
                    )
            );

            lblVersion.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            lblVersion.setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            0
                    )
            );

            menu.add(lblVersion);

            return menu;
        }

        private JPanel crearMarca() {

            JPanel panel
                    = new JPanel(
                            new BorderLayout(14, 0)
                    );

            panel.setOpaque(false);

            PanelRedondeado cuadro
                    = new PanelRedondeado(
                            22,
                            Color.WHITE
                    );

            cuadro.setLayout(
                    new BorderLayout()
            );

            cuadro.setPreferredSize(
                    new Dimension(62, 62)
            );

            JLabel cruz
                    = new JLabel(
                            "✚",
                            SwingConstants.CENTER
                    );

            cruz.setFont(
                    new Font(
                            "Segoe UI Symbol",
                            Font.BOLD,
                            28
                    )
            );

            cruz.setForeground(VERDE_MENU);

            cuadro.add(
                    cruz,
                    BorderLayout.CENTER
            );

            JLabel nombre
                    = new JLabel(
                            "<html>"
                            + "<span style='font-size:20px;'>"
                            + "<b>CLÍNICA</b>"
                            + "</span><br>"
                            + "<span style='font-size:14px;'>"
                            + "VETERINARIA"
                            + "</span>"
                            + "</html>"
                    );

            nombre.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            18
                    )
            );

            nombre.setForeground(Color.WHITE);

            panel.add(
                    cuadro,
                    BorderLayout.WEST
            );

            panel.add(
                    nombre,
                    BorderLayout.CENTER
            );

            return panel;
        }

        private void agregarBotonMenu(
                JPanel menu,
                String nombrePanel,
                String urlIcono,
                String texto) {

            BotonMenuURL boton
                    = new BotonMenuURL(
                            urlIcono,
                            texto,
                            VERDE_MENU,
                            VERDE_HOVER,
                            VERDE_ACTIVO
                    );

            boton.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            boton.addMouseListener(
                    new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(
                        java.awt.event.MouseEvent e) {

                    mostrarPanel(nombrePanel);
                }

            });

            botonesMenu.put(
                    nombrePanel,
                    boton
            );

            menu.add(boton);

            menu.add(
                    Box.createVerticalStrut(5)
            );
        }

        private JPanel crearZonaPrincipal() {

            JPanel zona
                    = new JPanel(
                            new BorderLayout()
                    );

            zona.setBackground(FONDO);

            zona.add(
                    crearEncabezado(),
                    BorderLayout.NORTH
            );

            administradorPaneles
                    = new CardLayout();

            contenedorPaneles
                    = new JPanel(
                            administradorPaneles
                    );

            contenedorPaneles.setBackground(FONDO);

            contenedorPaneles.add(
                    new PanelInicio(this),
                    "INICIO"
            );

            contenedorPaneles.add(
                    new PanelClientes(),
                    "CLIENTES"
            );

            contenedorPaneles.add(
                    new PanelMascotas(),
                    "MASCOTAS"
            );

            contenedorPaneles.add(
                    new PanelVeterinarios(),
                    "VETERINARIOS"
            );

            contenedorPaneles.add(
                    new PanelCitas(),
                    "CITAS"
            );

            contenedorPaneles.add(
                    new PanelConsultas(),
                    "CONSULTAS"
            );

            contenedorPaneles.add(
                    new PanelServicios(),
                    "SERVICIOS"
            );

            contenedorPaneles.add(
                    new PanelFacturacion(),
                    "FACTURACION"
            );

            zona.add(
                    contenedorPaneles,
                    BorderLayout.CENTER
            );

            return zona;
        }

        private JPanel crearEncabezado() {

            JPanel encabezado
                    = new JPanel(
                            new BorderLayout()
                    );

            encabezado.setBackground(
                    Color.WHITE
            );

            encabezado.setPreferredSize(
                    new Dimension(0, 78)
            );

            encabezado.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(
                                    0,
                                    0,
                                    1,
                                    0,
                                    new Color(230, 230, 230)
                            ),
                            new EmptyBorder(
                                    15,
                                    30,
                                    15,
                                    30
                            )
                    )
            );

            lblTituloActual
                    = new JLabel("Inicio");

            lblTituloActual.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            28
                    )
            );

            lblTituloActual.setForeground(
                    new Color(25, 80, 63)
            );

            DateTimeFormatter formato
                    = DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy"
                    );

            JLabel lblFecha
                    = new JLabel(
                            "📅 "
                            + LocalDate.now().format(formato)
                    );

            lblFecha.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );

            lblFecha.setForeground(
                    new Color(110, 90, 70)
            );

            encabezado.add(
                    lblTituloActual,
                    BorderLayout.WEST
            );

            encabezado.add(
                    lblFecha,
                    BorderLayout.EAST
            );

            return encabezado;
        }

        public void mostrarPanel(
                String nombrePanel) {

            administradorPaneles.show(
                    contenedorPaneles,
                    nombrePanel
            );

            actualizarTitulo(
                    nombrePanel
            );

            actualizarBotonActivo(
                    nombrePanel
            );

        }

        private void actualizarTitulo(
                String nombrePanel) {

            String titulo
                    = switch (nombrePanel) {

                case "CLIENTES" ->
                    "Clientes";

                case "MASCOTAS" ->
                    "Mascotas";

                case "VETERINARIOS" ->
                    "Veterinarios";

                case "CITAS" ->
                    "Citas";

                case "CONSULTAS" ->
                    "Consultas";

                case "SERVICIOS" ->
                    "Servicios";

                case "FACTURACION" ->
                    "Facturación";

                default ->
                    "Inicio";
            };

            lblTituloActual.setText(
                    titulo
            );

        }

        private void actualizarBotonActivo(
                String nombrePanel) {

            for (Map.Entry<String, BotonMenuURL> entrada
                    : botonesMenu.entrySet()) {

                entrada.getValue().setActivo(
                        entrada.getKey().equals(nombrePanel)
                );

            }

        }
    }

