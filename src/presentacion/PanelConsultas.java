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

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        setBackground(new Color(245, 247, 250));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenidoCentral(), BorderLayout.CENTER);
        add(crearPanelEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo =
                new JLabel("Gestión de Consultas");

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        lblTitulo.setForeground(new Color(35, 55, 75));

        JLabel lblDescripcion = new JLabel(
                "Registre el diagnóstico, tratamiento y "
                        + "observaciones de cada mascota"
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

        /* ID */
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

        /* Mascota */
        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Mascota:"), gbc);

        cmbMascota = new JComboBox<>();

        gbc.gridx = 3;
        gbc.weightx = 1;
        panel.add(cmbMascota, gbc);

        /* Veterinario */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Veterinario:"), gbc);

        cmbVeterinario = new JComboBox<>();

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(cmbVeterinario, gbc);

        /* Fecha */
        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Fecha (yyyy-MM-dd):"), gbc);

        txtFecha = new JTextField();

        gbc.gridx = 3;
        gbc.weightx = 1;
        panel.add(txtFecha, gbc);

        /* Diagnóstico */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Diagnóstico:"), gbc);

        txtDiagnostico = new JTextArea(3, 20);
        txtDiagnostico.setLineWrap(true);
        txtDiagnostico.setWrapStyleWord(true);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(
                new JScrollPane(txtDiagnostico),
                gbc
        );

        /* Tratamiento */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Tratamiento:"), gbc);

        txtTratamiento = new JTextArea(3, 20);
        txtTratamiento.setLineWrap(true);
        txtTratamiento.setWrapStyleWord(true);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(
                new JScrollPane(txtTratamiento),
                gbc
        );

        /* Observaciones */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(crearEtiqueta("Observaciones:"), gbc);

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        panel.add(
                new JScrollPane(txtObservaciones),
                gbc
        );

        /* Botones */
        gbc.gridx = 0;
        gbc.gridy = 5;
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
                new JLabel("Consultas registradas");

        lblTituloTabla.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        lblTituloTabla.setForeground(
                new Color(35, 55, 75)
        );

        modeloTabla = new DefaultTableModel(
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

        tablaConsultas = new JTable(modeloTabla);
        tablaConsultas.setRowHeight(28);

        tablaConsultas.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        tablaConsultas
                .getTableHeader()
                .setFont(
                        new Font("Arial", Font.BOLD, 13)
                );

        tablaConsultas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaConsultas.setAutoCreateRowSorter(true);
        tablaConsultas.setFillsViewportHeight(true);

        tablaConsultas.setGridColor(
                new Color(225, 228, 232)
        );

        JScrollPane scrollTabla =
                new JScrollPane(tablaConsultas);

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

            Consulta consulta =
                    obtenerConsultaFormulario(false);

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

            Consulta consulta =
                    obtenerConsultaFormulario(true);

            boolean actualizado =
                    consultaServicio.actualizar(consulta);

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

        Mascota mascota =
                (Mascota) cmbMascota.getSelectedItem();

        Veterinario veterinario =
                (Veterinario)
                        cmbVeterinario.getSelectedItem();

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

        String diagnostico =
                txtDiagnostico.getText().trim();

        String tratamiento =
                txtTratamiento.getText().trim();

        String observaciones =
                txtObservaciones.getText().trim();

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
