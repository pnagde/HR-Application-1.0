package com.example.hr_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.R;
import com.example.hr_application.models.GrantLeaveModels;

import java.util.ArrayList;

public class GrantLeaveAdapter extends RecyclerView.Adapter<GrantLeaveAdapter.viewHolder>{

    Context context;
    ArrayList<GrantLeaveModels> leaveModels;
    onItemClicked itemClicked;

    public GrantLeaveAdapter(Context context, onItemClicked itemClicked,ArrayList<GrantLeaveModels> leaveModels) {
        this.context = context;
        this.leaveModels = leaveModels;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_request, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        GrantLeaveModels models = leaveModels.get(position);
        holder.name.setText(models.getName());
        holder.fromDate.setText(models.getFromDate());
        holder.toDate.setText(models.getToDate());
        holder.reason.setText(models.getReason());
    }

    @Override
    public int getItemCount() {
        return leaveModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,fromDate,toDate,reason,approve,time;
        Button accept, decline;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.leaveName);
            fromDate = itemView.findViewById(R.id.dateFrom);
            toDate = itemView.findViewById(R.id.dateTo);
            reason = itemView.findViewById(R.id.grantReason);
            approve = itemView.findViewById(R.id.approve);
            time = itemView.findViewById(R.id.timeApproved);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);
            approve.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClicked != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            itemClicked.onAcceptClicked(position, leaveModels.get(position));
                        }
                    }
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClicked != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            itemClicked.onDeclineClicked(position, leaveModels.get(position));
                        }
                    }
                }
            });
        }
    }
    public interface onItemClicked{
        void onAcceptClicked(int pos, GrantLeaveModels models);
        void onDeclineClicked(int pos, GrantLeaveModels models);
    }
}
