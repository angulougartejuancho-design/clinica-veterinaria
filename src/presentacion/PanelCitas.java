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

    private static final DateTimeFormatter FORMATO_FECHA
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter FORMATO_HORA
            = DateTimeFormatter.ofPattern("HH:mm");

    public PanelCitas() {

        citaServicio = new CitaServicio();
        veterinarioServicio = new VeterinarioServicio();

        listaCitas = new ArrayList<>();
        idCitaSeleccionada = 0;

        inicializarComponentes();
        configurarEventos();
        cargarCombos();
        cargarCitas();
        limpiarFormulario();
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

        JPanel panel
                = new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JLabel lblTitulo
                = new JLabel(
                        "Gestión de Citas"
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
                        "Programe y administre las citas veterinarias."
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
                        320,
                        38
                )
        );

        campo.setCaretColor(
                new Color(
                        0,
                        84,
                        69
                )
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        215,
                                        212
                                )
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
                        320,
                        38
                )
        );

        combo.setBackground(
                Color.WHITE
        );

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        panel.add(
                etiqueta,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                combo,
                gbc
        );
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

        JLabel lblTitulo
                = new JLabel(
                        "Citas registradas"
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

        modeloTabla
                = new DefaultTableModel(
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

        tablaCitas
                = new JTable(modeloTabla);

        tablaCitas.setRowHeight(36);

        tablaCitas.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tablaCitas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaCitas.setAutoCreateRowSorter(true);
        tablaCitas.setFillsViewportHeight(true);

        tablaCitas.setBackground(Color.WHITE);

        tablaCitas.setGridColor(
                new Color(
                        235,
                        235,
                        235
                )
        );

        tablaCitas.setShowVerticalLines(false);
        tablaCitas.setShowHorizontalLines(true);

        tablaCitas.setSelectionBackground(
                new Color(
                        214,
                        245,
                        233
                )
        );

        tablaCitas.setSelectionForeground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaCitas.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tablaCitas.getTableHeader().setBackground(
                new Color(
                        0,
                        84,
                        69
                )
        );

        tablaCitas.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaCitas.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        tablaCitas.getTableHeader()
                .setReorderingAllowed(false);

        tablaCitas.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);

        tablaCitas.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(130);

        tablaCitas.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(180);

        tablaCitas.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        tablaCitas.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(80);

        tablaCitas.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(260);

        tablaCitas.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(110);

        JScrollPane scroll
                = new JScrollPane(
                        tablaCitas
                );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                225,
                                222
                        )
                )
        );

        scroll.getViewport()
                .setBackground(Color.WHITE);

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
                        290
                )
        );

        JLabel titulo
                = new JLabel(
                        "<html>"
                        + "<div style='font-size:24px; color:#00695C;'>"
                        + "<b>Programar Cita</b>"
                        + "</div>"
                        + "<br>"
                        + "<div style='font-size:12px; color:#666666;'>"
                        + "Seleccione la mascota, el veterinario"
                        + "<br>"
                        + "y la fecha de atención."
                        + "</div>"
                        + "</html>"
                );

        PanelImagenURL imagen
                = new PanelImagenURL(
                        "https://images.unsplash.com/"
                        + "photo-1576201836106-db1758fd1c97"
                        + "?auto=format&fit=crop&w=900&q=85"
                );

        imagen.setPreferredSize(
                new Dimension(
                        290,
                        210
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
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill
                = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(
                new Color(238, 241, 240)
        );

        cmbMascota = new JComboBox<>();
        cmbVeterinario = new JComboBox<>();

        cmbEstado
                = new JComboBox<>(
                        EstadoCita.values()
                );

        cmbEstado.setSelectedItem(
                EstadoCita.PROGRAMADA
        );

        txtFecha = new JTextField();

        txtFecha.setText(
                LocalDate.now()
                        .format(FORMATO_FECHA)
        );

        txtFecha.setToolTipText(
                "Formato: año-mes-día. Ejemplo: 2026-08-15"
        );

        txtHora = new JTextField("08:00");

        txtHora.setToolTipText(
                "Formato de 24 horas. Ejemplo: 14:30"
        );

        txtMotivo
                = new JTextArea(
                        3,
                        30
                );

        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);

        txtMotivo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

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

        agregarCampoCombo(
                panelCampos,
                gbc,
                3,
                "Estado",
                cmbEstado
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                4,
                "Fecha",
                txtFecha
        );

        agregarCampoTexto(
                panelCampos,
                gbc,
                5,
                "Hora",
                txtHora
        );

        JLabel lblMotivo
                = crearEtiqueta(
                        "Motivo"
                );

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor
                = GridBagConstraints.NORTH;

        panelCampos.add(
                lblMotivo,
                gbc
        );

        JScrollPane scrollMotivo
                = new JScrollPane(
                        txtMotivo
                );

        scrollMotivo.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                210,
                                215,
                                212
                        )
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill
                = GridBagConstraints.BOTH;

        panelCampos.add(
                scrollMotivo,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill
                = GridBagConstraints.HORIZONTAL;
        gbc.insets
                = new Insets(
                        16,
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
                        12
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
                        9,
                        14,
                        9,
                        14
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

        JPanel panel
                = new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.LEFT,
                                10,
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

        btnConfirmar
                = crearBotonColor(
                        "Confirmar",
                        new Color(96, 91, 190)
                );

        btnCancelar
                = crearBotonColor(
                        "Cancelar",
                        new Color(225, 135, 40)
                );

        btnCompletar
                = crearBotonColor(
                        "Completar",
                        new Color(0, 145, 120)
                );

        btnEliminar
                = crearBotonColor(
                        "Eliminar",
                        new Color(220, 70, 70)
                );

        btnLimpiar
                = crearBotonColor(
                        "Limpiar",
                        new Color(185, 135, 40)
                );

        btnRefrescar
                = crearBotonColor(
                        "Refrescar",
                        new Color(0, 110, 95)
                );

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
