package Fragments;

// BettingFragment.java
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BettingFragment extends Fragment implements OnRacesFetchedListener {

    private ServiceAPI serviceAPI;
    private RecyclerView recyclerView;
    private GrandPrixAdapter grandPrixAdapter;

    public BettingFragment() {
        // Required empty public constructor
    }

    public static BettingFragment newInstance(String param1, String param2) {
        BettingFragment fragment = new BettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceAPI = new ServiceAPI();
        fetchGrandPrix(); // Chargez la liste des Grands Prix (paris) depuis l'API
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_betting, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBetting);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

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
                Log.d("Circuit", circuit.toString());
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
            grandPrixAdapter = new GrandPrixAdapter(grandPrixList);
            recyclerView.setAdapter(grandPrixAdapter);


            // Faire défiler jusqu'au prochain Grand Prix
            scrollToNextGP(grandPrixList);
        }
    }

    private List<GrandPrix> parseGrandPrixJson(String grandPrixJson) {
        List<GrandPrix> grandPrixList = new ArrayList<>();
        Log.d("test",grandPrixJson);
        return grandPrixList;
    }
    private void fetchGrandPrix() {
        // Exécutez la tâche asynchrone pour récupérer les données des Grands Prix depuis l'API
        new FetchRacesTask(serviceAPI, this).execute();
    }

    private void scrollToNextGP(List<GrandPrix> grandPrixList) {
        // Logique pour trouver l'index du prochain Grand Prix
        int nextGPIndex = findNextGPIndex(grandPrixList);

        // Faites défiler jusqu'au prochain Grand Prix
        recyclerView.scrollToPosition(nextGPIndex);
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

}
