package com.example.hr_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hr_application.NewsFeed;
import com.example.hr_application.R;
import com.example.hr_application.models.Feed;
import com.example.hr_application.models.employeesModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.newsFeed> {

    private static final String TAG = "FeedAdapter";
    Context context;
    ArrayList<Feed> models;

    public FeedAdapter(NewsFeed context, ArrayList<Feed> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public newsFeed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_addapter, parent, false);
        return new FeedAdapter.newsFeed(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull newsFeed holder, int position) {
        Feed model1 = models.get(position);
        if (model1.getPosition().equals("Admin")){
            holder.developer.setTextColor(Color.parseColor("#BC0000"));
            holder.username.setTextColor(Color.parseColor("#BC0000"));
            holder.date.setTextColor(Color.parseColor("#BC0000"));
        }
        if (model1.getPosition().equals("HR")){
            holder.developer.setTextColor(Color.parseColor("#6800B3"));
            holder.username.setTextColor(Color.parseColor("#6800B3"));
            holder.date.setTextColor(Color.parseColor("#6800B3"));
            Log.d(TAG, "onBindViewHolder: "+"red");
        }
        if (model1.getPosition().equals("Android")){
            holder.developer.setTextColor(Color.parseColor("#00A115"));
            holder.username.setTextColor(Color.parseColor("#00A115"));
            holder.date.setTextColor(Color.parseColor("#00A115"));
        }
        holder.username.setText(model1.getByName());
        holder.date.setText(model1.getDate());
        holder.developer.setText(model1.getPosition());
        holder.content.setText(model1.getInformation());
        Glide.with(context).load(model1.getImageUrl())
                .placeholder(R.drawable.logo_circle)
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class newsFeed extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView username, developer, date;
        TextView content;
        CardView layout;
        public newsFeed(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imageView2);
            username = itemView.findViewById(R.id.UserName_1);
            developer = itemView.findViewById(R.id.position_a);
            date= itemView.findViewById(R.id.datePosted);
            content=itemView.findViewById(R.id.content_msg);
            layout=itemView.findViewById(R.id.card_view_main);
        }
    }
}
