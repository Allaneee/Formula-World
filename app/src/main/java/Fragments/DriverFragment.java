package Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.formula_world.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.DriverAdapter;
import Classes.Driver;

public class DriverFragment extends Fragment {

    private RecyclerView recyclerView;
    private DriverAdapter adapter;
    private List<Driver> DriverList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate le layout pour ce fragment
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        recyclerView = view.findViewById(R.id.rvDriver);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser la liste des Drivers (ceci pourrait venir d'une base de données ou d'une API)
        DriverList = new ArrayList<>();
        adapter = new DriverAdapter(this.getContext(), DriverList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
