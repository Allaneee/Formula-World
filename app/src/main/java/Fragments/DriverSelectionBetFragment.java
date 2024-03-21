package Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapter.DriverInfoBetAdapter;
import API.ServiceAPI;
import Classes.Driver;
import android.util.Log;


public class DriverSelectionBetFragment extends Fragment {

    private RecyclerView recyclerViewDrivers;
    private DriverInfoBetAdapter driverAdapter;
    private ServiceAPI serviceAPI = new ServiceAPI();
    private int podiumPlace;
    private String grandPrixName;

    public static DriverSelectionBetFragment newInstance(int podiumPlace, String grandPrixName) {
        DriverSelectionBetFragment fragment = new DriverSelectionBetFragment();
        Bundle args = new Bundle();
        args.putInt("PODIUM_PLACE", podiumPlace);
        args.putString("GRAND_PRIX_NAME", grandPrixName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            podiumPlace = getArguments().getInt("PODIUM_PLACE");
            grandPrixName = getArguments().getString("GRAND_PRIX_NAME");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_selection_bet, container, false);
        recyclerViewDrivers = view.findViewById(R.id.recyclerViewPilotes);
        recyclerViewDrivers.setLayoutManager(new LinearLayoutManager(getContext()));

        new FetchDriversTask().execute();

        return view;
    }

    private class FetchDriversTask extends AsyncTask<Void, Void, List<Driver>> {

        @Override
        protected List<Driver> doInBackground(Void... voids) {
            List<Driver> filteredDrivers = new ArrayList<>();
            try {
                String jsonResponse = serviceAPI.getDriversPhotos();
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 1; i < jsonArray.length(); i += 2) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Driver driver = new Driver();
                    driver.setFullName(jsonObject.optString("full_name"));
                    driver.setUrl(jsonObject.optString("headshot_url"));
                    filteredDrivers.add(driver);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return filteredDrivers;
        }

        @Override
        protected void onPostExecute(List<Driver> drivers) {
            super.onPostExecute(drivers);

            driverAdapter = new DriverInfoBetAdapter(getContext(), drivers, this::onDriverClicked);
            recyclerViewDrivers.setAdapter(driverAdapter);
        }

        private void onDriverClicked(Driver driver) {
            saveDriverInfoToJsonFile(driver);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, BettingFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }

    }

    private void saveDriverInfoToJsonFile(Driver driver) {
        try {
            String filename = "driver_selections.json";
            JSONArray savedData;

            try (FileInputStream fis = getContext().openFileInput(filename)) {
                StringBuilder sb = new StringBuilder();
                int character;
                while ((character = fis.read()) != -1) {
                    sb.append((char) character);
                }
                savedData = new JSONArray(sb.toString());
            } catch (FileNotFoundException e) {
                savedData = new JSONArray();
            }

            boolean found = false;
            for (int i = 0; i < savedData.length(); i++) {
                JSONObject selection = savedData.getJSONObject(i);
                if (selection.getString("grand_prix_id").equals(grandPrixName) && selection.getInt("podium_place") == podiumPlace) {
                    selection.put("full_name", driver.getFullName());
                    selection.put("url", driver.getUrl());
                    found = true;
                    break;
                }
            }

            if (!found) {
                JSONObject driverInfo = new JSONObject();
                driverInfo.put("full_name", driver.getFullName());
                driverInfo.put("url", driver.getUrl());
                driverInfo.put("podium_place", podiumPlace);
                driverInfo.put("grand_prix_id", grandPrixName);
                savedData.put(driverInfo);
            }

            try (FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(savedData.toString().getBytes());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void resetDriverSelectionsFile() {
        String filename = "driver_selections.json";
        try (FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
            String initialContent = "[]";
            fos.write(initialContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

