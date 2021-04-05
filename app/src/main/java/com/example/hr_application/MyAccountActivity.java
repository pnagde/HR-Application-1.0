package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MyAccountActivity extends AppCompatActivity {

    private String userName,  userPic, userEmail, userNumber, userDob, userDeveloper;
    private Button logout, editProfile, attendanceSheet ;
    private EditText editName, editEmailId, editPhoneNumber, editDob, editDeveloper;
    private TextView emailId, name, name1, userPhoneNumber, accountDob, developer,attendanceRecord,taskPercentage;
    private ImageView profileImage, save ;
    private FirebaseStorage storage;
    private Toolbar toolbar;
    private String userid,editable;
    private ProgressDialog dialog;
    private StorageReference reference;
    private DatabaseReference databaseReference;
    private SharedPreferences preferences;
    private double count1=0,total=0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_account);
        toolbar=findViewById(R.id.account_tool);
        setSupportActionBar(toolbar);
        intent=getIntent();
        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userid=getIntent().getStringExtra("uid");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Profile Image...");
        dialog.setCancelable(false);
        editable=getIntent().getStringExtra("editable");
        name1 = findViewById(R.id.name1);
        name = findViewById(R.id.accountName);
        emailId = findViewById(R.id.emailId);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        accountDob = findViewById(R.id.accountDob);
        developer = findViewById(R.id.developer);
//        attendanceRecord = findViewById(R.id.attendanceRecord);
        attendanceSheet = findViewById(R.id.attendanceSheet);
        taskPercentage = findViewById(R.id.taskPercentage);

        if (savedInstanceState != null){
            userName=savedInstanceState.getString("name");
            userEmail=savedInstanceState.getString("email");
            userNumber=savedInstanceState.getString("Number");
            userDob=savedInstanceState.getString("Date of Birth");
            userDeveloper=savedInstanceState.getString("Developer");
            userPic=savedInstanceState.getString("image");
        }
        editName = findViewById(R.id.editName);
        editEmailId = findViewById(R.id.editEmailId);
        editProfile = findViewById(R.id.editProfile);
        if(editable.equals("yes")){
            editProfile.setVisibility(View.VISIBLE);
        }else{
            editProfile.setVisibility(View.GONE);
        }
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editDob = findViewById(R.id.editDob);
        editDeveloper = findViewById(R.id.editDeveloper);

        profileImage = findViewById(R.id.profileImage);

        profileImage.setEnabled(false);
        reference = FirebaseStorage.getInstance().getReference();

        databaseReference =  FirebaseDatabase.getInstance().getReference().child("users").child(userid);

        logout = findViewById(R.id.logout);
        save = findViewById(R.id.saveProfile);
        save.setVisibility(View.INVISIBLE);
        fetchData();
        taskPercent();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });

        attendanceSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccountActivity.this, AttendanceActivity.class);
                i.putExtra("uid", userid);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyAccountActivity.this, loginActivity.class));
                finishAffinity();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",userName);
        outState.putString("email",userEmail);
        outState.putString("Number",userNumber);
        outState.putString("Date of Birth",userDob);
        outState.putString("Developer",userDeveloper);
        outState.putString("image",userPic);
