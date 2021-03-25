package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hr_application.adapters.responseAdapter;
import com.example.hr_application.models.responseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class ApplyLeaveActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView fromDate, toDate;
    private EditText reason1;
    private int date, month, year;
    private FirebaseUser user;
    private DatabaseReference  reference, databaseReference, databaseReference1;
    private Button apply;
    private String name1, name,from_Date,to_Date,reason,leaveResponse,uid,time, button;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<responseModel> responseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        toolbar = findViewById(R.id.account_tool);
        fromDate = findViewById(R.id.selectFromDate);
        toDate = findViewById(R.id.selectToDate);
        reason1 = findViewById(R.id.reason);
        apply = findViewById(R.id.apply);
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Apply For Leave");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        responseData = new ArrayList<>();
        fromDate.setOnClickListener(v -> {
            date =calendar.get(Calendar.DATE);
            month =calendar.get(Calendar.MONTH);
            year =calendar.get(Calendar.YEAR);
            DatePickerDialog dialog = new DatePickerDialog(ApplyLeaveActivity.this, (view, year, month, dayOfMonth) -> fromDate.setText(dayOfMonth+"/"+(month+1)+"/"+year),year,month,date);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            dialog.show();
        });
        toDate.setOnClickListener(v -> {
            date =calendar.get(Calendar.DATE);
            month =calendar.get(Calendar.MONTH);
            year =calendar.get(Calendar.YEAR);
            DatePickerDialog dialog = new DatePickerDialog(ApplyLeaveActivity.this, (view, year, month, dayOfMonth) -> toDate.setText(dayOfMonth+"/"+(month+1)+"/"+year),year,month,date);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            dialog.show();
        });
        reference = FirebaseDatabase.getInstance().getReference().child("leave").child(user.getUid());
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        databaseReference1 =  FirebaseDatabase.getInstance().getReference().child("grantLeave").child(user.getUid());
        Query query= (databaseReference1.orderByChild("time"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name1 = snapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                responseData.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    responseModel model = snapshot1.getValue(responseModel.class);
                    responseData.add(new responseModel(model.getName(),model.getFromDate(),model.getToDate(),model.getReason(),model.getLeaveResponse(),model.getUid(),model.getTime()));
                }
                Collections.reverse(responseData);
                mAdapter = new responseAdapter(ApplyLeaveActivity.this, responseData);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        apply.setOnClickListener(v -> sendData());

    }

    private void sendData() {
        String d = fromDate.getText().toString().trim();
        String to = toDate.getText().toString().trim();
        String reason = reason1.getText().toString().trim();
        if (d.isEmpty()&&to.isEmpty()){
            Toast.makeText(this, "Pick your date", Toast.LENGTH_SHORT).show();
            if (reason.isEmpty()){
                Toast.makeText(this, "Reason should not be empty", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Pick your date", Toast.LENGTH_SHORT).show();
            }
        }else if (!(reason.isEmpty())){
            HashMap<String , Object> data = new HashMap<>();
            data.put("fromDate", fromDate.getText().toString().trim());
            data.put("toDate", toDate.getText().toString().trim());
            data.put("reason", reason1.getText().toString().trim());
            data.put("name", name1.trim());
            data.put("uid", user.getUid().trim());
            reference.setValue(data);
            fromDate.setText("");
            toDate.setText("");
            reason1.setText("");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}