package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.formula_world.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.Driver;
import Classes.GrandPrix.GrandPrix;
import Classes.GrandPrix.Results;

public class GrandPrixAdapter extends RecyclerView.Adapter<GrandPrixAdapter.GrandPrixViewHolder> {

    private final List<GrandPrix> grandPrixList;
    private final Context context;
    private final PodiumPlaceClickListener clickListener;
    public static Map<String, List<Driver>> driverSelections = null;
    private static Map<String, List<Results>> actualResults = new HashMap<>();


    public GrandPrixAdapter(Context context, List<GrandPrix> grandPrixList, PodiumPlaceClickListener listener, Map<String, List<Driver>> driverSelections) {
        this.context = context;
        this.grandPrixList = grandPrixList;
        this.clickListener = listener;
        this.driverSelections = driverSelections; // Ajoutez cette ligne
    }

    @NonNull
    @Override
    public GrandPrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grandprix_card, parent, false);
        return new GrandPrixViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrandPrixViewHolder holder, int position) {
        GrandPrix grandPrix = grandPrixList.get(position);
        holder.bind(grandPrix);

        holder.itemView.findViewById(R.id.podiumFirst).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 1, grandPrixList.get(position).getRaceName()));
        holder.itemView.findViewById(R.id.podiumSecond).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 2, grandPrixList.get(position).getRaceName()));
        holder.itemView.findViewById(R.id.podiumThird).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 3, grandPrixList.get(position).getRaceName()));


    }

    @Override
    public int getItemCount() {
        return grandPrixList.size();
    }

    public void setActualResults(Map<String, List<Results>> actualResults) {
        GrandPrixAdapter.actualResults = actualResults;
        notifyDataSetChanged();
    }

    static class GrandPrixViewHolder extends RecyclerView.ViewHolder {
        private final TextView grandPrixNameTextView;
        private final TextView grandPrixDateTextView;
        private final ImageView podiumFirstImageView;
        private final ImageView podiumSecondImageView;
        private final ImageView podiumThirdImageView;
        private final ImageView resultFirstImageView;
        private final ImageView resultSecondImageView;
        private final ImageView resultThirdImageView;


        public GrandPrixViewHolder(@NonNull View itemView) {
            super(itemView);
            grandPrixNameTextView = itemView.findViewById(R.id.textViewGrandPrixName);
            grandPrixDateTextView = itemView.findViewById(R.id.textViewGrandPrixDate);
            podiumFirstImageView = itemView.findViewById(R.id.podiumFirst);
            podiumSecondImageView = itemView.findViewById(R.id.podiumSecond);
            podiumThirdImageView = itemView.findViewById(R.id.podiumThird);

            resultFirstImageView = itemView.findViewById(R.id.feedbackFirst);
            resultSecondImageView = itemView.findViewById(R.id.feedbackSecond);
            resultThirdImageView = itemView.findViewById(R.id.feedbackThird);
        }

        public void bind(GrandPrix grandPrix) {
            grandPrixNameTextView.setText(grandPrix.getRaceName());
            grandPrixDateTextView.setText(grandPrix.getDate());

            resultFirstImageView.setImageResource(R.drawable.baseline_question_mark_24);
            resultSecondImageView.setImageResource(R.drawable.baseline_question_mark_24);
            resultThirdImageView.setImageResource(R.drawable.baseline_question_mark_24);
            // Utilisation des sélections de pilotes pour charger les images dans les ImageView
            List<Driver> selections = driverSelections.get(grandPrix.getRaceName());
            if (selections != null) {
                loadDriverImage(podiumFirstImageView, selections.size() > 0 ? selections.get(0).getUrl() : null);
                loadDriverImage(podiumSecondImageView, selections.size() > 1 ? selections.get(1).getUrl() : null);
                loadDriverImage(podiumThirdImageView, selections.size() > 2 ? selections.get(2).getUrl() : null);
            } else {
                podiumFirstImageView.setImageResource(R.drawable.baseline_add_24);
                podiumSecondImageView.setImageResource(R.drawable.baseline_add_24);
                podiumThirdImageView.setImageResource(R.drawable.baseline_add_24);
            }

            List<Driver> predictions = driverSelections.get(grandPrix.getRaceName());
            List<Results> resultsForThisGrandPrix = actualResults.get(grandPrix.getRaceName());
            if (predictions != null && resultsForThisGrandPrix != null) {
                for (int i = 0; i < predictions.size() && i < resultsForThisGrandPrix.size(); i++) {
                    String predictedDriverFullName = normalizeName(predictions.get(i).getFullName());
                    String actualDriverFullName = normalizeName(resultsForThisGrandPrix.get(i).getDriver().getFullName());
                    Log.d("Predictate_Driver", predictedDriverFullName);
                    Log.d("actualDriverName", actualDriverFullName);

                    if (predictedDriverFullName.equals(actualDriverFullName)) {
                        // La prédiction pour cette position était correcte.
                        updateFeedbackImageViewBasedOnPosition(i, true);
                    } else {
                        // La prédiction pour cette position était incorrecte.
                        updateFeedbackImageViewBasedOnPosition(i, false);
                    }
                }
            }
        }

        private void updateFeedbackImageViewBasedOnPosition(int position, boolean isCorrect) {
            ImageView feedbackImageView;
            switch (position) {
                case 0:
                    feedbackImageView = resultFirstImageView;
                    break;
                case 1:
                    feedbackImageView = resultSecondImageView;
                    break;
                case 2:
                    feedbackImageView = resultThirdImageView;
                    break;
                default:
                    return; // Position non gérée
            }

            if (isCorrect) {
                feedbackImageView.setImageResource(R.drawable.baseline_check_24);
            } else {
                feedbackImageView.setImageResource(R.drawable.baseline_close_24);
            }
        }

        private void loadDriverImage(ImageView imageView, String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext()).load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.baseline_add_24); // Image par défaut si URL est nulle ou vide
            }
        }
        private String normalizeName(String name) {
            // Convertit le nom en minuscules, supprime les espaces et les tirets.
            return name.toLowerCase().replaceAll("\\s+", "").replaceAll("_", "");
        }
    }

    public interface PodiumPlaceClickListener {
        void onPodiumPlaceClicked(int position, int podiumPlace, String grandPrixName);
    }
    public void updateDriverSelections(Map<String, List<Driver>> newSelections) {
        driverSelections = newSelections; // Utilisez driverSelections ici
        Log.d("newSelections", newSelections.toString());
        notifyDataSetChanged();
    }




}
