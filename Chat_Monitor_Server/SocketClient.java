import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) {
        String hostname = "localhost";  // Server's hostname
        int port = Integer.parseInt(args[0]);               // Server's port number

        try (Socket socket = new Socket(hostname, port)) {
            // Get input and output streams
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Read user input from the console

            String message = "";
            Scanner userInput;

            System.out.println("Connected to the server. Type your messages:");
            do {
                userInput = new Scanner(System.in);
                if(userInput.hasNext()){
                    message = userInput.nextLine();
                    System.out.println("Message to send" + message);
                    writer.println(message);   // Send message to the server
                    System.out.println("Sent to socket");
                }
                String response = reader.readLine();  // Read server's response
                System.out.println(response);
            } while (!"bye".equalsIgnoreCase(message));
            userInput.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
