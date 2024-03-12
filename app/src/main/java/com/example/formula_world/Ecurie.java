package com.example.formula_world;

public class Ecurie {
    private int id;
    private String nom;
    private int points;
    private int classement;

    public Ecurie(int id, String nom, int points, int classement) {
        this.id = id;
        this.nom = nom;
        this.points = points;
        this.classement = classement;
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
}

