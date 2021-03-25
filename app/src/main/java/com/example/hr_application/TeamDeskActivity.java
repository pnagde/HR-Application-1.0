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
    private String user,teamKey;
    private ArrayList<employeesModel> teamDeskEmployees;
    private Boolean b=false;
    private TextView teamName,leaderName,meetingLink,meetingTime, leader,meet,meet1,member;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_desk);
        teamDeskEmployees = new ArrayList<>();
        teamName = findViewById(R.id.teamName);

        leader = findViewById(R.id.leader);
        leaderName = findViewById(R.id.leaderName);

        meet = findViewById(R.id.meet);
        meetingLink = findViewById(R.id.meetingLink);

        meet1 = findViewById(R.id.meet1);
        meetingTime = findViewById(R.id.meetingTime);

        member = findViewById(R.id.members);
        teamKey = getIntent().getStringExtra("teamKey");

        mRecyclerView = findViewById(R.id.teamDeskRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar_desk);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team Desk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams");
//        fetchUser();
        fetchDetails(teamKey);
    }

    private void fetchUser() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    Log.d("userFound", "onDataChange: under Snapshot1");
                    for (DataSnapshot snapshot2:snapshot1.child("members").getChildren()){
//                        Log.d("userFound", "onDataChange: under Snapshot2");
                        String userId = snapshot2.child("userid").getValue(String.class);
                        if (user.equals(userId)){
                            Toast.makeText(TeamDeskActivity.this, "userFound", Toast.LENGTH_SHORT).show();
//                            Log.d("userFound", "onDataChange: "+userId);
                            b=true;
                            break;
                        }
                    }
                    if (b){
                        String key1 = snapshot1.getKey();
//                        key = key1;
                        fetchDetails(key1);
//                        Toast.makeText(TeamDeskActivity.this, "key1", Toast.LENGTH_SHORT).show();
//                        Log.d("userFound", "onDataChange: key1 = "+key1);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (!b) {
            leader.setVisibility(View.INVISIBLE);
            leaderName.setVisibility(View.INVISIBLE);

            meet.setVisibility(View.INVISIBLE);
            meetingLink.setVisibility(View.INVISIBLE);

            meet1.setVisibility(View.INVISIBLE);
            meetingTime.setVisibility(View.INVISIBLE);

            member.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

            teamName.setText("No Team Found");
        }
    }

    private void fetchDetails(String key1) {
        leader.setVisibility(View.VISIBLE);
        leaderName.setVisibility(View.VISIBLE);

        meet.setVisibility(View.VISIBLE);
        meetingLink.setVisibility(View.VISIBLE);

        meet1.setVisibility(View.VISIBLE);
        meetingTime.setVisibility(View.VISIBLE);

        member.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams").child(key1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
//                    Log.d("userFound", "onDataChange: under fetchDetails");
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
//                    if (teamDeskEmployees.size() ==0){
//
//                    }
//                    Log.d("userFound", "onDataChange: "+teamDeskEmployees.size());
//                    Toast.makeText(TeamDeskActivity.this, "member Size"+teamDeskEmployees.size(), Toast.LENGTH_SHORT).show();
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