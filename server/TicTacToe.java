public class TicTacToe {
    private char[][] gameBoard;

    public TicTacToe() {
        gameBoard = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                gameBoard[i][j] = ' ';
    }

    public boolean movePlayed(int x, int y, char symbol) {
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || gameBoard[x][y] != ' ') {
            return false;
        }
        gameBoard[x][y] = symbol;
        return true;
    }

    public String checkWin() {
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i][0] != ' ' && gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2])
                return "WIN_" + gameBoard[i][0];
            if (gameBoard[0][i] != ' ' && gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i])
                return "WIN_" + gameBoard[0][i];
        }
        if (gameBoard[0][0] != ' ' && gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2])
            return "WIN_" + gameBoard[0][0];
        if (gameBoard[0][2] != ' ' && gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0])
            return "WIN_" + gameBoard[0][2];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (gameBoard[i][j] == ' ')
                    return "IN_PROGRESS";

        return "DRAW";
    }
}
