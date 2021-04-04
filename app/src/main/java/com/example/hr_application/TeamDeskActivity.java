package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hr_application.adapters.teamDeskAdapter;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamDeskActivity extends AppCompatActivity implements TeamDeskCustomDialog.DialogListener{
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private String teamKey,linkML;
    private ArrayList<employeesModel> teamDeskEmployees;
    private TextView teamName,leaderName,meetingTime;
    private TextView meetingLink;
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
        meetingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(linkML);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch(Exception e){

                }
            }
        });
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
                    linkML=mL;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.updatemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                TeamDeskCustomDialog dialog = new TeamDeskCustomDialog();
                dialog.show(getSupportFragmentManager(),"CustomDialogBox");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void addDetails(String link, String time) {
        linkML=link;
        meetingTime.setText(time);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams").child(teamKey);
        HashMap<String , Object> obj = new HashMap<>();
        obj.put("meetingLink",  link);
        obj.put("meetingTime",  time);
        databaseReference.updateChildren(obj);
    }
}