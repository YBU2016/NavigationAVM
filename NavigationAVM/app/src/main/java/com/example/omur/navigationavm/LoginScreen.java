package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public  class LoginScreen extends Activity {

    Button gotomainpage ;
    Button gotosigninpage ;
    Button gotocreateaccountpage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        gotomainpage= (Button) findViewById(R.id.skipbutton);
        gotosigninpage= (Button) findViewById(R.id.ebutton);
        gotocreateaccountpage = (Button) findViewById(R.id.kaydol);

        gotomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginScreen.this, MainPage.class);
                startActivity(intent);

            }
        });

        gotosigninpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginScreen.this, SigninScreen.class);
                startActivity(intent);

            }
        });

        gotocreateaccountpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginScreen.this, CreateAccountScreen.class);
                startActivity(intent);

            }
        });
    }
}
