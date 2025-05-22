import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class ClientJeu extends Morpion{
    private static char[][] plateau = new char[3][3];
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();
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
                        break;
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

    public static void jouerTourIA(char joueur) {
        System.out.println("Tour de l'IA...");
        int ligne, colonne;
        while (true) {
            ligne = random.nextInt(3);
            colonne = random.nextInt(3);
            if (plateau[ligne][colonne] == ' ') {
                plateau[ligne][colonne] = joueur;
                break;
            }
        }
    }

    public static void jouerMultiplayer() throws IOException {
        initPlateau();
        char playerSymbols = '0';
        char otherPlayerSymbols = '0';
        System.out.print("Veuillez entrer votre adresse IP :");
        String adresseIP = sc.nextLine();
        Socket socket = new Socket(adresseIP, 12345);
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
                System.out.println(msg);
                System.out.println("Fin du match. Résultat : " + msg.split(":")[1]);
                break;
            } else if (msg.equals("COUP_INVALIDE")) {
                System.out.println("Coup invalide, réessayez.");
            }
        }

        socket.close();
    }
    public  static void  jouerSolo() {
        initPlateau();
        char joueurHumain = 'X';
        char joueurIA = 'O';

        while (true) {
            afficherPlateau("Vous");
            jouerTour(joueurHumain);
            if (verifierVictoire(joueurHumain)) {
                afficherPlateau("Vous");
                System.out.println("Vous avez gagné !");
                break;
            }
            if (estPlein()) {
                afficherPlateau("Vous");
                System.out.println("Match nul !");
                break;
            }

            jouerTourIA(joueurIA);
            if (verifierVictoire(joueurIA)) {
                afficherPlateau("IA");
                System.out.println("L'IA a gagné !");
                break;
            }
            if (estPlein()) {
                afficherPlateau("IA");
                System.out.println("Match nul !");
                break;
            }
        }
    }

    public static boolean verifierVictoire(char joueur) {
        for (int i = 0; i < 3; i++) {
            if ((plateau[i][0] == joueur && plateau[i][1] == joueur && plateau[i][2] == joueur) ||
                    (plateau[0][i] == joueur && plateau[1][i] == joueur && plateau[2][i] == joueur)) {
                return true;
            }
        }
        if ((plateau[0][0] == joueur && plateau[1][1] == joueur && plateau[2][2] == joueur) ||
                (plateau[0][2] == joueur && plateau[1][1] == joueur && plateau[2][0] == joueur)) {
            return true;
        }
        return false;
    }

    public static boolean estPlein() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (plateau[i][j] == ' ')
                    return false;
        return true;
    }
    public static void jouerTour(char joueur) {
        int ligne, colonne;
        while (true) {
            System.out.print("Joueur " + joueur + ", entrez votre position (x y) : ");
            ligne = sc.nextInt();
            colonne = sc.nextInt();

            if (ligne >= 0 && ligne < 3 && colonne >= 0 && colonne < 3 && plateau[ligne][colonne] == ' ') {
                plateau[ligne][colonne] = joueur;
                break;
            } else {
                System.out.println("Position invalide. Réessayez.");
            }
        }
    }
}
