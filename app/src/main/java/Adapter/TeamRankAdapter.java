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

import Classes.Team;
import Fragments.TeamFragment;

public class TeamRankAdapter extends RecyclerView.Adapter<TeamRankAdapter.TeamViewHolder> {

    private List<Team> teamList;
    private final LayoutInflater inflater;
    private TeamClickListener clickListener;

    public TeamRankAdapter(List<Team> teamList, Context context) {
        this.teamList = teamList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team currentTeam = teamList.get(position);
        holder.tvName.setText(currentTeam.getname());
        holder.tvPoints.setText(currentTeam.getPoints() + " points");
        holder.tvRanking.setText(String.format(String.valueOf(currentTeam.getranking())));
        
        /*holder.itemView.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onTeamClick(currentTeam);
            }
        });*/

        switch (currentTeam.getname()) {
            case "Ferrari":
                holder.imgLogo.setImageResource(R.drawable.ferrari_logo);
                holder.itemView.setBackgroundResource(R.drawable.ferrari);
                break;
            case "Red Bull":
                holder.imgLogo.setImageResource(R.drawable.rb_logo);
                holder.itemView.setBackgroundResource(R.drawable.red_bull);
                break;
            case "McLaren":
                holder.imgLogo.setImageResource(R.drawable.mclaren_logo);
                holder.itemView.setBackgroundResource(R.drawable.mclaren);
                break;
            case "Mercedes":
                holder.imgLogo.setImageResource(R.drawable.mercedes_logo);
                holder.itemView.setBackgroundResource(R.drawable.mercedes);
                break;
            case "Aston Martin":
                holder.imgLogo.setImageResource(R.drawable.aston_logo);
                holder.itemView.setBackgroundResource(R.drawable.aston_martin);
                break;
            case "Williams":
                holder.imgLogo.setImageResource(R.drawable.williams_logo);
                holder.itemView.setBackgroundResource(R.drawable.williams);
                break;
            case "Sauber":
                holder.imgLogo.setImageResource(R.drawable.stake_logo);
                holder.itemView.setBackgroundResource(R.drawable.stake);
                break;
            case "Alpine F1 Team":
                holder.imgLogo.setImageResource(R.drawable.alpine_logo);
                holder.itemView.setBackgroundResource(R.drawable.alpine);
                break;
            case "Haas F1 Team":
                holder.imgLogo.setImageResource(R.drawable.haas_logo);
                holder.itemView.setBackgroundResource(R.drawable.haas);
                break;
            case "RB F1 Team":
                holder.imgLogo.setImageResource(R.drawable.bulls_logo );
                holder.itemView.setBackgroundResource(R.drawable.racing_bulls);
                break;

            default:
                holder.imgLogo.setImageResource(R.drawable.ferrari_logo);
                holder.itemView.setBackgroundResource(R.drawable.aston_martin);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPoints;
        TextView tvRanking;

        ImageView imgLogo;

        TeamViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTeamName);
            tvPoints = itemView.findViewById(R.id.tvTeamPoints);
            tvRanking = itemView.findViewById(R.id.tvTeamRanking);
            imgLogo = itemView.findViewById(R.id.imgLogo);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setTeams(List<Team> teams) {
        this.teamList = teams;
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return teamList.get(position).getId().hashCode();
    }

    public interface TeamClickListener {
        void onTeamClick(Team Team);
    }

    public void setTeamClickListener(TeamFragment clickListener) {
        this.clickListener = clickListener;
    }
    
}
