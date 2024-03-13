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

import com.example.formula_world.R;

import java.util.List;

import Classes.Driver;


public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private List<Driver> DriverList;
    private LayoutInflater inflater;

    // Constructeur de l'adaptateur
    public DriverAdapter(Context context, List<Driver> DriverList) {
        this.inflater = LayoutInflater.from(context);
        this.DriverList = DriverList;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.driver_item, parent, false);
        return new DriverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver currentDriver = DriverList.get(position);
        holder.tvNom.setText(currentDriver.getName());
        holder.tvPoints.setText(String.valueOf(currentDriver.getPoints()));

        @SuppressLint("DiscouragedApi") int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                currentDriver.getId(), "drawable", holder.itemView.getContext().getPackageName());
        holder.ivPhotoDriver.setImageResource(imageResId > 0 ? imageResId : R.drawable.ic_driver_default);
    }

    @Override
    public int getItemCount() {
        return DriverList.size();
    }

    // ViewHolder pour les éléments de la liste
    class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom;
        TextView tvPoints;
        ImageView ivPhotoDriver;

        DriverViewHolder(View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvDriverName);
            tvPoints = itemView.findViewById(R.id.tvDriverPoints);
            ivPhotoDriver = itemView.findViewById(R.id.ivDriverPhoto);
        }
    }
}
