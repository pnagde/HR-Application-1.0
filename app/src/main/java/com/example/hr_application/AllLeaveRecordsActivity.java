package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hr_application.adapters.allLeaveRecordAdapter;
import com.example.hr_application.models.responseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AllLeaveRecordsActivity extends AppCompatActivity {

    private static final String TAG ="AllLeaveActivity" ;
    private Toolbar toolbar;
    private ArrayList<responseModel> allLeave;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_leave_records);
        allLeave = new ArrayList<>();
        toolbar = findViewById(R.id.account_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Leave Records");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.leaveRecord);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("grantLeave");
        Query query = (databaseReference.orderByChild("time"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allLeave.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.getChildren()){
                        responseModel model = snapshot2.getValue(responseModel.class);
                        allLeave.add(model);
//                        allLeave.add(new responseModel(model.getName(),model.getFromDate(),model.getToDate(),model.getReason(),model.getLeaveResponse(),model.getUid(),model.getTime()));
                    }
                }
                Collections.reverse(allLeave);
                mAdapter = new allLeaveRecordAdapter(AllLeaveRecordsActivity.this, allLeave);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void processSearch(String username) {
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("grantLeave");
                Query query =(d.orderByChild("name").startAt(username).endAt(username+"\uf8ff"));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allLeave.clear();
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            for (DataSnapshot snapshot2:snapshot1.getChildren()){
                                responseModel model = snapshot2.getValue(responseModel.class);
                                String a=model.getName();
                                Log.d(TAG, "onDataChange:"+a);
                                allLeave.add(model);
                            }
                        }
                        Collections.reverse(allLeave);
                        mAdapter = new allLeaveRecordAdapter(AllLeaveRecordsActivity.this, allLeave);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                        error.getDetails();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear_menu,menu);
        MenuItem item = menu.findItem(R.id.SearchView);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    processSearch(newText);
                return false;
            }
        });
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearData:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Confirm")
                        .setMessage("Do you really want to delete all data?")
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("grantLeave").removeValue();
                            }
                        }).setNegativeButton("NO",null)
                        .show();
                break;
            case android.R.id.home:
//                Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
//        Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show();
        finish();
        return super.onSupportNavigateUp();
    }
}