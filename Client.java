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
        private String name;
        private String mensaje;
        private int port;
        private int port_salida;

	// constructor to put ip address and port
        public Client(int port,int port_salida, String name, String mensaje, String address)
	{
            this.name = name;
            this.address = address;
            this.mensaje = mensaje;
            this.port = port;
            this.port_salida = port_salida;
	}

    public void run(){
        // establish a connection
        try{
            socket = new Socket(address, port);
            System.out.println("Connected");
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(this.mensaje); //ver si puedo mandar una lista
            out.flush();
            out.close();
            socket.close();
        }
        catch(ConnectException p){
            try{
              System.out.println("error de coneccion con el port: "+port);
              //me mando un mensaje a mi mismo
              socket = new Socket(address, port_salida);
              System.out.println("Se envia un aviso al port_salida: "+Integer.toString(port_salida));
              out = new DataOutputStream(socket.getOutputStream());
              out.writeUTF("coordinacion,"+Integer.toString(port_salida));
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
            System.out.println("error IO, cliente");
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
