package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hr_application.adapters.spinnerAdapater;
import com.example.hr_application.models.employeesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TaskUploadActivity extends AppCompatActivity {
    private ArrayList<employeesModel> employeesModel;
    private spinnerAdapater mAdapter;
    private Spinner spinnerCountries;
    private EditText task;
    private CalendarView calendarView;
    String selectedUserUid,dueSelected;
    Toolbar toolbar;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_upload);
        toolbar=findViewById(R.id.task_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        employeesModel = new ArrayList<>();
        employeesModel.clear();
        spinnerCountries = findViewById(R.id.spinner);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(System.currentTimeMillis(),true,true);
        Calendar calendar = Calendar.getInstance();
        dueSelected=String.valueOf(calendar.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dueSelected=String.valueOf(calendar.getTimeInMillis());
            }
        });
        initList();
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeesModel clickedItem = (employeesModel) parent.getItemAtPosition(position);
                String userSelected = clickedItem.getUsername();
                selectedUserUid = clickedItem.getUserid();
                Toast.makeText(TaskUploadActivity.this, userSelected + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        task = findViewById(R.id.taskText);
        sendBtn=findViewById(R.id.button3);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(task.getText().toString()).trim().equals("") && dueSelected!=null){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(selectedUserUid).child("Tasks").push();
                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("Status", "Assigned");
                    hashMap.put("key",reference.getKey());
                    hashMap.put("Task", task.getText().toString());
                    hashMap.put("due",dueSelected+"");
                    hashMap.put("UploadTime", System.currentTimeMillis()+"");
                    reference.setValue(hashMap);
                    Toast.makeText(TaskUploadActivity.this, "Sent Successfully", Toast.LENGTH_SHORT).show();
                    task.setText("");
                    employeesModel.clear();
                }else{
                    Toast.makeText(TaskUploadActivity.this, "Task can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initList() {
        employeesModel.clear();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final employeesModel user = dataSnapshot.getValue(employeesModel.class);
                    employeesModel.add(new employeesModel(user.getImageUrl(),user.getUsername(),user.getNumber(),user.getDeveloper(),user.getUserid()));
                }
                mAdapter = new spinnerAdapater(TaskUploadActivity.this, employeesModel);
                spinnerCountries.setAdapter(mAdapter);
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}