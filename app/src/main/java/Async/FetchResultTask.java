package Async;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Classes.Driver;
import Classes.GrandPrix.Results;

public class FetchResultTask extends AsyncTask<Void, Void, List<Results>> {

    private final OnResultsFetchedListener listener;

    public FetchResultTask(OnResultsFetchedListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Results> doInBackground(Void... voids) {
        List<Results> resultsList = new ArrayList<>();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("https://ergast.com/api/f1/current/results.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String jsonResponse = builder.toString();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
            JsonElement racesElement = raceTable.get("Races");

            if (racesElement != null && racesElement.isJsonArray()) {
                JsonArray racesArray = racesElement.getAsJsonArray();

                for (JsonElement raceElement : racesArray) {
                    JsonObject raceObject = raceElement.getAsJsonObject();
                    JsonArray resultsArray = raceObject.getAsJsonArray("Results");
                    if (resultsArray != null) {
                        for (int i = 0; i < Math.min(3, resultsArray.size()); i++) {
                            JsonObject resultObject = resultsArray.get(i).getAsJsonObject();
                            JsonObject driverObject = resultObject.getAsJsonObject("Driver");
                            String givenName = driverObject.get("givenName").getAsString();
                            String familyName = driverObject.get("familyName").getAsString();
                            if(Objects.equals(familyName, "Pérez")){
                                familyName = "Perez";
                            }
                            if(Objects.equals(familyName, "Hülkenberg")){
                                familyName = "Hulkenberg";
                            }
                            String fullName = givenName + "_" + familyName;
                            Driver driver = new Driver();
                            driver.setFullName(fullName);

                            Results result = new Results();
                            result.setDriver(driver);
                            result.setPosition(resultObject.get("position").getAsInt());
                            result.setGrandPrixId(raceObject.get("raceName").getAsString());
                            resultsList.add(result);
                        }
                    }
                }
            }
            return resultsList;} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onPostExecute(List<Results> results) {
        listener.onResultsFetched(results);
    }
}
