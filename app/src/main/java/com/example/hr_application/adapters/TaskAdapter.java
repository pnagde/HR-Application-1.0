package com.example.hr_application.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hr_application.MainActivity;
import com.example.hr_application.R;
import com.example.hr_application.models.TaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Thread.sleep;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    private static final String TAG = "TaskAdapter";
    Context context;
    ArrayList<TaskModel> data;
    MainActivity mainActivity;
    DatabaseReference reference;

    public TaskAdapter(Context applicationContext, ArrayList<TaskModel> taskList) {
        this.context = applicationContext;
        this.data = taskList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task__adapter,parent,false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final TaskModel TaskModel = data.get(position);
//        holder.done2.setVisibility(View.INVISIBLE);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tasks").child(TaskModel.getKey()).child("Status");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        if (TaskModel.getDue() != null) {
            Date resultdate = new Date(Long.parseLong(TaskModel.getDue()));
            holder.due.setText((sdf.format(resultdate)).toString() + "");
            holder.task.setText(data.get(position).getTask());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Status", "completed");
        }
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.done.setVisibility(View.GONE);
                v.invalidate();
                Log.d(TAG, "onClick: ");
                holder.done2.setVisibility(View.VISIBLE);
                reference.setValue("completed");
                v.invalidate();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.done2.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView task, due, uploadTime;
        Button done;
        LottieAnimationView done2;
        public Holder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.taskName);
            due = itemView.findViewById(R.id.dueDate);
            done = itemView.findViewById(R.id.button2);
            done2=itemView.findViewById(R.id.doneAnimation_2);
        }

    }
        }