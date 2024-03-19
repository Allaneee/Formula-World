package Classes;

import java.io.Serializable;

public class Driver implements Serializable {
    private String driverId; // Correspond à "driverId" dans le JSON
    private int permanentNumber, points, position; // position correspond à "position" dans le JSON
    private String code, givenName, familyName, dateOfBirth, nationality, url, fullName; // Ajout de l'URL
    private int wins;
    private Team[] constructors; // Correspond aux "Constructors" dans le JSON, supposez que Team est correctement défini

    // Constructeur par défaut
    public Driver() {
    }

    // Getters et setters
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public int getPermanentNumber() { return permanentNumber; }
    public void setPermanentNumber(int permanentNumber) { this.permanentNumber = permanentNumber; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Team[] getConstructors() { return constructors; }
    public void setConstructors(Team[] constructors) { this.constructors = constructors; }
}
