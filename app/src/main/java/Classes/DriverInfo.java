package Classes;

public class DriverInfo {
    private String driverId; // Correspond au nom de famille du pilote en minuscules
    private int permanentNumber; // Numéro permanent du pilote
    private String code; // Code du pilote
    private String givenName; // Prénom du pilote
    private String familyName; // Nom de famille du pilote
    private String dateOfBirth; // Date de naissance du pilote
    private String nationality; // Nationalité du pilote
    private String url; // URL Wikipedia du pilote

    // Constructeur
    public DriverInfo(String driverId, int permanentNumber, String code, String givenName, String familyName, String dateOfBirth, String nationality, String url) {
        this.driverId = driverId;
        this.permanentNumber = permanentNumber;
        this.code = code;
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.url = url;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getPermanentNumber() {
        return permanentNumber;
    }

    public void setPermanentNumber(int permanentNumber) {
        this.permanentNumber = permanentNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", permanentNumber=" + permanentNumber +
                ", code='" + code + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", nationality='" + nationality + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // Getters et Setters
    // ...
}