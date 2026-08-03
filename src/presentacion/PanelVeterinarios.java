/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Negocio.VeterinarioServicio;
import modelo.Especialidad;
import modelo.Veterinario;
import java.awt.FlowLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

/**
 *
 * @author Daryelin
 */
public class PanelVeterinarios extends JPanel {

    private final VeterinarioServicio veterinarioServicio;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JComboBox<Especialidad> cmbEspecialidad;

    private JTable tablaVeterinarios;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    private JLabel lblEstado;

    private List<Veterinario> listaVeterinarios;

    private int idVeterinarioSeleccionado;

    public PanelVeterinarios() {

        veterinarioServicio
                = new VeterinarioServicio();

        listaVeterinarios
                = new ArrayList<>();

        idVeterinarioSeleccionado = 0;

        inicializarComponentes();
        configurarEventos();
        cargarVeterinarios();
    }

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
                new Color(248, 245, 238)
        );

        add(
                crearEncabezado(),
                BorderLayout.NORTH
        );

        add(
                crearContenidoCentral(),
                BorderLayout.CENTER
        );

        add(
                crearPanelEstado(),
                BorderLayout.SOUTH
        );
    }

    private JPanel crearEncabezado() {

        JPanel panelEncabezado
                = new JPanel(
                        new BorderLayout()
                );

        panelEncabezado.setOpaque(false);

        JLabel lblTitulo
                = new JLabel(
                        "Gestión de Veterinarios"
                );

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
                        "Registre, consulte, actualice y elimine veterinarios"
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

        panelEncabezado.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panelEncabezado.add(
                lblDescripcion,
                BorderLayout.SOUTH
        );

        return panelEncabezado;
    }

    private JPanel crearContenidoCentral() {

        JPanel panelContenido = new JPanel(new BorderLayout(20, 20));

        panelContenido.setOpaque(false);

        panelContenido.add(
                crearPanelFormulario(),
                BorderLayout.NORTH
        );

        panelContenido.add(
                crearPanelTabla(),
                BorderLayout.CENTER
        );

        panelContenido.setBorder(
                new EmptyBorder(
                        10,
                        0,
                        0,
                        0
                )
        );

        return panelContenido;
    }

    private JPanel crearPanelFormulario() {

        PanelRedondeado tarjeta
                = new PanelRedondeado(
                        28,
                        Color.WHITE
                );

        tarjeta.setMostrarSombra(true);

        tarjeta.setLayout(
                new BorderLayout(
                        25,
                        15
                )
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
                        310,
                        285
                )
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:24px; color:#00695C;'>"
                        + "<b>Registro de Veterinarios</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:12px; color:#666666;'>"
                        + "Administre la información del personal"
                        + "<br>"
                        + "médico de la clínica veterinaria."
                        + "</div>"
                        + "</html>"
                );

        PanelImagenURL imagen
                = new PanelImagenURL(
                        "https://images.unsplash.com/"
                        + "photo-1628009368231-7bb7cfcb0def"
                        + "?auto=format&fit=crop&w=900&q=85"
                );

        imagen.setPreferredSize(
                new Dimension(
                        300,
                        205
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
        // ZONA DERECHA: CAMPOS
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

        txtId = new JTextField();

        txtId.setEditable(false);

        txtId.setBackground(
                new Color(
                        238,
                        241,
                        240
                )
        );

        txtNombre = new JTextField();

        cmbEspecialidad
                = new JComboBox<>(
                        Especialidad.values()
                );

        cmbEspecialidad.setSelectedIndex(-1);

        txtTelefono = new JTextField();
        txtEmail = new JTextField();

        agregarCampoTexto(
                panelCampos,
                gbc,
                0,
                "ID",
                txtId
        );
        
        txtId.setBackground(
                new Color(238, 241, 240)
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                1,
                "Nombre",
                txtNombre
        );

        agregarCampoCombo(
                panelCampos,
                gbc,
                2,
                "Especialidad",
                cmbEspecialidad
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                3,
                "Teléfono",
                txtTelefono
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                4,
                "Correo",
                txtEmail
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        gbc.insets
                = new Insets(
                        20,
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

    private void agregarCampoTexto(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JTextField campo) {

        JLabel etiqueta = crearEtiqueta(texto);

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

        JLabel etiqueta = crearEtiqueta(texto);

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
                new Color(
                        0,
                        84,
                        69
                )
        );

        return etiqueta;
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
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
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
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent evento) {

                if (boton.isEnabled()) {
                    boton.setBackground(
                            color.brighter()
                    );
                }
            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent evento) {

                boton.setBackground(color);
            }
        }
        );

        return boton;
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

        btnRegistrar
                = crearBotonColor(
                        "Registrar",
                        new Color(34, 165, 95)
                );

        btnActualizar
                = crearBotonColor(
                        "Actualizar",
                        new Color(55, 125, 210)
                );

        btnEliminar
                = crearBotonColor(
                        "Eliminar",
                        new Color(220, 70, 70)
                );

        btnLimpiar
                = crearBotonColor(
                        "Limpiar",
                        new Color(230, 145, 35)
                );

        btnRefrescar
                = crearBotonColor(
                        "Refrescar",
                        new Color(0, 121, 107)
                );

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);

        return panelBotones;
    }

    private JPanel crearPanelTabla() {

        PanelRedondeado panelTabla
                = new PanelRedondeado(
                        26,
                        Color.WHITE
                );

        panelTabla.setMostrarSombra(true);

        panelTabla.setLayout(
                new BorderLayout(
                        10,
                        12
                )
        );

        panelTabla.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        20,
                        20
                )
        );

        JLabel lblTituloTabla
                = new JLabel(
                        "Veterinarios registrados"
                );

        lblTituloTabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblTituloTabla.setForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        modeloTabla
                = new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Nombre",
                            "Especialidad",
                            "Teléfono",
                            "Correo"
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

        tablaVeterinarios
                = new JTable(modeloTabla);

        tablaVeterinarios.setRowHeight(36);

        tablaVeterinarios.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tablaVeterinarios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaVeterinarios.setAutoCreateRowSorter(true);

        tablaVeterinarios.setFillsViewportHeight(true);

        tablaVeterinarios.setBackground(Color.WHITE);

        tablaVeterinarios.setGridColor(
                new Color(
                        235,
                        235,
                        235
                )
        );

        tablaVeterinarios.setShowVerticalLines(false);
        tablaVeterinarios.setShowHorizontalLines(true);

        tablaVeterinarios.setSelectionBackground(
                new Color(
                        214,
                        245,
                        233
                )
        );

        tablaVeterinarios.setSelectionForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaVeterinarios.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        tablaVeterinarios.getTableHeader().setBackground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaVeterinarios.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaVeterinarios.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        tablaVeterinarios.getTableHeader()
                .setReorderingAllowed(false);

        tablaVeterinarios.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        tablaVeterinarios.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        tablaVeterinarios.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(190);

        tablaVeterinarios.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(140);

        tablaVeterinarios.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(250);

        JScrollPane scrollTabla
                = new JScrollPane(
                        tablaVeterinarios
                );

        scrollTabla.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                225,
                                222
                        )
                )
        );

        scrollTabla.getViewport()
                .setBackground(Color.WHITE);

        panelTabla.add(
                lblTituloTabla,
                BorderLayout.NORTH
        );

        panelTabla.add(
                scrollTabla,
                BorderLayout.CENTER
        );

        return panelTabla;
    }


    private JPanel crearPanelEstado() {

        JPanel panelEstado =
                new JPanel(
                        new BorderLayout()
                );

        panelEstado.setOpaque(false);

        lblEstado =
                new JLabel(
                        "Seleccione una opción para comenzar."
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

        lblEstado.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        panelEstado.add(
                lblEstado,
                BorderLayout.CENTER
        );

        return panelEstado;
    }


    private void configurarEventos() {

        btnRegistrar.addActionListener(
                evento -> registrarVeterinario()
        );

        btnActualizar.addActionListener(
                evento -> actualizarVeterinario()
        );

        btnEliminar.addActionListener(
                evento -> eliminarVeterinario()
        );

        btnLimpiar.addActionListener(
                evento -> limpiarFormulario()
        );

        btnRefrescar.addActionListener(
                evento -> {
                    cargarVeterinarios();
                    limpiarFormulario();
                }
        );

        tablaVeterinarios
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                seleccionarVeterinario();
                            }
                        }
                );
    }


    private void registrarVeterinario() {

        try {

            Veterinario veterinario =
                    obtenerVeterinarioFormulario(false);

            veterinarioServicio.registrar(
                    veterinario
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Veterinario registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mostrarEstado(
                    "Veterinario registrado correctamente.",
                    false
            );

            cargarVeterinarios();
            limpiarFormulario();

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo registrar el veterinario.",
                    ex
            );
        }
    }


    private void actualizarVeterinario() {

        if (idVeterinarioSeleccionado <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar un veterinario "
                            + "de la tabla."
            );

            return;
        }

        try {

            Veterinario veterinario =
                    obtenerVeterinarioFormulario(true);

            boolean actualizado =
                    veterinarioServicio.actualizar(
                            veterinario
                    );

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Veterinario actualizado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Veterinario actualizado correctamente.",
                        false
                );

                cargarVeterinarios();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró el veterinario "
                                + "que se desea actualizar."
                );
            }

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo actualizar el veterinario.",
                    ex
            );
        }
    }


    private void eliminarVeterinario() {

        if (idVeterinarioSeleccionado <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar un veterinario "
                            + "de la tabla."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar "
                                + "el veterinario seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta
                != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            boolean eliminado =
                    veterinarioServicio.eliminar(
                            idVeterinarioSeleccionado
                    );

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Veterinario eliminado correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Veterinario eliminado correctamente.",
                        false
                );

                cargarVeterinarios();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró el veterinario "
                                + "que se desea eliminar."
                );
            }

        } catch (
                SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se puede eliminar este veterinario "
                            + "porque tiene citas registradas.",
                    "Eliminación no permitida",
                    JOptionPane.WARNING_MESSAGE
            );

            mostrarEstado(
                    "El veterinario tiene citas relacionadas.",
                    true
            );

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

 
            if (esErrorLlaveForanea(ex)) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se puede eliminar este veterinario "
                                + "porque tiene citas registradas.",
                        "Eliminación no permitida",
                        JOptionPane.WARNING_MESSAGE
                );

                mostrarEstado(
                        "El veterinario tiene citas relacionadas.",
                        true
                );

            } else {

                mostrarErrorBaseDatos(
                        "No se pudo eliminar el veterinario.",
                        ex
                );
            }
        }
    }


    private Veterinario obtenerVeterinarioFormulario(
            boolean incluirId) {

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        String telefono =
                txtTelefono
                        .getText()
                        .trim();

        String email =
                txtEmail
                        .getText()
                        .trim();

        Especialidad especialidad =
                (Especialidad)
                        cmbEspecialidad
                                .getSelectedItem();

        if (incluirId) {

            return new Veterinario(
                    idVeterinarioSeleccionado,
                    nombre,
                    telefono,
                    email,
                    especialidad
            );
        }

        return new Veterinario(
                nombre,
                telefono,
                email,
                especialidad
        );
    }


    public final void cargarVeterinarios() {

        try {

            listaVeterinarios =
                    veterinarioServicio.listar();

            modeloTabla.setRowCount(0);

            for (Veterinario veterinario
                    : listaVeterinarios) {

                modeloTabla.addRow(
                        new Object[]{
                            veterinario.getId(),
                            veterinario.getNombre(),
                            veterinario.getEspecialidad(),
                            mostrarTexto(
                                    veterinario.getTelefono()
                            ),
                            mostrarTexto(
                                    veterinario.getEmail()
                            )
                        }
                );
            }

            mostrarEstado(
                    "Veterinarios cargados: "
                            + listaVeterinarios.size(),
                    false
            );

        } catch (SQLException ex) {

            modeloTabla.setRowCount(0);

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los veterinarios.",
                    ex
            );
        }
    }


    private void seleccionarVeterinario() {

        int filaVista =
                tablaVeterinarios
                        .getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        int filaModelo =
                tablaVeterinarios
                        .convertRowIndexToModel(
                                filaVista
                        );

        if (filaModelo < 0
                || filaModelo
                >= listaVeterinarios.size()) {

            return;
        }


        int idSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        Veterinario veterinario =
                buscarVeterinarioEnLista(
                        idSeleccionado
                );

        if (veterinario == null) {
            return;
        }

        idVeterinarioSeleccionado =
                veterinario.getId();

        txtId.setText(
                String.valueOf(
                        veterinario.getId()
                )
        );

        txtNombre.setText(
                veterinario.getNombre()
        );

        txtTelefono.setText(
                veterinario.getTelefono() == null
                        ? ""
                        : veterinario.getTelefono()
        );

        txtEmail.setText(
                veterinario.getEmail() == null
                        ? ""
                        : veterinario.getEmail()
        );

        cmbEspecialidad.setSelectedItem(
                veterinario.getEspecialidad()
        );

        btnRegistrar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);

        mostrarEstado(
                "Veterinario seleccionado: "
                        + veterinario.getNombre(),
                false
        );
    }


    private Veterinario buscarVeterinarioEnLista(
            int idVeterinario) {

        for (Veterinario veterinario
                : listaVeterinarios) {

            if (veterinario.getId()
                    == idVeterinario) {

                return veterinario;
            }
        }

        return null;
    }
    
    
    private void limpiarFormulario() {

        idVeterinarioSeleccionado = 0;

        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");

        cmbEspecialidad.setSelectedIndex(-1);

        tablaVeterinarios.clearSelection();

        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocus();

        mostrarEstado(
                "Formulario preparado para un nuevo registro.",
                false
        );
    }

    
    private String mostrarTexto(
            String texto) {

        if (texto == null
                || texto.isBlank()) {

            return "No registrado";
        }

        return texto;
    }


    private void mostrarAdvertencia(
            String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE
        );

        mostrarEstado(
                mensaje,
                true
        );
    }


    private void mostrarErrorBaseDatos(
            String mensaje,
            SQLException ex) {

        JOptionPane.showMessageDialog(
                this,
                mensaje
                        + "\n\nDetalle: "
                        + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );

        mostrarEstado(
                mensaje,
                true
        );

        ex.printStackTrace();
    }


    private boolean esErrorLlaveForanea(
            SQLException ex) {


        if (ex.getErrorCode() == 1451) {
            return true;
        }

        String mensaje =
                ex.getMessage();

        if (mensaje == null) {
            return false;
        }

        String mensajeMinuscula =
                mensaje.toLowerCase();

        return mensajeMinuscula.contains(
                "foreign key constraint"
        )
                || mensajeMinuscula.contains(
                        "cannot delete or update a parent row"
                );
    }


    private void mostrarEstado(
            String mensaje,
            boolean esError) {

        lblEstado.setText(mensaje);

        if (esError) {

            lblEstado.setForeground(
                    new Color(180, 45, 45)
            );

        } else {

            lblEstado.setForeground(
                    new Color(45, 115, 75)
            );
        }
    }
}
