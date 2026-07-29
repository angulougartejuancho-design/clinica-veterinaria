/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private final JTabbedPane pestañas;

    public MainFrame() {
        setTitle("Sistema de Clínica Veterinaria");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pestañas = new JTabbedPane();

        pestañas.addTab("Clientes", new PanelClientes());
        pestañas.addTab("Mascotas", new PanelMascotas());

        add(pestañas);
    }
}