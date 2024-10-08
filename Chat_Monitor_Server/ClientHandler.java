import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Semaphore semaphore = new Semaphore(1);
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            System.out.println(clientHandlers.toString());
            broadCastMessage("SERVER: " + this.clientUsername + " joined");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    
    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(this.clientUsername + ": " + messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadCastMessage(String messageToSend){
        
        try{
            semaphore.acquire();
            storeLogs(messageToSend + "\n");
            semaphore.release();
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

        for(ClientHandler client: clientHandlers){
            try {
                System.out.println("MY PORT: " + this.socket.getLocalPort() + " TARGET PORT: " + client.socket.getLocalPort());
                if(!client.clientUsername.equals(clientUsername) && this.socket.getLocalPort() == client.socket.getLocalPort()){
                    client.bufferedWriter.write(messageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SERVER: Client " + clientUsername + " left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeLogs(String message){
        System.out.println("Storing logs");
        File logs = new File("logs.txt");
        try{
            Files.write(logs.toPath(), message.getBytes(), StandardOpenOption.APPEND);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}