import java.io.*;
import java.net.*;

public class Chatclient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";  // Change to actual IP if needed
        int port = 12345;

        try (Socket socket = new Socket(serverIP, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");
            System.out.println(in.readLine());

            String userMessage;
            while (true) {
                System.out.print("Enter message ('EXIT' to disconnect, 'BROADCAST' to send a global message): ");
                userMessage = userInput.readLine();
                out.println(userMessage);

                if (userMessage.equalsIgnoreCase("EXIT")) {
                    System.out.println("Disconnected from server.");
                    break;
                } else if (userMessage.equalsIgnoreCase("BROADCAST")) {
                    System.out.print("Enter broadcast message: ");
                    String broadcastMessage = userInput.readLine();
                    out.println(broadcastMessage);
                }

                System.out.println("Server: " + in.readLine());
            }
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
