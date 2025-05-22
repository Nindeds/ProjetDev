import java.io.*;

public class MatchHandler implements Runnable {
    private Match match;

    public MatchHandler(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(match.player1.socket.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(match.player2.socket.getInputStream()));
            PrintWriter out1 = new PrintWriter(match.player1.socket.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(match.player2.socket.getOutputStream(), true);

            out1.println("START_MATCH X");
            out2.println("START_MATCH O");


            int turn = 0;
            while (!match.matchEnded) {
                BufferedReader inActual = turn % 2 == 0 ? in1 : in2;
                PrintWriter outCurrentMove = turn % 2 == 0 ? out1 : out2;
                PrintWriter outEnemyMove = turn % 2 == 0 ? out2 : out1;
                char symbol = turn % 2 == 0 ? 'X' : 'O';

                outCurrentMove.println("YOUR_TURN");

                String[] playerMove = inActual.readLine().split(",");
                int x = Integer.parseInt(playerMove[0]);
                int y = Integer.parseInt(playerMove[1]);

                if ((x < 3 && y < 3) && match.game.movePlayed(x, y, symbol)) {
                    outEnemyMove.println("ENEMY_MOVE:" + x + "," + y);
                    String result = match.game.checkWin();
                    if (!result.equals("IN_PROGRESS")) {
                        match.matchEnded = true;
                        match.result = result;
                        if (result.split("_").length > 1) {
                            if (result.split("_")[1].contains("O")) {
                                match.result = match.player2.username;
                            } else {
                                match.result = match.player1.username;
                            }
                        }
                        out1.println("END_GAME:" + match.result);
                        out2.println("END_GAME:" + match.result);
                    }
                    turn++;
                } else {
                    outCurrentMove.println("INVALID_MOVE");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
