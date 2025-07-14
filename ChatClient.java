import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to the chat server");

            new ReadThread(socket).start();
            new WriteThread(socket).start();

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}

// Thread for reading messages from server
class ReadThread extends Thread {
    private BufferedReader reader;

    public ReadThread(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error reading from server: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if (response == null) break;
                System.out.println(response);
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
                break;
            }
        }
    }
}

// Thread for writing messages to server
class WriteThread extends Thread {
    private PrintWriter writer;
    private BufferedReader console;

    public WriteThread(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            console = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Error writing to server: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String message = console.readLine();
                writer.println(message);
            } catch (IOException e) {
                break;
            }
        }
    }
}
