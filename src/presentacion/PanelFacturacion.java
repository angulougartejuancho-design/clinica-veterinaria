/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Datos.ClienteDAO;
import Datos.ServicioDAO;
import Negocio.FacturaServicio;

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

    /*
     * Inicializa toda la interfaz gráfica.
     */
    private void inicializarComponentes() {

        setLayout(new BorderLayout(15, 15));

        setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        setBackground(
                new Color(245, 247, 250)
        );

        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        add(
                crearContenidoPrincipal(),
                BorderLayout.CENTER
        );

        add(
                crearPanelEstado(),
                BorderLayout.SOUTH
        );
    }

    /*
     * Encabezado superior.
     */
    private JPanel crearEncabezado() {

        JPanel panel
                = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel lblTitulo
                = new JLabel("Módulo de Facturación");

        lblTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        lblTitulo.setForeground(
                new Color(35, 55, 75)
        );

        JLabel lblDescripcion
                = new JLabel(
                        "Registre servicios y genere "
                        + "facturas para los clientes"
                );

        lblDescripcion.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        lblDescripcion.setForeground(
                new Color(90, 105, 120)
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

    /*
     * Contenido principal dividido en dos secciones.
     */
    private JSplitPane crearContenidoPrincipal() {

        JSplitPane divisor
                = new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        crearPanelFacturaActual(),
                        crearPanelFacturasRegistradas()
                );

        divisor.setResizeWeight(0.62);
        divisor.setDividerLocation(420);
        divisor.setBorder(null);
        divisor.setOpaque(false);

        return divisor;
    }

    /*
     * Panel superior para crear una factura.
     */
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

    /*
     * Formulario con cliente, servicio,
     * cantidad y observaciones.
     */
    private JPanel crearPanelDatosFactura() {

        JPanel panel
                = new JPanel(new GridBagLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        215,
                                        220,
                                        225
                                )
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        GridBagConstraints gbc
                = new GridBagConstraints();

        gbc.insets
                = new Insets(6, 6, 6, 6);

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        /*
         * Cliente.
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Cliente:"),
                gbc
        );

        cmbClientes = new JComboBox<>();

        cmbClientes.setPreferredSize(
                new Dimension(280, 32)
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(cmbClientes, gbc);

        /*
         * Servicio.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Servicio:"),
                gbc
        );

        cmbServicios = new JComboBox<>();

        cmbServicios.setPreferredSize(
                new Dimension(300, 32)
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        panel.add(cmbServicios, gbc);

        /*
         * Cantidad.
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Cantidad:"),
                gbc
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

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(spnCantidad, gbc);

        /*
         * Botón agregar.
         */
        btnAgregarServicio
                = new JButton(
                        "Agregar servicio"
                );

        configurarBoton(
                btnAgregarServicio
        );

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        panel.add(
                btnAgregarServicio,
                gbc
        );

        /*
         * Observaciones.
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor
                = GridBagConstraints.NORTH;

        panel.add(
                crearEtiqueta("Observaciones:"),
                gbc
        );

        txtObservaciones
                = new JTextArea(3, 30);

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObservaciones
                = new JScrollPane(
                        txtObservaciones
                );

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill
                = GridBagConstraints.BOTH;

        panel.add(
                scrollObservaciones,
                gbc
        );

        return panel;
    }

    /*
     * Tabla con los servicios agregados
     * a la factura actual.
     */
    private JPanel crearPanelDetalleFactura() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Detalle de la factura");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(35, 55, 75));

        lblNumeroFactura = new JLabel("Factura nueva");
        lblNumeroFactura.setFont(new Font("Arial", Font.BOLD, 15));
        lblNumeroFactura.setForeground(new Color(25, 105, 70));

        lblCantidadServicios = new JLabel("Servicios agregados: 0");
        lblCantidadServicios.setFont(new Font("Arial", Font.PLAIN, 13));
        lblCantidadServicios.setForeground(new Color(80, 95, 110));

        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        panelInfo.setOpaque(false);
        panelInfo.add(lblNumeroFactura);
        panelInfo.add(lblCantidadServicios);

        panelTitulo.add(lblTitulo, BorderLayout.WEST);
        panelTitulo.add(panelInfo, BorderLayout.EAST);

        modeloDetalles = new DefaultTableModel(
                new Object[]{
                    "ID servicio",
                    "Servicio",
                    "Tipo",
                    "Precio sin IVA",
                    "Cantidad",
                    "Subtotal sin IVA"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloDetalles);
        configurarTabla(tablaDetalles);

        btnModificarCantidad = new JButton("Modificar cantidad");
        btnEliminarDetalle = new JButton("Eliminar detalle");
        btnVaciarFactura = new JButton("Vaciar factura");

        configurarBoton(btnModificarCantidad);
        configurarBoton(btnEliminarDetalle);
        configurarBoton(btnVaciarFactura);

        btnModificarCantidad.setEnabled(false);
        btnEliminarDetalle.setEnabled(false);
        btnVaciarFactura.setEnabled(false);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        panelBotones.setOpaque(false);
        panelBotones.add(btnModificarCantidad);
        panelBotones.add(btnEliminarDetalle);
        panelBotones.add(btnVaciarFactura);

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    /*
     * Panel con subtotal, impuesto, total
     * y botones principales.
     */
    private JPanel crearPanelTotales() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(15, 15)
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        215,
                                        220,
                                        225
                                )
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        JPanel panelMontos
                = new JPanel(
                        new GridLayout(
                                3,
                                2,
                                15,
                                5
                        )
                );

        panelMontos.setOpaque(false);

        panelMontos.add(
                crearEtiquetaMonto("Subtotal:")
        );

        lblSubtotal
                = crearValorMonto("₡0.00");

        panelMontos.add(lblSubtotal);

        panelMontos.add(
                crearEtiquetaMonto("Impuesto:")
        );

        lblImpuesto
                = crearValorMonto("₡0.00");

        panelMontos.add(lblImpuesto);

        JLabel etiquetaTotal
                = crearEtiquetaMonto("TOTAL:");

        etiquetaTotal.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        panelMontos.add(etiquetaTotal);

        lblTotal
                = crearValorMonto("₡0.00");

        lblTotal.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        lblTotal.setForeground(
                new Color(25, 105, 70)
        );

        panelMontos.add(lblTotal);

        JPanel panelBotones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelBotones.setOpaque(false);

        btnGuardarFactura
                = new JButton(
                        "Guardar factura"
                );

        btnLimpiar
                = new JButton("Limpiar");

        btnRefrescar
                = new JButton("Refrescar");

        configurarBoton(
                btnGuardarFactura
        );

        configurarBoton(btnLimpiar);
        configurarBoton(btnRefrescar);

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);
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

    /*
     * Panel inferior con las facturas registradas.
     */
    private JPanel crearPanelFacturasRegistradas() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        215,
                                        220,
                                        225
                                )
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel lblTitulo
                = new JLabel(
                        "Facturas registradas"
                );

        lblTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        lblTitulo.setForeground(
                new Color(35, 55, 75)
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
                = new JTable(modeloFacturas);

        configurarTabla(tablaFacturas);

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(
                        tablaFacturas
                ),
                BorderLayout.CENTER
        );

        return panel;
    }

    /*
     * Estado inferior del panel.
     */
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

    private JLabel crearEtiqueta(
            String texto) {

        JLabel etiqueta
                = new JLabel(texto);

        etiqueta.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        etiqueta.setForeground(
                new Color(50, 65, 80)
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
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        etiqueta.setForeground(
                new Color(50, 65, 80)
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
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        etiqueta.setForeground(
                new Color(35, 55, 75)
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

    /*
     * Configura los eventos de los componentes.
     */
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
