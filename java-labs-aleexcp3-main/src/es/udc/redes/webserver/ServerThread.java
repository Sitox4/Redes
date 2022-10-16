    package es.udc.redes.webserver;

    import java.io.*;
    import java.net.Socket;
    import java.net.URLConnection;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Locale;

    public class ServerThread extends Thread {

        private final Socket socket;

        public ServerThread(Socket s) throws IOException {
            this.socket = s;
        }

        public void run() {
            try{
                handleRequest();
            }catch (Exception e){
                System.err.println("Error: " + e.getMessage());
            }
        }

        private void handleRequest() throws Exception {
            InputStream input;
            OutputStream output;
            try {
                input = socket.getInputStream();
                output = socket.getOutputStream();
                serverRequest(input, output);
                output.flush();
                input.close();
                output.close();

            } catch (Exception e){
                throw new Exception("Error: " + e.getMessage());
            }
        }

        private String ModifiedDate(long Modified) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
                    new Locale("EN", "ES"));
            return dateFormat.format(new Date(Modified));
        }

        private void serverRequest(InputStream input, OutputStream output) throws Exception {

            String line;
            int code;
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));

            line = bf.readLine();
            if (line != null) {
                File file;
                String[] filename = line.split(" ");

                if (filename[1].equals("/") || new File(filename[1].replace("/", "") + "/index.html").exists()) {
                    filename[1] = "/index.html";
                }


                String Archivo;
                Archivo = filename[1];
                String Version = filename[2];

                if (Archivo.length() > 15) {
                    file = new File(Archivo.replaceFirst("/", ""));
                } else file = new File(Archivo.replace("/", ""));

                String HttpAndState;
                String Method = filename[0];
                WebServer.PrintPetitions(Archivo, 5000, filename);

                if ((Method.equals("GET") || Method.equals("HEAD") && Version.equals("HTTP/1.0"))) {
                    if (file.exists()) {
                        HttpAndState = (Version + " 200 OK\n");
                        code = 200;
                    } else {
                        file = new File("error404.html");
                        HttpAndState = (Version + " 404 Not found\n");
                        code = 404;
                    }
                }else {
                    file = new File("error400.html");
                    HttpAndState = (Version + " 400 Bad request\n");
                    code = 400;
                }

                if (!file.isDirectory()) {
                    String CONTENT_TYPE;
                    String SERVER = "Server: WebServer_950\n";
                    String DATE = "Date: " + new Date() + "\n";
                    if(filename[1].equals("/index.html")){
                        CONTENT_TYPE = "Content-Type: text/html\n\n";
                    }else{
                        CONTENT_TYPE = "Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\n\n";
                    }

                    String LENGTH;
                    if(code == 400){
                        LENGTH = "Content-Length: 0\n";
                    }else{
                        LENGTH = "Content-Length: " + file.length() + "\n";
                    }
                    String lastModified = "Last-Modified: " + ModifiedDate(file.lastModified()) + "\n";
                    String HeaderWithBody = HttpAndState + SERVER + DATE + lastModified + LENGTH + CONTENT_TYPE;
                    output.write(HeaderWithBody.getBytes());
                    if(Method.equals("GET")) Files.copy(Paths.get(file.toString()), output);

                }

            }
        }
    }
