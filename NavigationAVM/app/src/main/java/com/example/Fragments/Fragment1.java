package com.example.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.omur.navigationavm.DatabaseScreen;
import com.example.omur.navigationavm.LoginScreen;
import com.example.omur.navigationavm.R;


public class Fragment1 extends Fragment
{
    Button goDatabaseButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1,container , false);

        goDatabaseButton = (Button) v.findViewById(R.id.AddData);
        goDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatabaseScreen.class);
                startActivity(intent);
            }
        });

        return v;
    }
}