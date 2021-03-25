package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hr_application.adapters.GrantLeaveAdapter;
import com.example.hr_application.models.GrantLeaveModels;
import com.example.hr_application.models.employeesModel;
import com.example.hr_application.models.responseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LeaveApplicationActivity extends AppCompatActivity implements GrantLeaveAdapter.onItemClicked {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<GrantLeaveModels> leaveModels;
    private  FirebaseUser user;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        toolbar = findViewById(R.id.account_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Applications");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = findViewById(R.id.grantLeaveRV);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaveModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("leave").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaveModels.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final GrantLeaveModels models = dataSnapshot.getValue(GrantLeaveModels.class);
                    leaveModels.add(new GrantLeaveModels(models.getFromDate(), models.getToDate(), models.getName(), models.getUid(), models.getReason()));
                }
                mAdapter = new GrantLeaveAdapter(LeaveApplicationActivity.this,LeaveApplicationActivity.this,leaveModels);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("grantLeave").child(user.getUid());
//        HashMap<String, Object> response = new HashMap<>();
//        response.put("leaveResponse","");
//        response.put("name","");
//        response.put("fromDate","");
//        response.put("toDate","");
//        response.put("reason","");
//        response.put("uid","");
//        response.put("time","");
//                reference1.setValue(response);

    }

//    HashMap<String, Object> obj = new HashMap<>();
//        obj.put("leaveResponse", "");
//        reference.setValue(obj);

    @Override
    public void onAcceptClicked(int pos, GrantLeaveModels models) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY,HH:mm", Locale.getDefault());
//        String time = simpleDateFormat.format(new Date());
        reference = FirebaseDatabase.getInstance().getReference().child("grantLeave").child(models.getUid());
        responseModel response = new responseModel(models.getName(), models.getFromDate(), models.getToDate(), models.getReason(), "Your Leave is Granted", models.getUid(), System.currentTimeMillis()+"");
        reference.push().setValue(response);
        FirebaseDatabase.getInstance().getReference().child("leave").child(models.getUid()).removeValue();
        Toast.makeText(this, "Accept position"+pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineClicked(int pos, GrantLeaveModels models) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY,HH:mm", Locale.getDefault());
//        String time = simpleDateFormat.format(new Date());
        reference = FirebaseDatabase.getInstance().getReference().child("grantLeave").child(models.getUid());
        responseModel response = new responseModel(models.getName(), models.getFromDate(), models.getToDate(), models.getReason(), "Your Leave is not Granted", models.getUid(), System.currentTimeMillis()+"");
        reference.push().setValue(response);
        FirebaseDatabase.getInstance().getReference().child("leave").child(models.getUid()).removeValue();
        Toast.makeText(this, "Decline position"+pos, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notify:
                startActivity(new Intent(LeaveApplicationActivity.this, AllLeaveRecordsActivity.class));
                break;
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