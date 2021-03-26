package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hr_application.models.Feed;
import com.example.hr_application.models.employeesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ComposeFeed extends AppCompatActivity {


    final private String TAG="ComposeFeed";
    FirebaseAuth auth;
    FirebaseDatabase database;
    Calendar calendar;
    EditText txtContent;
    FloatingActionButton send;
    String uid,purl,name,developer,key;
    Intent intent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_feed);
        hooks();
        supportTool();

        calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        intent=getIntent();
        uid=intent.getStringExtra("uid");
        //send data to database
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeesModel user=snapshot.getValue(employeesModel.class);
                purl=user.getImageUrl();
                name=user.getUsername();
                developer=user.getDeveloper();
                Log.d(TAG, "onDataChange: "+purl+"/"+name+"/"+developer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ComposeFeed.this, ""+error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("newsFeed").push();
                key=reference.getKey();
                String date=day+"/"+month+"/"+year;
                String content=txtContent.getText().toString();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!content.isEmpty()){
                            Feed feed=new Feed(name,date,content,developer,uid,purl,reference.getKey());
                            reference.setValue(feed);
                            onBackPressed();
                            finish();
                        }
                        else
                        {
                            txtContent.setError("Not Empty");
                            Toast.makeText(ComposeFeed.this, "Content should not be empty", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(ComposeFeed.this, ""+error.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void supportTool()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Compose News Feed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void hooks() {
        txtContent=(EditText)findViewById(R.id.text_content);
        send=(FloatingActionButton)findViewById(R.id.post_btn);
        toolbar=(Toolbar)findViewById(R.id.compose_id);
    }
}