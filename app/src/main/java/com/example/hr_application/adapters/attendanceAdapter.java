package com.example.hr_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.AttendanceActivity;
import com.example.hr_application.R;
import com.example.hr_application.models.attendanceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class attendanceAdapter extends RecyclerView.Adapter<attendanceAdapter.Holder> {

    TextView filter1;
    Context context;
    ArrayList<attendanceModel> data;
    AttendanceActivity attendanceActivity;
    public attendanceAdapter() {
    }

    public attendanceAdapter(Context context, ArrayList<attendanceModel> data,TextView filter1) {
        this.context = context;
        this.data = data;
        this.filter1=filter1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new attendanceAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final attendanceModel attendanceModel =data.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(Long.parseLong(attendanceModel.getTime()));
        holder.time.setText(sdf.format(resultdate));
        holder.status.setText(data.get(position).getStatus());


    }
    @Override
    public int getItemCount() {
        if (data.size() != 0) {
            return data.size();
        }
        filter1.setText("No data found");
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView status, time;
        public Holder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);


        }
    }
}