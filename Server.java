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
    public boolean coordinador = false;//indica si este servidor es el coordinador
    public boolean inicio_llamadas = false; //si este server debe iniciar la coordinacion en la primera iteracion
    public int port;
    public int prioridad;
    public int mensajes_port_priori = 0;
    public int port_jefe = 0; //de ser inicializado con 0
    public int numero_maquinas = 3;//se debe cambiar esto para probar con mas maquinas [Maquinas totales -1]
    public String ip = "localhost";
    public String name;
    public String messages;
    List <String> priori_port =new ArrayList<>(); //solo almacena los port con las prioridades mayores a la prioridad de este server
    List <String> ports_list = new ArrayList<>(); //lista de todos los ports
    List <String> dead_server = new ArrayList<>(); //lista de servidores que ya no se encuentran funcionando
    public int tiempo_espera = 0; // si es 0 espera infinito

    // constructor with port and name
    public Server(int port, List <String> ports_list, String name, int prioridad, boolean inicio_llamadas)
    {
        this.name = name;
        this.port = port;
        this.prioridad = prioridad;
        this.inicio_llamadas = inicio_llamadas;
        this.ports_list = ports_list;
    }

    @Override

    public void run(){
        // starts server and waits for a connection
        try{

            server = new ServerSocket(port);

            System.out.println("Servidor Iniciado");

            System.out.println("Esperando por un cliente...");
        }
        catch(IOException i){
            System.out.println("No se pudo iniciar el server");
        }

        while(server_alive){
            try{
                while(mensajes_port_priori<numero_maquinas){ //recibir todas las prioridades
                    socket = server.accept();

                    System.out.println("Cliente aceptado");
                    // takes input from the client socket
                    in = new DataInputStream(socket.getInputStream());
                    String line;
                    line = (String)in.readUTF();
                    this.messages= line;
                    System.out.println("Mensaje recibido");
                    String[] list_messages = messages.split(",");
                    String header = list_messages[0];
                    int prioridad_cliente = Integer.parseInt(list_messages[1]);
                    if ("prioridad".equals(header)){
                        this.mensajes_port_priori++; //cantidad de mensajes que recibo pon la prioridad de las otras maquinas
                        //se debe aclarar que no se escoje al coordinador mediante estas llamadas
                        if(prioridad_cliente >= this.prioridad){ //este if redundante es solo por precaucion
                            // solo se guardan las prioridades de las otras maquinas que sean mayores a lo de esta maquina
                            //[mensaje,prioridad,port]
                            this.priori_port.add(prioridad_cliente+","+list_messages[2]); //guarda la prioridad y el port del cliente
                        }
                    }
                    else{
                        System.out.print("Header irreconocible: "+list_messages[0]);
                    }
                }
                if (inicio_llamadas & mensajes_port_priori == numero_maquinas){ //inicia la coordinacion, se elije al primer bully , solo se ejecuta una vez

                        try{
                          Thread.sleep(20000);
                        }catch(InterruptedException e){
                          System.out.println(e);
                        }
                        System.out.println("Se realiza el inicio de llamadas\n");
                        System.out.println("lista de prioridades: "+priori_port+"\n");
                        for (int i = 0; i< priori_port.size(); i++){
                            int port_envio = Integer.parseInt(priori_port.get(i).split(",")[1]);
                            //[coordinacion,port,prioridad]
                            Client client1 = new Client(port_envio,port,"Client 2","coordinacion,"+Integer.toString(port)+","+Integer.toString(prioridad),ip);
                            client1.start();
                        }

                    this.inicio_llamadas = false;
                    System.out.println("Se realizo la cordinacion inicial del bully");
                }

                while(server_alive & mensajes_port_priori == numero_maquinas){ //manejar la coordinación
                    System.out.println("while2");
                    server.setSoTimeout(tiempo_espera); //tiempo de espera por una respuesta
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
                        if (port_llegada != port){ //se manda un mensaje de ok
                            System.out.println("port llegada"+port_llegada);
                            System.out.println("port server"+port);
                            int prioridad_llegada = Integer.parseInt(list_messages[2]);
                            Client client2 = new Client(port_llegada,port,"Client 2","ok,"+Integer.toString(prioridad),ip);
                            client2.start();
                        }
                        if(!dead_server.contains(port_jefe)){ //solo se agrega una vez el port del coordinador caido
                          dead_server.add(Integer.toString(port_jefe));
                          System.out.println("Se ha agregado un nuevo servidor muerto a la a lista");
                        }

                        for (int i = 0; i< priori_port.size(); i++){
                            //cada ves que llega un mensaje de coordinacion, se otro  mensaje de
                            //coordinacion a los que tengan mayor prioridad que este server
                            if (!dead_server.contains(priori_port.get(i))){ //no se le vuelven a enviar mensajes a los servidores ya caidos
                              int port_envio = Integer.parseInt(priori_port.get(i).split(",")[1]);
                              Client client3 = new Client(port_envio,port,"Client 3","coordinacion,"+Integer.toString(port)+","+Integer.toString(prioridad),ip);
                              client3.start();
                            }
                        }
                        //setear un tiempo de
                        tiempo_espera = 8000;
                    }

                    else if ("ok".equals(list_messages[0])){
                        this.coordinador = false;
                        this.tiempo_espera = 0;
                        System.out.println("no es coordinador\n");
                    }

                    else if("jefe".equals(list_messages[0])){
                        //["jefe,"+Integer.toString(prioridad)+","+Integer.toString(port)]
                        this.port_jefe = Integer.parseInt(list_messages[2]);
                        System.out.println("EL port del servidor coordinador es: "+list_messages[2]);
                        //poner una condicion que permita saber que ya hay coordinacion
                    }
                    else {
                        System.out.print("Header irreconocible:"+list_messages[0]+"\n");
                    }
                }
            }
            catch(SocketTimeoutException b){ //en caso de que no llegue ningun mensaje en el tiempo de espera
                System.out.println("Se escoje este servidor como coordinador");
                this.coordinador = true;
                this.tiempo_espera = 0;
                this.port_jefe = port;
                //se envian la confirmacion a todas las maquinas
                for (int i = 0; i< ports_list.size(); i++){
                    try{
                        //message [port,"name client",message,ip]
                        if (!dead_server.contains(ports_list.get(i)) & !Integer.toString(port).equals(ports_list.get(i))){ //se verifica que no se envia else {
                          //mensaje a un server muerto y a si mismo
                            Client client1 = new Client(Integer.parseInt(ports_list.get(i)),port,"Client 1","jefe,"+Integer.toString(prioridad)+","+Integer.toString(port),ip);
                            client1.start();
                        }
                    }
                    catch(NumberFormatException a){
                        System.out.println("Error al enviar un cliente al port "+ports_list.get(i));
                        System.out.println(a);
                    }
                }

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
        Server.server_alive = false;
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
