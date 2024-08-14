import java.io.*;
import java.lang.management.ThreadInfo;
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
                System.out.println("Client connected and running at treahd: " + Thread.currentThread().getId());
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
        ServerSocket serverSocket = new ServerSocket(7070);
        ServerSocket serverSocket2 = new ServerSocket(8080);
        ServerSocket serverSocket3 = new ServerSocket(9090);
        SocketListener socketListener = new SocketListener(serverSocket);
        SocketListener socketListener2 = new SocketListener(serverSocket2);
        SocketListener socketListener3 = new SocketListener(serverSocket3);
        new Thread(socketListener::startServer).start();
        new Thread(socketListener2::startServer).start();
        new Thread(socketListener3::startServer).start();
    }
    
}
