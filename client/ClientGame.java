import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class ClientGame extends TicTacToe {
    private static char[][] boardGame = new char[3][3];
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();
    public static void main(String[] args) throws IOException{

        System.out.println("===TIC TAC TOE===");
        System.out.println("1.Solo(vs IA)");
        System.out.println("2.Multiplayer");
        System.out.println("3.Leave");
        System.out.print("Choose your game mode : ");
        int choice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Choose a gamemode (1-3) : ");
                    choice = sc.nextInt();
                    validInput = true;
                } catch (Exception e) {
                    System.out.println("Error : Please insert a valid number.");
                    sc.nextLine();
                }
            }
            sc.nextLine();

            if (choice >= 1 && choice <= 3) {
                validChoice = true;
                switch (choice) {
                    case 1:
                        playSolo();
                        break;
                    case 2:
                        playMultiplayer();
                        break;
                    case 3:
                        // Thread.sleep(300);
                        System.out.println("Game is closing...");
                        //Thread.sleep(1500);
                        System.exit(0);
                        break;
                }
            } else {
                System.out.println("Invalid choice, please select a valid number between 1 and 3");
            }
        }
    }

    private static void initPlateau() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                boardGame[i][j] = ' ';
    }

    public static void showBoardGame(String username) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Your username is : " + username);
        System.out.println("  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(boardGame[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }

    public static void iaTurn(char joueur) {
        System.out.println("Tour de l'IA...");
        int line, column;
        while (true) {
            line = random.nextInt(3);
            column = random.nextInt(3);
            if (boardGame[line][column] == ' ') {
                boardGame[line][column] = joueur;
                break;
            }
        }
    }

    public static void playMultiplayer() throws IOException {
        initPlateau();
        char playerSymbol = '0';
        char otherPlayerSymbol = '0';
        System.out.print("Enter the IP adress you want to connect to (if you are the server host you can enter localhost) : ");
        String IPAdress = sc.nextLine();
        Socket socket = new Socket(IPAdress, 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.print("Enter your username : ");
        String username = sc.nextLine();
        out.println(username);


        while (true) {
            String msg = in.readLine();
            if (msg == null) break;
            if (msg.contains("START_MATCH")) {
                System.out.println("The game just started !");
                if (Objects.equals(msg.split(" ")[1], "O")) {
                    playerSymbol = 'O';
                    otherPlayerSymbol = 'X';
                } else {
                    playerSymbol = 'X';
                    otherPlayerSymbol = 'O';
                }
                System.out.println("Your symbol is : " + playerSymbol);
                showBoardGame(username);
            } else if (msg.equals("YOUR_TURN")) {
                System.out.print("Enter your move (x y) : ");
                int x = sc.nextInt();
                int y = sc.nextInt();
                while(x >= 3 || y >= 3 && boardGame[x][y] != ' ') {
                    System.out.print("Enter your move (x y) : ");
                    x = sc.nextInt();
                    y = sc.nextInt();
                }
                if (x < 3 && y < 3 && boardGame[x][y] == ' ') {
                    boardGame[x][y] = playerSymbol;
                }

                out.println(x + "," + y);
                showBoardGame(username);
            } else if (msg.startsWith("ENEMY_MOVE")) {
                String[] parts = msg.split(":")[1].split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                boardGame[x][y] = otherPlayerSymbol;
                System.out.println("The other player played : (" + x + "," + y + ")");
                showBoardGame(username);
            } else if (msg.startsWith("END_GAME")) {
                String result = msg.split(":")[1];
                System.out.println("End of match. Result : " + msg.split(":")[1]);
                break;
            } else if (msg.equals("INVALID_MOVE")) {
                System.out.println("Invalid move, try again !.");
            }
        }

        socket.close();
    }
    public  static void playSolo() {
        initPlateau();
        char humanPlayer = 'X';
        char iaPlayer = 'O';

        while (true) {
            showBoardGame("You");
            playerTurn(humanPlayer);
            if (checkWin(humanPlayer)) {
                showBoardGame("You");
                System.out.println("You won !");
                break;
            }
            if (isFull()) {
                showBoardGame("Vous");
                System.out.println("Draw !");
                break;
            }

            iaTurn(iaPlayer);
            if (checkWin(iaPlayer)) {
                showBoardGame("IA");
                System.out.println("The IA won !");
                break;
            }
            if (isFull()) {
                showBoardGame("IA");
                System.out.println("Draw !");
                break;
            }
        }
    }

    public static boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if ((boardGame[i][0] == player && boardGame[i][1] == player && boardGame[i][2] == player) ||
                    (boardGame[0][i] == player && boardGame[1][i] == player && boardGame[2][i] == player)) {
                return true;
            }
        }
        if ((boardGame[0][0] == player && boardGame[1][1] == player && boardGame[2][2] == player) ||
                (boardGame[0][2] == player && boardGame[1][1] == player && boardGame[2][0] == player)) {
            return true;
        }
        return false;
    }

    public static boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (boardGame[i][j] == ' ')
                    return false;
        return true;
    }
    public static void playerTurn(char player) {
        int line, column;
        while (true) {
            System.out.print("Player " + player + ", enter your move (x y) : ");
            line = sc.nextInt();
            column = sc.nextInt();

            if (line >= 0 && line < 3 && column >= 0 && column < 3 && boardGame[line][column] == ' ') {
                boardGame[line][column] = player;
                break;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}
