package com.example.hr_application;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.VoiceInteractor;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class CustomLoadingClass extends Dialog {
    public Activity c;
    public Dialog d;
    public LottieAnimationView lottieAnimationView;

    public CustomLoadingClass(@NonNull Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_diaog_box);
        lottieAnimationView=(LottieAnimationView)findViewById(R.id.loadingAnimation_1);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setSpeed(1);
        lottieAnimationView.playAnimation();
    }
}