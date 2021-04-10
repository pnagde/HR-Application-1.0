package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText role, email, pass;
    DatabaseReference reference;
    Button btnAddUser, btnAddRole;
    FirebaseAuth auth;
    private ArrayList<String> roles;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.dash_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Super Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        role = (EditText) findViewById(R.id.roleEdit);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        btnAddRole = (Button) findViewById(R.id.btnRole);
        btnAddUser = (Button) findViewById(R.id.addEmpBtn);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        roles=new ArrayList<>();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    addUser();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Fill details Carefully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        btnAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var = role.getText().toString().trim();
                if (validateRole() && !var.isEmpty()) {
                    addRole(var.trim());
                } else if(var.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Role not be empty", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void addRole(String var) {
        role.setText("");
        reference = FirebaseDatabase.getInstance().getReference().child("Role").push();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.setValue(var);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getDetails();
            }
        });
    }

    private boolean validate() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = pass.getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Not Empty");
            return false;
        }
        if (!emailInput.contains("@")) {
            email.setError("Not Valid");
            return false;
        }
        if (passwordInput.isEmpty()) {
            email.setError("Not Empty");
            return false;
        }
        if (passwordInput.length() < 6) {
            email.setError("Password Length should be 6");
            return false;
        }
        return true;
    }

    private boolean validateRole() {
        String var=role.getText().toString().trim();
        reference = FirebaseDatabase.getInstance().getReference().child("Role");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roles.clear();
                for (DataSnapshot areaSnapshot : snapshot.getChildren()) {
                    roles.add(areaSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for (int i=0;i<roles.size();i++){
            if (roles.get(i).trim().equals(var)){
                Toast.makeText(this, "Already Exist", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void addUser() {
        String em = email.getText().toString().trim();
        String pa = pass.getText().toString().trim();
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(em, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "User Added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    email.setText("");
                    pass.setText("");
                }
                if (!em.contains("@")) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Email does not Exist", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                if (!task.isSuccessful()) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "User Already Exist", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}