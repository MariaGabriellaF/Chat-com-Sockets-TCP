import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClientServer {
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        int port = 6666;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado na porta " + port);

        // Thread para escutar mensagens digitadas no terminal do servidor
        Thread terminalInput = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String serverMessage = scanner.nextLine();
                broadcast("[Servidor] " + serverMessage, null);
            }
        });
        terminalInput.setDaemon(true); // opcional: encerra quando main thread encerrar
        terminalInput.start();

        // Loop para aceitar conexões de clientes
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Novo cliente conectado: " + socket.getRemoteSocketAddress());
            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    // Envia mensagem para todos os clientes (exceto quem enviou, se especificado)
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // Classe que trata cada cliente em uma thread
    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        }

        public void sendMessage(String message) {
            try {
                output.writeUTF(message);
            } catch (IOException e) {
                System.out.println("Erro ao enviar mensagem para cliente.");
            }
        }

        @Override
        public void run() {
            try {
                sendMessage("Bem-vindo ao chat!");

                while (true) {
                    String msg = input.readUTF();
                    System.out.println("Recebido de " + socket.getRemoteSocketAddress() + ": " + msg);
                    broadcast("[" + socket.getRemoteSocketAddress() + "] " + msg, this);
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + socket.getRemoteSocketAddress());
            } finally {
                try {
                    input.close();
                    output.close();
                    socket.close();
                } catch (IOException ignored) {}
                clients.remove(this);
            }
        }
    }
}
