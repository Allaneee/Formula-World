package Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.formula_world.R;


import org.json.JSONException;
import org.json.JSONObject;

import com.example.formula_world.databinding.FragmentTeamInfosBinding;

import java.io.Serializable;
import java.util.Objects;

import Classes.Team;

public class TeamInfosFragment extends Fragment {

    private TextView tvTeamName;
    private TextView tvNationality;
    private TextView tvPosition;
    private TextView tvWins;
    private TextView tvPoints;
    private FragmentTeamInfosBinding binding;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_infos, container, false);

        tvTeamName = view.findViewById(R.id.tvTeamName);
        tvNationality = view.findViewById(R.id.tvNationality);
        tvPosition = view.findViewById(R.id.tvPosition);
        tvWins = view.findViewById(R.id.tvWins);
        tvPoints = view.findViewById(R.id.tvPoints);

        Bundle args = getArguments();
        if (args != null && args.containsKey("Team")) {
            Team Team = (Team) args.getSerializable("Team");

            if (Team != null) {
                tvTeamName.setText(Team.getname());
                Log.d("TeamInfo", "Nationality: " + Team.getNationality());
                tvPosition.setText("Position: " + Team.getranking());
                tvWins.setText("Victoire: " + Team.getWins());
                tvPoints.setText("Points: " + Team.getPoints());

                String nationalityKey = Team.getNationality().toLowerCase();
                nationalityKey = Character.toUpperCase(nationalityKey.charAt(0)) + nationalityKey.substring(1);
                @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(nationalityKey, "string", requireContext().getPackageName());
                String nationality = resId != 0 ? getString(resId) : Team.getNationality();
                tvNationality.setText("Nationalité: " + nationality);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
