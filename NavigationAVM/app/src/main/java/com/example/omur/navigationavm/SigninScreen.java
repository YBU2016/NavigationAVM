package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public  class SigninScreen extends Activity {

    Button createaccount2 ;
    Button Signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinscreen);

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

                Intent intent = new Intent(SigninScreen.this, MainPage.class);
                startActivity(intent);

            }
        });

    }
}
