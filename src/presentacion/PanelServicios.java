/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;


import Exception.ValidationException;
import Negocio.GestionServicio;
import modelo.Procedimiento;
import modelo.Servicio;
import modelo.TipoServicio;
import modelo.Vacunacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import static modelo.TipoServicio.CONSULTA_GENERAL;
import static modelo.TipoServicio.PROCEDIMIENTO;
import static modelo.TipoServicio.VACUNACION;

/**
 *
 * @author Anyel
 */
public class PanelServicios extends JPanel {

    private final GestionServicio gestionServicios;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCostoBase;
    private JTextField txtDetalle;

    private JComboBox<TipoServicio> cmbTipo;

    private JLabel lblEtiquetaDetalle;

    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    private JLabel lblEstado;

    private List<Servicio> listaServicios;
    private int idServicioSeleccionado;

    public PanelServicios() {

        gestionServicios = new GestionServicio();

        listaServicios = new ArrayList<>();
        idServicioSeleccionado = 0;

        inicializarComponentes();
        configurarEventos();
        cargarServicios();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenidoCentral(), BorderLayout.CENTER);
        add(crearPanelEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo =
                new JLabel("Catálogo de Servicios");

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        lblTitulo.setForeground(new Color(35, 55, 75));

        JLabel lblDescripcion = new JLabel(
                "Administre consultas generales, "
                        + "vacunaciones y procedimientos"
        );

        lblDescripcion.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        lblDescripcion.setForeground(
                new Color(90, 105, 120)
        );

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblDescripcion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearContenidoCentral() {

        JPanel panel =
                new JPanel(new BorderLayout(15, 15));

        panel.setOpaque(false);

        panel.add(
                crearPanelFormulario(),
                BorderLayout.NORTH
        );

        panel.add(crearPanelTabla(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("ID:"), gbc);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 238, 242));

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtId, gbc);

      
        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Tipo:"), gbc);

        cmbTipo = new JComboBox<>(TipoServicio.values());

        gbc.gridx = 3;
        gbc.weightx = 1;
        panel.add(cmbTipo, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Nombre:"), gbc);

        txtNombre = new JTextField();

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtNombre, gbc);

        
        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Costo base (₡):"), gbc);

        txtCostoBase = new JTextField();

        gbc.gridx = 3;
        gbc.weightx = 1;
        panel.add(txtCostoBase, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Descripción:"), gbc);

        txtDescripcion = new JTextField();

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(txtDescripcion, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        lblEtiquetaDetalle =
                crearEtiqueta("Tipo de vacuna:");

        panel.add(lblEtiquetaDetalle, gbc);

        txtDetalle = new JTextField();

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(txtDetalle, gbc);

   
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        panel.add(crearPanelBotones(), gbc);

        return panel;
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        etiqueta.setForeground(new Color(50, 65, 80));

        return etiqueta;
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnRefrescar = new JButton("Refrescar");

        configurarBoton(btnRegistrar);
        configurarBoton(btnActualizar);
        configurarBoton(btnEliminar);
        configurarBoton(btnLimpiar);
        configurarBoton(btnRefrescar);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);

        return panel;
    }

    private void configurarBoton(JButton boton) {

        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(125, 36));

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    private JPanel crearPanelTabla() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblTituloTabla =
                new JLabel("Servicios registrados");

        lblTituloTabla.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        lblTituloTabla.setForeground(
                new Color(35, 55, 75)
        );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Tipo",
                    "Nombre",
                    "Costo base (₡)",
                    "Precio calculado (₡)"
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

        tablaServicios = new JTable(modeloTabla);
        tablaServicios.setRowHeight(28);

        tablaServicios.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        tablaServicios
                .getTableHeader()
                .setFont(
                        new Font("Arial", Font.BOLD, 13)
                );

        tablaServicios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaServicios.setAutoCreateRowSorter(true);
        tablaServicios.setFillsViewportHeight(true);

        tablaServicios.setGridColor(
                new Color(225, 228, 232)
        );

        JScrollPane scrollTabla =
                new JScrollPane(tablaServicios);

        scrollTabla.setPreferredSize(
                new Dimension(800, 260)
        );

        panel.add(lblTituloTabla, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstado() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        lblEstado = new JLabel(
                "Seleccione una opción para comenzar."
        );

        lblEstado.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        lblEstado.setForeground(new Color(80, 95, 110));

        lblEstado.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        panel.add(lblEstado, BorderLayout.CENTER);

        return panel;
    }