//        Toast.makeText(this, "OnSaveStateInstance", Toast.LENGTH_SHORT).show();
    }

    private void taskPercent() {
//        count1=0;total=0;
        databaseReference.child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    total = total + 1;
                    String present;
                    present = snapshot1.child("Status").getValue(String.class);
                    if (present.equals("completed")) {
                        count1+=1;
                    }
                }
                double d = (count1/total);
                double percentage = Math.round((count1/total)*100);
                taskPercentage.setText((int) percentage+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchData() {
        if (userName != null && userEmail != null && userDob != null && userNumber != null && userDeveloper != null && userPic != null ){
            name1.setText(userName);

            name.setText(userName);
            loadImage();
            emailId.setText(userEmail);
            userPhoneNumber.setText(userNumber);
            accountDob.setText(userDob);
            developer.setText(userDeveloper);
//            Toast.makeText(this, "Not Null", Toast.LENGTH_SHORT).show();
            Log.d("test", "fetchData: Not Null");
        }else {
//            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
            Log.d("test", "fetchData: Null");
            databaseReference
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                userName = snapshot.child("username").getValue().toString();
                                name.setText(userName);
                                name1.setText(userName);

                                userPic = snapshot.child("ImageUrl").getValue().toString();
                                loadImage();

                                userEmail = snapshot.child("email").getValue().toString();
                                emailId.setText(userEmail);

                                userNumber = snapshot.child("PhoneNumber").getValue().toString();
                                userPhoneNumber.setText(userNumber);

                                userDob= snapshot.child("Date Of Birth").getValue().toString();
                                accountDob.setText(userDob);

                                userDeveloper= snapshot.child("Developer").getValue().toString();
                                developer.setText(userDeveloper);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    private void changeProfile() {
        editProfile.setVisibility(View.INVISIBLE);

        name.setVisibility(View.INVISIBLE);
        editName.setVisibility(View.VISIBLE);
        editName.setText(userName);

        emailId.setVisibility(View.INVISIBLE);
        editEmailId.setVisibility(View.VISIBLE);
        editEmailId.setText(userEmail);

        userPhoneNumber.setVisibility(View.INVISIBLE);
        editPhoneNumber.setVisibility(View.VISIBLE);
        editPhoneNumber.setText(userNumber);

        accountDob.setVisibility(View.INVISIBLE);
        editDob.setVisibility(View.VISIBLE);
        editDob.setText(userDob);

        developer.setVisibility(View.INVISIBLE);
        editDeveloper.setVisibility(View.VISIBLE);
        editDeveloper.setText(userDeveloper);

        save.setVisibility(View.VISIBLE);
        profileImage.setEnabled(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("Save Data", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String userName1 = editName.getText().toString().trim();
                String userEmail1 = editEmailId.getText().toString().trim();
                String userNumber1 = editPhoneNumber.getText().toString().trim();
                String userDob1 = editDob.getText().toString().trim();
                String userDeveloper1 = editDeveloper.getText().toString().trim();

                editor.putString("username", userName1);
                editor.putString("email", userEmail1);
                editor.putString("PhoneNumber", userNumber1);
                editor.putString("Date Of Birth", userDob1);
                editor.putString("Developer", userDeveloper1);
                editor.apply();

                userName = userName1;
                userEmail = userEmail1;
                userNumber = userNumber1;
                userDob = userDob1;
                userDeveloper = userDeveloper1;

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("username", userName1);
                obj.put("email", userEmail1);
                obj.put("PhoneNumber", userNumber1);
                obj.put("Date Of Birth", userDob1);
                obj.put("Developer", userDeveloper1);
                databaseReference.updateChildren(obj);

                name.setText(userName1);
                name.setVisibility(View.VISIBLE);
                editName.setVisibility(View.INVISIBLE);

                emailId.setText(userEmail);
                emailId.setVisibility(View.VISIBLE);
                editEmailId.setVisibility(View.INVISIBLE);

                userPhoneNumber.setText(userNumber);
                userPhoneNumber.setVisibility(View.VISIBLE);
                editPhoneNumber.setVisibility(View.INVISIBLE);

                accountDob.setText(userDob);
                accountDob.setVisibility(View.VISIBLE);
                editDob.setVisibility(View.INVISIBLE);

                developer.setText(userDeveloper);
                developer.setVisibility(View.VISIBLE);
                editDeveloper.setVisibility(View.INVISIBLE);

                save.setVisibility(View.INVISIBLE);
                editProfile.setVisibility(View.VISIBLE);
                profileImage.setEnabled(false);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 16);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null){
            dialog.show();
            reference  = FirebaseStorage.getInstance().getReference().child("ProfileImage").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        dialog.dismiss();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                userPic =imageUrl;
                                HashMap<String , Object> obj = new HashMap<>();
                                obj.put("ImageUrl", imageUrl);
                                databaseReference.updateChildren(obj);
                               loadImage();
                            }
                        });
                    }
                }
            });
        }
    }
    private void loadImage(){
        try{
            Glide.with(MyAccountActivity.this).load(userPic).placeholder(R.drawable.logo_circle).into(profileImage);
        }catch (Exception e) {

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (editable.equals("yes")){
            Intent i =new Intent(this,EmployeeActivity.class);
            i.putExtra("status", intent.getStringExtra("status"));
            startActivity(i);
            finish();
        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}