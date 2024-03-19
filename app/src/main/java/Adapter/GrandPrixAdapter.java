package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import java.util.List;

import Classes.GrandPrix.GrandPrix;

public class GrandPrixAdapter extends RecyclerView.Adapter<GrandPrixAdapter.GrandPrixViewHolder> {

    private final List<GrandPrix> grandPrixList;
    private final Context context;
    private final PodiumPlaceClickListener clickListener;

    public GrandPrixAdapter(Context context, List<GrandPrix> grandPrixList, PodiumPlaceClickListener listener) {
        this.context = context;
        this.grandPrixList = grandPrixList;
        this.clickListener = listener;
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

        // Gestion des clics sur les places du podium, appel à l'interface de callback
        holder.itemView.findViewById(R.id.podiumFirst).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 1));
        holder.itemView.findViewById(R.id.podiumSecond).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 2));
        holder.itemView.findViewById(R.id.podiumThird).setOnClickListener(v -> clickListener.onPodiumPlaceClicked(position, 3));
    }

    @Override
    public int getItemCount() {
        return grandPrixList.size();
    }

    static class GrandPrixViewHolder extends RecyclerView.ViewHolder {
        private final TextView grandPrixNameTextView;
        private final TextView grandPrixLocationTextView;
        private final TextView grandPrixDateTextView;

        public GrandPrixViewHolder(@NonNull View itemView) {
            super(itemView);
            grandPrixNameTextView = itemView.findViewById(R.id.textViewGrandPrixName);
            grandPrixLocationTextView = itemView.findViewById(R.id.textViewGrandPrixLocation);
            grandPrixDateTextView = itemView.findViewById(R.id.textViewGrandPrixDate);
        }

        public void bind(GrandPrix grandPrix) {
            grandPrixNameTextView.setText(grandPrix.getRaceName());
            grandPrixLocationTextView.setText(grandPrix.getCircuit().getCircuitName());
            grandPrixDateTextView.setText(grandPrix.getDate());
        }
    }

    public interface PodiumPlaceClickListener {
        void onPodiumPlaceClicked(int position, int podiumPlace);
    }
}
