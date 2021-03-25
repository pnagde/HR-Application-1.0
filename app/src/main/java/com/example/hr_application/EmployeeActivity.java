package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.hr_application.adapters.allLeaveRecordAdapter;
import com.example.hr_application.adapters.employeesAdapter;
import com.example.hr_application.models.employeesModel;
// <<<<<<< VinayLeaveApplication
import com.example.hr_application.models.responseModel;
// =======
import com.google.firebase.auth.FirebaseAuth;
// >>>>>>> master
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EmployeeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    ArrayList<employeesModel> modelArrayList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    Toolbar toolbar;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        toolbar=findViewById(R.id.tool_emp);
        setSupportActionBar(toolbar);
        i = getIntent();
        getSupportActionBar().setTitle("Employees");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.employeesList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final employeesModel user = dataSnapshot.getValue(employeesModel.class);
                    if (i.getStringExtra("status").equals("Admin")) {
                        modelArrayList.add(new employeesModel(user.getImageUrl(), user.getUsername(), user.getNumber(), user.getDeveloper(), user.getUserid()));
                    }
                    if(i.getStringExtra("status").equals("HR")){
                        if(!(user.getDeveloper().equals("HR")||user.getDeveloper().equals("Admin"))){
                            modelArrayList.add(new employeesModel(user.getImageUrl(), user.getUsername(), user.getNumber(), user.getDeveloper(), user.getUserid()));
                        }
                    }
                }
                mAdapter = new employeesAdapter(EmployeeActivity.this, modelArrayList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.searchEmployees);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEmployee(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchEmployee(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchEmployee(String query) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query query1 = (reference.orderByChild("username").equalTo(query));
//        Query query1  = (reference.orderByChild("username").startAt(query).equalTo(query).endAt(query+"\uf8ff"));
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final employeesModel user = dataSnapshot.getValue(employeesModel.class);
                    if (i.getStringExtra("status").equals("Admin")) {
                        modelArrayList.add(new employeesModel(user.getImageUrl(), user.getUsername(), user.getNumber(), user.getDeveloper(), user.getUserid()));
                    }
                    if(i.getStringExtra("status").equals("HR")){
                        if(!(user.getDeveloper().equals("HR")||user.getDeveloper().equals("Admin"))){
                            modelArrayList.add(new employeesModel(user.getImageUrl(), user.getUsername(), user.getNumber(), user.getDeveloper(), user.getUserid()));
                        }
                    }
                }
                mAdapter = new employeesAdapter(EmployeeActivity.this, modelArrayList);
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

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(EmployeeActivity.this,MainActivity.class));
//    }

    @Override
    protected void onStart() {
        modelArrayList.clear();
        super.onStart();
    }

    @Override
    protected void onResume() {
        modelArrayList.clear();
        super.onResume();
    }
}