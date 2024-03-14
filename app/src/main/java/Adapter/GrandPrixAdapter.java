package Adapter;// GrandPrixAdapter.java
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import java.util.List;

import Classes.GrandPrix.Circuit;
import Classes.GrandPrix.GrandPrix;

public class GrandPrixAdapter extends RecyclerView.Adapter<GrandPrixAdapter.GrandPrixViewHolder> {

    private List<GrandPrix> grandPrixList;

    public GrandPrixAdapter(List<GrandPrix> grandPrixList) {
        this.grandPrixList = grandPrixList;
    }

    @NonNull
    @Override
    public GrandPrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grandprix_card, parent, false);
        return new GrandPrixViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrandPrixViewHolder holder, int position) {
        GrandPrix grandPrix = grandPrixList.get(position);
        holder.bind(grandPrix);
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
            Circuit circuit = grandPrix.getCircuit();
            Log.d("grandprix", grandPrix.toString());
            grandPrixNameTextView.setText(grandPrix.getRaceName());
            if(circuit!=null) {
                grandPrixLocationTextView.setText(circuit.getCircuitName());
            }
            grandPrixDateTextView.setText(grandPrix.getDate());
        }
    }
}
