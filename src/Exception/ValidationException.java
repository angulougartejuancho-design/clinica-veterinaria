/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exception;

/**
 *
 * @author angul
 */
public class ValidationException extends Exception {

    public ValidationException(String mensaje) {
        super(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
