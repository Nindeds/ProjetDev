public class Match {
    public Player player1;
    public Player player2;
    public TicTacToe game;
    public boolean matchEnded = false;
    public String result = "EN_COURS";

    public Match(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.game = new TicTacToe();
    }
}