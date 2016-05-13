package com.example.DrawerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.omur.navigationavm.R;

/**
 * Created by omur on 20.04.2016.
 */
public class fragment_settings extends Fragment {

    TextView Sname, Ssurname;
    EditText Susername, Semail, Spassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navsettings, container, false);

        Sname = (TextView) view.findViewById(R.id.settingsname);
        Ssurname = (TextView) view.findViewById(R.id.settingssurname);
        Susername = (EditText) view.findViewById(R.id.settingsusername);
        Semail = (EditText) view.findViewById(R.id.settingsemail);
        Spassword = (EditText) view.findViewById(R.id.settingspassword);

      //  Intent i = view.getIntent();
      //  String Uname = i.getExtras().getString("uname");


        return view;
    }
}
