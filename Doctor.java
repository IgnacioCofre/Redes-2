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

    public int GetId(){
        return this.id;
    }
    public String GetNombre(){
        return this.nombre;
    }    //hacer los getters UnU 
    public String GetApellido(){
        return this.apellido;
    } 
    public int GetEstudios(){
        return this.estudios;
    }
    public int GetExperiencia(){
        return this.experiencia;
    }
    public String GetTipo(){
        return this.tipo;
    }

}