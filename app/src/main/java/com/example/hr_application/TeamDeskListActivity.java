package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hr_application.adapters.TeamDeskListAdapter;
import com.example.hr_application.models.TeamDeskListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamDeskListActivity extends AppCompatActivity implements TeamDeskListAdapter.itemClicked {

    private Toolbar toolbar;
    private String user;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<TeamDeskListModel> deskListModels;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_desk_list);
        deskListModels = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_deskList);
        mRecyclerView = findViewById(R.id.teamList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        status = getIntent().getStringExtra("status");
        fetchKeys();
    }
    private void fetchKeys() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teams");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deskListModels.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.child("members").getChildren()){
                        String userId = snapshot2.child("userid").getValue(String.class);
                        if (user.equals(userId)){
                            TeamDeskListModel model = snapshot1.getValue(TeamDeskListModel.class);
                            deskListModels.add(model);
                            break;
                        }
                    }
                }
                mAdapter = new TeamDeskListAdapter(TeamDeskListActivity.this, TeamDeskListActivity.this, deskListModels,status);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClicked(TeamDeskListModel model) {
        Log.d("key", "onItemClicked: "+model.getKey());
        Intent intent = new Intent(this, TeamDeskActivity.class);
        intent.putExtra("teamKey", model.getKey());
        intent.putExtra("status", status);
        startActivity(intent);
    }
}