package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Database.Repositories.LoginRepository;

public  class SigninScreen extends Activity {

    Button createaccount2 ;
    Button Signin;

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

                String storedPassword= LoginRepository.getSinlgeEntry(userName);

// check if the Stored password matches with Password entered by user
                if(!password.equals(storedPassword))
                {
                    Toast.makeText(SigninScreen.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Toast.makeText(SigninScreen.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(SigninScreen.this, MainPage.class);
                startActivity(intent);

            }
        });

    }
}