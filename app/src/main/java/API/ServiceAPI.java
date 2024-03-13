package API;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceAPI {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String URL = "http://ergast.com/api/f1";

    //Page classement et prédiction
    public String getDrivers() throws IOException {
        String url = URL + "/2024/drivers.json";
        return getJson(url);
    }

    //Page classement
    public String getDriverDetails(String driverId) throws IOException {
        String url = URL + "/drivers/" + driverId;
        return getJson(url);
    }

    //Page classement
    public String getCurrentRankings() throws IOException {
        String url = URL + "/current/driverStandings.json";
        return getJson(url);
    }

    //Page accueil
    public String getRaces() throws IOException {
        String url = URL + "/current";
        return getJson(url);
    }

    //Page accueil
    public String getRaceDetails(String raceId) throws IOException {
        String url = URL + "/2024/" + raceId;
        return getJson(url);
    }

    //Pour les predictions
    public String getRaceResult(int raceId) throws IOException {
        String url = URL + "/2024/"+ raceId + "/results";
        return getJson(url);
    }

    //Page classement
    public String getTeams() throws IOException {
        String url = URL + "/2024/constructors";
        return getJson(url);
    }

    private String getJson(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }
}

