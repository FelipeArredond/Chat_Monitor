import java.io.*;
import java.net.*;

public class SocketListener {

    public SocketListener(){}

    public static void main(String[] args) {
        int port1 = 12345;
        int port2 = 12346;

        ServerSocket server1;
        ServerSocket server2;
        try {

            server1 = new ServerSocket(port1);
            server2 = new ServerSocket(port2);

            new Thread(() -> chatSession(server1)).start();
            new Thread(() -> chatSession(server2)).start();

        } catch (IOException e) {

            e.printStackTrace();

        }

        
    }


    private static void chatSession(ServerSocket session){
        System.out.println("Client 2 connected chat ready ");
        while (true) {
            try (Socket socket = session.accept()) {
                System.out.println("New client connected" + socket.getInetAddress());

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String text;

                while ((text = reader.readLine()) != null) {
                    System.out.println("Received from client: " + text);
                    writer.println("Echo: " + text);

                    if ("bye".equalsIgnoreCase(text)) {
                        System.out.println("Client disconnected.");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
