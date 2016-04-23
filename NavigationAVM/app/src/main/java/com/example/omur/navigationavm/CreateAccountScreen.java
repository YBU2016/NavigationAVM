package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by omur on 03.03.2016.
 */
public class CreateAccountScreen extends Activity {

    Button createaccount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccountscreen);

        createaccount= (Button) findViewById(R.id.CreateAccount);

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateAccountScreen.this, MainPage.class);
                startActivity(intent);

            }
        });
    }

}
