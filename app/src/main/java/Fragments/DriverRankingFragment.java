package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import API.ServiceAPI;
import Classes.Driver;
import Adapter.DriverRankAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DriverRankingFragment extends Fragment implements DriverRankAdapter.DriverClickListener {

    private RecyclerView recyclerView;
    private DriverRankAdapter driverAdapter;
    private ServiceAPI serviceAPI;

    public DriverRankingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        recyclerView = view.findViewById(R.id.rvDriver);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        driverAdapter = new DriverRankAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(driverAdapter);

        serviceAPI = new ServiceAPI();
        fetchDriverDataAndPhotos();
        driverAdapter.setDriverClickListener(this);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDriverDataAndPhotos() {
        new Thread(() -> {
            try {
                // Récupère le classement des pilotes
                String driverJson = serviceAPI.getCurrentRankings();
                List<Driver> drivers = parseDriverJson(driverJson);

                // Récupère les URLs des photos des pilotes
                String photosJson = serviceAPI.getDriversPhotos();
                Map<String, String> photosMap = new HashMap<>();
                parsePhotosJson(photosJson, photosMap);

                for (Driver driver : drivers) {
                    String normalizedLastName = normalizeName(driver.getFamilyName());
                    String photoUrl = photosMap.get(normalizedLastName);
                    driver.setUrl(photoUrl);
                }
                // Met à jour l'UI sur le thread principal
                requireActivity().runOnUiThread(() -> {
                    driverAdapter.setDrivers(drivers);
                    driverAdapter.notifyDataSetChanged();
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<Driver> parseDriverJson(String driverJson) {
        List<Driver> driverList = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(driverJson, JsonObject.class);
        JsonObject standingsTable = jsonObject.getAsJsonObject("MRData").getAsJsonObject("StandingsTable");
        JsonArray standingsLists = standingsTable.getAsJsonArray("StandingsLists");
        if (standingsLists.size() > 0) {
            JsonObject standingsList = standingsLists.get(0).getAsJsonObject();
            JsonArray driverStandings = standingsList.getAsJsonArray("DriverStandings");
            for (JsonElement driverStanding : driverStandings) {
                JsonObject driverObj = driverStanding.getAsJsonObject().getAsJsonObject("Driver");
                Driver driver = new Driver();
                driver.setDriverId(driverObj.get("driverId").getAsString());
                driver.setGivenName(driverObj.get("givenName").getAsString());
                driver.setFamilyName(driverObj.get("familyName").getAsString());
                normalizeName(driver.getFamilyName());
                if(Objects.equals(driver.getFamilyName(), "Pérez")){
                    driver.setFamilyName("Perez");
                }
                if(Objects.equals(driver.getFamilyName(), "Hülkenberg")){
                    driver.setFamilyName("Hulkenberg");
                }
                driver.setCode(driverObj.has("code") ? driverObj.get("code").getAsString() : "");
                driver.setDateOfBirth(driverObj.has("dateOfBirth") ? driverObj.get("dateOfBirth").getAsString() : "");
                driver.setNationality(driverObj.has("nationality") ? driverObj.get("nationality").getAsString() : "");
                driver.setPoints(driverStanding.getAsJsonObject().has("points") ? driverStanding.getAsJsonObject().get("points").getAsInt() : 0);
                driver.setPosition(driverStanding.getAsJsonObject().has("position") ? driverStanding.getAsJsonObject().get("position").getAsInt() : 0);
                driver.setWins(driverStanding.getAsJsonObject().has("wins") ? driverStanding.getAsJsonObject().get("wins").getAsInt() : 0);

                driverList.add(driver);
            }
        }
        return driverList;
    }


    public void parsePhotosJson(String json, Map<String, String> photosMap) throws JSONException {
        JSONArray photosArray = new JSONArray(json);
        for (int i = 0; i < photosArray.length(); i++) {
            JSONObject photoObject = photosArray.getJSONObject(i);
            String lastName = photoObject.getString("last_name");
            lastName = normalizeName(lastName);
            Log.d("NOM DE FAMILLE NORMALISE2: ", lastName);
            String photoUrl = photoObject.getString("headshot_url");
            photosMap.put(lastName, photoUrl);
        }
    }
    @Override
    public void onDriverClick(Driver driver) {
        DriverInfosFragment detailFragment = new DriverInfosFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("driver", driver);
        detailFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        // Convertit en minuscules, retire les espaces de début et de fin, et supprime les caractères spéciaux.
        return name.trim().toLowerCase().replaceAll("[^a-z0-9]", "");
    }



}
