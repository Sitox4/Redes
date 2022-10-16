package es.udc.redes.tutorial.copy;

import java.io.*;

public class Copy {
    public Copy(String origen, String destino) throws IOException {

        try (InputStream entrada = new FileInputStream(origen); OutputStream salida = new FileOutputStream(destino)) {
            byte[] buffer = new byte[1024];
            int escritura;
            while ((escritura = entrada.read(buffer)) > 0) {
                salida.write(buffer, 0, escritura);
            }

        } catch (IOException h) {
            h.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        System.out.println(args[1]);
        Copy test = new Copy(args[0], args[1]);

    }

}

