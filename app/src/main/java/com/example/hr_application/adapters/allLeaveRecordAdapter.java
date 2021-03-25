package com.example.hr_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.R;
import com.example.hr_application.models.responseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class allLeaveRecordAdapter extends RecyclerView.Adapter<allLeaveRecordAdapter.allLeaveHolder> {
    Context context;
    ArrayList<responseModel> allLeave;

    public allLeaveRecordAdapter(Context context, ArrayList<responseModel> allLeave) {
        this.context = context;
        this.allLeave = allLeave;
    }

    @NonNull
    @Override
    public allLeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_request, parent, false);
        return new allLeaveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull allLeaveHolder holder, int position) {
        responseModel models = allLeave.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        Date resultDate = new Date(Long.parseLong(models.getTime()));
        holder.name.setText(models.getName());
        holder.fromDate.setText(models.getFromDate());
        holder.toDate.setText(models.getToDate());
        holder.reason.setText(models.getReason());
        holder.approve.setText(models.getLeaveResponse());
        holder.time.setText(sdf.format(resultDate));
    }

    @Override
    public int getItemCount() {
        return allLeave.size();
    }

    public class allLeaveHolder extends RecyclerView.ViewHolder {
        TextView name,fromDate,toDate,reason,approve,time;
        Button accept, decline;
        public allLeaveHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.leaveName);
            fromDate = itemView.findViewById(R.id.dateFrom);
            toDate = itemView.findViewById(R.id.dateTo);
            reason = itemView.findViewById(R.id.grantReason);
            approve = itemView.findViewById(R.id.approve);
            time = itemView.findViewById(R.id.timeApproved);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);
            accept.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
        }
    }
}
