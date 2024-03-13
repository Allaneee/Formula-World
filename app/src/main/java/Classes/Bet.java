package Classes;

public class Bet {
    private int id;
    private int idUtilisateur; // Identifiant de l'utilisateur qui fait le pari
    private int idGrandPrix; // Identifiant du Grand Prix sur lequel le pari est fait
    private String choixPilotes; // IDs des pilotes choisis, séparés par des virgules ou un format similaire
    private int score; // Score obtenu sur ce pari

    public Bet(int id, int idUtilisateur, int idGrandPrix, String choixPilotes, int score) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.idGrandPrix = idGrandPrix;
        this.choixPilotes = choixPilotes;
        this.score = score;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdGrandPrix() {
        return idGrandPrix;
    }

    public void setIdGrandPrix(int idGrandPrix) {
        this.idGrandPrix = idGrandPrix;
    }


}



