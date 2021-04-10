package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileActivity extends AppCompatActivity {

    private String emailId;
    private EditText phoneNumber, dateOfBirth, homeAddress, email, username;
    private DatabaseReference userData;
    private FirebaseUser user;
    private Button btnSetup;
    private DatePickerDialog datePickerDialog;
    private int mDate, mMonth, mYear;
    String img,role;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        homeAddress = findViewById(R.id.homeAddress);
        btnSetup = findViewById(R.id.setupProfile);
        emailId = getIntent().getStringExtra("emailId");
        email.setText(emailId);
        img="null";
        dateOfBirth.setClickable(false);
        dateOfBirth.setFocusableInTouchMode(false);
        dateOfBirth.setFocusable(false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userNumber = snapshot.child("PhoneNumber").getValue().toString();
                    phoneNumber.setText(userNumber);

                    String userDob = snapshot.child("Date Of Birth").getValue().toString();
                    dateOfBirth.setText(userDob);

                    String add = snapshot.child("HomeAddress").getValue().toString();
                    homeAddress.setText(add);

                    String dev= snapshot.child("Developer").getValue().toString();
                    if(!dev.equals("")){
                        role=dev;
                    }



                    String name = snapshot.child("username").getValue().toString();
                    username.setText(name);

                    if (snapshot.child("ImageUrl").exists()){
                        img = snapshot.child("ImageUrl").getValue().toString();
                    }
                    check();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dateOfBirth.setOnClickListener(v -> {
            if (dateOfBirth.getText().toString().trim().isEmpty()){
                final Calendar calendar = Calendar.getInstance();
                mDate = calendar.get(Calendar.DATE);
                mMonth = calendar.get(Calendar.MONTH);
                mYear = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(UserProfileActivity.this, android.R.style.Theme_DeviceDefault_Dialog, (view, year, month, dayOfMonth) -> dateOfBirth.setText(dayOfMonth + "-" + (month+1) + "-" + year), mYear, mMonth, mDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            else {
                Toast.makeText(UserProfileActivity.this, "Not Changeable Contact Your HR", Toast.LENGTH_SHORT).show();
            }

        });

        btnSetup.setOnClickListener(v -> {
            if (!username.getText().toString().isEmpty()){
                setupProfile();
            }
            else{
                username.setError("Name should not be empty");
            }
        });
}
    private boolean Validate_phone(String number) {
        Pattern p = Pattern.compile("[6-9][0-9]{9}");
        Matcher m =p.matcher(number);
        return (m.find()&&m.group().equals(number));
    }
    private void check() {
        if (!phoneNumber.getText().toString().isEmpty())
            phoneNumber.setFocusable(false);
        else{
            phoneNumber.setFocusableInTouchMode(true);
            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                       String length = (phoneNumber.getText().toString().trim());
                       if (length.length()==10){
                           if (Validate_phone(length)){
                               btnSetup.setEnabled(true);
                           }else {
                               phoneNumber.setError("Invalid Number");
                               btnSetup.setEnabled(false);
                           }

                       } else {
                           phoneNumber.setError("Number should be 10");
                           btnSetup.setEnabled(false);
                       }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        if (!homeAddress.getText().toString().trim().isEmpty()) {
            homeAddress.setFocusable(false);
        }
        else{
            homeAddress.setFocusableInTouchMode(true);
        }
        if(!email.getText().toString().trim().isEmpty()){
            email.setFocusable(false);
        }
        else{
            email.setFocusableInTouchMode(true);
        }
        if (!username.getText().toString().trim().isEmpty()){
            username.setFocusable(false);
        }
        else{
            username.setFocusableInTouchMode(true);
        }
    }

    private void setupProfile() {
        if (TextUtils.isEmpty(phoneNumber.getText()) || TextUtils.isEmpty(homeAddress.getText()) || TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(username.getText())){
            Toast.makeText(this, "Please fill all the fields ", Toast.LENGTH_SHORT).show();
            return;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("Date Of Birth", dateOfBirth.getText().toString().trim());
        usermap.put("PhoneNumber", phoneNumber.getText().toString().trim());
        usermap.put("HomeAddress", homeAddress.getText().toString().trim());
        usermap.put("Developer",role+"");
        usermap.put("username", username.getText().toString().trim());
        usermap.put("email", email.getText().toString().trim());
        usermap.put("userid",user.getUid());
        usermap.put("status","");
        usermap.put("post","Not Mentioned");
        usermap.put("gender","Not Mentioned");
        usermap.put("language","Not Mentioned");
        usermap.put("country","Not Mentioned");
        usermap.put("points","0");
        usermap.put("Badges","0");
        if(img.equals("null")){
            usermap.put("ImageUrl", "No Profile Image");
        }else{
            usermap.put("ImageUrl",img);
        }
        userData.setValue(usermap).addOnSuccessListener(aVoid -> {
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        setupProfile();
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        if (TextUtils.isEmpty(phoneNumber.getText()) || TextUtils.isEmpty(homeAddress.getText()) || TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(username.getText())){
            Toast.makeText(this, "Please fill all the fields ", Toast.LENGTH_SHORT).show();
            return;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("Date Of Birth", dateOfBirth.getText().toString().trim());
        usermap.put("PhoneNumber", phoneNumber.getText().toString().trim());
        usermap.put("HomeAddress", homeAddress.getText().toString().trim());
        usermap.put("Developer",role+"");
        usermap.put("username", username.getText().toString().trim());
        usermap.put("email", email.getText().toString().trim());
        usermap.put("userid",user.getUid());
        usermap.put("status","");
        usermap.put("post","Not Mentioned");
        usermap.put("gender","Not Mentioned");
        usermap.put("language","Not Mentioned");
        usermap.put("country","Not Mentioned");
        usermap.put("points","0");
        usermap.put("Badges","0");
        if(img.equals("null")){
            usermap.put("ImageUrl", "No Profile Image");
        }else{
            usermap.put("ImageUrl",img);
        }
        userData.setValue(usermap);
        super.onStop();
    }
}