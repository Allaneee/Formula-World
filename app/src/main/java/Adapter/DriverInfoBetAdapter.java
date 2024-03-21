package Adapter;

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

public class DriverInfoBetAdapter extends RecyclerView.Adapter<DriverInfoBetAdapter.DriverViewHolder> {

    private final Context context;
    private final List<Driver> driverList;
    private final OnDriverClickListener clickListener;

    public DriverInfoBetAdapter(Context context, List<Driver> driverList, OnDriverClickListener listener) {
        this.context = context;
        this.driverList = driverList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver_bet, parent, false);
        return new DriverViewHolder(view, clickListener); // Passer clickListener ici
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.bind(driver);


        holder.itemView.setOnClickListener(v -> { clickListener.onDriverClicked(driver);          });

    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public interface OnDriverClickListener {
        void onDriverClicked(Driver driver);
    }

    class DriverViewHolder extends RecyclerView.ViewHolder {
        private final TextView driverNameTextView;
        private final ImageView driverPhotoImageView;

        DriverViewHolder(View itemView, OnDriverClickListener listener) { // Correction ici
            super(itemView);
            driverNameTextView = itemView.findViewById(R.id.tvDriverName);
            driverPhotoImageView = itemView.findViewById(R.id.ivDriverPhoto);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDriverClicked(driverList.get(position)); // Utilisation correcte du listener
                }
            });
        }

        void bind(Driver driver) {
            driverNameTextView.setText(driver.getFullName());
            Glide.with(itemView.getContext()).load(driver.getUrl()).into(driverPhotoImageView);
        }
    }
}
