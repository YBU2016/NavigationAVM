package com.example.DrawerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Database.Entities.LoginEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.omur.navigationavm.MainPage;
import com.example.omur.navigationavm.R;
import com.example.omur.navigationavm.SigninScreen;

/**
 * Created by omur on 20.04.2016.
 */
public class fragment_settings extends Fragment {

    EditText Susername, Semail, Spassword,Scpassword ;
    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    String susername,semail,spassword ;
    Button Updateaccount ;
    int r=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navsettings, container, false);

        Susername = (EditText) view.findViewById(R.id.settingsusername);
        Semail = (EditText) view.findViewById(R.id.settingsemail);
        Spassword = (EditText) view.findViewById(R.id.settingspassword);
        Scpassword = (EditText) view.findViewById(R.id.settingscpassword);


        repositoryContainer = RepositoryContainer.create(getActivity());
        repository = repositoryContainer.getRepository(RepositoryNames.LOGIN);


        if(SigninScreen.controlsettings == 1) {

            String uname =SigninScreen.Uname ;
            r = Integer.parseInt(repository.GetId(uname));
            LoginEntity le = (LoginEntity) repository.GetRecord(r);
            susername =le.getUserName();
            semail=le.getEmail();
            spassword=le.getPassword();

            Susername.setText(susername);
            Semail.setText(semail);
            Spassword.setText(spassword);


            Updateaccount= (Button) view.findViewById(R.id.updateaccount);


            Updateaccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userName = Susername.getText().toString();
                    String mail = Semail.getText().toString();
                    String password = Spassword.getText().toString();
                    String confirmPassword = Scpassword.getText().toString();

                    Log.d("ubil",userName + mail + password ) ;

                    if (userName.equals("") || mail.equals("") || password.equals("") || confirmPassword.equals("")) {
                        Toast.makeText(getActivity(), "Field Vaccant", Toast.LENGTH_LONG).show();
                        return;
                    }
// check if both password matches
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(getActivity(), "Password does not match", Toast.LENGTH_LONG).show();
                        return;
                    } else {
// Save the Data in Database
                        repository.Update(new LoginEntity(r, null, null, userName, mail, password));
                        Toast.makeText(getActivity(), "Account Successfully Updated ", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getActivity(), MainPage.class);
                    startActivity(intent);
                }
            });



        }
        else{

            Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_LONG).show(); }


        SigninScreen.controlsettings=0;



        return view;
    }
}
