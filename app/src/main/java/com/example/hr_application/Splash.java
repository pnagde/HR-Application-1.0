package com.example.hr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("ojasss","in");
        logo=(ImageView) findViewById(R.id.logo_ecv);
        if (logo!=null){
            TranslateAnimation animate = new TranslateAnimation(0,0, -700, 0);
            animate.setDuration(1000);
            logo.startAnimation(animate);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(3 * 1000);
                        // After 5 seconds redirect to another intent
                        if (user != null) {
                                startActivity(new Intent(Splash.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            startActivity(new Intent(Splash.this, loginActivity.class));
                        }
                        //Remove activity
                        finish();
                    } catch (Exception e) {
                    }
                }
            };
            // start thread
            background.start();
        }else{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(3 * 1000);
                        // After 5 seconds redirect to another intent
                        if (user != null) {
                            if (auth.getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(Splash.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }

                        } else {
                            startActivity(new Intent(Splash.this, loginActivity.class));
                        }
                        //Remove activity
                        finish();
                    } catch (Exception e) {
                    }
                }
            };
        }
    }
}