package Fragments;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.DriverInfoBetAdapter;
import API.ServiceAPI;
import Classes.Driver;
import android.util.Log;


public class DriverSelectionBetFragment extends Fragment {

    private RecyclerView recyclerViewDrivers;
    private DriverInfoBetAdapter driverAdapter;
    private ServiceAPI serviceAPI = new ServiceAPI();
    private int podiumPlace; // Variable pour stocker la place du podium

    // Méthode statique pour créer une nouvelle instance du fragment avec un argument (place du podium)
    public static DriverSelectionBetFragment newInstance(int podiumPlace) {
        DriverSelectionBetFragment fragment = new DriverSelectionBetFragment();
        Bundle args = new Bundle();
        args.putInt("PODIUM_PLACE", podiumPlace); // Passer la place du podium comme argument
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            podiumPlace = getArguments().getInt("PODIUM_PLACE");
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
                // On parcourt le JSONArray en commençant par le premier élément
                for (int i = 1; i < jsonArray.length(); i += 2) { // i += 2 pour sauter un élément sur deux
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
            // Met à jour le RecyclerView avec la liste filtrée des pilotes
            driverAdapter = new DriverInfoBetAdapter(getContext(), drivers);
            recyclerViewDrivers.setAdapter(driverAdapter);
        }
    }

}
