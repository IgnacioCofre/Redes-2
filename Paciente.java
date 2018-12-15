//package server_part;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Paciente {

    private int id;
    private String nombre;
    private String rut;
    private int edad;
    private ArrayList<String> enfermedades;
    private ArrayList<String> tratamientos_a;
    private ArrayList<String> tratamientos_c;
    private ArrayList<String> examenes_re;
    private ArrayList<String> examenes_no_re;
    private ArrayList<String> medicamentos_suministrados;
    private ArrayList<String> medicamentos_recetados;


    public Paciente(int id, String nombre, String rut, int edad, ArrayList<String> enfermedades, ) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = rut;
        this.enfermedades = enfermedades;
        this.tratamientos_a = tratamientos_a;
        this.tratamientos_c = tratamientos_c;
        this.examenes_re = examenes_re;
        this.examenes_no_re = examenes_no_re;
        this.medicamentos_suministrados = medicamentos_suministrados;
        this.medicamentos_recetados = medicamentos_recetados;

    }

    public int GetId() {
        return this.id;
    }

    public String GetNombre() {
        return this.nombre;
    } // hacer los getters UnU

    public String GetRut() {
        return this.rut;
    }

    public int GetEstudios() {
        return this.estudios;
    }

    public int GetEdad() {
        return this.edad;
    }

    public ArrayList<String> GetEnfermedades() {
        return this.enfermedades;
    }

    // hacer los getters UnU

}