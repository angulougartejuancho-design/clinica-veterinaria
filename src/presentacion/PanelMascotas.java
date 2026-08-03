/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Negocio.ClienteServicio;
import Negocio.MascotaServicio;
import Exception.ValidationException;

import modelo.Cliente;
import modelo.Especie;
import modelo.Mascota;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Panel para administrar las mascotas de los clientes.
 *
 * @author angul
 */
public class PanelMascotas extends JPanel {

    private final MascotaServicio mascotaServicio;
    private final ClienteServicio clienteServicio;

    private JComboBox<Cliente> cmbClientes;
    private JTextField txtNombre;
    private JComboBox<Especie> cmbEspecie;
    private JTextField txtRaza;
    private JTextField txtFechaNacimiento;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRecargarClientes;

    private JTable tablaMascotas;
    private DefaultTableModel modeloTabla;

    private int idMascotaSeleccionada;

    public PanelMascotas() {
        mascotaServicio = new MascotaServicio();
        clienteServicio = new ClienteServicio();
        idMascotaSeleccionada = 0;

        configurarPanel();
        crearComponentes();
        cargarClientes();
    }

    private void configurarPanel() {

        setLayout(
                new BorderLayout(15, 15)
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
                new Color(248, 245, 238)
        );
    }

    private void crearComponentes() {
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {

        JPanel panelSuperior
                = new JPanel(
                        new BorderLayout(0, 15)
                );

        panelSuperior.setOpaque(false);

        panelSuperior.add(
                crearPanelSeleccionCliente(),
                BorderLayout.NORTH
        );

        panelSuperior.add(
                crearPanelFormulario(),
                BorderLayout.CENTER
        );

        return panelSuperior;
    }

    private JButton crearBoton(
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

    private JPanel crearPanelSeleccionCliente() {

        PanelRedondeado tarjetaCliente
                = new PanelRedondeado(
                        24,
                        new Color(231, 245, 239)
                );

        tarjetaCliente.setLayout(
                new BorderLayout(15, 0)
        );

        tarjetaCliente.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

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
                        "Cliente responsable"
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        titulo.setForeground(
                new Color(0, 84, 69)
        );

        JLabel descripcion
                = new JLabel(
                        "Seleccione el propietario de la mascota."
                );

        descripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        descripcion.setForeground(
                new Color(90, 105, 99)
        );

        textos.add(titulo);
        textos.add(
                Box.createVerticalStrut(3)
        );
        textos.add(descripcion);

        JPanel controles
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                0
                        )
                );

        controles.setOpaque(false);

        cmbClientes
                = new JComboBox<>();

        cmbClientes.setPreferredSize(
                new Dimension(330, 40)
        );

        cmbClientes.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        btnRecargarClientes
                = crearBoton(
                        "Recargar clientes",
                        new Color(0, 121, 107)
                );

        cmbClientes.addActionListener(
                e -> {
                    limpiarFormulario();
                    cargarMascotas();
                }
        );

        btnRecargarClientes.addActionListener(
                e -> {
                    cargarClientes();
                    cargarMascotas();
                }
        );

        controles.add(cmbClientes);
        controles.add(btnRecargarClientes);

        tarjetaCliente.add(
                textos,
                BorderLayout.WEST
        );

        tarjetaCliente.add(
                controles,
                BorderLayout.CENTER
        );

        return tarjetaCliente;
    }

    private void agregarCampoTexto(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JTextField campo) {

        JLabel etiqueta
                = crearEtiqueta(texto);

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        campo.setPreferredSize(
                new Dimension(330, 40)
        );

        campo.setBackground(Color.WHITE);

        campo.setCaretColor(
                new Color(0, 84, 69)
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 212)
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(campo, gbc);
    }

