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
    public boolean proceso_coordinacion = true;//indica si se esta realizando el proceso de eleccion de coordinador
    public boolean inicio_llamadas = false; //si este server debe iniciar la coordinacion en la primera iteracion
    public int port;
    public int prioridad;
    public int mensajes_port_priori = 0; //de ser inicializado con 0
    public String ip_jefe = "";
    public int numero_maquinas = 3;//se debe cambiar esto para probar con mas maquinas [Maquinas totales -1]
    public String ip;
    public String name;
    public String messages;
    //List <String> priori_port =new ArrayList<>(); //solo almacena los port con las prioridades mayores a la prioridad de este server
    List <String> priori_ip =new ArrayList<>();
    List <String> ips = new ArrayList<>(); //lista de todos los ports
    List <String> dead_server = new ArrayList<>(); //lista de servidores que ya no se encuentran funcionando
    public int tiempo_espera = 0; // si es 0 espera infinito
    public ArrayList<Doctor> Doctores = new ArrayList<Doctor>(); //lista de doctores
    public ArrayList<Doctor> Enfermeros = new ArrayList<Doctor>(); //lista de enfermeros
    public ArrayList<Doctor> Paramedicos = new ArrayList<Doctor>(); //lista de paramedicos
    public ArrayList<Paciente> Pacientes = new ArrayList<Paciente>(); //lista de pacientes
    public ArrayList<Requerimientos> Requirements = new ArrayList<Requerimientos>(); //lista de requerimientos
    private int iterante = 1;

    // constructor with port and name
    //new Server("server",ip,prioridad,inicio_llamadas);
    public Server(String name, String ip, int prioridad, boolean inicio_llamadas, List <String> ips, ArrayList<Doctor> Doctores, ArrayList<Doctor> Enfermeros, ArrayList<Doctor> Paramedicos, ArrayList<Paciente> Pacientes, ArrayList<Requerimientos> Requirements)
    {
        this.ip = ip;
        this.name = name;
        this.port = 6666;
        this.prioridad = prioridad;
        this.inicio_llamadas = inicio_llamadas;
        this.ips = ips;
        this.Doctores = Doctores; 
        this.Enfermeros = Enfermeros;
        this.Pacientes = Pacientes;
        this.Paramedicos = Paramedicos;
        this.Requirements = Requirements;
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

                    if ("prioridad".equals(header)){
                        int prioridad_cliente = Integer.parseInt(list_messages[1]);
                        this.mensajes_port_priori++; //cantidad de mensajes que recivo pon la prioridad de las otras maquinas
                        //se debe aclarar que no se escoje al coordinador mediante estas llamadas
                        if(prioridad_cliente > this.prioridad){ //este if redundante es solo por precaucion
                            // solo se guardan las prioridades de las otras maquinas que sean mayores a lo de esta maquina
                            //[mensaje,prioridad,ip]
                            this.priori_ip.add(prioridad_cliente+","+list_messages[2]); //guarda la prioridad y el port del cliente
                        }
                    }
                    else{
                        System.out.println("Header irreconocible: "+list_messages[0]);
                    }
                }
                if (inicio_llamadas & mensajes_port_priori == numero_maquinas){ //inicia la coordinacion, se elije al primer bully , solo se ejecuta una vez

                        try{
                          Thread.sleep(20000);
                        }catch(InterruptedException e){
                          System.out.println(e);
                        }
                        System.out.println("Se realiza el inicio de llamadas\n");
                        System.out.println("lista de prioridades: "+priori_ip+"\n");
                        for (int i = 0; i< priori_ip.size(); i++){
                            String ip_envio = priori_ip.get(i).split(",")[1];
                            //public Client(String name, String mensaje, String address_inicio, String address_fin)
                            //[coordinacion,ip,prioridad]
                            Client client1 = new Client("Client 2","coordinacion,"+ip+","+Integer.toString(prioridad),ip,ip_envio);
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
                        //[coordinacion,ip,prioridad]
                        //list_menssages = [coordinacion,ip,prioridad]
                        String ip_llegada = list_messages[1];
                        if (!ip_llegada.equals(ip)){ //se manda un mensaje de ok
                            System.out.println("ip llegada"+ip_llegada);
                            int prioridad_llegada = Integer.parseInt(list_messages[2]);
                            //public Client(String name, String mensaje, String address_inicio, String address_fin)

                            Client client2 = new Client("Client 2","ok,"+Integer.toString(prioridad),ip,ip_llegada);
                            client2.start();
                        }
                        if(!dead_server.contains(ip_jefe)){ //solo se agrega una vez el port del coordinador caido
                          dead_server.add(ip_jefe);
                          System.out.println("Se ha agregado un nuevo servidor muerto a la a lista");
                        }

                        for (int i = 0; i< priori_ip.size(); i++){
                            //cada ves que llega un mensaje de coordinacion, se otro  mensaje de
                            //coordinacion a los que tengan mayor prioridad que este server
                            if (!dead_server.contains(priori_ip.get(i))){ //no se le vuelven a enviar mensajes a los servidores ya caidos
                              String ip_envio = priori_ip.get(i).split(",")[1];
                              //public Client(String name, String mensaje, String address_inicio, String address_fin)
                              Client client3 = new Client("Client 3","coordinacion,"+ip+","+Integer.toString(prioridad),ip,ip_envio);
                              client3.start();
                            }
                        }
                        //setear un tiempo de
                        this.proceso_coordinacion = true;
                        tiempo_espera = 10000;
                    }
                    //"ok,"+Integer.toString(prioridad)
                    else if ("ok".equals(list_messages[0])){
                        this.coordinador = false;
                        this.tiempo_espera = 0;
                        System.out.println("no es coordinador\n");
                    }

                    else if("jefe".equals(list_messages[0])){
                        //["jefe,"+Integer.toString(prioridad)+","+Integer.toString(port)]
                        this.ip_jefe = list_messages[2];
                        this.proceso_coordinacion = false;
                        this.coordinador = false;
                        this.tiempo_espera = 18000;
                        System.out.println("La ip del servidor coordinador es: "+list_messages[2]);
                        // se envia el mensaje de los requerimientos al coordinador
                        //mensaje [requerimiento,]
                        String mensaje;
                        mensaje = Requirements.get(0).getMensaje();
                        Client client4 = new Client("Client 4", "requerimiento," + ip + "," + mensaje, ip, ip_jefe);
                        client4.start();
                        
                        

                    }
                    else if("actualizacion".equals(list_messages[0])){
                        //Se debe escribir en el archivo log
                        String [] mess = list_messages[2].split(":");
                        String quisitos = mess[2].replace("{", "");
                        String [] requi = quisitos.split(";");
                        for (int i = 0; i < requi.length ;i++){
                            System.out.println("Acceso del doctor " + mess[1] + "a la ficha del paciente "+ requi[i].split("=")[0] );
                        }

                        String ip_actualizada = list_messages[1];
                        if (ip_actualizada.equals(ip)){
                            if (iterante < Requirements.size()){
                                String mensaje;
                                mensaje = Requirements.get(iterante).getMensaje();
                                Client client5 = new Client("Client 5", "requerimiento," + ip + "," + mensaje, ip, this.ip_jefe);
                                iterante++;
                                client5.start();
                            }
                            
                        }
                    }


                    else if("requerimiento".equals(list_messages[0]) & coordinador){

                        
                        //realizar las colas!!!!! 

                        String ip_envio = list_messages[1];

                        for (int i = 0; i< ips.size(); i++){
                            if(!ip.equals(ips.get(i))){
                                try{
                                    String mensaje;
                                    mensaje = Requirements.get(iterante).getMensaje();
                                    // se envian los cambios a todas las maquinas
                                    Client client6 = new Client("Client 6","actualizacion,"+ip_envio+"," + mensaje,ip,ips.get(i));
                                    iterante++;
                                    client6.start();
                                }
                                catch(NumberFormatException a){
                                    System.out.println("Error al enviar un cliente a la ip "+ips.get(i));
                                    System.out.println(a);
                                }
                            }
                        }

                        //escribe en su archivo log
                    }

                    else {
                        System.out.print("Header irreconocible:"+list_messages[0]+"\n");
                    }
                }
            }
            catch(SocketTimeoutException b){ //en caso de que no llegue ningun mensaje en el tiempo de espera
                if (proceso_coordinacion){
                  System.out.println("Se escoje este servidor como coordinador");
                  this.coordinador = true;
                  this.tiempo_espera = 0;
                  this.ip_jefe = ip;
                  //se envian la confirmacion a todas las maquinas
                  for (int i = 0; i< ips.size(); i++){
                      try{
                          //message [jefe,prioridad,ip]
                          if (!dead_server.contains(ips.get(i)) & !ip.equals(ips.get(i))){ //se verifica que no se envia else {
                            //mensaje a un server muerto y a si mismo
                            //public Client(String name, String mensaje, String address_inicio, String address_fin)
                              Client client1 = new Client("Client 1","jefe,"+Integer.toString(prioridad)+","+ip,ip,ips.get(i));
                              client1.start();
                          }
                      }
                      catch(NumberFormatException a){
                          System.out.println("Error al enviar un cliente al port "+ips.get(i));
                          System.out.println(a);
                      }
                  }

                  this.proceso_coordinacion = false;
                }
                else{
                  System.out.println("Se realiza otra llamada al servidor coordinador");
                  //XAVI MEN TIENES QUE REENVIAR EL MENSAJE ACA!!!!
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
        System.out.println("Starting " +  name+" in port 6666");
        if (s == null) {
            s = new Thread (this, name);
            //Integer.toString(port)
            s.start ();
        }
    }
}
