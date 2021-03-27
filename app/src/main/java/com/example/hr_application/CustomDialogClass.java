package com.example.hr_application;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class CustomDialogClass extends Dialog implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    public Activity c;
    public Dialog d;
    public Button yes, no,time;
    public EditText link;
    private ImageView share;
    Calendar now = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;
    public CustomDialogClass(@NonNull Activity a) {
        super(a);
        this.c=a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.meet_box);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        link=(EditText)findViewById(R.id.meet_link);
        time=findViewById(R.id.meet_time);
        share=findViewById(R.id.shareicon);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "ECV Job Application");
                String shareMessage = ""+link.getText().toString()+"\n";
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                getContext().startActivity(Intent.createChooser(intent, "share by"));
            }
        });
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        time.setOnClickListener(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("meetingLink").child("googleMeet");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String[] lin = snapshot.getValue().toString().split("=");
                    if (lin != null) {
                        link.setText(lin[1].replace("}", ""));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("meetingLink").child("googleMeet");
                HashMap<String, String> hashMap = new HashMap<>();
                String l=link.getText().toString().trim();
                if (l!=null && !l.isEmpty()){
                    hashMap.put("GoogleMeetLink",l);
                    reference.setValue(hashMap);
                    dismiss();
                }
                else{
                    link.setError("not Empty");
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.meet_time:
                if (!link.getText().toString().isEmpty()){
                    TimePickerDialog tp1 = new TimePickerDialog(c, this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
                    tp1.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeSet = "";
        int hour,minutes;
        hour = hourOfDay;
        minutes = minute;
        now.set(Calendar.HOUR, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }
        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);

        String aTime = new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("meetingLink").child("time");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> hashMap = new HashMap<>();
                if (aTime!=null){
                    hashMap.put("time",aTime);
                    reference.setValue(hashMap);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
