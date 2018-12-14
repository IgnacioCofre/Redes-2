/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package server_part;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
/**
 *
 * @author Ignacio Cofre
 */
public class Server_part {
     /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String args[]) throws InterruptedException
    {
        Scanner scan = new Scanner(System.in);
        int prioridad = 0;
        int port = 0;
        boolean inicio_llamadas = false;
        List <String> ports;
        String line;

        System.out.print("Ingrese el port de este server: \n");
        try{
            port = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        System.out.print("Ingrese la prioridad de este server: \n"); //esto despues debe ser automatico
        try{
            prioridad = scan.nextInt();
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        scan.nextLine();

        System.out.print("Ingrese los ports de las demas maquinas separados por una coma:\n");
        line = scan.nextLine();
        ports = Arrays.asList(line.split(","));
        System.out.println(ports);

        System .out.print("Este servidor debe partir con la comunicacion? (Y/N) :");
        line = scan.nextLine();
        if("Y".equals(line) | "y".equals(line)){
            inicio_llamadas = true;
            System.out.println("Este servidor iniciara las llamadas");
        }

        String ip = "localhost";
        //public Server(int port, List ports_list, String name, int prioridad, boolean inicio_llamadas)
        Server server1 = new Server(port,ports,"server",prioridad,inicio_llamadas);
        server1.start();
        Thread.sleep(2000);
        System.out.println("Presione ENTER cuando esten todos los servidores incializados");
        String start = scan.nextLine();
        System.out.println("Tiene 8 segundos para iniciar los demas servidores");
        Thread.sleep(8000);

        //public Client(int port, String name,  int prioridad, String mensaje, String address)
        //Se manda un mensaje inicial a todas las maquinas
        for (int i = 0; i< ports.size(); i++){
            try{
                //message [port,"name client",message,ip]
                Client client1 = new Client(Integer.parseInt(ports.get(i)),port,"Client 1","prioridad,"+Integer.toString(prioridad)+","+Integer.toString(port),ip);
                client1.start();
            }
            catch(NumberFormatException a){
                System.out.println("Error al enviar un cliente al port "+ports.get(i));
                System.out.println(a);
            }
        }
    }
}
