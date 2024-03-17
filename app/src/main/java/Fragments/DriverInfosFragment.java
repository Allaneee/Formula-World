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

import com.example.formula_world.databinding.FragmentDriverInfosBinding;

import java.io.Serializable;
import java.util.Objects;

import Classes.Driver;

public class DriverInfosFragment extends Fragment {

    private TextView tvDriverName;
    private TextView tvNationality;
    private TextView tvPosition;
    private TextView tvWins;
    private TextView tvPoints;
    private FragmentDriverInfosBinding binding;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_infos, container, false);

        tvDriverName = view.findViewById(R.id.tvDriverName);
        tvNationality = view.findViewById(R.id.tvNationality);
        tvPosition = view.findViewById(R.id.tvPosition);
        tvWins = view.findViewById(R.id.tvWins);
        tvPoints = view.findViewById(R.id.tvPoints);

        Bundle args = getArguments();
        if (args != null && args.containsKey("driver")) {
            Driver driver = (Driver) args.getSerializable("driver");

            if (driver != null) {
                tvDriverName.setText(driver.getFamilyName());
                tvPosition.setText("Position: " + driver.getPosition());
                tvWins.setText("Victoire: " + driver.getWins());
                tvPoints.setText("Points: " + driver.getPoints());

                String nationalityKey = driver.getNationality().toLowerCase();
                nationalityKey = Character.toUpperCase(nationalityKey.charAt(0)) + nationalityKey.substring(1);
                @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(nationalityKey, "string", requireContext().getPackageName());
                String nationality = resId != 0 ? getString(resId) : driver.getNationality();
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
