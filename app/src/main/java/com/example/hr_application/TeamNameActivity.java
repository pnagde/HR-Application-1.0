package com.example.hr_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class TeamNameActivity extends AppCompatActivity {
    EditText teamName, teamLead ,meetingTime, meetingLink;
    FloatingActionButton nextBtn;
    private DatabaseReference userdata;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_name);
        toolbar=findViewById(R.id.team_tool_name);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Team Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        teamName =findViewById(R.id.editGroupName);
        teamLead =findViewById(R.id.teamLead);
        meetingLink=findViewById(R.id.meetingLinkTextView);
        meetingTime=findViewById(R.id.meetingTime);
        nextBtn=findViewById(R.id.floatingActionButton2);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String lead = teamLead.getText().toString();
                final String name = teamName.getText().toString();
                if((name.trim().equals(""))&&(lead.trim().equals(""))){
                    Toast.makeText(TeamNameActivity.this, "Please enter full details", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teams").push();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("teamName",name);
                    hashMap.put("teamLead",lead);
                    hashMap.put("key",reference.getKey());
                    hashMap.put("meetingTime",meetingTime.getText().toString()+"");
                    hashMap.put("meetingLink",meetingLink.getText().toString()+"");
                    reference.updateChildren(hashMap);
                    Intent i =new Intent(TeamNameActivity.this,CreateTeamActivity.class);
                    i.putExtra("key",reference.getKey());
                    Log.d("ojaskey",reference.getKey());
                    startActivity(i);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TeamNameActivity.this,MainActivity.class));
    }
//
//    @Override
//    protected void onDestroy() {
//        if(key!=null) {
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groups").child(key);
//            reference.setValue(null);
//            startActivity(new Intent(TeamNameActivity.this,MainActivity.class));
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onRestart() {
//        if(key!=null) {
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groups").child(key);
//            reference.setValue(null);
//            startActivity(new Intent(TeamNameActivity.this,MainActivity.class));
//        }
//        super.onRestart();
//    }

}