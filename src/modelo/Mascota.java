/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa una mascota registrada en el sistema.
 *
 * @author angul
 */
public class Mascota {

    private int id;
    private String nombre;
    private Especie especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private Cliente cliente;

    public Mascota(
            int id,
            String nombre,
            Especie especie,
            String raza,
            LocalDate fechaNacimiento) {

        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(
            LocalDate fechaNacimiento) {

        this.fechaNacimiento = fechaNacimiento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return nombre + " (" + especie + ")";
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Mascota otraMascota)) {
            return false;
        }

      
        if (id > 0 && otraMascota.id > 0) {
            return id == otraMascota.id;
        }

        return Objects.equals(nombre, otraMascota.nombre)
                && especie == otraMascota.especie
                && Objects.equals(
                        fechaNacimiento,
                        otraMascota.fechaNacimiento
                );
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }

        return Objects.hash(
                nombre,
                especie,
                fechaNacimiento
        );
    }
}