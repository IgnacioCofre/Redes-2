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
        private int port = 6666;
        private String name;
        private String mensaje;
        private String address_inicio;
        private String address_fin;

	// constructor to put ip address and port
        public Client(String name, String mensaje, String address_inicio, String address_fin)
	{
            this.name = name;
            this.mensaje = mensaje;
            this.address_inicio = address_inicio;
            this.address_fin = address_fin;
	}

    public void run(){
        // establish a connection
        try{
            socket = new Socket(address_fin, port);
            System.out.println("Connected");
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(this.mensaje); //ver si puedo mandar una lista
            out.flush();
            out.close();
            socket.close();
        }
        catch(ConnectException p){
            try{
              System.out.println("error de coneccion con la ip: "+address_fin);
              //me mando un mensaje a mi mismo
              socket = new Socket(address_inicio, port);
              System.out.println("Se envia un aviso a la ip: "+address_inicio);
              out = new DataOutputStream(socket.getOutputStream());
              out.writeUTF("coordinacion,"+address_inicio);
              out.flush();
              out.close();
              socket.close();
            }
            catch(ConnectException ex){
              System.out.println("ESTE ES UN ERROR FATAL PORFAVOR DESCONECTE SU COMPUTADOR INMEDIATAMENTE!!!!! :O");
            }
            catch(UnknownHostException u){
                System.out.println("Server desconocido, cliente");
            }
            catch(IOException i){
                System.out.println("error IO, cliente");
            }
        }
        catch(UnknownHostException u)
        {
            System.out.println("Server desconocido, cliente");
        }
        catch(IOException i)
        {
            System.out.println("error IO, cliente "+address_fin+" , "+Integer.toString(port));
        }

    }

    public void start(){
        System.out.println("Starting client" +  name+" in port "+Integer.toString(port)+" to server ip " + address_fin);
        if (c == null) {
            c = new Thread (this, name);
            //Integer.toString(port)
            c.start ();
        }
    }
}
