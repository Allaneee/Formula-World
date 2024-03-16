package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import API.ServiceAPI;
import Classes.Driver;
import Adapter.DriverAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverRankingFragment extends Fragment implements DriverAdapter.DriverClickListener{

    private RecyclerView recyclerView;
    private DriverAdapter driverAdapter;
    private ServiceAPI serviceAPI;

    public DriverRankingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        recyclerView = view.findViewById(R.id.rvDriver);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        driverAdapter = new DriverAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(driverAdapter);

        serviceAPI = new ServiceAPI();
        fetchDriverData();
        driverAdapter.setDriverClickListener(this);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchDriverData() {
        new Thread(() -> {
            try {
                String driverJson = serviceAPI.getCurrentRankings();
                List<Driver> drivers = parseDriverJson(driverJson);
                requireActivity().runOnUiThread(() -> {
                    driverAdapter.setDrivers(drivers);
                    driverAdapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<Driver> parseDriverJson(String driverJson) {
        List<Driver> driverList = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(driverJson, JsonObject.class);
        JsonObject standingsTable = jsonObject.getAsJsonObject("MRData")
                .getAsJsonObject("StandingsTable");
        JsonArray standingsLists = standingsTable.getAsJsonArray("StandingsLists");
        if (standingsLists != null && standingsLists.size() > 0) {
            JsonObject standingsList = standingsLists.get(0).getAsJsonObject();
            JsonArray driverStandings = standingsList.getAsJsonArray("DriverStandings");
            for (JsonElement driverStanding : driverStandings) {
                JsonObject driverObject = driverStanding.getAsJsonObject().getAsJsonObject("Driver");
                Driver driver = new Driver();
                driver.setDriverId((driverObject.get("driverId").getAsString()));
                driver.setFamilyName(((driverObject.get("givenName").getAsString() + " " +
                        driverObject.get("familyName").getAsString())));
                driver.setPoints(driverStanding.getAsJsonObject().get("points").getAsInt());
                driver.setPosition((driverStanding.getAsJsonObject().get("position").getAsInt()));
                driver.setNationality(driverObject.get("nationality").getAsString());
                driverList.add(driver);
            }
        }
        return driverList;
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
}
