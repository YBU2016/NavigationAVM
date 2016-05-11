package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.Database.Repositories.StaticData;
import com.example.models.CoordinateViewModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Alparslan on 7.5.2016.
 */
public class LocationScreen extends Activity
{
    private String Zone = "";
    public static HashMap<Double, String> FiveOfResultMap = new HashMap<>();
    public static WifiManager wifi;
    public static List<ScanResult> results;

    public double calculateDistance(double levelInDb, double freqInMHz){
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationscreen);

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
                    /**
                     * This part is finding zone. We sent nearest 5 modems to Area Class.
                     */

                    Area areaClass = new Area(c);
                    Zone = areaClass.findZone(FiveOfResultMap);

                    /**
                     * This Zone will be sent to database and its place in x y coordinate plane,
                     * will be taken from database. Finally the marker will be placed that exact place
                     */

                    RelativeLayout rv = (RelativeLayout) findViewById(R.id.OuterRelativeLayout);
                    RelativeLayout.LayoutParams params;
                    ImageButton image = new ImageButton(c);
                    image.setBackgroundResource(R.drawable.blue);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "Current Location", Toast.LENGTH_LONG).show();
                        }
                    });

                    List<Integer> coordinateList = new ArrayList<>();
                    coordinateList = getCoordinates("MarkerCoordinates.txt", c, Zone);

                    params = new RelativeLayout.LayoutParams(30,30);
                    params.leftMargin = coordinateList.get(0);
                    params.topMargin = coordinateList.get(1);
                    rv.addView(image, params);


                }else{
                    Toast.makeText(LocationScreen.this, "Please open Wi-Fi", Toast.LENGTH_SHORT).show();
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
        /**
         * Wifi scan code finishes Here
         */

    }

    public List<Integer> getCoordinates(String fileName, Context context, String sendedZone)
    {
        BufferedReader bufferedReader = null;
        List<Integer> returningList = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)
            ));
            String line;
            List<CoordinateViewModel> coordinateList = new ArrayList<>();

            while((line = bufferedReader.readLine()) != null)
            {
                String[] dataArray = line.split("\\+");
                CoordinateViewModel coordinates = new CoordinateViewModel(dataArray[0], Integer.parseInt(dataArray[1]), Integer.parseInt(dataArray[2]));
                coordinateList.add(coordinates);
            }


            Iterator<CoordinateViewModel> entryList = coordinateList.iterator();
            while(entryList.hasNext()) {
                CoordinateViewModel entry = entryList.next();
                if (entry.zone.equals(sendedZone)) {

                    returningList.add(entry.coordinateX);
                    returningList.add(entry.coordinateY);


                }
            }
        }catch (FileNotFoundException e){
            Log.d("File not found", e.getMessage());
        }catch (Exception e) {
            Log.d("Data was not taken", e.getMessage());
        }finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (Exception e) {
                Log.d("BF not closed",e.getMessage());
            }
        }
        return  returningList;
    }
}
