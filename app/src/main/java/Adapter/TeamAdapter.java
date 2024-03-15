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

import Classes.Team;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<Team> teamList;
    private LayoutInflater inflater;
    private Context context;

    public TeamAdapter(List<Team> teamList, Context context) {
        this.teamList = teamList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team currentTeam = teamList.get(position);
        holder.tvName.setText(currentTeam.getname());
        holder.tvPoints.setText(String.valueOf(currentTeam.getPoints()));
        holder.tvRanking.setText(String.format(String.valueOf(currentTeam.getranking())));
    }


    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPoints;
        TextView tvRanking;

        TeamViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTeamName);
            tvPoints = itemView.findViewById(R.id.tvTeamPoints);
            tvRanking = itemView.findViewById(R.id.tvTeamRanking);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setTeams(List<Team> teams) {
        this.teamList = teams;
        notifyDataSetChanged();
    }
}
