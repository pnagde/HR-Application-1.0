package com.example.hr_application.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hr_application.R;
import com.example.hr_application.models.employeesModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class teamDeskAdapter extends RecyclerView.Adapter<teamDeskAdapter.teamViewHolder> {

    Context context;
    ArrayList<employeesModel> teamDeskModel;

    public teamDeskAdapter(Context context, ArrayList<employeesModel> teamDeskModel) {
        this.context = context;
        this.teamDeskModel = teamDeskModel;
    }

    @NonNull
    @Override
    public teamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_list, parent, false);
        return new teamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull teamViewHolder holder, int position) {
        employeesModel model = teamDeskModel.get(position);
        holder.name.setText(model.getUsername());
        holder.developer.setText(model.getDeveloper());
        try{
            Glide.with(context).load(model.getImageUrl())
                    .placeholder(R.drawable.logo_circle)
                    .into(holder.circleImageView);
        }catch (Exception  e){
            Log.d("userFound", "onDataChange: "+e);
        }
    }

    @Override
    public int getItemCount() {
        return teamDeskModel.size();
    }

    public class teamViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView name,developer,number;
        public teamViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPic);
            name = itemView.findViewById(R.id.employeeName);
            developer = itemView.findViewById(R.id.employeeType);
            number = itemView.findViewById(R.id.employeeNumber);
            number.setVisibility(View.GONE);
        }
    }
}
