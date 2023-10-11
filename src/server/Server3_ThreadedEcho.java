package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Diese Klasse implementiert einen Echo-Server. Jede Eingabe des Benutzers wird 3x mal zurück geschickt.
 *
 *  Durch die Verwendung von Threads können sich mehrere Clients gleichzeitig verbinden.
 */


public class Server3_ThreadedEcho {

    public static int PORT = 10023;

    public void runServer() {

        // Server auf Port 10023 horchen lassen
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Echo-Server bereit am Port " + PORT);

            while (true) {

                System.out.println("Warten auf Verbindungsanfrage.");

                    Socket verbindung = server.accept();
                    System.out.println("Verbindung angenommen von " + verbindung.getRemoteSocketAddress());
                    ClientHandler handler = new ClientHandler(verbindung);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server3_ThreadedEcho().runServer();
    }

}

/* Die Klasse befindet sich nur im gleichen File um es kompakter zu machen. */
class ClientHandler extends Thread {
    private  static int ECHOS = 3;

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.start(); //start the thread
    }

    @Override
    public void run() {
        try (
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        ) {

            System.out.println("Verbindung angenommen von " + socket.getRemoteSocketAddress());

            while (true) {
                // Eingabeaufforderung senden und Zeile einlesen
                bw.write("Bitte rufen (Lerzeile zum Beenden):\r\n");
                bw.flush();

                String zeile = br.readLine();


                //eine Leerzeile beendet die Verbindung
                if (zeile == null || zeile.trim().isEmpty()) {
                    break;
                }

                // Zeile dreimal zurücksenden
                zeile = "(Echo): " +zeile+"\r\n";
                bw.write(zeile);
                bw.flush();

                for (int i = 0; i < ECHOS - 1; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    bw.write(zeile);
                    bw.flush();
                }
            }

            System.out.println("Verbindung beendet mit " + socket.getRemoteSocketAddress());
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}