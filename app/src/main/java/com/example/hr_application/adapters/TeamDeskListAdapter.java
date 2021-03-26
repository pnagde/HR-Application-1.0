package com.example.hr_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.R;
import com.example.hr_application.models.TeamDeskListModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamDeskListAdapter extends RecyclerView.Adapter<TeamDeskListAdapter.TeamListViewHolder>{

    Context context;
    itemClicked itemClicked;
    ArrayList<TeamDeskListModel> listModels;

    public TeamDeskListAdapter(Context context,itemClicked itemClicked, ArrayList<TeamDeskListModel> listModels) {
        this.context = context;
        this.itemClicked = itemClicked;
        this.listModels = listModels;
    }

    @NonNull
    @Override
    public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_list, parent, false);
        return new TeamListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListViewHolder holder, int position) {
        TeamDeskListModel model = listModels.get(position);
        holder.name.setText(model.getTeamName());

    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class TeamListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView circleImageView;
        TextView name,developer,number;
        public TeamListViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPic);
            name =itemView.findViewById(R.id.employeeName);
            developer =itemView.findViewById(R.id.employeeType);
            number =itemView.findViewById(R.id.employeeNumber);
            developer.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            circleImageView.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClicked.onItemClicked(listModels.get(getAdapterPosition()));
        }
    }
    public interface itemClicked{
        void onItemClicked(TeamDeskListModel model);
    }
}
