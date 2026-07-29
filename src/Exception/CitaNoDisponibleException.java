/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exception;

/**
 *
 * @author Daryelin
 */
public class CitaNoDisponibleException extends Exception{
       public CitaNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    public CitaNoDisponibleException(
            String mensaje,
            Throwable causa) {

        super(mensaje, causa);
    }
}
