package Classes.GrandPrix;

public class Location {
    private String lat;
    private String longitude;
    private String locality;
    private String country;

    // Constructeur, getters et setters

    public Location(String lat, String longitude, String locality, String country) {
        this.lat = lat;
        this.longitude = longitude;
        this.locality = locality;
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @Override
    public String toString() {
        return "Location{" +
                "lat='" + lat + '\'' +
                ", longitude='" + longitude + '\'' +
                ", locality='" + locality + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}