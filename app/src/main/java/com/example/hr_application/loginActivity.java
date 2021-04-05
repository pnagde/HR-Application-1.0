package com.example.hr_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private TextView username,forgetPassword;
    private TextView password;
    private FirebaseAuth auth;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.emailInput);
        password=findViewById(R.id.passwordInput);
        forgetPassword=findViewById(R.id.forgetPassword);

        login = (Button) findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_password(v);
            }
        });
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernameInput=username.getText().toString();
                String passwordInput=password.getText().toString();
                if(!(usernameInput.trim().equals("")||passwordInput.trim().equals(""))) {
                    loginUser(usernameInput, passwordInput,usernameInput);
                }else{
                    Toast.makeText(loginActivity.this, "please fill all the information", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    private Boolean validateEmailDialogBox(EditText reset,String val) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            Toast.makeText(loginActivity.this, "Email not be empty",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!val.matches(emailPattern)) {
            Toast.makeText(loginActivity.this, "Invalid Email",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            reset.setError(null);
            return true;
        }
    }
    private void forgot_password(View v) {

        EditText resetmail = new EditText(v.getContext());
        resetmail.setBackgroundColor(getResources().getColor(R.color.DarkBackground));
        resetmail.setHint("Email");
        resetmail.setGravity(Gravity.CENTER);
        resetmail.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        resetmail.setTextColor(getResources().getColor(R.color.colorAssent));
        resetmail.setHintTextColor(getResources().getColor(R.color.colorAssent));
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme);
        alertDialog.setTitle(Html.fromHtml("<font color='#d35a11'>Reset Password?</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#d35a11'>Enter Your Email</font>"));
        alertDialog.setView(resetmail);

        alertDialog.setPositiveButton(Html.fromHtml("<font color='#d35a11'>YES</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Extract the email and sent reset link
                String mail = resetmail.getText().toString().trim();
                if(validateEmailDialogBox(resetmail,mail)) {
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(loginActivity.this, "Reset Link Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(loginActivity.this, "Error! Reset Link is Not Sent. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(loginActivity.this, "Not Sent",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton(Html.fromHtml("<font color='#d35a11'>NO</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Back to the login screen
            }
        });

        alertDialog.create().show();


    }

    private void loginUser(String username,String password,String emailId) {
        auth.signInWithEmailAndPassword(username , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                if(auth.getCurrentUser().isEmailVerified()){
                    Toast.makeText(loginActivity.this, "log-in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(loginActivity.this, UserProfileActivity.class).putExtra("emailId", emailId));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Please Verify Your Email",Toast.LENGTH_SHORT).show();
                }
            }
        });
        auth.signInWithEmailAndPassword(username,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this, "entered email or password not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onStart(){
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            if(auth.getCurrentUser().isEmailVerified()){
                startActivity(new Intent(loginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            finish();
        }
    }
}