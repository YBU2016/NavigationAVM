package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Database.Entities.DistancesEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Toshıba on 24.4.2016.
 */
public class DatabaseScreen extends Activity
{
    private RepositoryContainer repositoryContainer;
    private IRepository repository;

    private TreeMap<String, String> fiveOfResultMap = new TreeMap<String, String>();
    EditText editZone, editBSSID, editFarthest, editShortest, editNearzone, editStoreName;
    Button btnAddData, Dbms;
    ImageButton btnDatabase;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_screen);

        repositoryContainer = RepositoryContainer.create(this);
        repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for(Map.Entry entry : preferences.getAll().entrySet())
        {
            fiveOfResultMap.put(entry.getKey().toString(), entry.getValue().toString());
        }

        textView = (TextView) findViewById(R.id.myTextView);
        textView.setText(fiveOfResultMap.toString());

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        editBSSID = (EditText) findViewById(R.id.BSSIDEditText);
        editFarthest = (EditText) findViewById(R.id.farthestEditText);
        editShortest = (EditText) findViewById(R.id.shortestEditText);
        editNearzone = (EditText) findViewById(R.id.NearZoneEditText);
        editZone = (EditText) findViewById(R.id.ZoneEditText);
        btnAddData = (Button) findViewById(R.id.AddData);

        Dbms = (Button) findViewById(R.id.DBMS);
        Dbms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbManager = new Intent(v.getContext(), AndroidDatabaseManager.class);
                startActivity(dbManager);
            }
        });

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = repository.Add(new DistancesEntity(0, editZone.getText().toString(),
                        editBSSID.getText().toString(), editNearzone.getText().toString(),
                        Integer.parseInt(editShortest.getText().toString()), Integer.parseInt(editFarthest.getText().toString())));
                if(isInserted)
                {
                    Toast.makeText(DatabaseScreen.this, "Succesfully Inserted!", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(DatabaseScreen.this, "Data is not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
