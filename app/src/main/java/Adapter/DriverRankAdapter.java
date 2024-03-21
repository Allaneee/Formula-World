package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.formula_world.R;

import java.util.List;

import Classes.Driver;


public class DriverRankAdapter extends RecyclerView.Adapter<DriverRankAdapter.DriverViewHolder> {

    private List<Driver> DriverList;
    private LayoutInflater inflater;
    private Context context;
    private DriverClickListener clickListener;

    public DriverRankAdapter(Context context, List<Driver> driverList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.DriverList = driverList;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_driver_ranking, parent, false);
        return new DriverViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver currentDriver = DriverList.get(position);
        holder.tvNom.setText(currentDriver.getGivenName() + " " + currentDriver.getFamilyName());
        holder.tvPoints.setText(String.valueOf(currentDriver.getPoints()));
        holder.tvPosition.setText(String.valueOf(currentDriver.getPosition()));

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDriverClick(currentDriver);
            }
        });

        // Utilisation de Glide pour charger l'image du pilote à partir de l'URL
        Glide.with(holder.itemView.getContext())
                .load(currentDriver.getUrl()) // Assurez-vous que Driver a une méthode getPhotoUrl() retournant l'URL correcte
                .placeholder(R.drawable.ic_driver_default) // Une image par défaut en attendant le chargement de l'image
                .into(holder.ivPhotoDriver);
    }

    @Override
    public int getItemCount() {
        return DriverList.size();
    }

    // ViewHolder pour les éléments de la liste
    static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom;
        TextView tvPoints;
        TextView tvPosition;
        ImageView ivPhotoDriver;

        DriverViewHolder(View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvDriverName);
            tvPoints = itemView.findViewById(R.id.tvDriverPoints);
            tvPosition = itemView.findViewById(R.id.tvDriverPosition);
            ivPhotoDriver = itemView.findViewById(R.id.ivDriverPhoto);
        }
    }
    public void setDrivers(List<Driver> drivers) {
        this.DriverList = drivers;
    }
    @Override
    public long getItemId(int position) {
        return DriverList.get(position).getDriverId().hashCode();
    }

    public interface DriverClickListener {
        void onDriverClick(Driver driver);
    }

    public void setDriverClickListener(DriverClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
