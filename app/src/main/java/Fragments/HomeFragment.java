package Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.formula_world.R;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;


    private final List<Integer> colorList = new ArrayList<>();

    public HomeFragment() {
        colorList.add(R.color.white);
        colorList.add(R.color.green);
        colorList.add(R.color.red);
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);

        setupIndividualEditTextClickListeners(gridLayout);

        Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);


        buttonToggleEditText.setOnClickListener(v -> toggleEditTexts());

        updateButtonText();
        toggleEditTexts();
        loadPredictions();

        return view;
    }


    private void setupIndividualEditTextClickListeners(ViewGroup gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                final EditText editText = (EditText) child;
                editText.setOnClickListener(v -> {
                    // Change la couleur du fond de l'EditText lors du clic
                    int colorIndex = editText.getTag() != null ? (int) editText.getTag() : 0;
                    int colorResource = getResources().getIdentifier(String.valueOf(colorList.get((colorIndex + 1) % colorList.size())), "color", requireContext().getPackageName());
                    editText.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResource));
                    editText.setTag((colorIndex + 1) % colorList.size()); // Met à jour l'indice de couleur
                    updateProgressBar();
                });
            }
        }
    }


    private void updateButtonText() {
        Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);
        boolean areEditTextsEnabled = areEditTextsEnabled();
        if (areEditTextsEnabled) {
            buttonToggleEditText.setText(R.string.valider);
        } else {
            buttonToggleEditText.setText(R.string.modifier);
        }
    }

    private boolean areEditTextsEnabled() {
        EditText firstEditText = view.findViewById(R.id.predictionCell_0_0);
        return firstEditText.isEnabled();
    }

    private void toggleEditTexts() {
        if (view != null) {
            ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
            boolean enableEditTexts = !areEditTextsEnabled();

            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View child = gridLayout.getChildAt(i);
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    editText.setEnabled(enableEditTexts);


                }
            }

            Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);
            if(enableEditTexts){
                savePredictions();
            }
            buttonToggleEditText.setText(enableEditTexts ? R.string.valider : R.string.modifier);


        }
    }


    private void savePredictions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                String prediction = editText.getText().toString();
                int backgroundColor = ((ColorDrawable) editText.getBackground()).getColor();
                editor.putString("prediction_" + i, prediction);
                editor.putInt("background_color_" + i, backgroundColor);
            }
        }
        editor.apply();
    }

    private void loadPredictions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            String prediction = preferences.getString("prediction_" + i, "");
            int backgroundColor = preferences.getInt("background_color_" + i, Color.WHITE);
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                editText.setText(prediction);
                editText.setBackgroundColor(backgroundColor);
            }
        }
        updateProgressBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePredictions();
    }

    private void updateProgressBar() {
        // Calcul du nombre de cases vertes et rouges
        int greenCount = 0;
        int redCount = 0;
        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                int color = ((ColorDrawable) editText.getBackground()).getColor();
                if (color == Color.parseColor("#00AC00")) {
                    greenCount++;
                } else if (color == Color.parseColor("#B30000")) {
                    redCount++;
                }
            }
        }

        double total = greenCount + redCount;
        double ratio = total == 0 ? 0 : greenCount / total;

        int progress = (int) (ratio * 100);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
    }


}
