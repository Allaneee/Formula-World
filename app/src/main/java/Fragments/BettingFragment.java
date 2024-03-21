package Fragments;

// BettingFragment.java
import static Adapter.GrandPrixAdapter.driverSelections;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.GrandPrixAdapter;
import Async.FetchRacesTask;
import Async.FetchResultTask;
import Async.OnResultsFetchedListener;
import Classes.Driver;
import Classes.GrandPrix.Circuit;
import Classes.GrandPrix.Location;
import Classes.GrandPrix.GrandPrix;
import API.ServiceAPI;
import Async.OnRacesFetchedListener;
import Classes.GrandPrix.Practice;
import Classes.GrandPrix.Predictions;
import Classes.GrandPrix.Results;

import com.example.formula_world.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingFragment extends Fragment implements OnRacesFetchedListener, OnResultsFetchedListener, GrandPrixAdapter.PodiumPlaceClickListener {

    private ServiceAPI serviceAPI;
    private RecyclerView recyclerRaceView;
    private GrandPrixAdapter grandPrixAdapter;
    private Map<String, List<Driver>> driverDetails = new HashMap<>();
    private List<Results> listResults;


    public BettingFragment() {
        // Required empty public constructor
    }

    public static BettingFragment newInstance() {
        return new BettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceAPI = new ServiceAPI();
        fetchGrandPrix();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_betting, container, false);

        recyclerRaceView = view.findViewById(R.id.recyclerViewBetting);
        recyclerRaceView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onRacesFetched(String grandPrixJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(grandPrixJson, JsonObject.class);
        JsonObject raceTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("RaceTable");
        JsonElement racesElement = raceTable.get("Races");

        if (racesElement != null && racesElement.isJsonArray()) {
            List<GrandPrix> grandPrixList = new ArrayList<>();
            JsonArray racesArray = racesElement.getAsJsonArray();

            for (JsonElement raceElement : racesArray) {
                JsonObject raceObject = raceElement.getAsJsonObject();
                JsonObject circuitObject = raceObject.getAsJsonObject("Circuit");
                JsonObject locationObject = circuitObject.getAsJsonObject("Location");
                JsonObject firstObject = raceObject.getAsJsonObject("FirstPractice");
                JsonObject secondObject = raceObject.getAsJsonObject("SecondPractice");
                JsonObject thirdObject = raceObject.getAsJsonObject("ThirdPractice");
                JsonObject qualiObject = raceObject.getAsJsonObject("Qualifying");
                //Log.d("Location", locationObject.toString());
                Circuit circuit = gson.fromJson(circuitObject, Circuit.class);
                Location location = gson.fromJson(locationObject, Location.class);
                Practice practice1 = gson.fromJson(firstObject, Practice.class);
                Practice practice2 = gson.fromJson(secondObject, Practice.class);
                Practice practice3 = gson.fromJson(thirdObject, Practice.class);
                Practice quali = gson.fromJson(qualiObject, Practice.class);
                location.setLongitude(String.valueOf((locationObject.get("long"))));
                circuit.setLocation(location);
                // Créez un objet GrandPrix avec les autres informations nécessaires
                GrandPrix grandPrix = gson.fromJson(raceElement, GrandPrix.class);
                grandPrix.setCircuit(circuit);
                grandPrix.setFirstPractice(practice1);
                grandPrix.setSecondPractice(practice2);
                grandPrix.setThirdPractice(practice3);
                grandPrix.setQualifying(quali);
                // Ajoutez d'autres informations au besoin

                grandPrixList.add(grandPrix);
            }

            // Initialisez et attachez l'adaptateur au RecyclerView
            grandPrixAdapter = new GrandPrixAdapter(getActivity(), grandPrixList, this, driverDetails);
            loadSelectedPilots();
            new FetchResultTask(this).execute();
            scrollToNextGP(grandPrixList);
        }
    }


    private void fetchGrandPrix() {
        new FetchRacesTask(serviceAPI, this).execute();
    }


    private void scrollToNextGP(List<GrandPrix> grandPrixList) {
        // Logique pour trouver l'index du prochain Grand Prix
        int nextGPIndex = findNextGPIndex(grandPrixList);

        // Faites défiler jusqu'au prochain Grand Prix
        recyclerRaceView.scrollToPosition(nextGPIndex);
    }

    private int findNextGPIndex(List<GrandPrix> grandPrixList) {
        // Obtenez la date et l'heure actuelles
        Date currentDate = new Date();

        // Parcourez la liste des Grands Prix pour trouver le prochain
        for (int i = 0; i < grandPrixList.size(); i++) {
            GrandPrix grandPrix = grandPrixList.get(i);

            // Convertissez la date du Grand Prix en objet Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date raceDate = dateFormat.parse(grandPrix.getDate() + "T" + grandPrix.getTime());

                // Comparez la date du Grand Prix avec la date actuelle
                if (raceDate != null && raceDate.after(currentDate)) {
                    // Si la date du Grand Prix est postérieure à la date actuelle, c'est le prochain
                    return i;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Si aucun prochain Grand Prix n'est trouvé, retournez 0 (le premier Grand Prix)
        return 0;
    }

    @Override
    public void onPodiumPlaceClicked(int position, int podiumPlace, String grandPrixName) {
        showDriverSelectionFragment(podiumPlace, grandPrixName);
    }

    private void showDriverSelectionFragment(int podiumPlace, String grandPrixName) {
        DriverSelectionBetFragment fragment = DriverSelectionBetFragment.newInstance(podiumPlace, grandPrixName);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadSelectedPilots() {
        String filename = "driver_selections.json";
        try {
            FileInputStream fis = requireContext().openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject selection = jsonArray.getJSONObject(i);
                String grandPrixId = selection.getString("grand_prix_id");
                int podiumPlace = selection.getInt("podium_place");
                String url = selection.getString("url");
                String fullName = selection.getString("full_name");

                Driver driver = new Driver(fullName, url); // Ajustez le constructeur de Driver pour accepter fullName et url

                driverDetails.putIfAbsent(grandPrixId, new ArrayList<>());
                List<Driver> details = driverDetails.get(grandPrixId);

                // Assurez-vous que la liste est assez grande
                while (details.size() <= podiumPlace) {
                    details.add(null); // Ajoutez des placeholders
                }
                details.set(podiumPlace - 1, driver);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        // Vous pouvez maintenant utiliser `driverDetails` pour ajuster les sélections dans votre adapter
        grandPrixAdapter.updateDriverSelections(driverDetails);
    }



    private List<Predictions> selectBetDriver() {
        String filename = "driver_selections.json";
        List<Predictions> predictionsList = new ArrayList<>();
        try {
            FileInputStream fis = requireContext().openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject predictionObject = jsonArray.getJSONObject(i);
                String grandPrixId = predictionObject.getString("grand_prix_id");
                int podiumPlace = predictionObject.getInt("podium_place");
                String fullName = predictionObject.getString("full_name");
                String url = predictionObject.getString("url");

                // Création et configuration de l'objet Driver
                Driver driver = new Driver();
                driver.setFullName(fullName);
                driver.setUrl(url);

                // Création et configuration de l'objet Predictions
                Predictions prediction = new Predictions();
                prediction.setGrandPrixId(grandPrixId);
                prediction.setPosition(podiumPlace);
                prediction.setDriver(driver);

                predictionsList.add(prediction);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Log.d("prediction4", predictionsList.toString());
        return predictionsList;
    }



    private void resetDriverSelectionsFile() {
        String filename = "driver_selections.json";
        try (FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
            // Structure JSON de base que vous souhaitez utiliser pour initialiser le fichier
            String initialContent = "[]"; // Pour un objet JSON vide
            // String initialContent = "[]"; // Pour un tableau JSON vide
            // Ou initialisez avec une structure plus complexe si nécessaire
            fos.write(initialContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void comparaisonPrediction() {

        List<Predictions> listPredictions = selectBetDriver(); // Charge les prédictions à partir du fichier interne

        if (listPredictions.isEmpty()) {
            recyclerRaceView.setAdapter(grandPrixAdapter);
            grandPrixAdapter.notifyDataSetChanged();
            return;
        }
        Log.d("resultats", listResults.toString());
        Log.d("predictions5", listPredictions.toString());


        int correctPredictionsCount = 0;
        int totalPredictions = listPredictions.size();

        // Utiliser une map pour regrouper les résultats par grandPrixId
        Map<String, List<Results>> resultsMap = new HashMap<>();
        for (Results result : listResults) {
            resultsMap.putIfAbsent(result.getGrandPrixId(), new ArrayList<>());
            resultsMap.get(result.getGrandPrixId()).add(result);
        }

        // Parcourir chaque prédiction
        for (Predictions prediction : listPredictions) {
            List<Results> resultsForThisGrandPrix = resultsMap.get(prediction.getGrandPrixId());
            if (resultsForThisGrandPrix != null) {
                for (Results result : resultsForThisGrandPrix) {
                    prediction.getDriver().setFullName(normalizeName(prediction.getDriver().getFullName()));
                    result.getDriver().setFullName(normalizeName(result.getDriver().getFullName()));
                    if (prediction.getPosition() == result.getPosition() && prediction.getDriver().getFullName().equals(result.getDriver().getFullName())) {
                        Log.d("comparaison1", prediction.getDriver().getFullName());
                        Log.d("comparaison2", result.getDriver().getFullName());

                        correctPredictionsCount++;
                        break; // Trouvé la correspondance, pas besoin de chercher d'autres résultats pour cette prédiction
                    }
                }
            }
        }
        recyclerRaceView.setAdapter(grandPrixAdapter);
        grandPrixAdapter.setActualResults(resultsMap);
        grandPrixAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResultsFetched(List<Results> results) {
        // Stockez les résultats dans un champ de la classe pour un accès ultérieur
        this.listResults = results;
        // Maintenant que vous avez les résultats, lancez la comparaison
        comparaisonPrediction();
    }

    private String normalizeName(String name) {
        // Convertit le nom en minuscules, supprime les espaces et les tirets.
        return name.toLowerCase().replaceAll("\\s+", "").replaceAll("_", "");
    }
}
