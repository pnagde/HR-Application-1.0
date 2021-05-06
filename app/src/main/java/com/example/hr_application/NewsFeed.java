package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hr_application.adapters.FeedAdapter;
import com.example.hr_application.adapters.employeesAdapter;
import com.example.hr_application.models.Feed;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NewsFeed extends AppCompatActivity {
    Toolbar toolbar;
    String uid,status="";
    RecyclerView newsFeed;
    ArrayList<Feed> news = new ArrayList<>();
    final private String TAG="NewsFeed";
    RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        status=intent.getStringExtra("status");
        toolbar=(Toolbar) findViewById(R.id.newsFeedTool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("News Feed");
        newsFeed=(RecyclerView)findViewById(R.id.recycle_news);
        newsFeed.setHasFixedSize(true);
        newsFeed.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("newsFeed");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                news.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Feed feed = dataSnapshot.getValue(Feed.class);
                    news.add(feed);
                    Log.d(TAG, "onDataChange:NewsFeed "+feed.getDate());
                }
                Collections.reverse(news);
                mAdapter = new FeedAdapter(NewsFeed.this, news);
                newsFeed.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        news.clear();
    }

    public void compose(View view){
        Intent intent=new Intent(this,ComposeFeed.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (status!=null){
            if (status.toLowerCase().equals("super admin")){
                getMenuInflater().inflate(R.menu.news, menu);
            }
        }
        else {
            super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.delete_news:
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("newsFeed");
                databaseReference.removeValue();
                Toast.makeText(this, "News Feed Cleared", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}