package API;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceAPI {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String URL = "http://ergast.com/api/f1";
    private final String DRIVERS_PHOTO_URL = "https://api.openf1.org/v1/drivers";

    // Page classement et prédiction
    public String getDrivers() throws IOException {
        String url = URL + "/2024/drivers.json";
        return getJson(url);
    }

    // Page classement
    public String getDriverDetails(String driverId) throws IOException {
        String url = URL + "/drivers/" + driverId + ".json";
        return getJson(url);
    }

    // Page classement
    public String getCurrentRankings() throws IOException {
        String url = URL + "/current/driverStandings.json";
        return getJson(url);
    }

    // Page accueil
    public String getRaces() throws IOException {
        String url = URL + "/current.json";
        return getJson(url);
    }

    // Page accueil
    public String getRaceDetails(String raceId) throws IOException {
        String url = URL + "/2024/" + raceId + ".json";
        return getJson(url);
    }

    // Pour les prédictions
    public String getRaceResult(int raceId) throws IOException {
        String url = URL + "/2024/" + raceId + "/results.json";
        return getJson(url);
    }

    // Page classement
    public String getTeams() throws IOException {
        String url = URL + "/2024/constructors.json";
        return getJson(url);
    }

    // Méthode pour récupérer les photos des pilotes
    public String getDriversPhotos() throws IOException {
        return getJson(DRIVERS_PHOTO_URL);
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
