package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hr_application.adapters.teamDeskAdapter;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamDeskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private String teamKey;
    private ArrayList<employeesModel> teamDeskEmployees;
    private TextView teamName,leaderName,meetingLink,meetingTime;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_desk);
        teamDeskEmployees = new ArrayList<>();
        teamName = findViewById(R.id.teamName);

        leaderName = findViewById(R.id.leaderName);

        meetingLink = findViewById(R.id.meetingLink);

        meetingTime = findViewById(R.id.meetingTime);

        teamKey = getIntent().getStringExtra("teamKey");

        mRecyclerView = findViewById(R.id.teamDeskRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar_desk);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team Desk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams");
        fetchDetails(teamKey);
    }
    private void fetchDetails(String key1) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams").child(key1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String tN,lN,mL,mT;
                    tN = snapshot.child("teamName").getValue(String.class);
                    lN = snapshot.child("teamLead").getValue(String.class);
                    mL = snapshot.child("meetingLink").getValue(String.class);
                    mT = snapshot.child("meetingTime").getValue(String.class);
                    teamName.setText(tN);
                    leaderName.setText(lN);
                    meetingLink.setText(mL);
                    meetingTime.setText(mT);
                    for (DataSnapshot snapshot1:snapshot.child("members").getChildren()){
                        employeesModel model = snapshot1.getValue(employeesModel.class);
                        teamDeskEmployees.add(model);
                    }
                    mAdapter = new teamDeskAdapter(TeamDeskActivity.this, teamDeskEmployees);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}