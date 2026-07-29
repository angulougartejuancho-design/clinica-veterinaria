/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import Negocio.ClienteServicio;
import modelo.Cliente;
import Exception.ValidationException;

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

    private int idClienteSeleccionado;

    public PanelClientes() {
        clienteServicio = new ClienteServicio();
        idClienteSeleccionado = 0;

        configurarPanel();
        crearComponentes();
        cargarClientes();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void crearComponentes() {
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new BorderLayout(10, 10));

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Información del cliente"
                )
        );

        JPanel panelCampos = new JPanel(new GridLayout(2, 4, 10, 10));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(new JLabel("Teléfono:"));
        panelCampos.add(new JLabel("Correo electrónico:"));
        panelCampos.add(new JLabel("Dirección:"));

        panelCampos.add(txtNombre);
        panelCampos.add(txtTelefono);
        panelCampos.add(txtEmail);
        panelCampos.add(txtDireccion);

        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(crearPanelBotones(), BorderLayout.SOUTH);

        return panelFormulario;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        btnGuardar.addActionListener(e -> guardarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

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
                    "Teléfono",
                    "Correo",
                    "Dirección"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);

        tablaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaClientes.getSelectionModel().addListSelectionListener(
                e -> {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarCliente();
                    }
                }
        );

        tablaClientes.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(40);

        tablaClientes.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(160);

        tablaClientes.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(100);

        tablaClientes.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        tablaClientes.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(220);

        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Clientes registrados"
                )
        );

        return scrollPane;
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

            List<Cliente> clientes =
                    clienteServicio.listar();

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

        } catch (SQLException ex) {
            mostrarErrorBaseDatos(
                    "No se pudieron cargar los clientes.",
                    ex
            );
        }
    }

    private void seleccionarCliente() {
        int filaSeleccionada =
                tablaClientes.getSelectedRow();

        if (filaSeleccionada < 0) {
            return;
        }

        idClienteSeleccionado =
                Integer.parseInt(
                        modeloTabla.getValueAt(
                                filaSeleccionada,
                                0
                        ).toString()
                );

        txtNombre.setText(
                obtenerValorTabla(filaSeleccionada, 1)
        );

        txtTelefono.setText(
                obtenerValorTabla(filaSeleccionada, 2)
        );

        txtEmail.setText(
                obtenerValorTabla(filaSeleccionada, 3)
        );

        txtDireccion.setText(
                obtenerValorTabla(filaSeleccionada, 4)
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
