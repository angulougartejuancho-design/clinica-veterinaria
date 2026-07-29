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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void crearComponentes() {
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelSuperior =
                new JPanel(new BorderLayout(10, 10));

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

    private JPanel crearPanelSeleccionCliente() {
        JPanel panelCliente =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelCliente.setBorder(
                BorderFactory.createTitledBorder(
                        "Cliente responsable"
                )
        );

        cmbClientes = new JComboBox<Cliente>();
        cmbClientes.setPreferredSize(
                new Dimension(300, 28)
        );

        btnRecargarClientes =
                new JButton("Recargar clientes");

        cmbClientes.addActionListener(e -> {
            limpiarFormulario();
            cargarMascotas();
        });

        btnRecargarClientes.addActionListener(e -> {
            cargarClientes();
            cargarMascotas();
        });

        panelCliente.add(new JLabel("Cliente:"));
        panelCliente.add(cmbClientes);
        panelCliente.add(btnRecargarClientes);

        return panelCliente;
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario =
                new JPanel(new BorderLayout(10, 10));

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Información de la mascota"
                )
        );

        JPanel panelCampos =
                new JPanel(new GridLayout(2, 4, 10, 10));

        txtNombre = new JTextField();

        cmbEspecie =
                new JComboBox<Especie>(Especie.values());

        txtRaza = new JTextField();
        txtFechaNacimiento = new JTextField();

        txtFechaNacimiento.setToolTipText(
                "Formato: año-mes-día. Ejemplo: 2024-05-20"
        );

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(new JLabel("Especie:"));
        panelCampos.add(new JLabel("Raza:"));
        panelCampos.add(
                new JLabel("Fecha de nacimiento:")
        );

        panelCampos.add(txtNombre);
        panelCampos.add(cmbEspecie);
        panelCampos.add(txtRaza);
        panelCampos.add(txtFechaNacimiento);

        panelFormulario.add(
                panelCampos,
                BorderLayout.CENTER
        );

        panelFormulario.add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );

        return panelFormulario;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

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
        modeloTabla = new DefaultTableModel(
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

        tablaMascotas = new JTable(modeloTabla);

        tablaMascotas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMascotas
                .getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarMascota();
                    }
                });

        tablaMascotas
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(40);

        tablaMascotas
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(150);

        tablaMascotas
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(100);

        tablaMascotas
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(150);

        tablaMascotas
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(150);

        JScrollPane scrollPane =
                new JScrollPane(tablaMascotas);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Mascotas del cliente seleccionado"
                )
        );

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