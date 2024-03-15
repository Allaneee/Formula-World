package Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import API.ServiceAPI;


public class Driver {
    private static String id; //nom de famille du pilote en minuscule
    private int number, points, ranking;
    private String code, firstname, name, dayofbirth, nationality;
    private Team team;
    private String picURL;
    private ServiceAPI Api;

    public Driver(String id) throws IOException, JSONException {
        parseFromJsonDriver(Api.getDriverDetails(id), id);
    }

    public Driver() {
    }

    public String getId() { return id; }
    public void setId(String id) { Driver.id = id; }

    public int getNumber() { return number; }
    public void setNumber(int id) { this.number = id; }

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
    public void setRanking(int pos) throws IOException { this.ranking = pos; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public static void parseFromJsonDriver(String driverJson, String id) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(driverJson);
        JSONArray DriverStandings = jsonObject.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0)
                .getJSONArray("DriverStandings");

        for (int i = 0; i < DriverStandings.length(); i++) {
            JSONObject driver = DriverStandings.getJSONObject(i).getJSONObject("Driver");
            if (driver.getString("driverId").equals(id)) {
                Driver d = new Driver(id);
                d.setNumber(driver.getInt("permanentNumber"));
                d.setCode(driver.getString("code"));
                d.setFirstname(driver.getString("givenName"));
                d.setName(driver.getString("familyName"));
                d.setDayofbirth(driver.getString("dateOfBirth"));
                d.setNationality(driver.getString("nationality"));
                return;
            }
        }
    }

    public List<Driver> parseDrivers(String jsonResponse) throws JSONException, IOException {
        List<Driver> drivers = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray DriverStandings = jsonObject.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0)
                .getJSONArray("DriverStandings");

        for (int i = 0; i < DriverStandings.length(); i++) {
            JSONObject standing = DriverStandings.getJSONObject(i);
            JSONObject driverInfo = standing.getJSONObject("Driver");

            Driver driver = new Driver();
            driver.setId(driverInfo.getString("driverId"));
            driver.setPoints(standing.getInt("points"));
            driver.setRanking(standing.getInt("position"));

            drivers.add(driver);
        }

        return drivers;
    }

}
