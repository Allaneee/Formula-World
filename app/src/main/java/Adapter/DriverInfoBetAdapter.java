package Adapter;

import android.annotation.SuppressLint;
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

import java.util.List;

import Classes.Driver;

public class DriverInfoBetAdapter extends RecyclerView.Adapter<DriverInfoBetAdapter.DriverViewHolder> {

    private final Context context;
    private final List<Driver> driverList;

    public DriverInfoBetAdapter(Context context, List<Driver> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver_bet, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.bind(driver);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    static class DriverViewHolder extends RecyclerView.ViewHolder {
        private final TextView driverNameTextView;
        private final ImageView driverPhotoUrl;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverNameTextView = itemView.findViewById(R.id.tvDriverName);
            driverPhotoUrl = itemView.findViewById(R.id.ivDriverPhoto);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Driver driver) {
            Log.d("test driver", driver.toString());
            driverNameTextView.setText(driver.getFullName());
            Glide.with(itemView.getContext()).load(driver.getUrl()).into(driverPhotoUrl); // Charger l'image à partir de l'URL

        }
    }
}

