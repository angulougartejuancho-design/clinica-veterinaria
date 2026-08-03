/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Negocio.ClienteServicio;
import modelo.Cliente;
import javax.swing.table.TableRowSorter;
import Exception.ValidationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author angul
 */
public class PanelClientes extends JPanel {

    private final ClienteServicio clienteServicio;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JLabel lblCantidadClientes;
    private TableRowSorter<DefaultTableModel> ordenadorTabla;

    private int idClienteSeleccionado;

    public PanelClientes() {

        clienteServicio = new ClienteServicio();

        idClienteSeleccionado = 0;

        configurarPanel();

        crearComponentes();

        cargarClientes();

        limpiarFormulario();
    }

    private void configurarPanel() {

        setLayout(
                new BorderLayout(
                        15,
                        15
                )
        );

        setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        setBackground(
                new Color(
                        248,
                        245,
                        238
                )
        );
    }

    private void crearComponentes() {
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelFormulario() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        30,
                        Color.WHITE
                );

        tarjeta.setMostrarSombra(true);

        tarjeta.setLayout(
                new BorderLayout(
                        30,
                        20
                )
        );

        tarjeta.setBorder(
                new EmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );

        //--------------------------------------------------
        // PANEL IZQUIERDO (Imagen)
        //--------------------------------------------------
        JPanel panelImagen
                = new JPanel(
                        new BorderLayout(0, 15)
                );

        panelImagen.setOpaque(false);

        panelImagen.setPreferredSize(
                new Dimension(
                        340,
                        330
                )
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:26px; color:#00695C;'>"
                        + "<b>Gestión de Clientes</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:13px; color:#666666;'>"
                        + "Registre, actualice y administre"
                        + "<br>"
                        + "la información de los propietarios"
                        + "<br>"
                        + "de las mascotas."
                        + "</div>"
                        + "</html>"
                );

