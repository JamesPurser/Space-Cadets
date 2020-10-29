import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 2000;

        try (
            Socket Socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(Socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        ) {
            String fromServer, fromUser;
            Scanner Scanner = new Scanner(System.in);
            while (true) {

                fromServer = in.readLine();

                System.out.println("Server: " + fromServer);

                if (fromServer.equals("Bye.")) {
                    break;
                }

                System.out.print("Client: ");
                fromUser = Scanner.nextLine();

                out.println(fromUser);
            }

            Scanner.close();
        }
    }
}