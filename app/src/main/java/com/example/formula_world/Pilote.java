package com.example.formula_world;

public class Pilote {
    private int id;
    private String nom;
    private int points;
    private int classement;
    private String ecurie;

    public Pilote(int id, String nom, int points, int classement, String ecurie) {
        this.id = id;
        this.nom = nom;
        this.points = points;
        this.classement = classement;
        this.ecurie = ecurie;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getClassement() { return classement; }
    public void setClassement(int classement) { this.classement = classement; }

    public String getEcurie() { return ecurie; }
    public void setEcurie(String ecurie) { this.ecurie = ecurie; }
}
