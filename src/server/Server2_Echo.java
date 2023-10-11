package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Diese Klasse implementiert einen Echo-Server. Jede Eingabe des Benutzers wird 3x mal zurück geschickt.
 *
 * Problem mit dieser Version: Es kann sich nur ein einziger Client gleichzeitig verbinden. Solange wir in der inneren Schleife mit
 * dem Client sprechen, werden keine weiteren Verbindungen mehr angenommen (das passiert in accept()). Weitere Clients müssen also warten.
 *
 * Wir wollen den Server verbesseren, in dem wir die Client-Logik in einen eigenen Thread auslagern. Der ursprüngliche Thread kann dann
 * mit accept wieder eine neue Verbindung öffnen.
 */

public class Server2_Echo {

    public static int PORT = 10023;

    public static int ECHOS = 3;

    public void runServer() {

        // Server auf Port 10023 horchen lassen
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Echo-Server bereit am Port " + PORT);

            //Wir warten solange auf eingehende Verbindungen, bis der Server beendet wird.
            while (true) {

                //Wir verwenden try-with-resources, damit alle Verbindungen und Streams automatisch geschlossen werden.
                try (
                    // Auf ankommende Verbindung warten, accept() blockiert so lange
                    Socket verbindung = server.accept();

                    // TODO: ab hier sollte der Rest der while-Schleife in einen eigenen
                    //       Client-Thread (z.B. EchoHandler.java) ausgelagert werden.
                    Writer w = new OutputStreamWriter(verbindung.getOutputStream(), StandardCharsets.UTF_8);
                    BufferedWriter bw = new BufferedWriter(w);

                    Reader r = new InputStreamReader(verbindung.getInputStream());
                    BufferedReader br = new BufferedReader(r)
                ) {

                    System.out.println("Verbindung angenommen von " + verbindung.getRemoteSocketAddress());

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

                    System.out.println("Verbindung beendet mit " + verbindung.getRemoteSocketAddress());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Server2_Echo().runServer();
    }

}
