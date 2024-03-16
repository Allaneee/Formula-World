package Classes;

import java.io.Serializable;

public class Team implements Serializable {
    private String teamid;
    private String name, nationality;
    private int points;
    private int ranking;
    private int wins;

    public Team() {
    }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }
    public String getId() { return teamid; }
    public void setId(String id) { this.teamid = id; }

    public String getname() { return name; }
    public void setname(String name) { this.name = name; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getranking() { return ranking; }
    public void setranking(int ranking) { this.ranking = ranking; }
}