    private void configurarEventos() {

        btnRegistrar.addActionListener(
                evento -> registrarServicio()
        );

        btnActualizar.addActionListener(
                evento -> actualizarServicio()
        );

        btnEliminar.addActionListener(
                evento -> eliminarServicio()
        );

        btnLimpiar.addActionListener(
                evento -> limpiarFormulario()
        );

        btnRefrescar.addActionListener(
                evento -> {
                    cargarServicios();
                    limpiarFormulario();
                }
        );

        cmbTipo.addItemListener(
                evento -> actualizarEtiquetaDetalle()
        );

        tablaServicios
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento
                                    .getValueIsAdjusting()) {

                                seleccionarServicio();
                            }
                        }
                );
    }

    private void actualizarEtiquetaDetalle() {

        TipoServicio tipo =
                (TipoServicio) cmbTipo.getSelectedItem();

        if (tipo == null) {
            return;
        }

        switch (tipo) {

            case VACUNACION ->
                lblEtiquetaDetalle.setText(
                        "Tipo de vacuna:"
                );

            case PROCEDIMIENTO ->
                lblEtiquetaDetalle.setText(
                        "Duración (minutos):"
                );

            case CONSULTA_GENERAL ->
                lblEtiquetaDetalle.setText(
                        "Motivo típico:"
                );
        }
    }

    private void registrarServicio() {

        try {

            Servicio servicio =
                    crearServicioDesdeFormulario();

            gestionServicios.registrar(servicio);

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mostrarEstado(
                    "Servicio registrado correctamente.",
                    false
            );

            cargarServicios();
            limpiarFormulario();

        } catch (ValidationException
                | NumberFormatException ex) {

            mostrarAdvertencia(
                    ex instanceof NumberFormatException
                            ? "El costo base y la duración "
                                    + "deben ser numéricos."
                            : ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo registrar el servicio.",
                    ex
            );
        }
    }

    private void actualizarServicio() {

        if (idServicioSeleccionado <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar un servicio "
                            + "de la tabla."
            );

            return;
        }

        try {

            Servicio servicio =
                    crearServicioDesdeFormulario();

            servicio.setId(idServicioSeleccionado);

            boolean actualizado =
                    gestionServicios.actualizar(servicio);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Servicio actualizado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Servicio actualizado correctamente.",
                        false
                );

                cargarServicios();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró el servicio "
                                + "que se desea actualizar."
                );
            }

        } catch (ValidationException
                | NumberFormatException ex) {

            mostrarAdvertencia(
                    ex instanceof NumberFormatException
                            ? "El costo base y la duración "
                                    + "deben ser numéricos."
                            : ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo actualizar el servicio.",
                    ex
            );
        }
    }

    private void eliminarServicio() {

        if (idServicioSeleccionado <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar un servicio "
                            + "de la tabla."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el servicio "
                        + "seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean eliminado = gestionServicios.eliminar(
                    idServicioSeleccionado
            );

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Servicio eliminado correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Servicio eliminado correctamente.",
                        false
                );

                cargarServicios();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró el servicio "
                                + "que se desea eliminar."
                );
            }

        } catch (ValidationException ex) {

            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {

            if (esErrorLlaveForanea(ex)) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se puede eliminar este servicio "
                                + "porque está incluido en "
                                + "una factura.",
                        "Eliminación no permitida",
                        JOptionPane.WARNING_MESSAGE
                );

                mostrarEstado(
                        "El servicio está relacionado "
                                + "con una factura.",
                        true
                );

            } else {

                mostrarErrorBaseDatos(
                        "No se pudo eliminar el servicio.",
                        ex
                );
            }
        }
    }

    private Servicio crearServicioDesdeFormulario()
            throws ValidationException {

        TipoServicio tipo =
                (TipoServicio) cmbTipo.getSelectedItem();

        String nombre = txtNombre.getText().trim();
        String descripcion =
                txtDescripcion.getText().trim();

        double costoBase = Double.parseDouble(
                txtCostoBase.getText().trim()
        );

        String detalle = txtDetalle.getText().trim();

        return switch (tipo) {

            case VACUNACION ->
                gestionServicios.crearVacunacion(
                        nombre,
                        descripcion,
                        costoBase,
                        detalle
                );

            case PROCEDIMIENTO ->
                gestionServicios.crearProcedimiento(
                        nombre,
                        descripcion,
                        costoBase,
                        Integer.parseInt(detalle)
                );

            case CONSULTA_GENERAL ->
                gestionServicios.crearConsultaGeneral(
                        nombre,
                        descripcion,
                        costoBase,
                        detalle
                );
        };
    }

    public final void cargarServicios() {

        try {

            listaServicios = gestionServicios.listar();

            modeloTabla.setRowCount(0);

            for (Servicio servicio : listaServicios) {

                modeloTabla.addRow(
                        new Object[]{
                            servicio.getId(),
                            servicio.getTipo(),
                            servicio.getNombre(),
                            String.format(
                                    "%.2f",
                                    servicio.getCostoBase()
                            ),
                            String.format(
                                    "%.2f",
                                    servicio.calcularPrecio()
                            )
                        }
                );
            }

            mostrarEstado(
                    "Servicios cargados: "
                            + listaServicios.size()
                            + " (total en memoria: "
                            + Servicio
                                    .getTotalServiciosCreados()
                            + ")",
                    false
            );

        } catch (SQLException ex) {

            modeloTabla.setRowCount(0);

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los servicios.",
                    ex
            );
        }
    }

    private void seleccionarServicio() {

        int filaVista =
                tablaServicios.getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        int filaModelo =
                tablaServicios.convertRowIndexToModel(
                        filaVista
                );

        int id = Integer.parseInt(
                modeloTabla
                        .getValueAt(filaModelo, 0)
                        .toString()
        );

        Servicio servicio = buscarServicioEnLista(id);

        if (servicio == null) {
            return;
        }

        idServicioSeleccionado = servicio.getId();

        txtId.setText(
                String.valueOf(servicio.getId())
        );

        cmbTipo.setSelectedItem(servicio.getTipo());
        actualizarEtiquetaDetalle();

        txtNombre.setText(servicio.getNombre());
        txtDescripcion.setText(servicio.getDescripcion());

        txtCostoBase.setText(
                String.valueOf(servicio.getCostoBase())
        );

        if (servicio instanceof Vacunacion vacunacion) {

            txtDetalle.setText(
                    vacunacion.getTipoVacuna()
            );

        } else if (servicio
                instanceof Procedimiento procedimiento) {

            txtDetalle.setText(
                    String.valueOf(
                            procedimiento
                                    .getDuracionMinutos()
                    )
            );

        } else {

            txtDetalle.setText("");
        }

        btnRegistrar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);

        mostrarEstado(
                "Servicio seleccionado: "
                        + servicio.getNombre(),
                false
        );
    }

    private Servicio buscarServicioEnLista(int idServicio) {

        for (Servicio servicio : listaServicios) {

            if (servicio.getId() == idServicio) {
                return servicio;
            }
        }

        return null;
    }

    private void limpiarFormulario() {

        idServicioSeleccionado = 0;

        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCostoBase.setText("");
        txtDetalle.setText("");

        cmbTipo.setSelectedIndex(0);
        actualizarEtiquetaDetalle();

        tablaServicios.clearSelection();

        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        mostrarEstado(
                "Formulario preparado para un "
                        + "nuevo servicio.",
                false
        );
    }

    private boolean esErrorLlaveForanea(SQLException ex) {

        if (ex.getErrorCode() == 1451) {
            return true;
        }

        String mensaje = ex.getMessage();

        if (mensaje == null) {
            return false;
        }

        String mensajeMinuscula = mensaje.toLowerCase();

        return mensajeMinuscula.contains(
                "foreign key constraint"
        )
                || mensajeMinuscula.contains(
                        "cannot delete or update a "
                                + "parent row"
                );
    }

    private void mostrarAdvertencia(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        mostrarEstado(mensaje, true);
    }

    private void mostrarErrorBaseDatos(
            String mensaje,
            SQLException ex) {

        JOptionPane.showMessageDialog(
                this,
                mensaje + "\n\nDetalle: " + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );

        mostrarEstado(mensaje, true);

        ex.printStackTrace();
    }

    private void mostrarEstado(
            String mensaje,
            boolean esError) {

        lblEstado.setText(mensaje);

        lblEstado.setForeground(
                esError
                        ? new Color(180, 45, 45)
                        : new Color(45, 115, 75)
        );
    }
}