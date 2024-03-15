package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formula_world.R;

import java.util.List;

import Classes.DriverInfo;

public class DriverInfoAdapter extends RecyclerView.Adapter<DriverInfoAdapter.DriverViewHolder> {

    private final Context context;
    private final List<DriverInfo> driverList;

    public DriverInfoAdapter(Context context, List<DriverInfo> driverList) {
        this.context = context;
        Log.d("driverlist", String.valueOf(driverList.size()));
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("test," , "test");
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        DriverInfo driver = driverList.get(position);
        holder.bind(driver);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    static class DriverViewHolder extends RecyclerView.ViewHolder {
        private final TextView driverNameTextView;
        private final TextView driverNationalityTextView;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverNameTextView = itemView.findViewById(R.id.textViewDriverName);
            driverNationalityTextView = itemView.findViewById(R.id.textViewDriverNationality);
        }

        public void bind(DriverInfo driver) {
            Log.d("test driver", driver.toString());
            driverNameTextView.setText(driver.getGivenName() + " " + driver.getFamilyName());
            driverNationalityTextView.setText(driver.getNationality());
        }
    }
}

