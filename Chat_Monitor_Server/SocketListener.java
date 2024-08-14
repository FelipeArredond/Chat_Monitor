import java.io.*;
import java.net.*;

public class SocketListener {

    private ServerSocket serverSocket;

    SocketListener(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connectec");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thred = new Thread(clientHandler);
                thred.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void closeServerSocket(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        SocketListener socketListener = new SocketListener(serverSocket);
        socketListener.startServer();
    }
    
}
