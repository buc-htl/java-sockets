package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
    Example of a client which can communicate with the echo server.

     In order to run it within IntelliJ, specify the correct "Program arguments" in the run configuration.
 **/
public class Client_Echo {

    public static void main(String[] args) {

        //read host name and port number from the terminal
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            //for the client we create a Socket (instead of a ServerSocket). The host name and port
            //are those of the server the client wants to connect to.
            Socket socket = new Socket(hostName, portNumber);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {

            String fromServer;
            String fromUser;

            //reading the input from the server
            while ((fromServer = br.readLine()) != null) {

                System.out.println("[Server] " + fromServer);

                //don't read user input as long as we print echos
                if (fromServer.contains("(Echo)")) {
                    continue;
                }

                //read input from the user and send it to the server
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("[Client] " + fromUser);
                    bw.write(fromUser+"\n");
                    bw.flush();
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }



    }


}
