public class Match {
    public Joueur joueur1;
    public Joueur joueur2;
    public Morpion jeu;
    public boolean termine = false;
    public String resultat = "EN_COURS";

    public Match(Joueur j1, Joueur j2) {
        this.joueur1 = j1;
        this.joueur2 = j2;
        this.jeu = new Morpion();
    }
}