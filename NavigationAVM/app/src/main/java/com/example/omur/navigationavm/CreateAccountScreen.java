package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Database.Entities.LoginEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.LoginRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;

/**
 * Created by omur on 03.03.2016.
 */

public class CreateAccountScreen extends Activity {

    EditText RegisterName,RegisterSurname,RegisterUsername,RegisterEmail,RegisterPassword,RegisterConfirmPassword ;
    Button registeraccount ;

    private IRepository rps;
    Button createaccount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccountscreen);

        RepositoryContainer repositoryContainer = RepositoryContainer.create(this);
        rps = repositoryContainer.getRepository(RepositoryNames.LOGIN);

        RegisterName= (EditText) findViewById(R.id.rname);
        RegisterSurname= (EditText) findViewById(R.id.rsurname);
        RegisterUsername= (EditText) findViewById(R.id.rusername);
        RegisterEmail= (EditText) findViewById(R.id.remail);
        RegisterPassword= (EditText) findViewById(R.id.rpassword);
        RegisterConfirmPassword= (EditText) findViewById(R.id.rcpassword);

        createaccount= (Button) findViewById(R.id.CreateAccount);

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name=RegisterName.getText().toString();
                String SurName=RegisterSurname.getText().toString();
                String userName=RegisterUsername.getText().toString();
                String mail=RegisterEmail.getText().toString();
                String password=RegisterPassword.getText().toString();
                String confirmPassword=RegisterConfirmPassword.getText().toString();

                if(Name.equals("")||SurName.equals("")||userName.equals("")||mail.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
// check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
// Save the Data in Database
                    rps.Add(new LoginEntity(0,Name,SurName,userName,mail, password));
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(CreateAccountScreen.this, MainPage.class);
                startActivity(intent);

            }
        });
    }

}