        titulo.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        15,
                        0
                )
        );

        panelImagen.add(
                titulo,
                BorderLayout.NORTH
        );

        PanelImagenURL imagen
                = new PanelImagenURL(
                        "https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?auto=format&fit=crop&w=900&q=80"
                );

        imagen.setPreferredSize(
                new Dimension(
                        320,
                        260
                )
        );

        panelImagen.add(
                imagen,
                BorderLayout.CENTER
        );

        //--------------------------------------------------
        // PANEL DERECHO (Formulario)
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
                        12,
                        12,
                        12,
                        12
                );

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        agregarCampo(
                panelCampos,
                gbc,
                0,
                "Nombre completo",
                txtNombre
        );

        agregarCampo(
                panelCampos,
                gbc,
                1,
                "Teléfono",
                txtTelefono
        );

        agregarCampo(
                panelCampos,
                gbc,
                2,
                "Correo electrónico",
                txtEmail
        );

        agregarCampo(
                panelCampos,
                gbc,
                3,
                "Dirección",
                txtDireccion
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        gbc.insets
                = new Insets(
                        25,
                        10,
                        10,
                        10
                );

        panelCampos.add(
                crearPanelBotones(),
                gbc
        );

        tarjeta.add(
                panelImagen,
                BorderLayout.WEST
        );

        tarjeta.add(
                panelCampos,
                BorderLayout.CENTER
        );

        return tarjeta;
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JTextField campo) {

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

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        campo.setPreferredSize(
                new Dimension(
                        330,
                        40
                )
        );

        campo.setBackground(Color.WHITE);

        campo.setCaretColor(
                new Color(0, 84, 69)
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 210, 210),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        panel.add(
                etiqueta,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                campo,
                gbc
        );
    }

    private JPanel crearPanelBotones() {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        15,
                        0
                )
        );

        panel.setOpaque(false);

        btnGuardar
                = crearBoton(
                        "Guardar",
                        new Color(34, 197, 94)
                );

        btnActualizar
                = crearBoton(
                        "Actualizar",
                        new Color(59, 130, 246)
                );

        btnEliminar
                = crearBoton(
                        "Eliminar",
                        new Color(239, 68, 68)
                );

        btnLimpiar
                = crearBoton(
                        "Limpiar",
                        new Color(245, 158, 11)
                );

        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        btnGuardar.addActionListener(e -> guardarCliente());

        btnActualizar.addActionListener(e -> actualizarCliente());

        btnEliminar.addActionListener(e -> eliminarCliente());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        return panel;
    }

    private JButton crearBoton(
            String texto,
            Color color) {

        JButton boton
                = new JButton(texto);

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        boton.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        22,
                        10,
                        22
                )
        );

        boton.addMouseListener(
                new MouseAdapter() {

            Color original = color;

            @Override
            public void mouseEntered(MouseEvent e) {

                boton.setBackground(
                        color.brighter()
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                boton.setBackground(
                        original
                );
            }

        });

        return boton;
    }

    private JPanel crearPanelTabla() {

        PanelRedondeado tarjetaTabla
                = new PanelRedondeado(
                        28,
                        Color.WHITE
                );

        tarjetaTabla.setMostrarSombra(true);

        tarjetaTabla.setLayout(
                new BorderLayout(
                        0,
                        16
                )
        );

        tarjetaTabla.setBorder(
                new EmptyBorder(
                        20,
                        22,
                        22,
                        22
                )
        );

        //--------------------------------------------------
        // ENCABEZADO
        //--------------------------------------------------
        JPanel encabezado
                = new JPanel(
                        new BorderLayout(
                                20,
                                10
                        )
                );

        encabezado.setOpaque(false);

        JPanel textos
                = new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo
                = new JLabel(
                        "Clientes registrados"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        titulo.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        JLabel descripcion
                = new JLabel(
                        "Consulte y seleccione un cliente para actualizarlo o eliminarlo."
                );

        descripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        descripcion.setForeground(
                new Color(
                        105,
                        105,
                        105
                )
        );

        lblCantidadClientes
                = new JLabel(
                        "0 clientes"
                );

        lblCantidadClientes.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblCantidadClientes.setForeground(
                new Color(
                        0,
                        110,
                        88
                )
        );

        lblCantidadClientes.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        199,
                                        230,
                                        219
                                )
                        ),
                        new EmptyBorder(
                                7,
                                13,
                                7,
                                13
                        )
                )
        );

        textos.add(titulo);
        textos.add(
                Box.createVerticalStrut(4)
        );
        textos.add(descripcion);

        encabezado.add(
                textos,
                BorderLayout.WEST
        );

        encabezado.add(
                lblCantidadClientes,
                BorderLayout.EAST
        );

        //--------------------------------------------------
        // BÚSQUEDA
        //--------------------------------------------------
        JPanel panelBusqueda
                = new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        panelBusqueda.setOpaque(false);

        JLabel lblBuscar
                = new JLabel(
                        "Buscar cliente:"
                );

        lblBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblBuscar.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        txtBuscar
                = new JTextField();

        txtBuscar.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtBuscar.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        txtBuscar.setToolTipText(
                "Buscar por nombre, teléfono, correo o dirección"
        );

        txtBuscar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        212
                                )
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        panelBusqueda.add(
                lblBuscar,
                BorderLayout.WEST
        );

        panelBusqueda.add(
                txtBuscar,
                BorderLayout.CENTER
        );

        //--------------------------------------------------
        // MODELO Y TABLA
        //--------------------------------------------------
        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Nombre",
                            "Teléfono",
                            "Correo",
                            "Dirección"
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

        tablaClientes
                = new JTable(modeloTabla);

        tablaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaClientes.setRowHeight(36);

        tablaClientes.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tablaClientes.setBackground(
                Color.WHITE
        );

        tablaClientes.setGridColor(
                new Color(
                        235,
                        235,
                        235
                )
        );

        tablaClientes.setShowVerticalLines(false);
        tablaClientes.setShowHorizontalLines(true);

        tablaClientes.setSelectionBackground(
                new Color(
                        214,
                        245,
                        233
                )
        );

        tablaClientes.setSelectionForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaClientes.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        tablaClientes.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        tablaClientes.getTableHeader().setBackground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaClientes.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaClientes.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        tablaClientes.getTableHeader()
                .setReorderingAllowed(false);

        tablaClientes.getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                seleccionarCliente();
                            }
                        }
                );

        tablaClientes.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        tablaClientes.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        tablaClientes.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        tablaClientes.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(250);

        tablaClientes.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(300);

        ordenadorTabla
                = new TableRowSorter<>(
                        modeloTabla
                );

        tablaClientes.setRowSorter(
                ordenadorTabla
        );

        txtBuscar.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent evento) {

                        filtrarClientes();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent evento) {

                        filtrarClientes();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent evento) {

                        filtrarClientes();
                    }
                }
                );

        JScrollPane scrollPane
                = new JScrollPane(
                        tablaClientes
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                225,
                                230,
                                227
                        )
                )
        );

        scrollPane.getViewport()
                .setBackground(
                        Color.WHITE
                );

        //--------------------------------------------------
        // ARMADO FINAL
        //--------------------------------------------------
        JPanel parteSuperior
                = new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        parteSuperior.setOpaque(false);

        parteSuperior.add(
                encabezado,
                BorderLayout.NORTH
        );

        parteSuperior.add(
                panelBusqueda,
                BorderLayout.SOUTH
        );

        tarjetaTabla.add(
                parteSuperior,
                BorderLayout.NORTH
        );

        tarjetaTabla.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return tarjetaTabla;
    }

    private void guardarCliente() {
        try {
            clienteServicio.registrar(
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtDireccion.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarClientes();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo registrar el cliente.",
                    ex
            );
        }
    }

    private void actualizarCliente() {
        if (idClienteSeleccionado <= 0) {
            mostrarAdvertencia(
                    "Debe seleccionar un cliente de la tabla."
            );
            return;
        }

        try {
            clienteServicio.actualizar(
                    idClienteSeleccionado,
                    txtNombre.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtDireccion.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente actualizado correctamente.",
                    "Actualización exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarClientes();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo actualizar el cliente.",
                    ex
            );
        }
    }

    private void filtrarClientes() {

        String texto
                = txtBuscar.getText().trim();

        if (texto.isEmpty()) {

            ordenadorTabla.setRowFilter(null);

        } else {

            ordenadorTabla.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)"
                            + java.util.regex.Pattern.quote(texto)
                    )
            );
        }

        actualizarCantidadVisible();
    }

    private void actualizarCantidadVisible() {

        int cantidad
                = tablaClientes.getRowCount();

        lblCantidadClientes.setText(
                cantidad == 1
                        ? "1 cliente"
                        : cantidad + " clientes"
        );
    }

    private void eliminarCliente() {
        if (idClienteSeleccionado <= 0) {
            mostrarAdvertencia(
                    "Debe seleccionar un cliente de la tabla."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar al cliente seleccionado?\n"
                        + "Sus mascotas también podrían eliminarse.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            clienteServicio.eliminar(
                    idClienteSeleccionado
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente eliminado correctamente.",
                    "Eliminación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarClientes();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo eliminar el cliente. "
                    + "Verifique si tiene registros relacionados.",
                    ex
            );
        }
    }

    private void cargarClientes() {

        try {

            modeloTabla.setRowCount(0);

            List<Cliente> clientes
                    = clienteServicio.listar();

            for (Cliente cliente : clientes) {

                modeloTabla.addRow(
                        new Object[]{
                            cliente.getId(),
                            cliente.getNombre(),
                            cliente.getTelefono(),
                            cliente.getEmail(),
                            cliente.getDireccion()
                        }
                );
            }

            actualizarCantidadVisible();

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los clientes.",
                    ex
            );
        }
    }

    private void seleccionarCliente() {

        int filaVista
                = tablaClientes.getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        /*
     * Convierte la posición visible de la tabla
     * a la posición real del modelo.
     *
     * Esto es necesario porque la tabla puede
     * estar ordenada o filtrada.
         */
        int filaModelo
                = tablaClientes.convertRowIndexToModel(
                        filaVista
                );

        idClienteSeleccionado
                = Integer.parseInt(
                        modeloTabla.getValueAt(
                                filaModelo,
                                0
                        ).toString()
                );

        txtNombre.setText(
                obtenerValorTabla(
                        filaModelo,
                        1
                )
        );

        txtTelefono.setText(
                obtenerValorTabla(
                        filaModelo,
                        2
                )
        );

        txtEmail.setText(
                obtenerValorTabla(
                        filaModelo,
                        3
                )
        );

        txtDireccion.setText(
                obtenerValorTabla(
                        filaModelo,
                        4
                )
        );

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private String obtenerValorTabla(
            int fila,
            int columna) {

        Object valor =
                modeloTabla.getValueAt(fila, columna);

        return valor != null
                ? valor.toString()
                : "";
    }

    private void limpiarFormulario() {
        idClienteSeleccionado = 0;

        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");

        tablaClientes.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocus();
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos incorrectos",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarErrorBaseDatos(
            String mensaje,
            SQLException excepcion) {

        JOptionPane.showMessageDialog(
                this,
                mensaje + "\n\nDetalle: "
                        + excepcion.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );
    }

   
    public void refrescarClientes() {
        cargarClientes();
    }
}
