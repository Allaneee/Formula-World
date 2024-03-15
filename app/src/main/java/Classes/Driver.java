package Classes;

public class Driver {
    private String id; // nom de famille du pilote en minuscule, rendu non statique
    private int number, points, ranking;
    private String code, firstname, name, dayofbirth, nationality, picURL;
    private Team team; // Assurez-vous que la classe Team est bien définie quelque part

    // Constructeur par défaut
    public Driver() {
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; } // Modifier pour utiliser l'instance courante

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; } // Corriger le paramètre

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDayofbirth() { return dayofbirth; }
    public void setDayofbirth(String dayofbirth) { this.dayofbirth = dayofbirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getRanking() { return ranking; }
    public void setRanking(int ranking) { this.ranking = ranking; } // Modifier pour utiliser l'instance courante

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }


    public String getPicURL() { return picURL; }
    public void setPicURL(String picURL) { this.picURL = picURL; }

}
