import java.io.*;

public class MatchHandler implements Runnable {
    private Match match;

    public MatchHandler(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(match.joueur1.socket.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(match.joueur2.socket.getInputStream()));
            PrintWriter out1 = new PrintWriter(match.joueur1.socket.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(match.joueur2.socket.getOutputStream(), true);

            out1.println("DEBUT_MATCH X");
            out2.println("DEBUT_MATCH O");


            int tour = 0;
            while (!match.termine) {
                BufferedReader inActuel = tour % 2 == 0 ? in1 : in2;
                PrintWriter outActuel = tour % 2 == 0 ? out1 : out2;
                PrintWriter outAdverse = tour % 2 == 0 ? out2 : out1;
                char symbole = tour % 2 == 0 ? 'X' : 'O';

                outActuel.println("VOTRE_TOUR");

                String[] coup = inActuel.readLine().split(",");
                int x = Integer.parseInt(coup[0]);
                int y = Integer.parseInt(coup[1]);

                if (match.jeu.jouer(x, y, symbole) && (x < 3 || y < 3)) {
                    outAdverse.println("COUP_ADVERSE:" + x + "," + y);
                    String resultat = match.jeu.checkVictoire();
                    if (!resultat.equals("EN_COURS")) {
                        match.termine = true;
                        match.resultat = resultat;
                        out1.println("FIN:" + resultat);
                        out2.println("FIN:" + resultat);
                    }
                    tour++;
                } else {
                    outActuel.println("COUP_INVALIDE");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
