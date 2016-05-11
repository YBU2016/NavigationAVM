package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class DatabaseScreen extends Activity
{
    private IRepository repository;
    public static HashMap<Double, String> FiveOfResultMap = new HashMap<>();
    public static WifiManager wifi;
    public static List<ScanResult> results;

    EditText editZone, editBSSID, editFarthest, editShortest, editNearzone, editStoreName;
    Button btnAddData, Dbms;
    TextView textView;

    public double calculateDistance(double levelInDb, double freqInMHz){
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_screen);

        RepositoryContainer repositoryContainer = RepositoryContainer.create(this);
        repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);



        /**
         * Scanning Wifi code, Starting here.
         */
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
                TreeMap resultMap = new TreeMap<>();

                /**
                 * This loop is the most important part of the project.
                 * We send Signal Level and Signal Frequency to calculateDistance method.
                 * The calculated distance and MAC adress of modem will be recorded to (TreeMap)resultMap.
                 */
                for (ScanResult s : results) {
                    resultMap.put(calculateDistance(s.level, s.frequency), s.BSSID);
                }

                /**
                 * Here we got the 5 nearest modems from resultMap. FiveOfResultMap is also a TreeMap
                 */
                int count = 0;
                Iterator<Map.Entry<Double, String>> entries = resultMap.entrySet().iterator();
                while(entries.hasNext())
                {
                    Map.Entry<Double, String> entry = entries.next();
                    if(count > 4) break;
                    FiveOfResultMap.put(entry.getKey(), entry.getValue());
                    count++;
                }



                if(FiveOfResultMap.size() > 0)
                {
                  /*  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();

                    for (Map.Entry entryMap : FiveOfResultMap.entrySet())
                        editor.putString(entryMap.getKey().toString(), entryMap.getValue().toString());
                        editor.commit(); */

                    textView = (TextView) findViewById(R.id.myTextView);
                    textView.setText(FiveOfResultMap.toString());
                }else{
                    Toast.makeText(DatabaseScreen.this, "Please open Wi-Fi", Toast.LENGTH_SHORT).show();
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
        /**
         * Wifi scan code finishes Here
         */

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for(Map.Entry entry : preferences.getAll().entrySet())
        {
            fiveOfResultMap.put(entry.getKey().toString(), entry.getValue().toString());
        }



        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        }*/


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
