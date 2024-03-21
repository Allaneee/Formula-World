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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import API.ServiceAPI;
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
            // Construisez l'URL pour accéder à l'API Ergast pour les résultats
            URL url = new URL("https://ergast.com/api/f1/current/results.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lire la réponse
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String jsonResponse = builder.toString();
            // Convertir la réponse en JSON
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
                            Log.d("givenName", givenName);
                            if(Objects.equals(familyName, "Pérez")){
                                familyName = "Perez";
                            }
                            if(Objects.equals(familyName, "Hülkenberg")){
                                familyName = "Hulkenberg";
                            }
                            Log.d("familyName", familyName);
                            String fullName = givenName + "_" + familyName; // Concaténer pour obtenir le nom complet
                            Log.d("fullName", fullName);
                            Driver driver = new Driver(); // Supposons que Driver a un constructeur approprié ou des setters
                            driver.setFullName(fullName);

                            Results result = new Results(); // Supposons que Results a un constructeur approprié ou des setters
                            result.setDriver(driver);
                            result.setPosition(resultObject.get("position").getAsInt());
                            result.setGrandPrixId(raceObject.get("raceName").getAsString());
                            Log.d("result1", String.valueOf(result));
                            resultsList.add(result);
                        }
                    }
                }
            }
            Log.d("result", resultsList.toString());
            return resultsList;} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onPostExecute(List<Results> results) {
        listener.onResultsFetched(results);
    }
}
