/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_part;

/**
 *
 * @author Ignacio Cofre
 */
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
    private Socket		 socket = null; 
    private ServerSocket server = null; 
    private DataInputStream in	 = null; 
    private boolean server_alive = true;
    public int port;
    public int num_maquinas = 4;
    public String name;
    List <String> messages =new ArrayList<>();  

    // constructor with port and name 
    public Server(int port, String name) 
    {
        this.name = name;
        this.port = port;
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

        try{
            while(messages.size()<num_maquinas){
                socket = server.accept();
                System.out.println("Client accepted");

                // takes input from the client socket
                in = new DataInputStream(socket.getInputStream()); 
                String line;
                line = (String)in.readUTF();
                System.out.println(line);
                this.messages.add(line);
                System.out.println("Mensaje recibido");
                
            }
        }
        catch(IOException i){
            System.out.println("Error al realizar la coneccion con el cliente");
        }
        
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
    
    public List get_message(){
        return this.messages;
    }
    
    public void clean(){
        this.messages = null;
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

