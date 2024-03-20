package Fragments;

// BettingFragment.java
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.DriverInfoBetAdapter;
import Adapter.GrandPrixAdapter;
import Async.FetchRacesTask;
import Async.FetchResultTask;
import Async.OnResultFetchedTask;
import Classes.Driver;
import Classes.GrandPrix.Circuit;
import Classes.GrandPrix.Location;
import Classes.GrandPrix.GrandPrix;
import API.ServiceAPI;
import Async.OnRacesFetchedListener;
import Classes.GrandPrix.Practice;

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

public class BettingFragment extends Fragment implements OnRacesFetchedListener, OnResultFetchedTask, GrandPrixAdapter.PodiumPlaceClickListener {

    private ServiceAPI serviceAPI;
    private RecyclerView recyclerRaceView;
    private GrandPrixAdapter grandPrixAdapter;
    private Map<String, List<String>> selectedPilotsForGrandPrix = new HashMap<>();


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
            grandPrixAdapter = new GrandPrixAdapter(getActivity(), grandPrixList, this, selectedPilotsForGrandPrix);
            recyclerRaceView.setAdapter(grandPrixAdapter);
            loadSelectedPilots();
            fetchResult();
            scrollToNextGP(grandPrixList);
        }
    }


    private void fetchGrandPrix() {
        new FetchRacesTask(serviceAPI, this).execute();
    }

    private void fetchResult(){
        new FetchResultTask(serviceAPI, this).execute();
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

            // Structure pour stocker les sélections des pilotes
            Map<String, List<String>> driverSelections = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject selection = jsonArray.getJSONObject(i);
                String grandPrixId = selection.getString("grand_prix_id");
                int podiumPlace = selection.getInt("podium_place");
                String url = selection.getString("url");

                Log.d("load", String.valueOf(selection));
                // Initialiser la liste des URL pour chaque Grand Prix si nécessaire
                driverSelections.putIfAbsent(grandPrixId, new ArrayList<>(Arrays.asList("", "", "")));
                // Assurez-vous de décrémenter podiumPlace pour l'indexation basée sur zéro
                driverSelections.get(grandPrixId).set(podiumPlace - 1, url);
            }

            // Maintenant, mettez à jour le GrandPrixAdapter avec les nouvelles sélections

            Log.d("driverselection", driverSelections.toString());
            grandPrixAdapter.updateDriverSelections(driverSelections);
            grandPrixAdapter.notifyDataSetChanged();


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
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


    @Override
    public void onResultsFetched(String grandPrixJson) {
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
            grandPrixAdapter = new GrandPrixAdapter(getActivity(), grandPrixList, this, selectedPilotsForGrandPrix);
            recyclerRaceView.setAdapter(grandPrixAdapter);
            loadSelectedPilots();
            fetchResult();
            scrollToNextGP(grandPrixList);
        }
    }
}
