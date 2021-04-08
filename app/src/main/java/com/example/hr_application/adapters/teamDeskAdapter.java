package com.example.hr_application.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hr_application.R;
import com.example.hr_application.models.employeesModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class teamDeskAdapter extends RecyclerView.Adapter<teamDeskAdapter.teamViewHolder> {

    Context context;
    ArrayList<employeesModel> teamDeskModel;
    String status;
    String teamKey;
    public teamDeskAdapter(Context context, ArrayList<employeesModel> teamDeskModel,String status,String teamKey) {
        this.context = context;
        this.teamDeskModel = teamDeskModel;
        this.status=status;
        this.teamKey=teamKey;
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(model.getUserid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final employeesModel user = snapshot.getValue(employeesModel.class);
                try{
                    holder.name.setText(user.getUsername());//exception
                    holder.developer.setText(user.getDeveloper());//exception
                    Glide.with(context).load(user.getImageUrl())
                            .placeholder(R.drawable.logo_circle)
                            .into(holder.circleImageView);
                }catch (Exception  e){
                    Log.d("userFound", "onDataChange: "+e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (status.equalsIgnoreCase("super admin")){
                    if (model.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        Toast.makeText(context, "Cannot delete your own account ", Toast.LENGTH_SHORT).show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete");
                        builder.setMessage("Do you want to delete this employee ?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("teams").child(teamKey).child("members").child(model.getUserid()).setValue(null)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, model.getUsername()+" removed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.show();
                    }
                }else {
                    Toast.makeText(context, "Yo're not admin", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return teamDeskModel.size();
    }

    public class teamViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView name,developer,number;
        CardView layout;
        public teamViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.userPic);
            name = itemView.findViewById(R.id.employeeName);
            developer = itemView.findViewById(R.id.employeeType);
            number = itemView.findViewById(R.id.employeeNumber);
            number.setVisibility(View.GONE);
            layout = itemView.findViewById(R.id.employeeLayout);
        }
    }
}