    private void agregarCampoCombo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JComboBox<?> combo) {

        JLabel etiqueta
                = crearEtiqueta(texto);

        combo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        combo.setPreferredSize(
                new Dimension(330, 40)
        );

        combo.setBackground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(combo, gbc);
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
                new Color(0, 84, 69)
        );

        return etiqueta;
    }

    private JPanel crearPanelFormulario() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        28,
                        Color.WHITE
                );

        tarjeta.setMostrarSombra(true);

        tarjeta.setLayout(
                new BorderLayout(30, 15)
        );

        tarjeta.setBorder(
                new EmptyBorder(
                        22,
                        24,
                        22,
                        24
                )
        );

        //--------------------------------------------------
        // Imagen y encabezado
        //--------------------------------------------------
        JPanel zonaImagen
                = new JPanel(
                        new BorderLayout(0, 12)
                );

        zonaImagen.setOpaque(false);

        zonaImagen.setPreferredSize(
                new Dimension(310, 285)
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:24px; color:#00695C;'>"
                        + "<b>Gestión de Mascotas</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:12px; color:#666666;'>"
                        + "Registre y administre las mascotas"
                        + "<br>"
                        + "asociadas con cada cliente."
                        + "</div>"
                        + "</html>"
                );

        PanelImagenURL imagen
                = new PanelImagenURL(
                        "https://images.unsplash.com/"
                        + "photo-1548199973-03cce0bbc87b"
                        + "?auto=format&fit=crop&w=900&q=85"
                );

        imagen.setPreferredSize(
                new Dimension(300, 205)
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
        // Formulario
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
                        10,
                        10,
                        10,
                        10
                );

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtNombre = new JTextField();

        cmbEspecie
                = new JComboBox<>(
                        Especie.values()
                );

        txtRaza = new JTextField();
        txtFechaNacimiento = new JTextField();

        txtFechaNacimiento.setToolTipText(
                "Formato: año-mes-día. Ejemplo: 2024-05-20"
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                0,
                "Nombre",
                txtNombre
        );

        agregarCampoCombo(
                panelCampos,
                gbc,
                1,
                "Especie",
                cmbEspecie
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                2,
                "Raza",
                txtRaza
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                3,
                "Fecha de nacimiento",
                txtFechaNacimiento
        );

        JLabel ayudaFecha
                = new JLabel(
                        "Formato requerido: AAAA-MM-DD"
                );

        ayudaFecha.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        ayudaFecha.setForeground(
                new Color(125, 125, 125)
        );

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets
                = new Insets(0, 10, 8, 10);

        panelCampos.add(
                ayudaFecha,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        gbc.insets
                = new Insets(
                        18,
                        5,
                        5,
                        5
                );

        panelCampos.add(
                crearPanelBotones(),
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

    private JPanel crearPanelBotones() {

        JPanel panelBotones
                = new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        btnGuardar
                = crearBoton(
                        "Guardar",
                        new Color(34, 165, 95)
                );

        btnActualizar
                = crearBoton(
                        "Actualizar",
                        new Color(55, 125, 210)
                );

        btnEliminar
                = crearBoton(
                        "Eliminar",
                        new Color(220, 70, 70)
                );

        btnLimpiar
                = crearBoton(
                        "Limpiar",
                        new Color(230, 145, 35)
                );

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        btnGuardar.addActionListener(
                e -> guardarMascota()
        );

        btnActualizar.addActionListener(
                e -> actualizarMascota()
        );

        btnEliminar.addActionListener(
                e -> eliminarMascota()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        return panelBotones;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Nombre",
                            "Especie",
                            "Raza",
                            "Fecha de nacimiento"
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

        tablaMascotas
                = new JTable(modeloTabla);

        tablaMascotas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMascotas.setRowHeight(36);

        tablaMascotas.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tablaMascotas.setBackground(Color.WHITE);

        tablaMascotas.setSelectionBackground(
                new Color(214, 245, 233)
        );

        tablaMascotas.setSelectionForeground(
                new Color(0, 84, 69)
        );

        tablaMascotas.setGridColor(
                new Color(235, 235, 235)
        );

        tablaMascotas.setShowVerticalLines(false);
        tablaMascotas.setShowHorizontalLines(true);

        tablaMascotas.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        tablaMascotas.getTableHeader().setBackground(
                new Color(0, 84, 69)
        );

        tablaMascotas.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaMascotas.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 40)
                );

        tablaMascotas.getTableHeader()
                .setReorderingAllowed(false);

        tablaMascotas.getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {
                                seleccionarMascota();
                            }
                        }
                );

        tablaMascotas.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        tablaMascotas.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);

        tablaMascotas.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        tablaMascotas.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        tablaMascotas.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(180);

        JScrollPane scrollPane
                = new JScrollPane(tablaMascotas);

        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 222)
                        ),
                        new EmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        scrollPane.getViewport()
                .setBackground(Color.WHITE);

        return scrollPane;
    }

    private void guardarMascota() {
        Cliente cliente =
                obtenerClienteSeleccionado();

        if (cliente == null) {
            mostrarAdvertencia(
                    "Debe seleccionar un cliente."
            );
            return;
        }

        try {
            LocalDate fechaNacimiento =
                    obtenerFechaNacimiento();

            mascotaServicio.registrar(
                    cliente.getId(),
                    txtNombre.getText(),
                    (Especie) cmbEspecie.getSelectedItem(),
                    txtRaza.getText(),
                    fechaNacimiento
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Mascota registrada correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarMascotas();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (DateTimeParseException ex) {
            mostrarAdvertencia(
                    "La fecha debe escribirse con el formato "
                            + "año-mes-día.\n"
                            + "Ejemplo: 2024-05-20."
            );

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo registrar la mascota.",
                    ex
            );
        }
    }

    private void actualizarMascota() {
        if (idMascotaSeleccionada <= 0) {
            mostrarAdvertencia(
                    "Debe seleccionar una mascota de la tabla."
            );
            return;
        }

        try {
            LocalDate fechaNacimiento =
                    obtenerFechaNacimiento();

            mascotaServicio.actualizar(
                    idMascotaSeleccionada,
                    txtNombre.getText(),
                    (Especie) cmbEspecie.getSelectedItem(),
                    txtRaza.getText(),
                    fechaNacimiento
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Mascota actualizada correctamente.",
                    "Actualización exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarMascotas();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (DateTimeParseException ex) {
            mostrarAdvertencia(
                    "La fecha debe escribirse con el formato "
                            + "año-mes-día.\n"
                            + "Ejemplo: 2024-05-20."
            );

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo actualizar la mascota.",
                    ex
            );
        }
    }

    private void eliminarMascota() {
        if (idMascotaSeleccionada <= 0) {
            mostrarAdvertencia(
                    "Debe seleccionar una mascota de la tabla."
            );
            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar "
                                + "la mascota seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mascotaServicio.eliminar(
                    idMascotaSeleccionada
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Mascota eliminada correctamente.",
                    "Eliminación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();
            cargarMascotas();

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudo eliminar la mascota.",
                    ex
            );
        }
    }

    private void cargarClientes() {
        Cliente clienteAnterior =
                obtenerClienteSeleccionado();

        Integer idClienteAnterior =
                clienteAnterior != null
                        ? clienteAnterior.getId()
                        : null;

        try {
            cmbClientes.removeAllItems();

            List<Cliente> clientes =
                    clienteServicio.listar();

            for (Cliente cliente : clientes) {
                cmbClientes.addItem(cliente);
            }

            if (idClienteAnterior != null) {
                seleccionarClientePorId(
                        idClienteAnterior
                );
            }

            if (cmbClientes.getItemCount() == 0) {
                modeloTabla.setRowCount(0);
                btnGuardar.setEnabled(false);

                mostrarAdvertencia(
                        "No hay clientes registrados. "
                                + "Debe registrar un cliente primero."
                );
            } else {
                btnGuardar.setEnabled(true);
                cargarMascotas();
            }

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudieron cargar los clientes.",
                    ex
            );
        }
    }

    private void cargarMascotas() {
        modeloTabla.setRowCount(0);

        Cliente cliente =
                obtenerClienteSeleccionado();

        if (cliente == null) {
            return;
        }

        try {
            List<Mascota> mascotas =
                    mascotaServicio.listarPorCliente(
                            cliente.getId()
                    );

            for (Mascota mascota : mascotas) {
                modeloTabla.addRow(
                        new Object[]{
                            mascota.getId(),
                            mascota.getNombre(),
                            mascota.getEspecie(),
                            mascota.getRaza(),
                            mascota.getFechaNacimiento()
                        }
                );
            }

        } catch (ValidationException ex) {
            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudieron cargar las mascotas.",
                    ex
            );
        }
    }

    private void seleccionarMascota() {
        int filaSeleccionada =
                tablaMascotas.getSelectedRow();

        if (filaSeleccionada < 0) {
            return;
        }

        idMascotaSeleccionada =
                Integer.parseInt(
                        obtenerValorTabla(
                                filaSeleccionada,
                                0
                        )
                );

        txtNombre.setText(
                obtenerValorTabla(
                        filaSeleccionada,
                        1
                )
        );

        String especieTexto =
                obtenerValorTabla(
                        filaSeleccionada,
                        2
                );

        try {
            cmbEspecie.setSelectedItem(
                    Especie.valueOf(especieTexto)
            );
        } catch (IllegalArgumentException ex) {
            cmbEspecie.setSelectedItem(
                    Especie.OTRO
            );
        }

        txtRaza.setText(
                obtenerValorTabla(
                        filaSeleccionada,
                        3
                )
        );

        txtFechaNacimiento.setText(
                obtenerValorTabla(
                        filaSeleccionada,
                        4
                )
        );

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private Cliente obtenerClienteSeleccionado() {
        return (Cliente) cmbClientes.getSelectedItem();
    }

    private LocalDate obtenerFechaNacimiento()
            throws DateTimeParseException {

        String fechaTexto =
                txtFechaNacimiento
                        .getText()
                        .trim();

        if (fechaTexto.isEmpty()) {
            return null;
        }

        return LocalDate.parse(fechaTexto);
    }

    private String obtenerValorTabla(
            int fila,
            int columna) {

        Object valor =
                modeloTabla.getValueAt(
                        fila,
                        columna
                );

        if (valor == null) {
            return "";
        }

        return valor.toString();
    }

    private void seleccionarClientePorId(
            int idCliente) {

        for (int i = 0;
             i < cmbClientes.getItemCount();
             i++) {

            Cliente cliente =
                    cmbClientes.getItemAt(i);

            if (cliente.getId() == idCliente) {
                cmbClientes.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiarFormulario() {
        idMascotaSeleccionada = 0;

        txtNombre.setText("");

        if (cmbEspecie.getItemCount() > 0) {
            cmbEspecie.setSelectedIndex(0);
        }

        txtRaza.setText("");
        txtFechaNacimiento.setText("");

        tablaMascotas.clearSelection();

        btnGuardar.setEnabled(
                cmbClientes.getSelectedItem() != null
        );

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocus();
    }

    private void mostrarAdvertencia(
            String mensaje) {

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
                mensaje
                        + "\n\nDetalle: "
                        + excepcion.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void refrescarDatos() {
        cargarClientes();
    }
}