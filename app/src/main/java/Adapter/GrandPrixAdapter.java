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

import Classes.GrandPrix.GrandPrix;

public class GrandPrixAdapter extends RecyclerView.Adapter<GrandPrixAdapter.GrandPrixViewHolder> {

    private final List<GrandPrix> grandPrixList;
    private final Context context;
    private final PodiumPlaceClickListener clickListener;
    private static Map<String, List<String>> driverSelections = null;


    public GrandPrixAdapter(Context context, List<GrandPrix> grandPrixList, PodiumPlaceClickListener listener, Map<String, List<String>> driverSelections) {
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

    static class GrandPrixViewHolder extends RecyclerView.ViewHolder {
        private final TextView grandPrixNameTextView;
        private final TextView grandPrixLocationTextView;
        private final TextView grandPrixDateTextView;
        private final ImageView podiumFirstImageView;
        private final ImageView podiumSecondImageView;
        private final ImageView podiumThirdImageView;


        public GrandPrixViewHolder(@NonNull View itemView) {
            super(itemView);
            grandPrixNameTextView = itemView.findViewById(R.id.textViewGrandPrixName);
            grandPrixLocationTextView = itemView.findViewById(R.id.textViewGrandPrixLocation);
            grandPrixDateTextView = itemView.findViewById(R.id.textViewGrandPrixDate);
            podiumFirstImageView = itemView.findViewById(R.id.podiumFirst);
            podiumSecondImageView = itemView.findViewById(R.id.podiumSecond);
            podiumThirdImageView = itemView.findViewById(R.id.podiumThird);
        }

        public void bind(GrandPrix grandPrix) {
            grandPrixNameTextView.setText(grandPrix.getRaceName());
            grandPrixLocationTextView.setText(grandPrix.getCircuit().getCircuitName());
            grandPrixDateTextView.setText(grandPrix.getDate());

            // Utilisation des sélections de pilotes pour charger les images dans les ImageView
            List<String> selections = driverSelections.get(grandPrix.getRaceName());
            if (selections != null) {
                loadDriverImage(podiumFirstImageView, selections.size() > 0 ? selections.get(0) : null);
                loadDriverImage(podiumSecondImageView, selections.size() > 1 ? selections.get(1) : null);
                loadDriverImage(podiumThirdImageView, selections.size() > 2 ? selections.get(2) : null);
            } else {
                podiumFirstImageView.setImageResource(R.drawable.baseline_add_24);
                podiumSecondImageView.setImageResource(R.drawable.baseline_add_24);
                podiumThirdImageView.setImageResource(R.drawable.baseline_add_24);
            }
        }

        private void loadDriverImage(ImageView imageView, String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext()).load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.baseline_add_24); // Image par défaut si URL est nulle ou vide
            }
        }


    }

    public interface PodiumPlaceClickListener {
        void onPodiumPlaceClicked(int position, int podiumPlace, String grandPrixName);
    }
    public void updateDriverSelections(Map<String, List<String>> newSelections) {
        driverSelections = newSelections; // Utilisez driverSelections ici
        Log.d("newSelections", newSelections.toString());
        notifyDataSetChanged();
    }

}
