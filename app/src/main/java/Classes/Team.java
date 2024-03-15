package Classes;

public class Team {
    private String id;
    private String name;
    private int points;
    private int ranking;

    public Team() {
    }
    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getname() { return name; }
    public void setname(String name) { this.name = name; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getranking() { return ranking; }
    public void setranking(int ranking) { this.ranking = ranking; }
}

