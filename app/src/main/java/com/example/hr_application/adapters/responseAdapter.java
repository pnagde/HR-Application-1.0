package com.example.hr_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hr_application.R;
import com.example.hr_application.models.responseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class responseAdapter extends RecyclerView.Adapter<responseAdapter.responseViewHolder>{

    Context context;
    ArrayList<responseModel> responseData;
    public responseAdapter(Context context, ArrayList<responseModel> responseData) {
        this.context = context;
        this.responseData = responseData;
    }

    @NonNull
    @Override
    public responseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_response, parent, false);
        return new responseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull responseViewHolder holder, int position) {
        responseModel model=responseData.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY,HH:mm", Locale.getDefault());
        Date resultDate = new Date(Long.parseLong(model.getTime()));
        if (!Objects.equals(model.getLeaveResponse(), "Your Leave is Granted")) {
            holder.correct.setVisibility(View.INVISIBLE);
            holder.wrong.setVisibility(View.VISIBLE);
        } else {
            holder.correct.setVisibility(View.VISIBLE);
            holder.wrong.setVisibility(View.INVISIBLE);
        }
        holder.responseReceive.setText(model.getLeaveResponse());
        holder.from.setText(model.getFromDate());
        holder.to.setText(model.getToDate());
        holder.time.setText(sdf.format(resultDate));



    }

    @Override
    public int getItemCount() {
        return responseData.size();
    }

    public class responseViewHolder extends RecyclerView.ViewHolder {
        TextView responseReceive,from,to,time;
        ImageView correct,wrong;
        View linearLayout;
        public responseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseReceive = itemView.findViewById(R.id.responseReceive);
            from = itemView.findViewById(R.id.startDate);
            to = itemView.findViewById(R.id.endDate);
            time = itemView.findViewById(R.id.timeClicked);
            linearLayout = itemView.findViewById(R.id.mainLayout);
            correct = itemView.findViewById(R.id.correct);
            wrong = itemView.findViewById(R.id.wrong);
        }
    }
}
