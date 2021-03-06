package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.models.CoordinateViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Alparslan on 7.5.2016.
 */
public class LocationScreen extends Activity {

    private final String FILENAME = "destination";
    public static TreeMap<Double, String> FiveOfResultMap = new TreeMap<>();
    public static WifiManager wifi;
    public static List<ScanResult> results;
    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    public String Zone = null;
    public String destinationZone;
    public List<String> PathZoneList = new ArrayList<>();


    public double calculateDistance(double levelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationscreen);

        repositoryContainer = RepositoryContainer.create(this);

        String fpath = "/sdcard/" + FILENAME + ".txt";
        File file = new File(fpath);

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);

            BufferedReader br = new BufferedReader(isr);
            String line = "";
            List<Integer> coordinateList = new ArrayList<>();
            if ((line = br.readLine()) != null) {

                repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);
                destinationZone = repository.getZoneFromStoreName(line);
                // Deleting the file.
                file.delete();

                if ((coordinateList = getCoordinates("MarkerCoordinates.txt", this, destinationZone)) != null) {
                    RelativeLayout rv = (RelativeLayout) findViewById(R.id.OuterRelativeLayout);
                    RelativeLayout.LayoutParams params;
                    ImageButton image = new ImageButton(this);
                    image.setBackgroundResource(R.drawable.blue);
                    image.setId(View.NO_ID);

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "Destination", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (coordinateList.size() > 0) {
                        params = new RelativeLayout.LayoutParams(30, 30);
                        params.leftMargin = coordinateList.get(0);
                        params.topMargin = coordinateList.get(1);
                        rv.addView(image, params);
                    }

                } else {
                    Toast.makeText(this, "This zone has not markercoordinates", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "File is empty", Toast.LENGTH_LONG).show();
            }
            fis.close();
            isr.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


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
                while (entries.hasNext()) {
                    Map.Entry<Double, String> entry = entries.next();
                    if (count > 4) break;
                    FiveOfResultMap.put(entry.getKey(), entry.getValue());
                    count++;
                }


                if (FiveOfResultMap.size() > 0) {
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
                    if (!Zone.equals(null) && (coordinateList = getCoordinates("MarkerCoordinates.txt", c, Zone)) != null && !Zone.equals("")) {
                        if (coordinateList.size() > 0) {
                            params = new RelativeLayout.LayoutParams(30, 30);
                            params.leftMargin = coordinateList.get(0);
                            params.topMargin = coordinateList.get(1);
                            rv.addView(image, params);
                        } else {
                            Toast.makeText(c, Zone + "Is not in MarkerCoordinates.txt", Toast.LENGTH_LONG).show();
                        }

                    } else if (Zone == "" || Zone.isEmpty()) {
                        Toast.makeText(c, "Zone could not retrieved", Toast.LENGTH_LONG).show();
                    }


                    if(Zone != null && destinationZone != null) {
                        PathFinder path = new PathFinder();
                        PathZoneList = path.findZonePath(destinationZone, Zone, c);
                        Log.d("destination and zone", Zone + " +" + destinationZone);
                    }
                    if (PathZoneList != null) {
                        Iterator<String> pathListIterator = PathZoneList.iterator();
                        while (pathListIterator.hasNext()) {

                            RelativeLayout relative = (RelativeLayout) findViewById(R.id.OuterRelativeLayout);
                            RelativeLayout.LayoutParams params2;
                            ImageButton image2 = new ImageButton(getApplicationContext());
                            image2.setBackgroundResource(R.drawable.lightblue);


                            List<Integer> coordinateList2 = new ArrayList<>();
                            if ((coordinateList2 = getCoordinates("MarkerCoordinates.txt", getApplicationContext(), pathListIterator.next())) != null) {
                                if (coordinateList2.size() > 0) {
                                    params2 = new RelativeLayout.LayoutParams(30, 30);
                                    params2.leftMargin = coordinateList2.get(0);
                                    params2.topMargin = coordinateList2.get(1);
                                    relative.addView(image2, params2);

                                } else {
                                    Toast.makeText(getApplicationContext(), Zone + "Is not in MarkerCoordinates.txt", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }

                    if(PathZoneList != null && destinationZone != null) {
                        VoiceManager voiceManager = new VoiceManager();
                        voiceManager.makeVoice(PathZoneList, c);
                    }

                    if(PathZoneList.size() > 0 && !destinationZone.isEmpty()) {
                        if (Zone.equals(destinationZone)) {
                            MediaPlayer mp = MediaPlayer.create(c, R.raw.destination);
                            mp.start();
                        }
                    }
/**
 *
 * if(Zone == null && destinationZone != null)
 {
 Toast.makeText(c, "Zone", Toast.LENGTH_LONG).show();
 }
 else if(destinationZone == null && Zone != null)
 {
 Toast.makeText(c, "Destination Zone", Toast.LENGTH_LONG).show();
 }else if(destinationZone == null && Zone == null)
 {
 Toast.makeText(c, "Zone and destination Zone", Toast.LENGTH_LONG).show();
 }
 */



                } else {
                    Toast.makeText(LocationScreen.this, "Please open Wi-Fi", Toast.LENGTH_SHORT).show();
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
        /**
         * Wifi scan code finishes Here
         *
         * If user choose the store which he/she wants to go, we will show this store's place in floor plan
         */
    }

    public List<Integer> getCoordinates(String fileName, Context context, String sendedZone) {
        BufferedReader bufferedReader = null;
        List<Integer> returningList = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)
            ));
            String line;
            List<CoordinateViewModel> coordinateList = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                String[] dataArray = line.split("\\+");
                CoordinateViewModel coordinates = new CoordinateViewModel(dataArray[0], Integer.parseInt(dataArray[1]), Integer.parseInt(dataArray[2]));
                coordinateList.add(coordinates);
            }


            Iterator<CoordinateViewModel> entryList = coordinateList.iterator();
            while (entryList.hasNext()) {
                CoordinateViewModel entry = entryList.next();
                if (entry.zone.equals(sendedZone)) {

                    returningList.add(entry.coordinateX);
                    returningList.add(entry.coordinateY);
                }
            }
        } catch (FileNotFoundException e) {
            Log.d("File not found", e.getMessage());
        } catch (Exception e) {
            Log.d("Data was not taken", e.getMessage());
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (Exception e) {
                Log.d("BF not closed", e.getMessage());
            }
        }
        return returningList;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }
}
