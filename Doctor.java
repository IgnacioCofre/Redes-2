//package server_part;
import java.io.*;
import java.net.*;

public class Doctor {

    private long id;
    private String nombre;
    private String apellido;
    private long estudios;
    private long experiencia;
    private String tipo;

    public Doctor(long id, String nombre, String apellido, long estudios, long experiencia, String tipo){
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estudios = estudios;
        this.experiencia = experiencia;
        this.tipo = tipo;
    }

    public long getId(){
        return this.id;
    }
    public String getNombre(){
        return this.nombre;
    }    
    public String getApellido(){
        return this.apellido;
    } 
    public long getEstudios(){
        return this.estudios;
    }
    public long getExperiencia(){
        return this.experiencia;
    }
    public String getTipo(){
        return this.tipo;
    }
    public long getPrioridad(){
        return this.estudios+this.experiencia;
    }

}