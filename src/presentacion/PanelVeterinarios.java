/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
import Negocio.VeterinarioServicio;
import modelo.Especialidad;
import modelo.Veterinario;

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
public class PanelVeterinarios  extends JPanel{
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

    /*
     * Lista utilizada para relacionar las filas de la tabla
     * con los objetos Veterinario.
     */
    private List<Veterinario> listaVeterinarios;

    /*
     * Guarda el ID del veterinario seleccionado.
     * El valor 0 indica que no existe selección.
     */
    private int idVeterinarioSeleccionado;

    /**
     * Constructor principal.
     */
    public PanelVeterinarios() {

        veterinarioServicio =
                new VeterinarioServicio();

        listaVeterinarios =
                new ArrayList<>();

        idVeterinarioSeleccionado = 0;

        inicializarComponentes();
        configurarEventos();
        cargarVeterinarios();
    }

    /**
     * Inicializa todos los componentes visuales.
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
                crearContenidoCentral(),
                BorderLayout.CENTER
        );

        add(
                crearPanelEstado(),
                BorderLayout.SOUTH
        );
    }

    /**
     * Crea el encabezado del módulo.
     */
    private JPanel crearEncabezado() {

        JPanel panelEncabezado =
                new JPanel(
                        new BorderLayout()
                );

        panelEncabezado.setOpaque(false);

        JLabel lblTitulo =
                new JLabel(
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

        JLabel lblDescripcion =
                new JLabel(
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

    /**
     * Crea el contenido central del panel.
     */
    private JPanel crearContenidoCentral() {

        JPanel panelContenido =
                new JPanel(
                        new BorderLayout(15, 15)
                );

        panelContenido.setOpaque(false);

        panelContenido.add(
                crearPanelFormulario(),
                BorderLayout.NORTH
        );

        panelContenido.add(
                crearPanelTabla(),
                BorderLayout.CENTER
        );

        return panelContenido;
    }

    /**
     * Crea el formulario para registrar o actualizar
     * veterinarios.
     */
    private JPanel crearPanelFormulario() {

        JPanel panelFormulario =
                new JPanel(
                        new GridBagLayout()
                );

        panelFormulario.setBackground(
                Color.WHITE
        );

        panelFormulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 220, 225)
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        7,
                        7,
                        7,
                        7
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        /*
         * Campo ID.
         */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        panelFormulario.add(
                crearEtiqueta("ID:"),
                gbc
        );

        txtId =
                new JTextField();

        txtId.setEditable(false);

        txtId.setBackground(
                new Color(235, 238, 242)
        );

        txtId.setPreferredSize(
                new Dimension(180, 32)
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtId,
                gbc
        );

        /*
         * Campo nombre.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                crearEtiqueta("Nombre:"),
                gbc
        );

        txtNombre =
                new JTextField();

        txtNombre.setPreferredSize(
                new Dimension(240, 32)
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtNombre,
                gbc
        );

        /*
         * Especialidad.
         */
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                crearEtiqueta("Especialidad:"),
                gbc
        );

        cmbEspecialidad =
                new JComboBox<>(
                        Especialidad.values()
                );

        cmbEspecialidad.setPreferredSize(
                new Dimension(180, 32)
        );

        cmbEspecialidad.setSelectedIndex(-1);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbEspecialidad,
                gbc
        );

        /*
         * Campo teléfono.
         */
        gbc.gridx = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                crearEtiqueta("Teléfono:"),
                gbc
        );

        txtTelefono =
                new JTextField();

        txtTelefono.setPreferredSize(
                new Dimension(240, 32)
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtTelefono,
                gbc
        );

