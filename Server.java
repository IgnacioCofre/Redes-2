/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package server_part;
/**
 *
 * @author Ignacio Cofre
 */
import java.util.Timer;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable
{
    //initialize socket
    private Thread s;
    private Socket socket ;
    private ServerSocket server ;
    private DataInputStream in	;
    private static boolean server_alive = true;
    public static boolean mensaje_recibido = false;
    public boolean coordinador = false;
    public boolean inicio_llamadas = false; //si este server debe iniciar la coordinacion en la primera iteracion
    public int port;
    public int prioridad;
    public String ip = "localhost";
    public String name;
    public String messages;
    List <String> priori_port =new ArrayList<>(); //solo almacena los port con las prioridades mayores a la prioridad de este server
    int[] ports; //se guardan los ports de todas las demás maquinas
    public int tiempo_espera = 0; // si es 0 espera infinito

    // constructor with port and name
    public Server(int port, String name, int prioridad, boolean inicio_llamadas)
    {
        this.name = name;
        this.port = port;
        this.prioridad = prioridad;
        this.inicio_llamadas = inicio_llamadas;

    }

    @Override

    public void run(){
        // starts server and waits for a connection
        try{

            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
        }
        catch(IOException i){
            System.out.println("No se pudo iniciar el server");
        }

        while(server_alive){
            try{
                while(priori_port.size()<1){ //recibir todas las prioridades

                    socket = server.accept();

                    System.out.println("Client accepted");
                    // takes input from the client socket
                    in = new DataInputStream(socket.getInputStream());
                    String line;
                    line = (String)in.readUTF();
                    System.out.println(line);
                    this.messages= line;
                    System.out.println("Mensaje recibido");
                    String[] list_messages = messages.split(",");
                    String header = list_messages[0];
                    int prioridad_cliente = Integer.parseInt(list_messages[1]);
                    if ("prioridad".equals(header)){
                        if(prioridad_cliente >= this.prioridad){ //este if redundante es solo por precaucion
                            // solo se guardan las prioridades de las otras maquinas que sean mayores a lo de esta maquina
                            //[mensaje,prioridad,port]
                            this.priori_port.add(prioridad_cliente+","+list_messages[2]); //guarda la prioridad y el port del cliente
                        }
                    }
                    else{
                        System.out.print("Header irreconocible");
                    }
                }
                if (inicio_llamadas){ //inicia la coordinacion, se elije al primer bully , solo se ejecuta una vez
                        for (int i = 0; i< priori_port.size(); i++){
                            int port_envio = Integer.parseInt(priori_port.get(i).split("")[1]);

                            Client client1 = new Client(port_envio,"Client 1","coordinacion,"+Integer.toString(port)+","+Integer.toString(prioridad),ip);
                            client1.start();
                        }

                    this.inicio_llamadas = false;
                    System.out.println("Se realizo la cordinacion inicial del bully");
                }

                while(server_alive){ //manejar la coordinación
                    System.out.println("lista de prioridades"+priori_port);
                    socket.setSoTimeout(tiempo_espera); //tiempo de espera por una respuesta
                    socket = server.accept();
                    System.out.println("Client accepted");
                    // takes input from the client socket
                    in = new DataInputStream(socket.getInputStream());
                    String line;
                    line = (String)in.readUTF();
                    System.out.println(line);
                    this.messages= line;
                    System.out.println("Mensaje recibido");
                    String[] list_messages = messages.split(",");

                    if ("coordinacion".equals(list_messages[0]) ){
                        //[mensaje,prioridad,port]
                        //list_menssages = [header,prioridad,port]
                        int port_llegada = Integer.parseInt(list_messages[1]);
                        int prioridad_llegada = Integer.parseInt(list_messages[2]);
                        if(prioridad_llegada <= prioridad){
                            // se envia el ok si la prioridad del mensaje de llamada es menor que la de este server
                            Client client2 = new Client(port_llegada,"Client 2","ok,"+Integer.toString(prioridad),ip);
                            client2.start();
                            for (int i = 0; i< priori_port.size(); i++){
                                //cada ves que llega un mensaje de coordinacion, se otro  mensaje de
                                //coordinacion a los que tengan mayor prioridad que este server
                                int port_envio = Integer.parseInt(priori_port.get(i).split("")[1]);
                                Client client3 = new Client(port_envio,"Client 3","coordinacion,"+Integer.toString(port)+","+Integer.toString(prioridad),ip);
                                client3.start();
                            }
                        }
                        //setear un tiempo de
                        tiempo_espera = 8000;
                    }


                    if ("ok".equals(list_messages[0]) ){
                        this.coordinador = false;
                        this.tiempo_espera = 0;
                    }

                    else{
                        System.out.print("Header irreconocible");
                    }
                }

            }
            catch(SocketTimeoutException b){ //en caso de que no llegue ningun mensaje en el tiempo de espera
                System.out.println("Se escoje este servidor como coordinador");
                this.coordinador = true;
                this.tiempo_espera = 0;
            }
            catch(IOException i){
                System.out.println("Error al realizar la coneccion con el cliente");
            }
        }// termino de while  de servidor
        try{
            System.out.println("Closing connection");
            socket.close();
        }
        catch(IOException i){
            System.out.println("Error al cerrar el servidor");
        }
    }



    public void kill_server(){
        this.server_alive = false;
        System.out.println("El servidor con puerto "+port+" se cerro");
    }

    public void start(){
        System.out.println("Starting " +  name+" in port "+Integer.toString(port));
        if (s == null) {
            s = new Thread (this, name);
            //Integer.toString(port)
            s.start ();
        }
    }
}
