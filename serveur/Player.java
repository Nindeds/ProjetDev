import java.net.Socket;

public class Player {
    public Socket socket;
    public String username;

    public Player(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
    }
}