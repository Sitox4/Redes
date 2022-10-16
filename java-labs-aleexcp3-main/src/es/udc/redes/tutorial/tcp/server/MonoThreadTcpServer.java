package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        int puerto = Integer.parseInt(argv[0]);
        ServerSocket serverSocket = null;
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        try {
            // Create a server socket
            serverSocket = new ServerSocket(puerto);
            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket server = serverSocket.accept();
                // Set the input channel
                BufferedReader entrada = new BufferedReader(new InputStreamReader(server.getInputStream()));
                // Set the output channel
                PrintWriter salida = new PrintWriter(server.getOutputStream(), true);
                // Receive the client message
                String mensaje = entrada.readLine();
                System.out.println("SERVER: Received "+mensaje+" my TCP server from "+server.getLocalAddress()+":"+server.getPort());
                // Send response to the client
                salida.println(mensaje);
                System.out.println("SERVER: Sending "+mensaje+" my TCP server to "+server.getLocalAddress()+":"+server.getPort());
                // Close the streams
                entrada.close();
                salida.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            if(serverSocket != null){
                serverSocket.close();
            }
        }
    }
}
