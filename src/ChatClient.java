import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        System.out.println("Conectado ao servidor!");

        // Thread para receber mensagens
        Thread receiver = new Thread(() -> {
            try {
                while (true) {
                    String msg = input.readUTF();
                    System.out.println("\n" + msg);
                }
            } catch (IOException e) {
                System.out.println("Desconectado do servidor.");
            }
        });
        receiver.start();

        // Thread principal envia mensagens
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            if (msg.equalsIgnoreCase("sair")) break;
            output.writeUTF(msg);
        }

        stop();
    }

    public void stop() throws IOException {
        input.close();
        output.close();
        socket.close();
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int port = 6666;

        try {
            ChatClient client = new ChatClient();
            client.start(serverIp, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
