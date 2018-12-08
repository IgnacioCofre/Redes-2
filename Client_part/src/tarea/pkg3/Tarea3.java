/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the templ

    /**ate in the editor.
 */
package tarea.pkg3;

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
            String ip = "localhost";
            Server server1 = new Server(port,"server 2",prioridad);
            server1.start();
            Thread.sleep(4000);
            //public Client(int port, String name,  int prioridad, String mensaje, String address)
            Client client1 = new Client(5000,"Client 1",prioridad,"saludos desde proceso 1",ip);
            client1.start();
            Thread.sleep(4000);
            Client client3 = new Client(5000,"Client 1",3,"saludos desde proceso 1",ip);
            client3.start();
	} 
    
}
