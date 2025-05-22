import java.net.Socket;

public class Joueur {
    public Socket socket;
    public String pseudo;

    public Joueur(Socket socket, String pseudo) {
        this.socket = socket;
        this.pseudo = pseudo;
    }
}