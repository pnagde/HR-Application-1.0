package com.example.hr_application.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hr_application.MyAccountActivity;
import com.example.hr_application.R;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class employeesAdapter extends RecyclerView.Adapter<employeesAdapter.employeesViewHolder>{

    Context context;
    ArrayList<employeesModel> models;
    String status;

    public employeesAdapter(Context context, ArrayList<employeesModel> models, String status) {
        this.context = context;
        this.models = models;
        this.status=status;
    }

    @NonNull
    @Override
    public employeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_list, parent, false);
        return new employeesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull employeesViewHolder holder, int position) {
        employeesModel model1 = models.get(position);
        holder.username.setText(model1.getUsername());
        holder.number.setText(model1.getNumber());
        holder.developer.setText(model1.getDeveloper());
        Glide.with(context).load(model1.getImageUrl())
                .placeholder(R.drawable.logo_circle)
                .into(holder.circleImageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MyAccountActivity.class);
                i.putExtra("uid", models.get(position).getUserid()+"");
                i.putExtra("editable","yes");
                i.putExtra("status", status);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class employeesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView username, developer, number;
        CardView layout;
        public employeesViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPic);
            username = itemView.findViewById(R.id.employeeName);
            developer = itemView.findViewById(R.id.employeeType);
            number= itemView.findViewById(R.id.employeeNumber);
            layout=itemView.findViewById(R.id.employeeLayout);
        }
    }
}
