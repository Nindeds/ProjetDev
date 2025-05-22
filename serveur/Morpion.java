public class Morpion {
    private char[][] plateau;

    public Morpion() {
        plateau = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                plateau[i][j] = ' ';
    }

    public boolean jouer(int x, int y, char symbole) {
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || plateau[x][y] != ' ') {
            return false;
        }
        plateau[x][y] = symbole;
        return true;
    }

    public String checkVictoire() {
        for (int i = 0; i < 3; i++) {
            if (plateau[i][0] != ' ' && plateau[i][0] == plateau[i][1] && plateau[i][1] == plateau[i][2])
                return "VICTOIRE_" + plateau[i][0];
            if (plateau[0][i] != ' ' && plateau[0][i] == plateau[1][i] && plateau[1][i] == plateau[2][i])
                return "VICTOIRE_" + plateau[0][i];
        }
        if (plateau[0][0] != ' ' && plateau[0][0] == plateau[1][1] && plateau[1][1] == plateau[2][2])
            return "VICTOIRE_" + plateau[0][0];
        if (plateau[0][2] != ' ' && plateau[0][2] == plateau[1][1] && plateau[1][1] == plateau[2][0])
            return "VICTOIRE_" + plateau[0][2];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (plateau[i][j] == ' ')
                    return "EN_COURS";

        return "EGALITE";
    }
}
