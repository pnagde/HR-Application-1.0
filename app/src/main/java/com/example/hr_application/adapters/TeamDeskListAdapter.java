package com.example.hr_application.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.R;
import com.example.hr_application.models.TeamDeskListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamDeskListAdapter extends RecyclerView.Adapter<TeamDeskListAdapter.TeamListViewHolder>{

    Context context;
    itemClicked itemClicked;
    ArrayList<TeamDeskListModel> listModels;
    String status;
    public TeamDeskListAdapter(Context context,itemClicked itemClicked, ArrayList<TeamDeskListModel> listModels,String  status) {
        this.context = context;
        this.itemClicked = itemClicked;
        this.listModels = listModels;
        this.status = status;
    }

    @NonNull
    @Override
    public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_list, parent, false);
        return new TeamListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListViewHolder holder, int position) {
//        Toast.makeText(context, ""+status, Toast.LENGTH_SHORT).show();
        TeamDeskListModel model = listModels.get(position);
        holder.name.setText(model.getTeamName());
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (status.equalsIgnoreCase("super admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete");
                    builder.setMessage("Do you want to delete this team ?");
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("teams").child(model.getKey()).removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, model.getTeamName()+" Team deleted", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
                    builder.setNegativeButton("CANCEL", null);
                    builder.show();

                }else {
                    Toast.makeText(context, "Yo're not super admin.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class TeamListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView circleImageView;
        TextView name,developer,number;
        CardView layout;
        public TeamListViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPic);
            name =itemView.findViewById(R.id.employeeName);
            developer =itemView.findViewById(R.id.employeeType);
            number =itemView.findViewById(R.id.employeeNumber);
            layout =itemView.findViewById(R.id.employeeLayout);
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
