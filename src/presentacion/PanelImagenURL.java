/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author PC
 */
public class PanelImagenURL  extends JPanel{
    
    private final String direccionImagen;

    /**
     * Imagen descargada desde Internet.
     */
    private BufferedImage imagen;

    /**
     * Color opcional que se dibuja encima de la imagen.
     */
    private Color colorSuperposicion;

    /**
     * Nivel de transparencia de la superposición.
     *
     * 0 = completamente transparente.
     * 255 = completamente opaco.
     */
    private int transparencia;

    /**
     * Crea un panel que carga una imagen desde una URL.
     *
     * @param direccionImagen dirección completa de la imagen
     */
    public PanelImagenURL(String direccionImagen) {

        this.direccionImagen = direccionImagen;
        this.imagen = null;
        this.colorSuperposicion = null;
        this.transparencia = 0;

        setOpaque(true);

        /*
         * Este color se muestra mientras la imagen se descarga
         * o cuando no existe conexión a Internet.
         */
        setBackground(
                new Color(238, 233, 224)
        );

        cargarImagen();
    }

    /**
     * Descarga la imagen en segundo plano.
     */
    private void cargarImagen() {

        SwingWorker<BufferedImage, Void> trabajador =
                new SwingWorker<>() {

            @Override
            protected BufferedImage doInBackground()
                    throws Exception {

                URL url =
                        new URL(direccionImagen);

                HttpURLConnection conexion =
                        (HttpURLConnection)
                                url.openConnection();

                /*
                 * Evita que la aplicación quede esperando
                 * indefinidamente si la conexión falla.
                 */
                conexion.setConnectTimeout(8000);
                conexion.setReadTimeout(12000);

                /*
                 * Algunos servidores rechazan conexiones
                 * que no poseen un User-Agent.
                 */
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

                    BufferedImage imagenDescargada =
                            ImageIO.read(
                                    conexion.getInputStream()
                            );

                    if (imagenDescargada == null) {

                        throw new IllegalStateException(
                                "La dirección no contiene "
                                + "una imagen válida."
                        );
                    }

                    return imagenDescargada;

                } finally {

                    conexion.disconnect();
                }
            }

            @Override
            protected void done() {

                try {

                    imagen = get();

                } catch (Exception ex) {

                    imagen = null;

                    System.err.println(
                            "No se pudo cargar la imagen: "
                            + direccionImagen
                            + " | "
                            + ex.getMessage()
                    );
                }

                /*
                 * Actualiza el panel después de completar
                 * o fallar la descarga.
                 */
                revalidate();
                repaint();
            }
        };

        trabajador.execute();
    }

    /**
     * Permite volver a intentar cargar la imagen.
     */
    public void recargarImagen() {

        imagen = null;
        repaint();

        cargarImagen();
    }

    /**
     * Establece un color que se dibujará encima de la imagen.
     *
     * Es útil para oscurecer imágenes y colocar texto blanco.
     *
     * @param colorSuperposicion color de la capa superior
     */
    public void setColorSuperposicion(
            Color colorSuperposicion) {

        this.colorSuperposicion =
                colorSuperposicion;

        repaint();
    }

    /**
     * Configura la transparencia de la superposición.
     *
     * @param transparencia valor entre 0 y 255
     */
    public void setTransparencia(
            int transparencia) {

        this.transparencia =
                Math.max(
                        0,
                        Math.min(
                                255,
                                transparencia
                        )
                );

        repaint();
    }

    /**
     * Retorna la transparencia actual.
     *
     * @return valor entre 0 y 255
     */
    public int getTransparencia() {

        return transparencia;
    }

    /**
     * Indica si la imagen terminó de cargar.
     *
     * @return true si existe una imagen cargada
     */
    public boolean tieneImagen() {

        return imagen != null;
    }

    @Override
    protected void paintComponent(
            Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D g2 =
                (Graphics2D)
                        graphics.create();

        /*
         * Mejora la calidad al escalar las imágenes.
         */
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (imagen != null) {

            dibujarImagenCubriendoPanel(g2);
        }

        /*
         * Dibuja una capa de color sobre la imagen,
         * únicamente si fue configurada.
         */
        if (colorSuperposicion != null
                && transparencia > 0) {

            g2.setColor(
                    new Color(
                            colorSuperposicion.getRed(),
                            colorSuperposicion.getGreen(),
                            colorSuperposicion.getBlue(),
                            transparencia
                    )
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }

        g2.dispose();
    }

    /**
     * Escala la imagen para cubrir todo el panel sin deformarla.
     *
     * Si sobra contenido, se recorta desde el centro.
     */
    private void dibujarImagenCubriendoPanel(
            Graphics2D g2) {

        int anchoPanel =
                getWidth();

        int altoPanel =
                getHeight();

        int anchoImagen =
                imagen.getWidth();

        int altoImagen =
                imagen.getHeight();

        if (anchoPanel <= 0
                || altoPanel <= 0
                || anchoImagen <= 0
                || altoImagen <= 0) {

            return;
        }

        /*
         * Se utiliza la escala mayor para cubrir
         * completamente todo el panel.
         */
        double escala =
                Math.max(
                        (double) anchoPanel
                        / anchoImagen,

                        (double) altoPanel
                        / altoImagen
                );

        int anchoEscalado =
                (int) Math.ceil(
                        anchoImagen * escala
                );

        int altoEscalado =
                (int) Math.ceil(
                        altoImagen * escala
                );

        /*
         * Centra la imagen escalada.
         */
        int posicionX =
                (anchoPanel - anchoEscalado)
                / 2;

        int posicionY =
                (altoPanel - altoEscalado)
                / 2;

        g2.drawImage(
                imagen,
                posicionX,
                posicionY,
                anchoEscalado,
                altoEscalado,
                null
        );
    } 
    
}
