/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package server_part;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Ignacio Cofre
 */
public class Server_part {
     /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     *
     */
    public static void main(String args[]) throws InterruptedException
    {
        Scanner scan = new Scanner(System.in);
        int prioridad = 0;
        boolean inicio_llamadas = false;
        List <String> ips = new ArrayList<String>();
        String ip = "";
        String line;
        JSONParser par = new JSONParser();
        ArrayList<Doctor> Doctores = new ArrayList<Doctor>();
        ArrayList<Doctor> Enfermeros = new ArrayList<Doctor>();
        ArrayList<Doctor> Paramedicos = new ArrayList<Doctor>();
        ArrayList<Paciente> Pacientes = new ArrayList<Paciente>();
        ArrayList<Requerimientos> Requirements = new ArrayList<Requerimientos>();
        try{
            Object cosa = par.parse(new FileReader("Doctores.json"));
            JSONObject jsonObject = (JSONObject) cosa;
            JSONArray docs = (JSONArray) jsonObject.get("Doctor");
            int i;
            String a = "Doctor";
            for(i = 0; i < docs.size(); i++){
                JSONObject doctor = (JSONObject) docs.get(i);
                Doctor theDoctor = new Doctor((Long)doctor.get("id"),(String)doctor.get("nombre"),(String)doctor.get("apellido"),(Long)doctor.get("estudios"),(Long)doctor.get("experiencia"),a);
                Doctores.add(theDoctor);
            }
            JSONArray enfs = (JSONArray) jsonObject.get("Enfermero");
            a = "Enfermero";
            for(i = 0; i < enfs.size(); i++){
                JSONObject enfermero = (JSONObject) enfs.get(i);
                Doctor elEnfermero = new Doctor((Long) enfermero.get("id"), (String) enfermero.get("nombre"), (String) enfermero.get("apellido"), (Long) enfermero.get("estudios"), (Long) enfermero.get("experiencia"), a);
                Enfermeros.add(elEnfermero);
            }
            JSONArray param = (JSONArray) jsonObject.get("Paramedico");
            a = "Paramedico";
            for (i = 0; i < param.size(); i++){
                JSONObject paramedico = (JSONObject) param.get(i);
                Doctor unParamedico = new Doctor((Long) paramedico.get("id"), (String) paramedico.get("nombre"), (String) paramedico.get("apellido"), (Long) paramedico.get("estudios"), (Long) paramedico.get("experiencia"), a);
                Paramedicos.add(unParamedico);
            }
            



            cosa = par.parse(new FileReader("Pacientes.json"));
            jsonObject = (JSONObject) cosa;
            JSONArray paci = (JSONArray) jsonObject.get("Paciente");
            for (i = 0; i < paci.size() ; i++){
                JSONObject paciente = (JSONObject) paci.get(i);
                JSONArray datos = (JSONArray) paciente.get("datos personales");
                JSONObject dato = (JSONObject) datos.get(0);
                JSONArray sick = (JSONArray) paciente.get("enfermedades");
                String [] enfermedades = new String[sick.size()];
                for(int j = 0; j< sick.size(); j++){
                    enfermedades[j] = (String) sick.get(j);
                }
                JSONArray tratamientos = (JSONArray) paciente.get("tratamientos/procedimientos");
                JSONObject asignados = (JSONObject) tratamientos.get(0);
                JSONArray asignado = (JSONArray) asignados.get("asignados");
                JSONObject completados = (JSONObject) tratamientos.get(1);
                JSONArray completado = (JSONArray) completados.get("completados");
                String [] trata_asignado = new String[asignado.size()];
                String [] trata_completado = new String[completado.size()];
                for(int j = 0 ; j < asignado.size() ; j++){
                    trata_asignado[j] = (String) asignado.get(j);
                }
                for(int j = 0; j < completado.size() ; j++){
                    trata_completado[j] = (String) completado.get(j);
                }


                JSONArray examenes = (JSONArray) paciente.get("examenes");
                JSONObject realizados = (JSONObject) examenes.get(0);
                JSONArray realizado = (JSONArray) realizados.get("realizados");
                JSONObject nRealizados = (JSONObject) examenes.get(1);
                JSONArray nRealizado = (JSONArray) nRealizados.get("no realizados");
                String [] examen_realizados = new String[realizado.size()];
                String [] examen_no_realizados = new String[nRealizado.size()];
                for(int j = 0; j < realizado.size() ; j++){
                    examen_realizados[j] = (String) realizado.get(j);
                }
                for(int j = 0; j < nRealizado.size(); j++){
                    examen_no_realizados[j] = (String) nRealizado.get(j);
                }
                
                JSONArray medicamentos = (JSONArray) paciente.get("medicamentos");
                JSONObject recetados = (JSONObject) medicamentos.get(0);
                JSONArray recetado = (JSONArray) recetados.get("recetados");
                JSONObject suministrados = (JSONObject) medicamentos.get(1);
                JSONArray suministrado = (JSONArray) suministrados.get("suministrados");
                String [] medicamentos_recetados = new String[recetado.size()];
                String [] medicamentos_suministrados = new String[suministrado.size()];
                for(int j = 0; j < recetado.size();j++){
                    medicamentos_recetados[j] = (String) recetado.get(j);
                }    
                for(int j = 0; j < suministrado.size();j++){
                    medicamentos_suministrados[j] = (String) suministrado.get(j);
                }

                Paciente elPaciente = new Paciente((Long) paciente.get("paciente_id"), (String) dato.get("nombre"), (String) dato.get("rut"), (Long) dato.get("edad"), enfermedades, trata_asignado, trata_completado, examen_realizados, examen_no_realizados, medicamentos_suministrados, medicamentos_recetados);
                Pacientes.add(elPaciente);
            }

            cosa = par.parse(new FileReader("Requerimientos.json"));
            jsonObject = (JSONObject) cosa;
            JSONArray requi = (JSONArray) jsonObject.get("requerimientos");
            for( i = 0 ; i < requi.size() ; i++){
                JSONObject requerimiento = (JSONObject) requi.get(i);
                JSONArray lista_pacientes = (JSONArray) requerimiento.get("pacientes");
                Map<String, String> requerim;
                requerim = new HashMap<>();
                for (int j = 0 ; j < lista_pacientes.size() ; j++){
                    JSONObject proce_paciente = (JSONObject) lista_pacientes.get(j);
                    requerim.put( proce_paciente.keySet().toArray()[0].toString() ,(String) proce_paciente.get(proce_paciente.keySet().toArray()[0]));
                }
                Requerimientos elRequerimiento = new Requerimientos( requerimiento.get("id").toString(), (String) requerimiento.get("cargo"), requerim);
                Requirements.add(elRequerimiento);
            }
            

        }
        catch(FileNotFoundException e){
            System.out.println("No se Encuentra el Archivo");
        // manejo de error no esta el archivo
        }
        catch(IOException e){
            System.out.println("El Json esta mal parseado");
        // manejo de error malo el json
        }
        catch(ParseException e){
            System.out.println("Mal Parseo UnU");
        // manejo de error  parseo malo
        }

        System .out.println("Este servidor debe partir con la comunicacion? (Y/N) :");
        line = scan.nextLine();
        if("Y".equals(line) | "y".equals(line)){
            inicio_llamadas = true;
            System.out.println("Este servidor iniciara las llamadas");
        }
        else{
            inicio_llamadas = false;
            System.out.println("Este servidor no iniciara las llamadas");}


        for(int i = 0 ; i < Doctores.size() ; i++){
            if(prioridad < Doctores.get(i).getPrioridad()){
                prioridad = (int) Doctores.get(i).getPrioridad();
            }
        }

        try{
          ip = InetAddress.getLocalHost().getHostAddress();//talves esto falle
        }
        catch( UnknownHostException r){
          System.out.println("Error al obtener la ip de esta maquina");
        }

        ips.add("10.6.40.141");
        ips.add("10.6.40.142");
        ips.add("10.6.40.143");
        ips.add("10.6.40.144");

        //public Server(int port, List ports_list, String name, int prioridad, boolean inicio_llamadas)
        Server server1 = new Server("server",ip,prioridad,inicio_llamadas,ips);
        server1.start();
        Thread.sleep(2000);
        System.out.println("Tiene 8 segundos para iniciar los demas servidores");
        System.out.println(prioridad);
        Thread.sleep(8000);

        //public Client(int port, String name,  int prioridad, String mensaje, String address)
        //Se manda un mensaje inicial a todas las maquinas
        for (int i = 0; i< ips.size(); i++){
            if(!ip.equals(ips.get(i))){
                try{
                    //public Client(String name, String mensaje, String address_inicio, String address_fin)
                    Client client1 = new Client("Client 1","prioridad,"+Integer.toString(prioridad)+","+ip,ip,ips.get(i));
                    client1.start();
                }
                catch(NumberFormatException a){
                    System.out.println("Error al enviar un cliente a la ip "+ips.get(i));
                    System.out.println(a);
                }
            }
        }
    }
}

