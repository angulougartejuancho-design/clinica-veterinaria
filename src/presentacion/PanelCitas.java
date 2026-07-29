/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
import Datos.ConexionBD;
import Exception.CitaNoDisponibleException;
import Negocio.CitaServicio;
import Negocio.VeterinarioServicio;
import modelo.Cita;
import modelo.Especie;
import modelo.EstadoCita;
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
import java.time.LocalTime;
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
 * @author Daryelin
 */
public class PanelCitas  extends JPanel{
    private final CitaServicio citaServicio;
    private final VeterinarioServicio veterinarioServicio;

    private JTextField txtId;
    private JTextField txtFecha;
    private JTextField txtHora;

    private JComboBox<Mascota> cmbMascota;
    private JComboBox<Veterinario> cmbVeterinario;
    private JComboBox<EstadoCita> cmbEstado;

    private JTextArea txtMotivo;

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JButton btnCompletar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;

    private JLabel lblEstado;

    private List<Cita> listaCitas;
    private int idCitaSeleccionada;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    public PanelCitas() {

        citaServicio = new CitaServicio();
        veterinarioServicio = new VeterinarioServicio();

        listaCitas = new ArrayList<>();
        idCitaSeleccionada = 0;

        inicializarComponentes();
        configurarEventos();
        cargarCombos();
        cargarCitas();
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
                new Color(245, 247, 250)
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

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JLabel lblTitulo =
                new JLabel(
                        "Gestión de Citas"
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

        JLabel lblDescripcion =
                new JLabel(
                        "Programe y administre las citas veterinarias"
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


    private JPanel crearContenidoCentral() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(15, 15)
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

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        /*
         * ID.
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("ID:"),
                gbc
        );

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(
                new Color(235, 238, 242)
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(txtId, gbc);

        /*
         * Mascota.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Mascota:"),
                gbc
        );

        cmbMascota =
                new JComboBox<>();

        gbc.gridx = 3;
        gbc.weightx = 1;

        panel.add(cmbMascota, gbc);

        /*
         * Veterinario.
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Veterinario:"),
                gbc
        );

        cmbVeterinario =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(cmbVeterinario, gbc);

        /*
         * Estado.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Estado:"),
                gbc
        );

        cmbEstado =
                new JComboBox<>(
                        EstadoCita.values()
                );

        cmbEstado.setSelectedItem(
                EstadoCita.PROGRAMADA
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        panel.add(cmbEstado, gbc);

        /*
         * Fecha.
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Fecha:"),
                gbc
        );

        txtFecha =
                new JTextField();

        txtFecha.setToolTipText(
                "Formato: año-mes-día. Ejemplo: 2026-08-15"
        );

        txtFecha.setText(
                LocalDate.now()
                        .format(FORMATO_FECHA)
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(txtFecha, gbc);

        /*
         * Hora.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panel.add(
                crearEtiqueta("Hora:"),
                gbc
        );

        txtHora =
                new JTextField();

        txtHora.setToolTipText(
                "Formato de 24 horas. Ejemplo: 14:30"
        );

        txtHora.setText("08:00");

        gbc.gridx = 3;
        gbc.weightx = 1;

        panel.add(txtHora, gbc);

        /*
         * Motivo.
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.anchor =
                GridBagConstraints.NORTH;

        panel.add(
                crearEtiqueta("Motivo:"),
                gbc
        );

        txtMotivo =
                new JTextArea(3, 30);

        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);

        txtMotivo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scrollMotivo =
                new JScrollPane(txtMotivo);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill =
                GridBagConstraints.BOTH;

        panel.add(
                scrollMotivo,
                gbc
        );

        /*
         * Botones.
         */
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        panel.add(
                crearPanelBotones(),
                gbc
        );

        return panel;
    }


    private JLabel crearEtiqueta(
            String texto) {

        JLabel etiqueta =
                new JLabel(texto);

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


    private JPanel crearPanelBotones() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        btnRegistrar =
                new JButton("Registrar");

        btnActualizar =
                new JButton("Actualizar");

        btnConfirmar =
                new JButton("Confirmar");

        btnCancelar =
                new JButton("Cancelar");

        btnCompletar =
                new JButton("Completar");

        btnEliminar =
                new JButton("Eliminar");

        btnLimpiar =
                new JButton("Limpiar");

        btnRefrescar =
                new JButton("Refrescar");

        configurarBoton(btnRegistrar);
        configurarBoton(btnActualizar);
        configurarBoton(btnConfirmar);
        configurarBoton(btnCancelar);
        configurarBoton(btnCompletar);
        configurarBoton(btnEliminar);
        configurarBoton(btnLimpiar);
        configurarBoton(btnRefrescar);

        btnActualizar.setEnabled(false);
        btnConfirmar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnCompletar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnConfirmar);
        panel.add(btnCancelar);
        panel.add(btnCompletar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnRefrescar);

        return panel;
    }


    private void configurarBoton(
            JButton boton) {

        boton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        boton.setFocusPainted(false);

        boton.setPreferredSize(
                new Dimension(105, 34)
        );

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }


    private JPanel crearPanelTabla() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "Citas registradas"
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

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                            "ID",
                            "Mascota",
                            "Veterinario",
                            "Fecha",
                            "Hora",
                            "Motivo",
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

        tablaCitas =
                new JTable(modeloTabla);

        tablaCitas.setRowHeight(28);

        tablaCitas.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        tablaCitas
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        tablaCitas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaCitas.setAutoCreateRowSorter(true);
        tablaCitas.setFillsViewportHeight(true);

        JScrollPane scroll =
                new JScrollPane(tablaCitas);

        scroll.setPreferredSize(
                new Dimension(900, 250)
        );

        panel.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    /**
     * Crea el mensaje de estado inferior.
     */
    private JPanel crearPanelEstado() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

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

        panel.add(
                lblEstado,
                BorderLayout.CENTER
        );

        return panel;
    }

