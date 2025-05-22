import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Scanner;

public class ClientJeu extends Morpion{
    private static char[][] plateau = new char[3][3];
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException{

        System.out.println("===MORPION===");
        System.out.println("1.Solo(vs IA)");
        System.out.println("2.Multijoueur");
        System.out.println("3.Quitter");
        System.out.print("Choisissez votre mode : ");
        int choix = -1;
        boolean choixValide = false;

        while (!choixValide) {
            boolean saisieValide = false;
            while (!saisieValide) {
                try {
                    System.out.print("Choisissez un mode (1-3) : ");
                    choix = sc.nextInt();
                    saisieValide = true;
                } catch (Exception e) {
                    System.out.println("Erreur : veuillez entrer un nombre valide.");
                    sc.nextLine();
                }
            }
            sc.nextLine();

            if (choix >= 1 && choix <= 3) {
                choixValide = true;
                switch (choix) {
                    case 1:
                        break;
                    case 2:
                        jouerMultiplayer();
                    case 3:
                        // Thread.sleep(300);
                        System.out.println("Fermeture du jeu...");
                        //Thread.sleep(1500);
                        System.exit(0);
                        break;
                }
            } else {
                System.out.println("Choix invalide. Veuillez choisir entre 1 et 3.");
            }
        }
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

    public static void jouerMultiplayer() throws IOException {
        initPlateau();
        char playerSymbols = '0';
        char otherPlayerSymbols = '0';
        Socket socket = new Socket("", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
                while(x >= 3 || y >= 3) {
                    System.out.print("Entrez votre coup (x y) : ");
                    x = sc.nextInt();
                    y = sc.nextInt();
                }
                plateau[x][y] = playerSymbols;

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
}
