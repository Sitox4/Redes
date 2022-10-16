package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        int puerto = Integer.parseInt(argv[0]);
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket datagramSocket = null;
        try {
            // Create a server socket
            datagramSocket = new DatagramSocket(puerto);
            // Set maximum timeout to 300 secs
            datagramSocket.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                // Receive the message
                datagramSocket.receive(packet);
                System.out.println("SERVER: Received "
                        + new String(packet.getData(), 0, packet.getLength())
                        + " from " + packet.getAddress().toString() + ":"
                        + packet.getPort());
                // Prepare datagram to send response
                String respuesta = new String(packet.getData());
                DatagramPacket respuesta2 = new DatagramPacket(respuesta.getBytes(),respuesta.getBytes().length, packet.getAddress(), packet.getPort());
                // Send response
                datagramSocket.send(respuesta2);
                System.out.println("SERVER: Sending "
                        + new String(respuesta2.getData()) + " to "
                        + respuesta2.getAddress().toString() + ":"
                        + respuesta2.getPort());
            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            if(datagramSocket != null){
                datagramSocket.close();
            }
        }
    }
}
