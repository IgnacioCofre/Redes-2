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
        int port = 0;
        boolean inicio_llamadas = false;
        List <String> ports;
        String line;
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
        
       
        System.out.print("Ingrese el port de este server: \n");
        
        try{
            port = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        System.out.print("Ingrese la prioridad de este server: \n"); //esto despues debe ser automatico
        try{
            prioridad = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        scan.nextLine();

        System.out.print("Ingrese los ports de las demas maquinas separados por una coma:\n");
        line = scan.nextLine();
        ports = Arrays.asList(line.split(","));
        System.out.println(ports);

        System .out.print("Este servidor debe partir con la comunicacion? (Y/N) :");
        line = scan.nextLine();
        if("Y".equals(line) | "y".equals(line)){
            inicio_llamadas = true;
            System.out.println("Este servidor iniciara las llamadas");
        }

        String ip = "localhost";
        Server server1 = new Server(port,"server",prioridad,inicio_llamadas);
        server1.start();
        Thread.sleep(2000);
        System.out.println("Presione Start cuando esten todos los servidores incializados");
        String start = scan.nextLine();
        Thread.sleep(8000);
        //public Client(int port, String name,  int prioridad, String mensaje, String address)
        //Se manda un mensaje inicial a todas las maquinas
        for (int i = 0; i< ports.size(); i++){
            try{
                //message [port,"name client",message,ip]
                Client client1 = new Client(Integer.parseInt(ports.get(i)),"Client 1","prioridad,"+Integer.toString(prioridad)+","+Integer.toString(port),ip);
                client1.start();
            }
            catch(NumberFormatException a){
                System.out.println("Error al enviar un cliente al port "+ports.get(i));
                System.out.println(a);
            }
        }
    }
}
