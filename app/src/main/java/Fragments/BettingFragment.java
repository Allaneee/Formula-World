package Fragments;


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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingFragment extends Fragment implements OnRacesFetchedListener, OnResultsFetchedListener, GrandPrixAdapter.PodiumPlaceClickListener {

    private ServiceAPI serviceAPI;
    private RecyclerView recyclerRaceView;
    private GrandPrixAdapter grandPrixAdapter;
    private final Map<String, List<Driver>> driverDetails = new HashMap<>();
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
                String circuitId = circuitObject.get("circuitId").getAsString();
                JsonObject locationObject = circuitObject.getAsJsonObject("Location");
                JsonObject firstObject = raceObject.getAsJsonObject("FirstPractice");
                JsonObject secondObject = raceObject.getAsJsonObject("SecondPractice");
                JsonObject thirdObject = raceObject.getAsJsonObject("ThirdPractice");
                JsonObject qualiObject = raceObject.getAsJsonObject("Qualifying");
                Circuit circuit = gson.fromJson(circuitObject, Circuit.class);

                Location location = gson.fromJson(locationObject, Location.class);

                Practice practice1 = gson.fromJson(firstObject, Practice.class);
                Practice practice2 = gson.fromJson(secondObject, Practice.class);
                Practice practice3 = gson.fromJson(thirdObject, Practice.class);
                Practice quali = gson.fromJson(qualiObject, Practice.class);
                circuit.setLocation(location);
                circuit.setCircuitId(circuitId);

                GrandPrix grandPrix = gson.fromJson(raceElement, GrandPrix.class);
                grandPrix.setCircuit(circuit);
                grandPrix.setCircuitId(circuitId);
                grandPrix.setFirstPractice(practice1);
                grandPrix.setSecondPractice(practice2);
                grandPrix.setThirdPractice(practice3);
                grandPrix.setQualifying(quali);



                //resetDriverSelectionsFile();



                grandPrixList.add(grandPrix);
            }

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
        int nextGPIndex = findNextGPIndex(grandPrixList);

        recyclerRaceView.scrollToPosition(nextGPIndex);
    }

    private int findNextGPIndex(List<GrandPrix> grandPrixList) {
        Date currentDate = new Date();

        for (int i = 0; i < grandPrixList.size(); i++) {
            GrandPrix grandPrix = grandPrixList.get(i);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date raceDate = dateFormat.parse(grandPrix.getDate() + "T" + grandPrix.getTime());

                if (raceDate != null && raceDate.after(currentDate)) {

                    return i;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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
        Map<String, List<Driver>> driverSelections = new HashMap<>();
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

                Driver driverInfo = new Driver(url, fullName);

                driverSelections.putIfAbsent(grandPrixId, new ArrayList<>());
                List<Driver> currentSelections = driverSelections.get(grandPrixId);


                while (currentSelections.size() <= podiumPlace) {
                    currentSelections.add(null);
                }
                currentSelections.set(podiumPlace - 1, driverInfo);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        grandPrixAdapter.updateDriverSelections(driverSelections);
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

                        correctPredictionsCount++;
                        break;
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
