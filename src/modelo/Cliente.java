/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author angul
 */
public class Cliente extends Persona {

    private String direccion;
    private final List<Mascota> mascotas;

    public Cliente(
            int id,
            String nombre,
            String telefono,
            String email,
            String direccion) {

        super(id, nombre, telefono, email);

        this.direccion = direccion;
        this.mascotas = new ArrayList<>();
    }

   
    public void agregarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException(
                    "La mascota no puede ser nula."
            );
        }

        if (!mascotas.contains(mascota)) {
            mascota.setCliente(this);
            mascotas.add(mascota);
        }
    }

   
    public boolean quitarMascota(Mascota mascota) {
        if (mascota == null) {
            return false;
        }

        boolean eliminada = mascotas.remove(mascota);

        if (eliminada && mascota.getCliente() == this) {
            mascota.setCliente(null);
        }

        return eliminada;
    }

  
    public Mascota buscarMascotaPorId(int idMascota) {
        for (Mascota mascota : mascotas) {
            if (mascota.getId() == idMascota) {
                return mascota;
            }
        }

        return null;
    }

   
    public List<Mascota> getMascotas() {
        return Collections.unmodifiableList(mascotas);
    }

    public int obtenerCantidadMascotas() {
        return mascotas.size();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}