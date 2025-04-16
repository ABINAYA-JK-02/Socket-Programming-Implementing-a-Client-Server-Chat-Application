import java.io.*;
import java.net.*;
import java.util.*;

public class Chatserver {
    private static Set<ClientHandler> clients = new HashSet<>();
    private static boolean serverRunning = true;

    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + "...");

            while (serverRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                } catch (SocketTimeoutException e) {
                    // Continue to check for shutdown
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage("Broadcast: " + message);
            }
        }
    }

    // Remove a client from the set
    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    // Handle each client
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Welcome to the server!");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("EXIT")) {
                        out.println("Goodbye!");
                        break;
                    } else if (message.equalsIgnoreCase("BROADCAST")) {
                        out.println("Enter the broadcast message:");
                        String broadcastMessage = in.readLine();
                        broadcast(broadcastMessage, this);
                    } else if (message.equalsIgnoreCase("SHUTDOWN")) {
                        System.out.println("Shutdown command received. Stopping server...");
                        serverRunning = false;
                        break;
                    } else {
                        System.out.println("Received: " + message);
                        out.println("Message received: " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                removeClient(this);
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
