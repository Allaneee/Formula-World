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
import Classes.Team;
import Adapter.TeamAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment implements TeamAdapter.TeamClickListener {

    private RecyclerView recyclerView;
    private TeamAdapter teamAdapter;
    private ServiceAPI serviceAPI;

    public TeamFragment() {    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        recyclerView = view.findViewById(R.id.rvTeam);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        teamAdapter = new TeamAdapter(new ArrayList<>(), requireContext());
        recyclerView.setAdapter(teamAdapter);

        serviceAPI = new ServiceAPI();
        fetchTeamData();
        teamAdapter.setTeamClickListener(this);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchTeamData() {
        new Thread(() -> {
            try {
                String teamJson = serviceAPI.getTeams();
                List<Team> teams = parseTeamJson(teamJson);
                requireActivity().runOnUiThread(() -> {
                    teamAdapter.setTeams(teams);
                    teamAdapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private List<Team> parseTeamJson(String teamJson) {

        List<Team> teamList = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(teamJson, JsonObject.class);
        JsonObject mrData = jsonObject.getAsJsonObject("MRData");
        if (mrData != null) {
            JsonObject standingsTable = mrData.getAsJsonObject("StandingsTable");
            if (standingsTable != null) {
                JsonArray standingsLists = standingsTable.getAsJsonArray("StandingsLists");
                if (standingsLists != null && standingsLists.size() > 0) {
                    JsonObject standingsList = standingsLists.get(0).getAsJsonObject();
                    JsonArray teamStandings = standingsList.getAsJsonArray("ConstructorStandings");
                    for (JsonElement teamStanding : teamStandings) {
                        JsonObject teamObject = teamStanding.getAsJsonObject().getAsJsonObject("Constructor");
                        Team team = new Team();
                        team.setId(teamObject.get("constructorId").getAsString());
                        team.setname(teamObject.get("name").getAsString());
                        team.setPoints(Integer.parseInt(teamStanding.getAsJsonObject().get("points").getAsString()));
                        team.setranking(Integer.parseInt(teamStanding.getAsJsonObject().get("position").getAsString()));
                        team.setNationality(teamObject.get("nationality").getAsString());
                        team.setWins(Integer.parseInt(teamStanding.getAsJsonObject().get("wins").getAsString()));
                        teamList.add(team);
                    }
                }
            }
        }
        return teamList;
    }
    @Override
    public void onTeamClick(Team team) {
        TeamInfosFragment detailFragment = new TeamInfosFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Team", team);
        detailFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
