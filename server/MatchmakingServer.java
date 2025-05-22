import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class MatchmakingServer {
    private static final int PORT = 12345;
    private static final Queue<Player> fileAttente = new ConcurrentLinkedQueue<>();
    private static final List<Match> matchsEnCours = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serveur en attente sur le port " + PORT);

        new Thread(() -> {
            while (true) {
                if (fileAttente.size() >= 2) {
                    Player j1 = fileAttente.poll();
                    Player j2 = fileAttente.poll();
                    Match match = new Match(j1, j2);
                    matchsEnCours.add(match);
                    new Thread(new MatchHandler(match)).start();
                }
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }).start();

        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String pseudo = in.readLine();
            System.out.print(LocalDateTime.now() + ": ");
            System.out.println("Nouveau joueur : " + pseudo);
            fileAttente.add(new Player(socket, pseudo));
        }
    }
}
