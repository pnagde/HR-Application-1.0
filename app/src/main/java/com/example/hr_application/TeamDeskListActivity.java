package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("teams");
    private ArrayList<String> keys;
    private ArrayList<TeamDeskListModel> deskListModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_desk_list);
        keys = new ArrayList<>();
        deskListModels = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_deskList);
        mRecyclerView = findViewById(R.id.teamList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchKeys();
    }
    private void fetchKeys() {
        deskListModels.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.child("members").getChildren()){
                        String userId = snapshot2.child("userid").getValue(String.class);
                        if (user.equals(userId)){
                            String key1 = snapshot1.getKey();
                            Log.d("userFound", "onDataChange: "+key1);
                            showKeys(key1);
                            break;
                        }
                    }
                }
                mAdapter = new TeamDeskListAdapter(TeamDeskListActivity.this, TeamDeskListActivity.this, deskListModels);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showKeys(String path) {
//        for (int i=0;i<keys.size();i++){
//            Log.d("userFound", "onDataChange: "+ keys);
//            String path=keys.get(i);
        //        }
            databaseReference.child(path).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.d("userFound", "onDataChange: data entry");
                    if (snapshot.exists()){
                        TeamDeskListModel model = snapshot.getValue(TeamDeskListModel.class);
//                        String teamName,teamKey;
//                        teamName = snapshot.child("teamName").getValue(String.class);
//                        teamKey = snapshot.child("key").getValue(String.class);
//                        Log.d("userFound", "onDataChange: name "+teamName);
//                        Log.d("userFound", "onDataChange: key "+teamKey);
//                        TeamDeskListModel model = new TeamDeskListModel(teamName, teamKey);
                        deskListModels.add(model);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("userFound", "onDataChange: "+error);
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
        Toast.makeText(this, ""+model.getKey(), Toast.LENGTH_SHORT).show();
        Log.d("key", "onItemClicked: "+model.getKey());
        Intent i = new Intent(this, TeamDeskActivity.class);
        i.putExtra("teamKey", model.getKey());
        startActivity(i);
    }
}