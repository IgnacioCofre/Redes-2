//package server_part;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Paciente {

    private long id;
    private String nombre;
    private String rut;
    private long edad;
    private String [] enfermedades;
    private String [] tratamientos_a;
    private String [] tratamientos_c;
    private String [] examenes_re;
    private String [] examenes_no_re;
    private String [] medicamentos_suministrados;
    private String [] medicamentos_recetados;


    public Paciente(long id, String nombre, String rut, long edad, String [] enfermedades, String [] tratamientos_a, String [] tratamientos_c, String [] examenes_re, String [] examenes_no_re, String [] medicamentos_suministrados, String [] medicamentos_recetados ) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.edad = edad;
        this.enfermedades = enfermedades;
        this.tratamientos_a = tratamientos_a;
        this.tratamientos_c = tratamientos_c;
        this.examenes_re = examenes_re;
        this.examenes_no_re = examenes_no_re;
        this.medicamentos_suministrados = medicamentos_suministrados;
        this.medicamentos_recetados = medicamentos_recetados;

    }

    public long GetId() {
        return this.id;
    }

    public String GetNombre() {
        return this.nombre;
    } // hacer los getters UnU

    public String GetRut() {
        return this.rut;
    }

    public long GetEdad() {
        return this.edad;
    }

    public String [] GetEnfermedades() {
        return this.enfermedades;
    }

    public String [] GetTratamientosA() {
        return this.tratamientos_a;
    }
    
    public String [] GetTratamientosC() {
        return this.tratamientos_c;
    }
    
    public String [] GetExamenes() {
        return this.examenes_re;
    }

    public String [] GetExamenesNR() {
        return this.examenes_no_re;
    }
    
    public String [] GetMedicamentos() {
        return this.medicamentos_suministrados;
    }

    public String [] GetMedicamentos_recetados() {
        return this.medicamentos_recetados;
    }
    // hacer los getters UnU

}