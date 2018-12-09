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
/**
 *
 * @author Ignacio Cofre
 */
// A Java program for a Client 
import java.net.*; 
import java.io.*; 

//Client 2
public class Client implements Runnable
{ 
	// initialize socket and input output streams 
        private Thread c;
	private Socket socket		 = null; 
	private DataOutputStream out	 = null; 
        private String address;
        private String name;
        private int prioridad; //despues borrar
        private String mensaje;
        private int port; 

	// constructor to put ip address and port
        public Client(int port,String name, String mensaje, String address) 
	{
            this.name = name;
            this.address = address; 
            this.mensaje = mensaje;
            this.port = port;
	}
        
    public void run(){ 
        // establish a connection 
        try{ 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(this.mensaje+","+Integer.toString(port)); //ver si puedo mandar una lista
            out.flush();
            out.close();
            socket.close();
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 

    }
    
    public void start(){
        System.out.println("Starting client" +  name+" in port "+Integer.toString(port)+" to server ip " + address);
        if (c == null) {
            c = new Thread (this, name);
            //Integer.toString(port)
            c.start ();
        }
    }
} 