    /**
     * Configura los eventos.
     */
    private void configurarEventos() {

        btnRegistrar.addActionListener(
                evento -> registrarCita()
        );

        btnActualizar.addActionListener(
                evento -> actualizarCita()
        );

        btnConfirmar.addActionListener(
                evento -> confirmarCita()
        );

        btnCancelar.addActionListener(
                evento -> cancelarCita()
        );

        btnCompletar.addActionListener(
                evento -> completarCita()
        );

        btnEliminar.addActionListener(
                evento -> eliminarCita()
        );

        btnLimpiar.addActionListener(
                evento -> limpiarFormulario()
        );

        btnRefrescar.addActionListener(
                evento -> {
                    cargarCombos();
                    cargarCitas();
                    limpiarFormulario();
                }
        );

        tablaCitas
                .getSelectionModel()
                .addListSelectionListener(
                        evento -> {

                            if (!evento.getValueIsAdjusting()) {
                                seleccionarCita();
                            }
                        }
                );
    }


    private void registrarCita() {

        try {

            Cita cita =
                    obtenerCitaFormulario(false);

            citaServicio.registrar(cita);

            JOptionPane.showMessageDialog(
                    this,
                    "Cita registrada correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCitas();
            limpiarFormulario();

        } catch (
                IllegalArgumentException
                | DateTimeParseException ex) {

            mostrarAdvertencia(
                    ex instanceof DateTimeParseException
                            ? "La fecha o la hora tienen un formato incorrecto."
                            : ex.getMessage()
            );

        } catch (CitaNoDisponibleException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            if (esHorarioDuplicado(ex)) {

                mostrarAdvertencia(
                        "El veterinario ya tiene una cita "
                                + "en esa fecha y hora."
                );

            } else {

                mostrarErrorBaseDatos(
                        "No se pudo registrar la cita.",
                        ex
                );
            }
        }
    }


    private void actualizarCita() {

        if (idCitaSeleccionada <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar una cita."
            );

            return;
        }

        try {

            Cita cita =
                    obtenerCitaFormulario(true);

            boolean actualizada =
                    citaServicio.actualizar(cita);

            if (actualizada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cita actualizada correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCitas();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró la cita."
                );
            }

        } catch (
                IllegalArgumentException
                | DateTimeParseException ex) {

            mostrarAdvertencia(
                    ex instanceof DateTimeParseException
                            ? "La fecha o la hora tienen un formato incorrecto."
                            : ex.getMessage()
            );

        } catch (CitaNoDisponibleException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            if (esHorarioDuplicado(ex)) {

                mostrarAdvertencia(
                        "El veterinario ya tiene otra cita "
                                + "en esa fecha y hora."
                );

            } else {

                mostrarErrorBaseDatos(
                        "No se pudo actualizar la cita.",
                        ex
                );
            }
        }
    }


    private void confirmarCita() {

        cambiarEstadoCita(
                EstadoCita.CONFIRMADA,
                "Cita confirmada correctamente."
        );
    }


    private void cancelarCita() {

        cambiarEstadoCita(
                EstadoCita.CANCELADA,
                "Cita cancelada correctamente."
        );
    }


    private void completarCita() {

        cambiarEstadoCita(
                EstadoCita.COMPLETADA,
                "Cita completada correctamente."
        );
    }


    private void cambiarEstadoCita(
            EstadoCita nuevoEstado,
            String mensajeExito) {

        if (idCitaSeleccionada <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar una cita."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea cambiar la cita al estado "
                                + nuevoEstado
                                + "?",
                        "Confirmar cambio",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta
                != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            boolean actualizada =
                    citaServicio.cambiarEstado(
                            idCitaSeleccionada,
                            nuevoEstado
                    );

            if (actualizada) {

                JOptionPane.showMessageDialog(
                        this,
                        mensajeExito,
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCitas();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se pudo cambiar el estado."
                );
            }

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo cambiar el estado de la cita.",
                    ex
            );
        }
    }


    private void eliminarCita() {

        if (idCitaSeleccionada <= 0) {

            mostrarAdvertencia(
                    "Debe seleccionar una cita."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar la cita seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta
                != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            boolean eliminada =
                    citaServicio.eliminar(
                            idCitaSeleccionada
                    );

            if (eliminada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cita eliminada correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCitas();
                limpiarFormulario();

            } else {

                mostrarAdvertencia(
                        "No se encontró la cita."
                );
            }

        } catch (IllegalArgumentException ex) {

            mostrarAdvertencia(
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudo eliminar la cita.",
                    ex
            );
        }
    }


    private Cita obtenerCitaFormulario(
            boolean incluirId) {

        Mascota mascota =
                (Mascota)
                        cmbMascota
                                .getSelectedItem();

        Veterinario veterinario =
                (Veterinario)
                        cmbVeterinario
                                .getSelectedItem();

        EstadoCita estado =
                (EstadoCita)
                        cmbEstado
                                .getSelectedItem();

        LocalDate fecha =
                LocalDate.parse(
                        txtFecha
                                .getText()
                                .trim(),
                        FORMATO_FECHA
                );

        LocalTime hora =
                LocalTime.parse(
                        txtHora
                                .getText()
                                .trim(),
                        FORMATO_HORA
                );

        String motivo =
                txtMotivo
                        .getText()
                        .trim();

        if (incluirId) {

            return new Cita(
                    idCitaSeleccionada,
                    mascota,
                    veterinario,
                    fecha,
                    hora,
                    motivo,
                    estado
            );
        }

        return new Cita(
                mascota,
                veterinario,
                fecha,
                hora,
                motivo,
                estado
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

            for (Veterinario veterinario
                    : veterinarios) {

                cmbVeterinario.addItem(
                        veterinario
                );
            }

            cmbVeterinario.setSelectedIndex(-1);

        } catch (SQLException ex) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los veterinarios.",
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

                Date fechaSQL =
                        resultado.getDate(
                                "fecha_nacimiento"
                        );

                LocalDate fechaNacimiento =
                        fechaSQL == null
                                ? null
                                : fechaSQL.toLocalDate();

                Mascota mascota =
                        new Mascota(
                                resultado.getInt(
                                        "id_mascota"
                                ),
                                resultado.getString(
                                        "nombre"
                                ),
                                Especie.valueOf(
                                        resultado
                                                .getString("especie")
                                                .toUpperCase()
                                ),
                                resultado.getString(
                                        "raza"
                                ),
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


    public final void cargarCitas() {

        try {

            listaCitas =
                    citaServicio.listar();

            modeloTabla.setRowCount(0);

            for (Cita cita : listaCitas) {

                modeloTabla.addRow(
                        new Object[]{
                            cita.getId(),
                            cita.getMascota()
                                    .getNombre(),
                            cita.getVeterinario()
                                    .getNombre(),
                            cita.getFecha()
                                    .format(FORMATO_FECHA),
                            cita.getHora()
                                    .format(FORMATO_HORA),
                            cita.getMotivo(),
                            cita.getEstado()
                        }
                );
            }

            mostrarEstado(
                    "Citas cargadas: "
                            + listaCitas.size(),
                    false
            );

        } catch (SQLException ex) {

            modeloTabla.setRowCount(0);

            mostrarErrorBaseDatos(
                    "No se pudieron cargar las citas.",
                    ex
            );
        }
    }


    private void seleccionarCita() {

        int filaVista =
                tablaCitas.getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        int filaModelo =
                tablaCitas.convertRowIndexToModel(
                        filaVista
                );

        int id =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        filaModelo,
                                        0
                                )
                                .toString()
                );

        Cita cita =
                buscarCitaEnLista(id);

        if (cita == null) {
            return;
        }

        idCitaSeleccionada =
                cita.getId();

        txtId.setText(
                String.valueOf(
                        cita.getId()
                )
        );

        seleccionarMascota(
                cita.getMascota().getId()
        );

        seleccionarVeterinario(
                cita.getVeterinario().getId()
        );

        txtFecha.setText(
                cita.getFecha()
                        .format(FORMATO_FECHA)
        );

        txtHora.setText(
                cita.getHora()
                        .format(FORMATO_HORA)
        );

        txtMotivo.setText(
                cita.getMotivo()
        );

        cmbEstado.setSelectedItem(
                cita.getEstado()
        );

        btnRegistrar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);

        configurarBotonesEstado(
                cita.getEstado()
        );

        mostrarEstado(
                "Cita seleccionada: "
                        + cita.getId(),
                false
        );
    }


    private void configurarBotonesEstado(
            EstadoCita estado) {

        btnConfirmar.setEnabled(
                estado == EstadoCita.PROGRAMADA
        );

        btnCancelar.setEnabled(
                estado == EstadoCita.PROGRAMADA
                || estado == EstadoCita.CONFIRMADA
        );

        btnCompletar.setEnabled(
                estado == EstadoCita.CONFIRMADA
        );

        btnActualizar.setEnabled(
                estado != EstadoCita.CANCELADA
                && estado != EstadoCita.COMPLETADA
        );
    }


    private Cita buscarCitaEnLista(
            int idCita) {

        for (Cita cita : listaCitas) {

            if (cita.getId() == idCita) {
                return cita;
            }
        }

        return null;
    }

    
    private void seleccionarMascota(
            int idMascota) {

        for (int i = 0;
                i < cmbMascota.getItemCount();
                i++) {

            Mascota mascota =
                    cmbMascota.getItemAt(i);

            if (mascota.getId()
                    == idMascota) {

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

            if (veterinario.getId()
                    == idVeterinario) {

                cmbVeterinario.setSelectedIndex(i);
                return;
            }
        }
    }


    private void limpiarFormulario() {

        idCitaSeleccionada = 0;

        txtId.setText("");

        cmbMascota.setSelectedIndex(-1);
        cmbVeterinario.setSelectedIndex(-1);

        cmbEstado.setSelectedItem(
                EstadoCita.PROGRAMADA
        );

        txtFecha.setText(
                LocalDate.now()
                        .format(FORMATO_FECHA)
        );

        txtHora.setText("08:00");
        txtMotivo.setText("");

        tablaCitas.clearSelection();

        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnConfirmar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnCompletar.setEnabled(false);
        btnEliminar.setEnabled(false);

        mostrarEstado(
                "Formulario preparado para una nueva cita.",
                false
        );
    }


    private boolean esHorarioDuplicado(
            SQLException ex) {

        if (ex.getErrorCode() == 1062) {
            return true;
        }

        String mensaje =
                ex.getMessage();

        return mensaje != null
                && mensaje
                        .toLowerCase()
                        .contains(
                                "uq_veterinario_fecha_hora"
                        );
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