        /*
         * Campo correo.
         */
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                crearEtiqueta("Correo:"),
                gbc
        );

        txtEmail =
                new JTextField();

        txtEmail.setPreferredSize(
                new Dimension(180, 32)
        );

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;

        panelFormulario.add(
                txtEmail,
                gbc
        );

        /*
         * Botones.
         */
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        panelFormulario.add(
                crearPanelBotones(),
                gbc
        );

        return panelFormulario;
    }

    /**
     * Crea las etiquetas del formulario.
     */
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

    /**
     * Crea el panel de botones.
     */
    private JPanel crearPanelBotones() {

        JPanel panelBotones =
                new JPanel();

        panelBotones.setOpaque(false);

        btnRegistrar =
                new JButton("Registrar");

        btnActualizar =
                new JButton("Actualizar");

        btnEliminar =
                new JButton("Eliminar");

        btnLimpiar =
                new JButton("Limpiar");

        btnRefrescar =
                new JButton("Refrescar");

        configurarBoton(btnRegistrar);
        configurarBoton(btnActualizar);
        configurarBoton(btnEliminar);
        configurarBoton(btnLimpiar);
        configurarBoton(btnRefrescar);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);

        return panelBotones;
    }

    /**
     * Aplica una configuración común a los botones.
     */
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
                new Dimension(125, 36)
        );

        boton.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );
    }

    /**
     * Crea el panel que contiene la tabla.
     */
    private JPanel crearPanelTabla() {

        JPanel panelTabla =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelTabla.setBackground(
                Color.WHITE
        );

        panelTabla.setBorder(
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

        JLabel lblTituloTabla =
                new JLabel(
                        "Veterinarios registrados"
                );

        lblTituloTabla.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        lblTituloTabla.setForeground(
                new Color(35, 55, 75)
        );

        modeloTabla =
                new DefaultTableModel(
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

        tablaVeterinarios =
                new JTable(modeloTabla);

        tablaVeterinarios.setRowHeight(28);

        tablaVeterinarios.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        tablaVeterinarios
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        tablaVeterinarios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaVeterinarios.setAutoCreateRowSorter(
                true
        );

        tablaVeterinarios.setFillsViewportHeight(
                true
        );

        tablaVeterinarios.setGridColor(
                new Color(225, 228, 232)
        );

        JScrollPane scrollTabla =
                new JScrollPane(
                        tablaVeterinarios
                );

        scrollTabla.setPreferredSize(
                new Dimension(800, 300)
        );

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

    /**
     * Crea el mensaje inferior del panel.
     */
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

    /**
     * Configura todos los eventos del panel.
     */
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

    /**
     * Registra un veterinario nuevo.
     */
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

    /**
     * Actualiza el veterinario seleccionado.
     */
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

    /**
     * Elimina el veterinario seleccionado.
     */
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

            /*
             * Algunos controladores MySQL entregan una SQLException
             * normal aunque el error sea de llave foránea.
             */
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

    /**
     * Obtiene los datos ingresados en el formulario.
     */
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

    /**
     * Carga todos los veterinarios desde la base
     * de datos y los muestra en la tabla.
     *
     * Es público para permitir actualizar la tabla
     * desde MainFrame u otros paneles.
     */
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

    /**
     * Coloca los datos de la fila seleccionada
     * dentro del formulario.
     */
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

        /*
         * Debido al ordenamiento de JTable, buscamos
         * el veterinario mediante el ID mostrado.
         */
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

    /**
     * Busca un veterinario dentro de la lista cargada.
     */
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

    /**
     * Limpia los campos del formulario.
     */
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

    /**
     * Muestra un valor vacío de forma legible.
     */
    private String mostrarTexto(
            String texto) {

        if (texto == null
                || texto.isBlank()) {

            return "No registrado";
        }

        return texto;
    }

    /**
     * Muestra un mensaje de validación.
     */
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

    /**
     * Muestra un error relacionado con JDBC.
     */
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

    /**
     * Comprueba si una excepción fue provocada por una
     * restricción de llave foránea.
     */
    private boolean esErrorLlaveForanea(
            SQLException ex) {

        /*
         * Código MySQL 1451:
         * Cannot delete or update a parent row.
         */
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

    /**
     * Actualiza el mensaje inferior del panel.
     */
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
