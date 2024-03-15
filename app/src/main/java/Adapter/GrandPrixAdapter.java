package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import java.util.List;

import Classes.GrandPrix.Circuit;
import Classes.GrandPrix.GrandPrix;
import Fragments.BettingFragment;

public class GrandPrixAdapter extends RecyclerView.Adapter<GrandPrixAdapter.GrandPrixViewHolder> {

    private final List<GrandPrix> grandPrixList;
    private final Context context; // Ajout du contexte

    public GrandPrixAdapter(Context context, List<GrandPrix> grandPrixList) {
        this.context = context;
        this.grandPrixList = grandPrixList;
    }

    @NonNull
    @Override
    public GrandPrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grandprix_card, parent, false);

        // Écouteur de clic pour la première place du podium
        ImageView podiumFirst = view.findViewById(R.id.podiumFirst);
        podiumFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, "1");
            }
        });

        // Écouteur de clic pour la deuxième place du podium
        ImageView podiumSecond = view.findViewById(R.id.podiumSecond);
        podiumSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, "2");
            }
        });

        // Écouteur de clic pour la troisième place du podium
        ImageView podiumThird = view.findViewById(R.id.podiumThird);
        podiumThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, "3");
            }
        });
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
            grandPrixNameTextView.setText(grandPrix.getRaceName());
            Log.d("test driver", circuit.toString());

            if(circuit!=null) {
                grandPrixLocationTextView.setText(circuit.getCircuitName());
            }
            grandPrixDateTextView.setText(grandPrix.getDate());
        }
    }

    private void showPopup(View anchorView, String podiumPosition) {
        // Créer la vue pour la fenêtre contextuelle
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);

        // Initialiser la fenêtre contextuelle
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        // Gérer les événements de clic sur la fenêtre contextuelle
        TextView popupText = popupView.findViewById(R.id.listePilote); // Utilisez l'ID correct
        popupText.setText("Position du podium : " + podiumPosition);

        // Afficher la fenêtre contextuelle au centre de l'écran
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }


}
