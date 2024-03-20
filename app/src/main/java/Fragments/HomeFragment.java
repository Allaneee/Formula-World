package Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.formula_world.R;

import org.checkerframework.checker.index.qual.LengthOf;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view; // Déclaration de la variable view en tant que membre de classe

    // Liste des couleurs pour représenter les différentes étapes de couleur
    private List<Integer> colorList = new ArrayList<>();

    public HomeFragment() {
        colorList.add(R.color.white); // Blanc
        colorList.add(R.color.green); // Vert
        colorList.add(R.color.red); // Rouge
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Recherche du GridLayout dans le layout du fragment
        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);

        // Configuration des écouteurs de clic pour chaque EditText
        setupIndividualEditTextClickListeners(gridLayout);

        // Recherche du bouton dans la vue inflatée
        Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);

        // Ajout d'un écouteur de clic au bouton
        buttonToggleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditTexts(); // Méthode à implémenter pour activer/désactiver les champs de texte
            }
        });

        updateButtonText(); // Mettre à jour le texte du bouton
        toggleEditTexts(); // Activer/Désactiver les EditText
        loadPredictions(); // Charger les prédictions sauvegardées

        return view;
    }

    // Méthode pour configurer les écouteurs de clic pour chaque EditText individuellement
    private void setupIndividualEditTextClickListeners(ViewGroup gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                final EditText editText = (EditText) child;
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Change la couleur du fond de l'EditText lors du clic
                        int colorIndex = editText.getTag() != null ? (int) editText.getTag() : 0;
                        int colorResource = getResources().getIdentifier(String.valueOf(colorList.get((colorIndex + 1) % colorList.size())), "color", requireContext().getPackageName());
                        editText.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResource));
                        editText.setTag((colorIndex + 1) % colorList.size()); // Met à jour l'indice de couleur
                        updateProgressBar();
                    }
                });
            }
        }
    }


    private void updateButtonText() {
        Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);
        boolean areEditTextsEnabled = areEditTextsEnabled(); // Vérifiez si les EditText sont activés ou non
        if (areEditTextsEnabled) {
            buttonToggleEditText.setText(R.string.valider);
        } else {
            buttonToggleEditText.setText(R.string.modifier);
        }
    }

    private boolean areEditTextsEnabled() {
        EditText firstEditText = view.findViewById(R.id.predictionCell_0_0); // Prenez n'importe quel EditText
        return firstEditText.isEnabled();
    }

    private void toggleEditTexts() {
        if (view != null) {
            ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
            boolean enableEditTexts = !areEditTextsEnabled(); // Inverse l'état actuel des EditTexts

            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View child = gridLayout.getChildAt(i);
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    editText.setEnabled(enableEditTexts);


                }
            }


            // Recherche du bouton dans la vue inflatée
            Button buttonToggleEditText = view.findViewById(R.id.buttonToggleEditText);
            // Définir le texte du bouton en fonction de l'état des EditTexts
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
                int backgroundColor = ((ColorDrawable) editText.getBackground()).getColor(); // Obtenir la couleur de fond
                editor.putString("prediction_" + i, prediction);
                editor.putInt("background_color_" + i, backgroundColor); // Sauvegarder la couleur de fond
            }
        }
        editor.apply();
        Toast.makeText(requireContext(), "Prédictions enregistrées", Toast.LENGTH_SHORT).show();
    }

    private void loadPredictions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        ViewGroup gridLayout = view.findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            String prediction = preferences.getString("prediction_" + i, "");
            int backgroundColor = preferences.getInt("background_color_" + i, Color.WHITE); // Charger la couleur de fond, par défaut blanc
            View child = gridLayout.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                editText.setText(prediction);
                editText.setBackgroundColor(backgroundColor); // Appliquer la couleur de fond
            }
        }
        updateProgressBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePredictions(); // Enregistrer les prédictions lorsque le fragment passe en arrière-plan
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
                if (color == Color.parseColor("#00AC00")) { // Vérifie si la couleur est verte
                    greenCount++;
                } else if (color == Color.parseColor("#B30000")) { // Vérifie si la couleur est rouge
                    redCount++;
                }
            }
        }

        // Calcul du ratio
        double total = greenCount + redCount;
        double ratio = total == 0 ? 0 : greenCount / total;

        // Conversion du ratio en pourcentage
        int progress = (int) (ratio * 100);

        // Mise à jour de la ProgressBar
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
    }


}
