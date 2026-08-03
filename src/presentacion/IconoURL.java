/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;



import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 *
 * @author PC
 */
public class IconoURL extends JLabel{
   
    
    private final String direccionURL;
    private final int ancho;
    private final int alto;
    private final String textoAlternativo;

    /**
     * Constructor principal.
     *
     * @param direccionURL URL completa del icono
     * @param ancho ancho del icono
     * @param alto alto del icono
     * @param textoAlternativo texto mostrado si falla la imagen
     */
    public IconoURL(
            String direccionURL,
            int ancho,
            int alto,
            String textoAlternativo) {

        this.direccionURL = direccionURL;
        this.ancho = ancho;
        this.alto = alto;
        this.textoAlternativo = textoAlternativo;

        configurarEtiqueta();
        cargarIcono();
    }

    /**
     * Configura el tamaño y la alineación del JLabel.
     */
    private void configurarEtiqueta() {

        setPreferredSize(
                new Dimension(
                        ancho,
                        alto
                )
        );

        setMinimumSize(
                new Dimension(
                        ancho,
                        alto
                )
        );

        setMaximumSize(
                new Dimension(
                        ancho,
                        alto
                )
        );

        setHorizontalAlignment(
                SwingConstants.CENTER
        );

        setVerticalAlignment(
                SwingConstants.CENTER
        );

        setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        Math.max(
                                12,
                                Math.min(ancho, alto) / 2
                        )
                )
        );

        setText("");
    }

    /**
     * Descarga el icono en segundo plano.
     */
    private void cargarIcono() {

        SwingWorker<ImageIcon, Void> trabajador =
                new SwingWorker<>() {

            @Override
            protected ImageIcon doInBackground()
                    throws Exception {

                URL url =
                        new URL(direccionURL);

                HttpURLConnection conexion =
                        (HttpURLConnection)
                                url.openConnection();

                conexion.setConnectTimeout(7000);
                conexion.setReadTimeout(10000);

                conexion.setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 ClinicaVeterinaria"
                );

                conexion.setRequestProperty(
                        "Accept",
                        "image/*"
                );

                conexion.connect();

                try {

                    Image imagenOriginal =
                            ImageIO.read(
                                    conexion.getInputStream()
                            );

                    if (imagenOriginal == null) {

                        throw new IllegalStateException(
                                "La URL no contiene "
                                + "una imagen válida."
                        );
                    }

                    Image imagenEscalada =
                            imagenOriginal.getScaledInstance(
                                    ancho,
                                    alto,
                                    Image.SCALE_SMOOTH
                            );

                    return new ImageIcon(
                            imagenEscalada
                    );

                } finally {

                    conexion.disconnect();
                }
            }

            @Override
            protected void done() {

                try {

                    setIcon(get());
                    setText("");

                } catch (Exception ex) {

                    mostrarTextoAlternativo();

                    System.err.println(
                            "No se pudo cargar el icono: "
                            + direccionURL
                            + " | "
                            + ex.getMessage()
                    );
                }

                revalidate();
                repaint();
            }
        };

        trabajador.execute();
    }

    /**
     * Muestra el texto alternativo cuando el icono falla.
     */
    private void mostrarTextoAlternativo() {

        setIcon(null);

        if (textoAlternativo == null
                || textoAlternativo.isBlank()) {

            setText("•");

        } else {

            setText(textoAlternativo);
        }
    }

    /**
     * Permite volver a intentar descargar el icono.
     */
    public void recargarIcono() {

        setIcon(null);
        setText("");

        cargarIcono();
    }

    /**
     * Indica si el icono se cargó correctamente.
     *
     * @return true si el JLabel contiene un ImageIcon
     */
    public boolean tieneIcono() {

        return getIcon() != null;
    }
  
}
