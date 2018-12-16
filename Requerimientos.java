import java.io.*;
import java.net.*;
import java.util.*;

public class Requerimientos {

    private String doc_id;
    private String cargo;
    private Map<String,String> pacientes;


    public Requerimientos(String doc_id, String cargo, Map<String,String> paciente){
        this.doc_id = doc_id;
        this.cargo = cargo;
        this.pacientes = paciente;

    }

    public String getId(){
        return this.doc_id;
    }

    public Map<String, String> proced(){
        return this.pacientes;
    }

}