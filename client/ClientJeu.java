import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Scanner;

public class ClientJeu {
    private static char[][] plateau = new char[3][3];

    public static void main(String[] args) throws IOException {
        initPlateau();
        char playerSymbols = '0';
        char otherPlayerSymbols = '0';
        Socket socket = new Socket("", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner sc = new Scanner(System.in);
        System.out.print("Entrez votre pseudo : ");
        String pseudo = sc.nextLine();
        out.println(pseudo);


        while (true) {
            String msg = in.readLine();
            if (msg == null) break;
            if (msg.contains("DEBUT_MATCH")) {
                System.out.println("Match commencé !");
                if (Objects.equals(msg.split(" ")[1], "O")) {
                    playerSymbols = 'O';
                    otherPlayerSymbols = 'X';
                } else {
                    playerSymbols = 'X';
                    otherPlayerSymbols = 'O';
                }
                System.out.println("Votre symbole est : " + playerSymbols);
                afficherPlateau(pseudo);
            } else if (msg.equals("VOTRE_TOUR")) {
                System.out.print("Entrez votre coup (x y) : ");
                int x = sc.nextInt();
                int y = sc.nextInt();
                if (plateau[x][y] == ' ') {
                    plateau[x][y] = playerSymbols;
                }
                out.println(x + "," + y);
                afficherPlateau(pseudo);
            } else if (msg.startsWith("COUP_ADVERSE")) {
                String[] parts = msg.split(":")[1].split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                plateau[x][y] = otherPlayerSymbols;
                System.out.println("L'adversaire a joué : (" + x + "," + y + ")");
                afficherPlateau(pseudo);
            } else if (msg.startsWith("FIN")) {
                String resultat = msg.split(":")[1];
                System.out.println(resultat);
                System.out.println("Fin du match. Résultat : " + resultat);
                break;
            } else if (msg.equals("COUP_INVALIDE")) {
                System.out.println("Coup invalide, réessayez.");
            }
        }

        socket.close();
    }

    private static void initPlateau() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                plateau[i][j] = ' ';
    }

    public static void afficherPlateau(String pseudo) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Votre pseudo est : " + pseudo);
        System.out.println("  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(plateau[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }
}
