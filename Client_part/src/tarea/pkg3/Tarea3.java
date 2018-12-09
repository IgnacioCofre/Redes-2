/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the templ

    /**ate in the editor.
 */
package tarea.pkg3;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ignacio Cofre
 */
public class Tarea3 {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String args[]) throws InterruptedException 
    { 
        int port = 5001;
        
        int prioridad = 1;
        List <String> ports = Arrays.asList("5000");
        String ip = "localhost";
        boolean inicio_llamadas = false;
        Server server1 = new Server(port,"server 1",prioridad,inicio_llamadas);
        server1.start();
        Thread.sleep(4000);
        //public Client(int port, String name,  int prioridad, String mensaje, String address)
        //Se manda un mensaje inicial a todas las maquinas
        for (int i = 0; i< ports.size(); i++){
            try{
                //message [port,"name client",message,ip]
                Client client1 = new Client(Integer.parseInt(ports.get(i)),"Client 1","prioridad,"+Integer.toString(prioridad),ip);
                client1.start();
            }
            catch(NumberFormatException a){
                System.out.println("Error al enviar un cliente al port "+ports.get(i));
                System.out.println(a);
            }
        }
    } 
}
