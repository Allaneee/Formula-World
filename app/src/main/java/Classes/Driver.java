package Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import API.ServiceAPI;


public class Driver {
    private static String id; //nom de famille du pilote en minuscule
    private int number, points, ranking;
    private String code, firstname, name, dayofbirth, nationality;
    private Team team;
    private ServiceAPI Api;

    public Driver(String id) throws IOException, JSONException {
        parseFromJson(Api.getDriverDetails(id), id);
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
    public void setRanking(int Json) throws IOException { Api.getCurrentRankings(); }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }



    public static void parseFromJson(String driverJson, String id) throws JSONException, IOException {
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



}
