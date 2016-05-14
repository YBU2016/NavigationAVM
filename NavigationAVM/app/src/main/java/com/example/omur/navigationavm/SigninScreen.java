package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Database.Entities.LoginEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.LoginRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;

import java.util.ArrayList;
import java.util.List;

public  class SigninScreen extends Activity {

    private RepositoryContainer repositoryContainer;
    private IRepository repository;

    Button createaccount2 ;
    Button Signin;
    public static int controluser=0 ;
    public static int controlsettings=0 ;
    public static String Uname ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinscreen);


        final EditText SignUserName=(EditText) findViewById(R.id.signusername);
        final EditText SignPassword=(EditText) findViewById(R.id.signpassword);

        createaccount2= (Button) findViewById(R.id.CreateAccount2);
        Signin = (Button) findViewById(R.id.signinbutton);

        createaccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SigninScreen.this, CreateAccountScreen.class);
                startActivity(intent);

            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=SignUserName.getText().toString();
                String password=SignPassword.getText().toString();

                repositoryContainer = RepositoryContainer.create(v.getContext());
                repository = repositoryContainer.getRepository(RepositoryNames.LOGIN);

                LoginEntity le = (LoginEntity) repository.GetRecord(1);
               String adminpassword = String.valueOf(repository.getSinlgeEntry(le.getUserName()));

                String storedPassword;
                Intent intent = null;

                if ( userName.equals(le.getUserName()) && password.equals(adminpassword))
                {
                    Toast.makeText(SigninScreen.this, "Hello Admin" + userName, Toast.LENGTH_LONG).show();
                    intent= new Intent(SigninScreen.this, DatabaseScreen.class);
                    startActivity(intent);
                    return;

                } else if ((storedPassword = repository.getSinlgeEntry(userName)) != null && !password.equals(storedPassword)) {
                    Toast.makeText(SigninScreen.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(SigninScreen.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                     Uname =userName ;

                    intent = new Intent(SigninScreen.this, MainPage.class);
                    intent.putExtra("uname",Uname);
                    Log.d("bilgiler",Uname) ;
                    controluser=1 ;
                    controlsettings=1 ;
                    startActivity(intent);

                }





            }
        });

    }
}