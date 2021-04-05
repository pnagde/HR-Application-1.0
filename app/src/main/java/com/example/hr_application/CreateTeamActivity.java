package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hr_application.adapters.employeesAdapter;
import com.example.hr_application.adapters.spinnerAdapater;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTeamActivity extends AppCompatActivity {
    private ArrayList<employeesModel> employeesModel, acceptedEmployeeModel;
    private spinnerAdapater mAdapter;
    private employeesAdapter employeesAdapter;
    private Spinner spinnerCountries;
    RecyclerView recyclerView;
    Toolbar toolbar;
    Boolean bool = true;
    String selectedUserUid;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        toolbar=findViewById(R.id.create_tool_);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Team");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.recyclerViewct);
        employeesModel = new ArrayList<>();
        acceptedEmployeeModel = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readUsers();
        employeesModel.clear();

        spinnerCountries = findViewById(R.id.spinnerct);
        initList();
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeesModel clickedItem = (employeesModel) parent.getItemAtPosition(position);
                String userSelected = clickedItem.getUsername();
                selectedUserUid = clickedItem.getUserid();
                if(selectedUserUid != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teams").child(getIntent().getStringExtra("key")).child("members").child(clickedItem.getUserid());
                    databaseReference.setValue(clickedItem);
                }
                Toast.makeText(CreateTeamActivity.this, userSelected + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sendBtn=findViewById(R.id.button3ct);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bool=false;
                startActivity(new Intent(CreateTeamActivity.this,MainActivity.class));
            }
        });
    }
    private void initList() {
        employeesModel.clear();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeesModel.add(new employeesModel("","","","",null));
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final employeesModel user = dataSnapshot.getValue(employeesModel.class);
                    employeesModel.add(new employeesModel(user.getImageUrl(),user.getUsername(),user.getNumber(),user.getDeveloper(),user.getUserid()));
                }
                mAdapter = new spinnerAdapater(CreateTeamActivity.this, employeesModel);
                spinnerCountries.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUsers(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("teams").child(getIntent().getStringExtra("key")).child("members");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptedEmployeeModel.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final employeesModel user = dataSnapshot.getValue(employeesModel.class);
                    acceptedEmployeeModel.add(new employeesModel(user.getImageUrl(),user.getUsername(),user.getNumber(),user.getDeveloper(),user.getUserid()));
                }
                employeesAdapter = new employeesAdapter(CreateTeamActivity.this, acceptedEmployeeModel,"");
                recyclerView.setLayoutManager(new LinearLayoutManager(CreateTeamActivity.this));
                recyclerView.setAdapter(employeesAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        final Intent i= getIntent();
        if(i.getStringExtra("key")!=null)
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teams").child(i.getStringExtra("key"));
            reference.setValue(null);
            Log.d("ojasinside","back");
            startActivity(new Intent(CreateTeamActivity.this,TeamNameActivity.class));
        }
        Log.d("ojasoutside","back");
    }

    @Override
    protected void onPause() {
        super.onPause();
        final Intent i= getIntent();
// <<<<<<< VinayLeaveApplication
//         if(i.getStringExtra("key")!=null&&bool) {
// =======
        if(i.getStringExtra("key")!=null && bool) {
// >>>>>>> master
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teams").child(i.getStringExtra("key"));
            reference.setValue(null);
            Log.d("ojasinside","dist");
            startActivity(new Intent(CreateTeamActivity.this,TeamNameActivity.class));
        }
        Log.d("ojasoutside","dist");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        final Intent i= getIntent();
// <<<<<<< VinayLeaveApplication
//         if(i.getStringExtra("key")!=null&&bool) {
// =======
        if(i.getStringExtra("key")!=null&& bool) {
// >>>>>>> master
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teams").child(i.getStringExtra("key"));
            reference.setValue(null);
            Log.d("ojasinside","rest");
            startActivity(new Intent(CreateTeamActivity.this,TeamNameActivity.class));
        }
        Log.d("ojasoutside","rest");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}