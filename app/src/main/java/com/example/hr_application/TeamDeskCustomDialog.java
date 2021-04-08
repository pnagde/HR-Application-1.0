package com.example.hr_application;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TeamDeskCustomDialog extends AppCompatDialogFragment {
    private EditText updateLink;
    private TextView updateTime;
    private DialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.teamdesk_customdialog_box,null);
        updateLink = view.findViewById(R.id.updateLink);
        updateTime = view.findViewById(R.id.updateTime);
                builder.setView(view)
                        .setTitle("Update Details")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String link = updateLink.getText().toString().trim();
                                        String time = updateTime.getText().toString().trim();
                                        listener.addDetails(link,time);
                                    }
                                });
        updateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = updateLink.getText().toString().trim();
                if (link.isEmpty()){
                    updateLink.setError("Link Required");
                }else {
                    setTime(updateTime);
                }
            }
        });
                return builder.create();
    }

    private void setTime(TextView updateTime) {
        Toast.makeText(getContext(), "time Clicked", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                String t=hourOfDay+":"+minute;
                TeamDeskCustomDialog.this.updateTime.setText(t);
            }
        };
        new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getActivity())).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must Implement DialogListener");
        }
    }

    public interface DialogListener{
        void addDetails(String link, String time);
    }
}
