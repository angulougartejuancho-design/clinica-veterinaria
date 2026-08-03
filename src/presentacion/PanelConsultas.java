/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Datos.ConexionBD;
import Exception.ValidationException;
import Negocio.ConsultaServicio;
import Negocio.VeterinarioServicio;
import modelo.Consulta;
import modelo.Especie;
import modelo.Mascota;
import modelo.Veterinario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anyel
 */
public class PanelConsultas extends JPanel {

    private final ConsultaServicio consultaServicio;
    private final VeterinarioServicio veterinarioServicio;

    private JTextField txtId;
    private JTextField txtFecha;

    private JComboBox<Mascota> cmbMascota;
    private JComboBox<Veterinario> cmbVeterinario;

    private JTextArea txtDiagnostico;
    private JTextArea txtTratamiento;
    private JTextArea txtObservaciones;

    private JTable tablaConsultas;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    private JLabel lblEstado;

    private List<Consulta> listaConsultas;
    private int idConsultaSeleccionada;

    private static final DateTimeFormatter FORMATO_FECHA
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PanelConsultas() {

        consultaServicio = new ConsultaServicio();
        veterinarioServicio = new VeterinarioServicio();

        listaConsultas = new ArrayList<>();
        idConsultaSeleccionada = 0;

        inicializarComponentes();
        configurarEventos();
        cargarCombos();
        cargarConsultas();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(15, 15));

        setBorder(new EmptyBorder(20, 20, 20, 20));

