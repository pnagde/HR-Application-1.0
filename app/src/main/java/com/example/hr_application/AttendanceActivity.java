package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hr_application.adapters.attendanceAdapter;
import com.example.hr_application.models.attendanceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
    private attendanceAdapter attendanceAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView filter;
    private String searchDate,userid;
    private int year,month,date;
    private DatabaseReference databaseReference;
    ArrayList<attendanceModel> attendanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        toolbar=findViewById(R.id.toolbar_attend);
        filter=findViewById(R.id.filterAttendance);
        setSupportActionBar(toolbar);
        userid = getIntent().getStringExtra("uid");
        getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.attendanceRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readusers();
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        searchDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        filter.setText(searchDate);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = calendar.get(Calendar.DATE);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        searchDate = dayOfMonth+"/"+(month+1)+"/"+year;
                        filter.setText(searchDate);
                        processSearch(searchDate);
                    }
                },year,month,date);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
        });
    }

    private void processSearch(String searchDate) {
        Query query = (databaseReference.orderByChild("date").equalTo(searchDate));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    attendanceModel model = snapshot.getValue(attendanceModel.class);
                    attendanceList.add(model);
                }
                attendanceAdapter = new attendanceAdapter(getApplicationContext(), attendanceList,filter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
                recyclerView.setAdapter(attendanceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    private void readusers() {
        if (userid == null) {
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid()+"";
        }
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(userid).child("attendance");
        Query query= (databaseReference.orderByChild("time"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    attendanceModel model = snapshot.getValue(attendanceModel.class);
                    attendanceList.add(model);
                }
                attendanceAdapter = new attendanceAdapter(getApplicationContext(), attendanceList,filter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
                recyclerView.setAdapter(attendanceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceActivity.this, "check your network connection", Toast.LENGTH_SHORT).show();

            }
        });

    }
}