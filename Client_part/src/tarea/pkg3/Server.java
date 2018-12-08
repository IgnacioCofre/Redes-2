/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea.pkg3;
/**
 *
 * @author Ignacio Cofre
 */
// A Java program for a Server 
import java.net.*; 
import java.io.*; 
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//server 1

public class Server implements Runnable
{ 
    //initialize socket 
    private Thread s;    
    private Socket socket = null; 
    private ServerSocket server = null; 
    private DataInputStream in	 = null; 
    private boolean server_alive = true;
    public boolean mensaje_recibido = false;
    public boolean coordinador = true;
    public int port;
    public int prioridad;
    public String name;
    public String messages;  
    List <String> priori_port =new ArrayList<>();

    // constructor with port and name 
    public Server(int port, String name, int prioridad) 
    {
        this.name = name;
        this.port = port;
        this.prioridad = prioridad;
        
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
                while(!mensaje_recibido){
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

                    if ("prioridad".equals(list_messages[0])){
                        //[mensaje,prioridad,port]
                        priori_port.add(list_messages[1]+","+list_messages[2]);
                        if(Integer.parseInt(list_messages[1]) > prioridad){
                            coordinador = false;
                            //revisa si es inicialmente el coordinador
                        }
                    }        
                    else{
                        System.out.print("Header irreconocible");
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
