package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  Unsere Programme können bisher Dateien einlesen und schreiben
 *  sowie Benutzereingaben lesen, bzw. Daten über die Konsole oder eine GUI ausgeben.
 *  In einem nächsten Schritt lassen wir Programme miteinander kommunizieren - egal ob sie sich
 *  am gleichen Rechner befinden oder über ein Netzwerk verbunden sind.
 *  Dazu verwenden wir sogenannte "Sockets".
 *
 * "Ein Socket (von engl. Sockel, Steckverbindung oder Steckdose) ist ein vom Betriebssystem bereitgestelltes Objekt,
 * das als Kommunikationsendpunkt dient. Ein Programm verwendet Sockets, um Daten mit anderen Programmen auszutauschen.
 * Das andere Programm kann sich dabei auf demselben Computer (Interprozesskommunikation) oder
 * einem anderen, via Netzwerk erreichbaren Computer befinden.
 * Die Kommunikation über Sockets erfolgt in der Regel bidirektional, das heißt, über das Socket können Daten
 * sowohl empfangen als auch gesendet werden. "
 * (Quelle: https://de.wikipedia.org/wiki/Socket_%28Software%29)
 *
 *  Weiterführende Infos: https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
 *
 *  Info: Um eine Ressource über HTTP zu laden, gibt es bessere Möglichkeiten (https://docs.oracle.com/javase/tutorial/networking/urls/index.html)
 *
 *
 * Wie kann man sich mit diesem Server verbinden?
 *
 *  - telnet (versucht aber das telnet-Protokoll), - https://praxistipps.chip.de/windows-10-telnet-client-aktivieren-und-deaktivieren_92627, telnet localhost 10023
 *  - putty mit type raw
 *  - netcat
 *
 *  Mac OSX: netcat -> nc
 *  nc localhost 10023
 *
 *  TODO - konkrete Beispiele für andere OS
 */

public class Server1_HelloWorld {
    private static final int PORT = 10023;


    public void runServer(){

        // ein ServerSocket wartet auf eingehende Requests (über TCP/IP). Basierend auf einen Request kann
        // der Server dann Operationen durchführen und ggf. eine Antwort zurück schicken.

        // Server auf Port 10023 horchen lassen (Achtung: Der Port muss frei sein, sonst wird eine Exception geworfen)
        //try-with-resources -> Socket wird automatisch geschlossen.
        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("Server bereit");

            // Auf ankommende Verbindung warten, accept() blockiert so lange
            //Für jede ankommende Verbindung wird ein neuer Socket erzeugt.
            //Einen Socket kann man sich ähnlich zu einer Datei vorstellen, auf die man schreiben und lesen kann.
            Socket verbindung = server.accept();
            System.out.println("Verbindung angenommen");

            // Zum Senden eines Texts wird ein Writer benötigt --
            // diesen über den OutputStream stülpen
            Writer w = new OutputStreamWriter(verbindung.getOutputStream());

            // Nachricht senden
            w.write("Hallo, Welt!\n");

            //leert den Buffer und stellt sicher, dass die Daten gesendet werden
            w.flush();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        new Server1_HelloWorld().runServer();
    }
}
