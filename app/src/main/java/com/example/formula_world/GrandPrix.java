package com.example.formula_world;

public class GrandPrix {
    private int id;
    private String nom;
    private String lieu;
    private String date;

    public GrandPrix(int id, String nom, String lieu, String date) {
        this.id = id;
        this.nom = nom;
        this.lieu = lieu;
        this.date = date;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}

