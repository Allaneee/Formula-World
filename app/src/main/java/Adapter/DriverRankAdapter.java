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
        holder.tvPoints.setText(String.valueOf(currentDriver.getPoints()) + " points");
        holder.tvPosition.setText(String.valueOf(currentDriver.getPosition()));

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDriverClick(currentDriver);
            }
        });
        switch (currentDriver.getFamilyName()) {
            case "Verstappen":
                holder.itemView.setBackgroundResource(R.drawable.verstappen);
                break;
            case "Perez":
                holder.itemView.setBackgroundResource(R.drawable.perez);
                break;
            case "Leclerc":
                holder.itemView.setBackgroundResource(R.drawable.leclerc);
                break;
            case "Russell":
                holder.itemView.setBackgroundResource(R.drawable.russel);
                break;
            case "Piastri":
                holder.itemView.setBackgroundResource(R.drawable.piastri);
                break;
            case "Sainz":
                holder.itemView.setBackgroundResource(R.drawable.sainz);
                break;
            case "Alonso":
                holder.itemView.setBackgroundResource(R.drawable.alonso);
                break;
            case "Norris":
                holder.itemView.setBackgroundResource(R.drawable.norris);
                break;
            case "Hamilton":
                holder.itemView.setBackgroundResource(R.drawable.hamilton);
                break;
            case "Bearman":
                holder.itemView.setBackgroundResource(R.drawable.bearman);
                break;
            case "Hulkenberg":
                holder.itemView.setBackgroundResource(R.drawable.hulkenberg);
                break;
            case "Stroll":
                holder.itemView.setBackgroundResource(R.drawable.stroll);
                break;
            case "Albon":
                holder.itemView.setBackgroundResource(R.drawable.albon);
                break;
            case "Zhou":
                holder.itemView.setBackgroundResource(R.drawable.guanyu);
                break;
            case "Magnussen":
                holder.itemView.setBackgroundResource(R.drawable.magnussen);
                break;
            case "Ricciardo":
                holder.itemView.setBackgroundResource(R.drawable.riccardo);
                break;
            case "Ocon":
                holder.itemView.setBackgroundResource(R.drawable.ocon);
                break;
            case "Tsunoda":
                holder.itemView.setBackgroundResource(R.drawable.tsunoda);
                break;
            case "Sargeant":
                holder.itemView.setBackgroundResource(R.drawable.sargeant);
                break;
            case "Bottas":
                holder.itemView.setBackgroundResource(R.drawable.bottas);
                break;
            case "Gasly":
                holder.itemView.setBackgroundResource(R.drawable.gasly);
                break;
            // Ajoutez d'autres cas selon vos besoins pour d'autres pilotes
            default:
                holder.itemView.setBackgroundResource(R.drawable.verstappen);
                break;
        }

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
