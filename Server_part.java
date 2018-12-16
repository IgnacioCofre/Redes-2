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
        String line;
        String ip = "";

        JSONParser par = new JSONParser();
        try{
            Object cosa = par.parse(new FileReader("Doctores.json"));
            JSONObject jsonObject = (JSONObject) cosa;
            JSONArray docs = (JSONArray) jsonObject.get("Doctor");
            System.out.println("holi");

        }
        catch(FileNotFoundException e){
        // manejo de error no esta el archivo
        }
        catch(IOException e){
        // manejo de error malo el json
        }
        catch(ParseException e){
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
            System.out.println("Este servidor no iniciara las llamadas");


        System.out.println("Ingrese la prioridad de este server:"); //esto despues debe ser automatico
        try{
            prioridad = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
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
