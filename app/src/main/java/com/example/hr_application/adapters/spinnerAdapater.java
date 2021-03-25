package com.example.hr_application.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hr_application.R;
import com.example.hr_application.models.employeesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class spinnerAdapater extends ArrayAdapter<employeesModel> {
    public spinnerAdapater(Context context, ArrayList<employeesModel> userList) {
        super(context, 0, userList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_addapter, parent, false
            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.user_pic);
        TextView textViewName = convertView.findViewById(R.id.taskName);
        TextView textViewDeveloper = convertView.findViewById(R.id.dueDate);
        employeesModel employeesModel = getItem(position);
        if (employeesModel != null) {
            if(!employeesModel.getImageUrl().equals("No Profile Image")) {
                Picasso.get().load(Uri.parse(employeesModel.getImageUrl())).into(imageViewFlag);
            }
            textViewName.setText(employeesModel.getUsername());
            textViewDeveloper.setText(employeesModel.getDeveloper());
        }
        return convertView;
    }
}