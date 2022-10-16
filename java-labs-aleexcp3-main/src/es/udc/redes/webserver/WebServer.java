package es.udc.redes.webserver;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;


public class WebServer{

    public static final HashMap<String,Integer> requestedRes = new HashMap<>();

    public static void PrintPetitions(String res, int port2, String[] string) {
        requestedRes.merge(res, 1, Integer::sum);
        System.out.println(string[0] +" " + res + " | port: " + port2 + " " + requestedRes.get(res));
    }

    public static void main(String[] argv) throws IOException {
        Socket socket = null;
        ServerSocket serverSocket;
        int port =5000;
        try {
            System.out.println("\n==================== Detalles del servidor HTTP ====================\n");
            System.out.println("Server: WebServer_950");
            System.out.println("Port number: " + port);
            System.out.println("Esperando peticiones...");
            System.out.println();
            serverSocket = new ServerSocket(port);
            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                socket = serverSocket.accept();
                // Create a ServerThread object, with the new connection as parameter
                ServerThread thread = new ServerThread(socket);
                // Initiate thread using the start() method
                thread.start();
            }
        }catch(SocketTimeoutException e){
            System.err.println("Nothing received in 300 secs");
        } catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            if (socket != null){
                socket.close();
            }
        }
    }
}
