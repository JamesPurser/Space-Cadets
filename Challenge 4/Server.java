import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 2000;

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            outputLine = "Thank you for connecting to the server.";
            out.println(outputLine);

            while (true) {

                inputLine = in.readLine();
                outputLine = inputLine;

                System.out.println("Client: " + inputLine);
                System.out.println("Server: " + outputLine);

                out.println(outputLine);

                if (outputLine.equals("Bye.")) {
                    break;
                }
            }
        }
    }
}