        setBackground(new Color(248, 245, 238));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenidoCentral(), BorderLayout.CENTER);
        add(crearPanelEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JLabel lblTitulo
                = new JLabel(
                        "Gestión de Consultas"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        lblTitulo.setForeground(
                new Color(0, 84, 69)
        );

        JLabel lblDescripcion
                = new JLabel(
                        "Registre diagnósticos, tratamientos y observaciones médicas."
                );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblDescripcion.setForeground(
                new Color(95, 105, 100)
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

    private JPanel crearContenidoCentral() {

        JPanel panel
                = new JPanel(
                        new BorderLayout(
                                18,
                                18
                        )
                );

        panel.setOpaque(false);

        panel.add(
                crearPanelFormulario(),
                BorderLayout.NORTH
        );

        panel.add(
                crearPanelTabla(),
                BorderLayout.CENTER
        );

        return panel;
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
                        300,
                        315
                )
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:24px; color:#00695C;'>"
                        + "<b>Consulta Médica</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:12px; color:#666666;'>"
                        + "Registre el diagnóstico, tratamiento"
                        + "<br>"
                        + "y seguimiento de cada mascota."
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
                        290,
                        225
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
                        7,
                        8,
                        7,
                        8
                );

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtId = new JTextField();
        txtId.setEditable(false);

        cmbMascota = new JComboBox<>();
        cmbVeterinario = new JComboBox<>();

        txtFecha = new JTextField(
                LocalDate.now()
                        .format(FORMATO_FECHA)
        );

        txtFecha.setToolTipText(
                "Formato: yyyy-MM-dd"
        );

        txtDiagnostico
                = crearAreaTexto();

        txtTratamiento
                = crearAreaTexto();

        txtObservaciones
                = crearAreaTexto();

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

        agregarCampoCombo(
                panelCampos,
                gbc,
                1,
                "Mascota",
                cmbMascota
        );

        agregarCampoCombo(
                panelCampos,
                gbc,
                2,
                "Veterinario",
                cmbVeterinario
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                3,
                "Fecha",
                txtFecha
        );

        agregarAreaTexto(
                panelCampos,
                gbc,
                4,
                "Diagnóstico",
                txtDiagnostico
        );

        agregarAreaTexto(
                panelCampos,
                gbc,
                5,
                "Tratamiento",
                txtTratamiento
        );

        agregarAreaTexto(
                panelCampos,
                gbc,
                6,
                "Observaciones",
                txtObservaciones
        );

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        gbc.insets
                = new Insets(
                        15,
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

    private JPanel crearPanelEstado() {

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        lblEstado
                = new JLabel(
                        "Seleccione una opción para comenzar."
                );

        lblEstado.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblEstado.setForeground(
                new Color(
                        80,
                        95,
                        110
                )
        );

        lblEstado.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        panel.add(
                lblEstado,
                BorderLayout.CENTER
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

    private JPanel crearPanelBotones() {

        JPanel panel
                = new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        panel.setOpaque(false);

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

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);

        return panel;
    }

    private JPanel crearPanelTabla() {

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

        JLabel lblTituloTabla
                = new JLabel(
                        "Consultas registradas"
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
                            "Mascota",
                            "Veterinario",
                            "Fecha",
                            "Diagnóstico"
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

        tablaConsultas
                = new JTable(modeloTabla);

        tablaConsultas.setRowHeight(36);

        tablaConsultas.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaConsultas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaConsultas.setAutoCreateRowSorter(true);
        tablaConsultas.setFillsViewportHeight(true);

        tablaConsultas.setBackground(Color.WHITE);

        tablaConsultas.setGridColor(
                new Color(
                        235,
                        235,
                        235
                )
        );

        tablaConsultas.setShowVerticalLines(false);
        tablaConsultas.setShowHorizontalLines(true);

        tablaConsultas.setSelectionBackground(
                new Color(
                        214,
                        245,
                        233
                )
        );

        tablaConsultas.setSelectionForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaConsultas.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tablaConsultas.getTableHeader().setBackground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaConsultas.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaConsultas.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        tablaConsultas.getTableHeader()
                .setReorderingAllowed(false);

        tablaConsultas.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        tablaConsultas.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(160);

        tablaConsultas.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(200);

        tablaConsultas.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(110);

        tablaConsultas.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(360);

        JScrollPane scrollTabla
                = new JScrollPane(
                        tablaConsultas
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

        panel.add(
                lblTituloTabla,
                BorderLayout.NORTH
        );

        panel.add(
                scrollTabla,
                BorderLayout.CENTER
        );

        return panel;
    }

    private void configurarEventos() {

        btnRegistrar.addActionListener(
                evento -> registrarConsulta()
        );

        btnActualizar.addActionListener(
                evento -> actualizarConsulta()
        );

        btnEliminar.addActionListener(
                evento -> eliminarConsulta()
        );

        btnLimpiar.addActionListener(
                evento -> limpiarFormulario()
        );

        btnRefrescar.addActionListener(
                evento -> {
                    cargarCombos();
                    cargarConsultas();
                    limpiarFormulario();
                }
        );

        tablaConsultas
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento
                                    .getValueIsAdjusting()) {

                                seleccionarConsulta();
                            }
                        }
                );
    }

    private void registrarConsulta() {

        try {

            Consulta consulta
                    = obtenerConsultaFormulario(false);

            consultaServicio.registrar(consulta);

            JOptionPane.showMessageDialog(
                    this,
                    "Consulta registrada correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mostrarEstado(
                    "Consulta registrada correctamente.",
                    false
            );

            cargarConsultas();
            limpiarFormulario();

        } catch (ValidationException
                | IllegalArgumentException ex) {

            mostrarAdvertencia(ex.getMessage());

        } catch (DateTimeParseException ex) {

            mostrarAdvertencia(
                    "La fecha debe tener el formato "
                    + "yyyy-MM-dd."
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo registrar la consulta.",
                    ex
            );
        }
    }

    private void actualizarConsulta() {

        if (idConsultaSeleccionada <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar una consulta "
                    + "de la tabla."
            );

            return;
        }

        try {

            Consulta consulta
                    = obtenerConsultaFormulario(true);

            boolean actualizado
                    = consultaServicio.actualizar(consulta);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consulta actualizada correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Consulta actualizada correctamente.",
                        false
                );

                cargarConsultas();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró la consulta "
                        + "que se desea actualizar."
                );
            }

        } catch (ValidationException
                | IllegalArgumentException ex) {

            mostrarAdvertencia(ex.getMessage());

        } catch (DateTimeParseException ex) {

            mostrarAdvertencia(
                    "La fecha debe tener el formato "
                    + "yyyy-MM-dd."
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo actualizar la consulta.",
                    ex
            );
        }
    }

    private void eliminarConsulta() {

        if (idConsultaSeleccionada <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar una consulta "
                    + "de la tabla."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la consulta "
                + "seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean eliminado = consultaServicio.eliminar(
                    idConsultaSeleccionada
            );

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consulta eliminada correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mostrarEstado(
                        "Consulta eliminada correctamente.",
                        false
                );

                cargarConsultas();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró la consulta "
                        + "que se desea eliminar."
                );
            }

        } catch (ValidationException ex) {

            mostrarAdvertencia(ex.getMessage());

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo eliminar la consulta.",
                    ex
            );
        }
    }

    private Consulta obtenerConsultaFormulario(
            boolean incluirId) {

        Mascota mascota
                = (Mascota) cmbMascota.getSelectedItem();

        Veterinario veterinario
                = (Veterinario) cmbVeterinario.getSelectedItem();

        if (mascota == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una mascota."
            );
        }

        if (veterinario == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un veterinario."
            );
        }

        LocalDate fecha = LocalDate.parse(
                txtFecha.getText().trim(),
                FORMATO_FECHA
        );

        String diagnostico
                = txtDiagnostico.getText().trim();

        String tratamiento
                = txtTratamiento.getText().trim();

        String observaciones
                = txtObservaciones.getText().trim();

        if (incluirId) {

            return new Consulta(
                    idConsultaSeleccionada,
                    mascota,
                    veterinario,
                    fecha,
                    diagnostico,
                    tratamiento,
                    observaciones
            );
        }

        return new Consulta(
                mascota,
                veterinario,
                fecha,
                diagnostico,
                tratamiento,
                observaciones
        );
    }

    public final void cargarCombos() {
        cargarMascotas();
        cargarVeterinarios();
    }

    private JTextArea crearAreaTexto() {

        JTextArea area
                = new JTextArea(
                        3,
                        25
                );

        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        area.setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        return area;
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
                new Dimension(
                        330,
                        38
                )
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
                                7,
                                11,
                                7,
                                11
                        )
                )
        );

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill
                = GridBagConstraints.HORIZONTAL;

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
                new Dimension(
                        330,
                        38
                )
        );

        combo.setBackground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(combo, gbc);
    }

    private void agregarAreaTexto(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JTextArea area) {

        JLabel etiqueta
                = crearEtiqueta(texto);

        JScrollPane scroll
                = new JScrollPane(area);

        scroll.setPreferredSize(
                new Dimension(
                        330,
                        70
                )
        );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 215, 212)
                )
        );

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor
                = GridBagConstraints.NORTH;
        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill
                = GridBagConstraints.BOTH;

        panel.add(scroll, gbc);
    }

    private void cargarVeterinarios() {

        try {

            List<Veterinario> veterinarios =
                    veterinarioServicio.listar();

            cmbVeterinario.removeAllItems();

            for (Veterinario veterinario : veterinarios) {
                cmbVeterinario.addItem(veterinario);
            }

            cmbVeterinario.setSelectedIndex(-1);

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los "
                            + "veterinarios.",
                    ex
            );
        }
    }

    private void cargarMascotas() {

        String sql =
                "SELECT "
                + "id_mascota, "
                + "nombre, "
                + "especie, "
                + "raza, "
                + "fecha_nacimiento "
                + "FROM mascotas "
                + "ORDER BY nombre ASC";

        cmbMascota.removeAllItems();

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Date fechaSQL = resultado.getDate(
                        "fecha_nacimiento"
                );

                LocalDate fechaNacimiento =
                        fechaSQL == null
                                ? null
                                : fechaSQL.toLocalDate();

                Mascota mascota = new Mascota(
                        resultado.getInt("id_mascota"),
                        resultado.getString("nombre"),
                        Especie.valueOf(
                                resultado
                                        .getString("especie")
                                        .toUpperCase()
                        ),
                        resultado.getString("raza"),
                        fechaNacimiento
                );

                cmbMascota.addItem(mascota);
            }

            cmbMascota.setSelectedIndex(-1);

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar las mascotas.",
                    ex
            );
        }
    }

    public final void cargarConsultas() {

        try {

            listaConsultas = consultaServicio.listar();

            modeloTabla.setRowCount(0);

            for (Consulta consulta : listaConsultas) {

                modeloTabla.addRow(
                        new Object[]{
                            consulta.getId(),
                            consulta.getMascota()
                                    .getNombre(),
                            consulta.getVeterinario()
                                    .getNombre(),
                            consulta.getFecha()
                                    .format(FORMATO_FECHA),
                            consulta.getDiagnostico()
                        }
                );
            }

            mostrarEstado(
                    "Consultas cargadas: "
                            + listaConsultas.size(),
                    false
            );

        } catch (SQLException ex) {

            modeloTabla.setRowCount(0);

            mostrarErrorBaseDatos(
                    "No se pudieron cargar las consultas.",
                    ex
            );
        }
    }

    private void seleccionarConsulta() {

        int filaVista =
                tablaConsultas.getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        int filaModelo =
                tablaConsultas.convertRowIndexToModel(
                        filaVista
                );

        int id = Integer.parseInt(
                modeloTabla
                        .getValueAt(filaModelo, 0)
                        .toString()
        );

        Consulta consulta = buscarConsultaEnLista(id);

        if (consulta == null) {
            return;
        }

        idConsultaSeleccionada = consulta.getId();

        txtId.setText(
                String.valueOf(consulta.getId())
        );

        seleccionarMascota(
                consulta.getMascota().getId()
        );

        seleccionarVeterinario(
                consulta.getVeterinario().getId()
        );

        txtFecha.setText(
                consulta.getFecha().format(FORMATO_FECHA)
        );

        txtDiagnostico.setText(
                consulta.getDiagnostico()
        );

        txtTratamiento.setText(
                consulta.getTratamiento()
        );

        txtObservaciones.setText(
                consulta.getObservaciones()
        );

        btnRegistrar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);

        mostrarEstado(
                "Consulta seleccionada: "
                        + consulta.getId(),
                false
        );
    }

    private Consulta buscarConsultaEnLista(int idConsulta) {

        for (Consulta consulta : listaConsultas) {

            if (consulta.getId() == idConsulta) {
                return consulta;
            }
        }

        return null;
    }

    private void seleccionarMascota(int idMascota) {

        for (int i = 0;
                i < cmbMascota.getItemCount();
                i++) {

            Mascota mascota = cmbMascota.getItemAt(i);

            if (mascota.getId() == idMascota) {
                cmbMascota.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarVeterinario(
            int idVeterinario) {

        for (int i = 0;
                i < cmbVeterinario.getItemCount();
                i++) {

            Veterinario veterinario =
                    cmbVeterinario.getItemAt(i);

            if (veterinario.getId() == idVeterinario) {
                cmbVeterinario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limpiarFormulario() {

        idConsultaSeleccionada = 0;

        txtId.setText("");

        cmbMascota.setSelectedIndex(-1);
        cmbVeterinario.setSelectedIndex(-1);

        txtFecha.setText(
                LocalDate.now().format(FORMATO_FECHA)
        );

        txtDiagnostico.setText("");
        txtTratamiento.setText("");
        txtObservaciones.setText("");

        tablaConsultas.clearSelection();

        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        mostrarEstado(
                "Formulario preparado para una "
                        + "nueva consulta.",
                false
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
