/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Datos.ClienteDAO;
import Datos.ServicioDAO;
import Negocio.FacturaServicio;
import javax.swing.ScrollPaneConstants;
import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.Factura;
import modelo.Servicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.SQLException;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import javax.swing.border.EmptyBorder;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class PanelFacturacion extends JPanel {

    /*
     * Capa de negocio y acceso a datos.
     */
    private final FacturaServicio facturaServicio;
    private final ClienteDAO clienteDAO;
    private final ServicioDAO servicioDAO;

    /*
     * Componentes del formulario.
     */
    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Servicio> cmbServicios;
    private JSpinner spnCantidad;
    private JTextArea txtObservaciones;

    /*
     * Tabla de detalles de la factura actual.
     */
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;

    /*
     * Tabla de facturas registradas.
     */
    private JTable tablaFacturas;
    private DefaultTableModel modeloFacturas;

    /*
     * Etiquetas de montos.
     */
    private JLabel lblSubtotal;
    private JLabel lblImpuesto;
    private JLabel lblTotal;
    private JLabel lblEstado;
    private JLabel lblNumeroFactura;
    private JLabel lblCantidadServicios;

    /*
     * Botones.
     */
    private JButton btnAgregarServicio;
    private JButton btnEliminarDetalle;
    private JButton btnModificarCantidad;
    private JButton btnVaciarFactura;
    private JButton btnGuardarFactura;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    /*
     * Datos cargados desde la base de datos.
     */
    private List<Cliente> listaClientes;
    private List<Servicio> listaServicios;

    /*
     * Factura que se está construyendo actualmente.
     */
    private Factura facturaActual;

    private final DateTimeFormatter formatoFecha
            = DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            );

    public PanelFacturacion() {

        facturaServicio = new FacturaServicio();
        clienteDAO = new ClienteDAO();
        servicioDAO = new ServicioDAO();

        listaClientes = new ArrayList<>();
        listaServicios = new ArrayList<>();

        inicializarComponentes();
        configurarEventos();

        cargarDatosIniciales();
    }

    private void inicializarComponentes() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                new Color(248, 245, 238)
        );

        /*
     * Contenedor que tendrá todo el contenido
     * del módulo de facturación.
         */
        JPanel contenido
                = new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        contenido.setBackground(
                new Color(248, 245, 238)
        );

        contenido.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        contenido.add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        contenido.add(
                crearContenidoPrincipal(),
                BorderLayout.CENTER
        );

        contenido.add(
                crearPanelEstado(),
                BorderLayout.SOUTH
        );

        /*
     * Scroll general para poder subir y bajar
     * cuando el contenido no cabe en pantalla.
         */
        JScrollPane scrollGeneral
                = new JScrollPane(
                        contenido
                );

        scrollGeneral.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollGeneral.getViewport().setBackground(
                new Color(248, 245, 238)
        );

        scrollGeneral.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollGeneral.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollGeneral.getVerticalScrollBar()
                .setUnitIncrement(18);

        add(
                scrollGeneral,
                BorderLayout.CENTER
        );
    }

    private JPanel crearEncabezado() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JLabel lblTitulo
                = new JLabel(
                        "Módulo de Facturación"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        lblTitulo.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        JLabel lblDescripcion
                = new JLabel(
                        "Agregue servicios, calcule los montos y genere facturas para los clientes."
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblDescripcion.setForeground(
                new Color(
                        95,
                        105,
                        100
                )
        );

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                lblDescripcion,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JSplitPane crearContenidoPrincipal() {

        JSplitPane divisor
                = new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        crearPanelFacturaActual(),
                        crearPanelFacturasRegistradas()
                );

        divisor.setResizeWeight(0.65);

        divisor.setDividerLocation(550);

        divisor.setDividerSize(8);

        divisor.setBorder(
                BorderFactory.createEmptyBorder()
        );

        divisor.setOpaque(false);

        divisor.setContinuousLayout(true);

        return divisor;
    }

    private JPanel crearPanelFacturaActual() {

        JPanel panel
                = new JPanel(new BorderLayout(15, 15));

        panel.setOpaque(false);

        panel.add(
                crearPanelDatosFactura(),
                BorderLayout.NORTH
        );

        panel.add(
                crearPanelDetalleFactura(),
                BorderLayout.CENTER
        );

        panel.add(
                crearPanelTotales(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel crearPanelDatosFactura() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        26,
                        Color.WHITE
                );

        tarjeta.setMostrarSombra(true);

        tarjeta.setLayout(
                new BorderLayout(
                        24,
                        15
                )
        );

        tarjeta.setBorder(
                new EmptyBorder(
                        20,
                        22,
                        20,
                        22
                )
        );

        //--------------------------------------------------
        // ZONA IZQUIERDA: IMAGEN Y TÍTULO
        //--------------------------------------------------
        JPanel zonaImagen
                = new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        zonaImagen.setOpaque(false);

        zonaImagen.setPreferredSize(
                new Dimension(
                        290,
                        250
                )
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:23px; color:#00695C;'>"
                        + "<b>Nueva Factura</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:12px; color:#666666;'>"
                        + "Seleccione el cliente y agregue"
                        + "<br>"
                        + "los servicios correspondientes."
                        + "</div>"
                        + "</html>"
                );

        PanelImagenURL imagen
                = new PanelImagenURL(
                        "https://images.unsplash.com/"
                        + "photo-1556742049-0cfed4f6a45d"
                        + "?auto=format&fit=crop&w=900&q=85"
                );

        imagen.setPreferredSize(
                new Dimension(
                        280,
                        180
                )
        );

        zonaImagen.add(
                titulo,
                BorderLayout.NORTH
        );

        zonaImagen.add(
                imagen,
                BorderLayout.CENTER
        );

        //--------------------------------------------------
        // ZONA DERECHA: FORMULARIO
        //--------------------------------------------------
        JPanel panelCampos
                = new JPanel(
                        new GridBagLayout()
                );

        panelCampos.setOpaque(false);

        GridBagConstraints gbc
                = new GridBagConstraints();

        gbc.insets
                = new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        cmbClientes = new JComboBox<>();

        configurarCombo(
                cmbClientes
        );

        cmbServicios = new JComboBox<>();

        configurarCombo(
                cmbServicios
        );

        spnCantidad
                = new JSpinner(
                        new SpinnerNumberModel(
                                1,
                                1,
                                100,
                                1
                        )
                );

        spnCantidad.setPreferredSize(
                new Dimension(
                        160,
                        38
                )
        );

        spnCantidad.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        btnAgregarServicio
                = crearBotonColor(
                        "Agregar servicio",
                        new Color(
                                34,
                                165,
                                95
                        )
                );

        txtObservaciones
                = new JTextArea(
                        3,
                        30
                );

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        txtObservaciones.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtObservaciones.setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        //--------------------------------------------------
        // CLIENTE
        //--------------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panelCampos.add(
                crearEtiqueta(
                        "Cliente"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelCampos.add(
                cmbClientes,
                gbc
        );

        //--------------------------------------------------
        // SERVICIO
        //--------------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelCampos.add(
                crearEtiqueta(
                        "Servicio"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelCampos.add(
                cmbServicios,
                gbc
        );

        //--------------------------------------------------
        // CANTIDAD
        //--------------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelCampos.add(
                crearEtiqueta(
                        "Cantidad"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelCampos.add(
                spnCantidad,
                gbc
        );

        //--------------------------------------------------
        // BOTÓN AGREGAR
        //--------------------------------------------------
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.anchor
                = GridBagConstraints.WEST;

        panelCampos.add(
                btnAgregarServicio,
                gbc
        );

        //--------------------------------------------------
        // OBSERVACIONES
        //--------------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.anchor
                = GridBagConstraints.NORTH;

        panelCampos.add(
                crearEtiqueta(
                        "Observaciones"
                ),
                gbc
        );

        JScrollPane scrollObservaciones
                = new JScrollPane(
                        txtObservaciones
                );

        scrollObservaciones.setPreferredSize(
                new Dimension(
                        330,
                        80
                )
        );

        scrollObservaciones.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                210,
                                215,
                                212
                        )
                )
        );

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill
                = GridBagConstraints.BOTH;

        panelCampos.add(
                scrollObservaciones,
                gbc
        );

        tarjeta.add(
                zonaImagen,
                BorderLayout.WEST
        );

        tarjeta.add(
                panelCampos,
                BorderLayout.CENTER
        );

        return tarjeta;
    }

    private JPanel crearPanelDetalleFactura() {

        PanelRedondeado panel
                = new PanelRedondeado(
                        26,
                        Color.WHITE
                );

        panel.setMostrarSombra(true);

        panel.setLayout(
                new BorderLayout(
                        10,
                        12
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        20,
                        20
                )
        );

        //--------------------------------------------------
        // ENCABEZADO
        //--------------------------------------------------
        JPanel panelTitulo
                = new JPanel(
                        new BorderLayout()
                );

        panelTitulo.setOpaque(false);

        JLabel lblTitulo
                = new JLabel(
                        "Detalle de la factura"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblTitulo.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        lblNumeroFactura
                = new JLabel(
                        "Factura nueva"
                );

        lblNumeroFactura.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblNumeroFactura.setForeground(
                new Color(
                        25,
                        105,
                        70
                )
        );

        lblCantidadServicios
                = new JLabel(
                        "Servicios agregados: 0"
                );

        lblCantidadServicios.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblCantidadServicios.setForeground(
                new Color(
                        80,
                        95,
                        110
                )
        );

        JPanel panelInfo
                = new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        panelInfo.setOpaque(false);

        panelInfo.add(
                lblNumeroFactura
        );

        panelInfo.add(
                lblCantidadServicios
        );

        panelTitulo.add(
                lblTitulo,
                BorderLayout.WEST
        );

        panelTitulo.add(
                panelInfo,
                BorderLayout.EAST
        );

        //--------------------------------------------------
        // TABLA DE DETALLES
        //--------------------------------------------------
        modeloDetalles
                = new DefaultTableModel(
                        new Object[]{
                            "ID servicio",
                            "Servicio",
                            "Tipo",
                            "Precio unitario",
                            "Cantidad",
                            "Subtotal"
                        },
                        0
                ) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tablaDetalles
                = new JTable(
                        modeloDetalles
                );

        configurarTabla(
                tablaDetalles
        );

        tablaDetalles.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        tablaDetalles.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        tablaDetalles.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        tablaDetalles.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(140);

        tablaDetalles.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        tablaDetalles.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(150);

        JScrollPane scrollDetalles
                = new JScrollPane(
                        tablaDetalles
                );

        scrollDetalles.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                225,
                                222
                        )
                )
        );

        scrollDetalles.getViewport()
                .setBackground(
                        Color.WHITE
                );

        //--------------------------------------------------
        // BOTONES DEL DETALLE
        //--------------------------------------------------
        btnModificarCantidad
                = crearBotonColor(
                        "Modificar cantidad",
                        new Color(
                                55,
                                125,
                                210
                        )
                );

        btnEliminarDetalle
                = crearBotonColor(
                        "Eliminar detalle",
                        new Color(
                                220,
                                70,
                                70
                        )
                );

        btnVaciarFactura
                = crearBotonColor(
                        "Vaciar factura",
                        new Color(
                                230,
                                145,
                                35
                        )
                );

        btnModificarCantidad.setEnabled(false);
        btnEliminarDetalle.setEnabled(false);
        btnVaciarFactura.setEnabled(false);

        JPanel panelBotones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        panelBotones.add(
                btnModificarCantidad
        );

        panelBotones.add(
                btnEliminarDetalle
        );

        panelBotones.add(
                btnVaciarFactura
        );

        //--------------------------------------------------
        // ARMADO FINAL
        //--------------------------------------------------
        panel.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                scrollDetalles,
                BorderLayout.CENTER
        );

        panel.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel crearPanelTotales() {

        PanelRedondeado panel
                = new PanelRedondeado(
                        24,
                        Color.WHITE
                );

        panel.setMostrarSombra(true);

        panel.setLayout(
                new BorderLayout(
                        20,
                        15
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        16,
                        20,
                        16,
                        20
                )
        );

        JPanel panelMontos
                = new JPanel(
                        new GridLayout(
                                3,
                                2,
                                15,
                                8
                        )
                );

        panelMontos.setOpaque(false);

        panelMontos.add(
                crearEtiquetaMonto(
                        "Subtotal:"
                )
        );

        lblSubtotal
                = crearValorMonto(
                        "₡0.00"
                );

        panelMontos.add(
                lblSubtotal
        );

        panelMontos.add(
                crearEtiquetaMonto(
                        "Impuesto:"
                )
        );

        lblImpuesto
                = crearValorMonto(
                        "₡0.00"
                );

        panelMontos.add(
                lblImpuesto
        );

        JLabel etiquetaTotal
                = crearEtiquetaMonto(
                        "TOTAL:"
                );

        etiquetaTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        panelMontos.add(
                etiquetaTotal
        );

        lblTotal
                = crearValorMonto(
                        "₡0.00"
                );

        lblTotal.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        lblTotal.setForeground(
                new Color(
                        0,
                        105,
                        82
                )
        );

        panelMontos.add(
                lblTotal
        );

        JPanel panelBotones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        btnLimpiar
                = crearBotonColor(
                        "Limpiar",
                        new Color(
                                230,
                                145,
                                35
                        )
                );

        btnRefrescar
                = crearBotonColor(
                        "Refrescar",
                        new Color(
                                0,
                                121,
                                107
                        )
                );

        btnGuardarFactura
                = crearBotonColor(
                        "Guardar factura",
                        new Color(
                                34,
                                165,
                                95
                        )
                );

        panelBotones.add(
                btnLimpiar
        );

        panelBotones.add(
                btnRefrescar
        );

        panelBotones.add(
                btnGuardarFactura
        );

        panel.add(
                panelMontos,
                BorderLayout.WEST
        );

        panel.add(
                panelBotones,
                BorderLayout.EAST
        );

        return panel;
    }

    private JPanel crearPanelFacturasRegistradas() {

        PanelRedondeado panel
                = new PanelRedondeado(
                        26,
                        Color.WHITE
                );

        panel.setMostrarSombra(true);

        panel.setLayout(
                new BorderLayout(
                        10,
                        12
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        20,
                        20
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        "Facturas registradas"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblTitulo.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        modeloFacturas
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Fecha",
                            "Cliente",
                            "Subtotal",
                            "Impuesto",
                            "Total",
                            "Estado"
                        },
                        0
                ) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tablaFacturas
                = new JTable(
                        modeloFacturas
                );

        configurarTabla(
                tablaFacturas
        );

        tablaFacturas.setAutoCreateRowSorter(true);

        tablaFacturas.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        tablaFacturas.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(150);

        tablaFacturas.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(220);

        tablaFacturas.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        tablaFacturas.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(120);

        tablaFacturas.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(140);

        tablaFacturas.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        JScrollPane scrollFacturas
                = new JScrollPane(
                        tablaFacturas
                );

        scrollFacturas.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                225,
                                222
                        )
                )
        );

        scrollFacturas.getViewport()
                .setBackground(
                        Color.WHITE
                );

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                scrollFacturas,
                BorderLayout.CENTER
        );

        return panel;
    }
    
    private JPanel crearPanelEstado() {

        JPanel panel
                = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        lblEstado
                = new JLabel(
                        "Preparando módulo de facturación..."
                );

        lblEstado.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        lblEstado.setForeground(
                new Color(80, 95, 110)
        );

        panel.add(
                lblEstado,
                BorderLayout.WEST
        );

        return panel;
    }

    private JButton crearBotonColor(
            String texto,
            Color color) {

        JButton boton
                = new JButton(texto);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(color);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        boton.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent evento) {

                if (boton.isEnabled()) {
                    boton.setBackground(
                            color.brighter()
                    );
                }
            }

            @Override
            public void mouseExited(
                    MouseEvent evento) {

                boton.setBackground(color);
            }
        }
        );

        return boton;
    }

    private JLabel crearEtiqueta(
            String texto) {

        JLabel etiqueta
                = new JLabel(texto);

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        etiqueta.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        return etiqueta;
    }

    private JLabel crearEtiquetaMonto(
            String texto) {

        JLabel etiqueta
                = new JLabel(
                        texto,
                        SwingConstants.RIGHT
                );

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        etiqueta.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        return etiqueta;
    }

    private JLabel crearValorMonto(
            String texto) {

        JLabel etiqueta
                = new JLabel(
                        texto,
                        SwingConstants.RIGHT
                );

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        etiqueta.setForeground(
                new Color(
                        45,
                        65,
                        60
                )
        );

        return etiqueta;
    }
    
    private void configurarBoton(
            JButton boton) {

        boton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        boton.setFocusPainted(false);

        boton.setPreferredSize(
                new Dimension(150, 36)
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void configurarTabla(
            JTable tabla) {

        tabla.setRowHeight(28);

        tabla.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        tabla.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        tabla.setFillsViewportHeight(true);
    }

    private void configurarCombo(
            JComboBox<?> combo) {

        combo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        combo.setPreferredSize(
                new Dimension(
                        330,
                        38
                )
        );

        combo.setBackground(
                Color.WHITE
        );
    }
    private void configurarEventos() {

        btnAgregarServicio.addActionListener(
                evento -> agregarServicio()
        );

        btnEliminarDetalle.addActionListener(
                evento -> eliminarDetalle()
        );

        btnModificarCantidad.addActionListener(
                evento -> modificarCantidadDetalle()
        );

        btnVaciarFactura.addActionListener(
                evento -> vaciarFactura()
        );

        btnGuardarFactura.addActionListener(
                evento -> guardarFacturaConMultihilo()
        );

        btnLimpiar.addActionListener(
                evento -> limpiarFormulario()
        );

        btnRefrescar.addActionListener(
                evento -> cargarDatosIniciales()
        );

        cmbClientes.addActionListener(
                evento -> cambiarCliente()
        );

        tablaDetalles
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            boolean seleccion
                            = tablaDetalles
                                    .getSelectedRow()
                            >= 0;

                            btnEliminarDetalle
                                    .setEnabled(seleccion);

                            btnModificarCantidad
                                    .setEnabled(seleccion);
                        }
                );

        tablaDetalles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (evento.getClickCount() == 2
                        && tablaDetalles.getSelectedRow() >= 0) {
                    modificarCantidadDetalle();
                }
            }
        });
    }

    /*
     * Carga clientes, servicios y facturas
     * utilizando SwingWorker.
     */
    private void cargarDatosIniciales() {

        cambiarEstadoComponentes(false);

        lblEstado.setText(
                "Cargando datos desde la base de datos..."
        );

        SwingWorker<DatosIniciales, Void> trabajador
                = new SwingWorker<>() {

            @Override
            protected DatosIniciales doInBackground()
                    throws Exception {

                List<Cliente> clientes
                        = clienteDAO.listarTodos();

                List<Servicio> servicios
                        = servicioDAO.listar();

                List<Factura> facturas
                        = facturaServicio
                                .obtenerFacturas();

                return new DatosIniciales(
                        clientes,
                        servicios,
                        facturas
                );
            }

            @Override
            protected void done() {

                try {

                    DatosIniciales datos
                            = get();

                    listaClientes
                            = datos.clientes();

                    listaServicios
                            = datos.servicios();

                    cargarComboClientes();
                    cargarComboServicios();

                    cargarTablaFacturas(
                            datos.facturas()
                    );

                    crearFacturaNueva();

                    lblEstado.setText(
                            "Datos cargados correctamente."
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread()
                            .interrupt();

                    mostrarError(
                            "La carga fue interrumpida."
                    );

                } catch (ExecutionException ex) {

                    mostrarError(
                            obtenerMensajeError(
                                    ex.getCause()
                            )
                    );

                } finally {

                    cambiarEstadoComponentes(true);
                }
            }
        };

        trabajador.execute();
    }

    /*
     * Carga el JComboBox de clientes.
     */
    private void cargarComboClientes() {

        cmbClientes.removeAllItems();

        for (Cliente cliente
                : listaClientes) {

            cmbClientes.addItem(cliente);
        }
    }

    /*
     * Carga el JComboBox de servicios.
     */
    private void cargarComboServicios() {

        cmbServicios.removeAllItems();

        for (Servicio servicio
                : listaServicios) {

            cmbServicios.addItem(servicio);
        }
    }

    /*
     * Crea una factura nueva con el cliente
     * actualmente seleccionado.
     */
    private void crearFacturaNueva() {

        Cliente cliente
                = (Cliente) cmbClientes
                        .getSelectedItem();

        if (cliente == null) {

            facturaActual = null;

            lblEstado.setText(
                    "No existen clientes registrados."
            );

            actualizarTablaDetalles();
            actualizarMontos();

            return;
        }

        facturaActual
                = facturaServicio.crearFactura(
                        cliente,
                        txtObservaciones.getText()
                );

        actualizarTablaDetalles();
        actualizarMontos();
    }

    /*
     * Cambia el cliente de la factura actual.
     */
    private void cambiarCliente() {

        Cliente cliente
                = (Cliente) cmbClientes
                        .getSelectedItem();

        if (cliente == null) {
            return;
        }

        /*
         * Si todavía no existe factura,
         * crea una nueva.
         */
        if (facturaActual == null) {

            facturaActual
                    = facturaServicio.crearFactura(
                            cliente,
                            txtObservaciones.getText()
                    );

            return;
        }

        /*
         * Si ya existen detalles, se solicita
         * confirmación antes de cambiar.
         */
        if (facturaActual.tieneDetalles()
                && facturaActual.getCliente() != cliente) {

            int respuesta
                    = JOptionPane.showConfirmDialog(
                            this,
                            "Cambiar el cliente eliminará "
                            + "los servicios agregados.\n"
                            + "¿Desea continuar?",
                            "Confirmar cambio",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (respuesta
                    != JOptionPane.YES_OPTION) {

                cmbClientes.setSelectedItem(
                        facturaActual.getCliente()
                );

                return;
            }

            crearFacturaNueva();
        } else {

            facturaActual.setCliente(cliente);
        }
    }

    /*
     * Agrega el servicio seleccionado.
     */
    private void agregarServicio() {

        try {

            Cliente cliente
                    = (Cliente) cmbClientes
                            .getSelectedItem();

            Servicio servicio
                    = (Servicio) cmbServicios
                            .getSelectedItem();

            int cantidad
                    = (Integer) spnCantidad.getValue();

            if (facturaActual == null) {

                facturaActual
                        = facturaServicio.crearFactura(
                                cliente,
                                txtObservaciones.getText()
                        );
            }

            facturaActual.setCliente(cliente);

            facturaServicio.agregarServicio(
                    facturaActual,
                    servicio,
                    cantidad
            );

            actualizarTablaDetalles();
            actualizarMontos();

            spnCantidad.setValue(1);

            lblEstado.setText(
                    "Servicio agregado a la factura."
            );

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );
        }
    }

    /*
     * Elimina el detalle seleccionado.
     */
    private void eliminarDetalle() {

        int fila
                = tablaDetalles.getSelectedRow();

        if (fila < 0) {

            mostrarAdvertencia(
                    "Seleccione un detalle "
                    + "para eliminar."
            );

            return;
        }

        int respuesta
                = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el servicio "
                        + "seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta
                != JOptionPane.YES_OPTION) {

            return;
        }

        boolean eliminado
                = facturaServicio.eliminarDetalle(
                        facturaActual,
                        fila
                );

        if (eliminado) {

            actualizarTablaDetalles();
            actualizarMontos();

            btnEliminarDetalle
                    .setEnabled(false);

            lblEstado.setText(
                    "Detalle eliminado."
            );
        }
    }

    /**
     * Permite modificar la cantidad del detalle seleccionado. También se
     * ejecuta con doble clic sobre la fila.
     */
    private void modificarCantidadDetalle() {

        int fila = tablaDetalles.getSelectedRow();

        if (fila < 0 || facturaActual == null) {
            mostrarAdvertencia(
                    "Seleccione un detalle para modificar."
            );
            return;
        }

        DetalleFactura detalle
                = facturaActual.getDetalles().get(fila);

        JSpinner selectorCantidad = new JSpinner(
                new SpinnerNumberModel(
                        detalle.getCantidad(),
                        1,
                        100,
                        1
                )
        );

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                selectorCantidad,
                "Nueva cantidad para "
                + detalle.getServicio().getNombre(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }

        int nuevaCantidad
                = (Integer) selectorCantidad.getValue();

        detalle.setCantidad(nuevaCantidad);
        facturaActual.calcularTotales();

        actualizarTablaDetalles();
        actualizarMontos();

        lblEstado.setText(
                "Cantidad modificada correctamente."
        );
    }

    /**
     * Elimina todos los servicios de la factura actual.
     */
    private void vaciarFactura() {

        if (facturaActual == null
                || !facturaActual.tieneDetalles()) {
            mostrarAdvertencia(
                    "La factura no contiene servicios."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar todos los servicios de la factura?",
                "Vaciar factura",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        facturaActual.limpiarDetalles();
        tablaDetalles.clearSelection();

        actualizarTablaDetalles();
        actualizarMontos();

        lblEstado.setText("Factura vaciada correctamente.");
    }

    /*
     * Guarda la factura usando SwingWorker.
     *
     * Esta es la integración principal
     * de multihilo del Integrante 4.
     */
    private void guardarFacturaConMultihilo() {

        try {

            prepararFacturaParaGuardar();

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

            return;
        }

        int respuesta
                = JOptionPane.showConfirmDialog(
                        this,
                        "Cliente: "
                        + facturaActual.getCliente().getNombre()
                        + "\nServicios: "
                        + facturaActual.obtenerCantidadTotalServicios()
                        + "\nSubtotal: "
                        + formatearMoneda(facturaActual.getSubtotal())
                        + "\nImpuesto: "
                        + formatearMoneda(facturaActual.getImpuesto())
                        + "\nTOTAL: "
                        + formatearMoneda(facturaActual.getTotal())
                        + "\n\n¿Desea guardar la factura?",
                        "Confirmar factura",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta
                != JOptionPane.YES_OPTION) {

            return;
        }

        /*
         * Se almacena una referencia local para que
         * la tarea trabaje con la factura confirmada.
         */
        Factura facturaGuardar
                = facturaActual;

        cambiarEstadoComponentes(false);

        lblEstado.setText(
                "Guardando factura en segundo plano..."
        );

        SwingWorker<Void, Void> trabajador
                = new SwingWorker<>() {

            @Override
            protected Void doInBackground()
                    throws Exception {

                facturaServicio.guardarFactura(
                        facturaGuardar
                );

                return null;
            }

            @Override
            protected void done() {

                try {

                    get();

                    JOptionPane.showMessageDialog(
                            PanelFacturacion.this,
                            "Factura #"
                            + facturaGuardar
                                    .getIdFactura()
                            + " registrada correctamente.",
                            "Factura registrada",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    limpiarFormulario();

                    cargarFacturasConMultihilo();

                } catch (InterruptedException ex) {

                    Thread.currentThread()
                            .interrupt();

                    mostrarError(
                            "El guardado fue interrumpido."
                    );

                } catch (ExecutionException ex) {

                    mostrarError(
                            obtenerMensajeError(
                                    ex.getCause()
                            )
                    );

                } finally {

                    cambiarEstadoComponentes(true);
                }
            }
        };

        trabajador.execute();
    }

    /*
     * Prepara y valida la factura.
     */
    private void prepararFacturaParaGuardar() {

        Cliente cliente
                = (Cliente) cmbClientes
                        .getSelectedItem();

        if (facturaActual == null) {

            throw new IllegalArgumentException(
                    "No existe una factura activa."
            );
        }

        facturaActual.setCliente(cliente);

        facturaActual.setObservaciones(
                txtObservaciones
                        .getText()
                        .trim()
        );

        facturaActual.calcularTotales();

        if (!facturaActual.tieneDetalles()) {

            throw new IllegalArgumentException(
                    "Debe agregar al menos "
                    + "un servicio."
            );
        }
    }

    /*
     * Recarga únicamente la tabla de facturas.
     */
    private void cargarFacturasConMultihilo() {

        lblEstado.setText(
                "Actualizando facturas registradas..."
        );

        SwingWorker<List<Factura>, Void> trabajador
                = new SwingWorker<>() {

            @Override
            protected List<Factura>
                    doInBackground()
                    throws SQLException {

                return facturaServicio
                        .obtenerFacturas();
            }

            @Override
            protected void done() {

                try {

                    List<Factura> facturas
                            = get();

                    cargarTablaFacturas(
                            facturas
                    );

                    lblEstado.setText(
                            "Facturas actualizadas."
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread()
                            .interrupt();

                    mostrarError(
                            "La actualización "
                            + "fue interrumpida."
                    );

                } catch (ExecutionException ex) {

                    mostrarError(
                            obtenerMensajeError(
                                    ex.getCause()
                            )
                    );
                }
            }
        };

        trabajador.execute();
    }

    /*
     * Actualiza la tabla de detalles.
     */
    private void actualizarTablaDetalles() {

        modeloDetalles.setRowCount(0);

        if (facturaActual == null) {
            if (lblNumeroFactura != null) {
                lblNumeroFactura.setText("Factura nueva");
                lblCantidadServicios.setText("Servicios agregados: 0");
            }
            btnVaciarFactura.setEnabled(false);
            return;
        }

        if (facturaActual.getIdFactura() > 0) {
            lblNumeroFactura.setText(
                    String.format("Factura #%05d", facturaActual.getIdFactura())
            );
        } else {
            lblNumeroFactura.setText("Factura nueva");
        }

        lblCantidadServicios.setText(
                "Servicios agregados: "
                + facturaActual.obtenerCantidadTotalServicios()
        );

        btnVaciarFactura.setEnabled(facturaActual.tieneDetalles());

        for (DetalleFactura detalle
                : facturaActual.getDetalles()) {

            Servicio servicio
                    = detalle.getServicio();

            modeloDetalles.addRow(
                    new Object[]{
                        servicio.getId(),
                        servicio.getNombre(),
                        servicio.getTipo(),
                        formatearMoneda(
                                detalle
                                        .getPrecioUnitario()
                        ),
                        detalle.getCantidad(),
                        formatearMoneda(
                                detalle.getSubtotal()
                        )
                    }
            );
        }
    }

    /*
     * Actualiza las etiquetas de montos.
     */
    private void actualizarMontos() {

        if (facturaActual == null) {

            lblSubtotal.setText("₡0.00");
            lblImpuesto.setText("₡0.00");
            lblTotal.setText("₡0.00");

            return;
        }

        facturaActual.calcularTotales();

        lblSubtotal.setText(
                formatearMoneda(
                        facturaActual.getSubtotal()
                )
        );

        lblImpuesto.setText(
                formatearMoneda(
                        facturaActual.getImpuesto()
                )
        );

        lblTotal.setText(
                formatearMoneda(
                        facturaActual.getTotal()
                )
        );
    }

    /*
     * Carga las facturas registradas.
     */
    private void cargarTablaFacturas(
            List<Factura> facturas) {

        modeloFacturas.setRowCount(0);

        for (Factura factura : facturas) {

            modeloFacturas.addRow(
                    new Object[]{
                        factura.getIdFactura(),
                        factura.getFecha()
                                .format(formatoFecha),
                        factura.getCliente()
                                .getNombre(),
                        formatearMoneda(
                                factura.getSubtotal()
                        ),
                        formatearMoneda(
                                factura.getImpuesto()
                        ),
                        formatearMoneda(
                                factura.getTotal()
                        ),
                        factura.getEstado()
                    }
            );
        }
    }

    /*
     * Limpia el formulario y crea una factura nueva.
     */
    private void limpiarFormulario() {

        txtObservaciones.setText("");
        spnCantidad.setValue(1);

        tablaDetalles.clearSelection();

        crearFacturaNueva();

        lblEstado.setText(
                "Formulario de facturación limpio."
        );
    }

    /*
     * Habilita o deshabilita componentes
     * durante tareas en segundo plano.
     */
    private void cambiarEstadoComponentes(
            boolean habilitado) {

        cmbClientes.setEnabled(habilitado);
        cmbServicios.setEnabled(habilitado);
        spnCantidad.setEnabled(habilitado);
        txtObservaciones.setEnabled(habilitado);

        btnAgregarServicio
                .setEnabled(habilitado);

        btnGuardarFactura
                .setEnabled(habilitado);

        btnLimpiar
                .setEnabled(habilitado);

        btnRefrescar
                .setEnabled(habilitado);

        boolean filaSeleccionada
                = tablaDetalles.getSelectedRow() >= 0;

        btnEliminarDetalle.setEnabled(
                habilitado && filaSeleccionada
        );

        btnModificarCantidad.setEnabled(
                habilitado && filaSeleccionada
        );

        btnVaciarFactura.setEnabled(
                habilitado
                && facturaActual != null
                && facturaActual.tieneDetalles()
        );
    }

    private String formatearMoneda(
            double monto) {

        return "₡"
                + String.format(
                        "%,.2f",
                        monto
                );
    }

    private void mostrarAdvertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        lblEstado.setText(
                "Ocurrió un error."
        );
    }

    private String obtenerMensajeError(
            Throwable error) {

        if (error == null) {
            return "Ocurrió un error desconocido.";
        }

        Throwable causa = error;

        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        String mensaje
                = causa.getMessage();

        if (mensaje == null
                || mensaje.isBlank()) {

            return causa.getClass()
                    .getSimpleName();
        }

        return mensaje;
    }

    private record DatosIniciales(
            List<Cliente> clientes,
            List<Servicio> servicios,
            List<Factura> facturas) {

    }
}
