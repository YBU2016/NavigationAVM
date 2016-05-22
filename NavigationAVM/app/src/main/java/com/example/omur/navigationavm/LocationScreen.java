package com.example.omur.navigationavm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

                    } else if (Zone == "" || Zone == null) {
                        Toast.makeText(c, "Zone could not retrieved", Toast.LENGTH_LONG).show();
                    }

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


        /**
         * HERE STARTS NAVIGATION CODE
         * Running steady for these inputs
         * destinationZone = "1.2.14";
         * Zone = "1.2.4";
         *
         * destinationZone = "1.2.4";
         * Zone = "1.2.14";
         *
         * destinationZone = "1.2.13";
         * Zone = "1.2.3";
         *
         * destinationZone = "1.2.16";
         * Zone = "1.2.3";
         *
         * destinationZone = "1.2.3";
         * Zone = "1.2.13";
         *
         * destinationZone = "1.2.3";
         * Zone = "1.2.6";
         * destinationZone = "1.2.16";
         * Zone = "1.2.5";
         *
         * destinationZone = "1.1.16";
         * Zone = "1.2.5";
         *
         * destinationZone = "1.2.16";
         * Zone = "1.1.15";
         */

        destinationZone = "1.2.16";
        Zone = "1.1.15";

        if(destinationZone != null && Zone != null)
        {
            String[] destinationZoneArray = destinationZone.split("\\.");
            String[] locationZoneArray = Zone.split("\\.");
            int destinationStoreNumber = Integer.parseInt(destinationZoneArray[2]);
            int locationStoreNumber = Integer.parseInt(locationZoneArray[2]);

            //Firstly control for floor
            if(destinationZoneArray[0].equals(locationZoneArray[0]))
            {
                //Secondly control for corridor
                if(destinationZoneArray[1].equals(locationZoneArray[1]))
                {
                    // Thirdly Control for Store Number
                    if((destinationStoreNumber % 2 == 0 && locationStoreNumber % 2 == 0) ||
                       (destinationStoreNumber % 2 == 1 && locationStoreNumber % 2 == 1))
                    {
                        if(destinationStoreNumber < locationStoreNumber)
                        {
                            for(int counter = locationStoreNumber ; counter > (destinationStoreNumber-2); counter -= 2)
                            {
                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append(counter );
                                PathZoneList.add(sb.toString());
                            }
                        }else{
                            for(int counter = (locationStoreNumber - 2) ; counter < destinationStoreNumber; counter += 2)
                            {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append((counter + 2));
                                PathZoneList.add(sb.toString());
                            }
                        }

                    }else if((destinationStoreNumber % 2 == 0 && locationStoreNumber % 2 == 1) ||
                            (destinationStoreNumber % 2 == 1 && locationStoreNumber % 2 == 0))
                    {
                        // location 1.1.6 destination 1.1.1    location 1.1.2 destionation 1.1.17
                        if(destinationStoreNumber < locationStoreNumber)
                        {
                            for(int counter = locationStoreNumber ; counter > (destinationStoreNumber - 3); counter -= 2) {

                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append((counter));
                                repositoryContainer = RepositoryContainer.create(this);
                                repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);
                                if(repository.isZoneInNearZones(sb.toString()))
                                {
                                    PathZoneList.add(sb.toString());
                                }else{
                                    break;
                                }
                            }
                        }else if(locationStoreNumber < destinationStoreNumber){
                            for(int counter = locationStoreNumber ; counter < destinationStoreNumber ; counter += 2) {

                                StringBuilder sb = new StringBuilder();
                                sb.append((locationZoneArray[0] + "."));
                                sb.append((locationZoneArray[1] + "."));
                                sb.append(counter);

                                repositoryContainer = RepositoryContainer.create(this);
                                repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);
                                if(repository.isZoneInNearZones(sb.toString()))
                                {
                                    PathZoneList.add(sb.toString());
                                }else{
                                    break;
                                }
                            }
                        }
                    }
                }else{
                    /**
                     * If corridor numbers are different
                     * We find 2 different path and from their FarthestDistance - ShortestDistance difference,
                     * we will find out the shorter one.
                     * This part is specialized for if location corridor is 1 and destination corridor is 2.
                     * Location 1.1.16 Destination 1.2.15
                     */
                    int firstCorridorHighLimit = 31;
                    int secondCorridorHighLimit = 24;
                    int limit = firstCorridorHighLimit;
                    int firstCorridorUnderLimit = 9;
                    int secondCorridorUnderLimit = 1;
                    int underLİmit = firstCorridorUnderLimit;
                    if(destinationZoneArray[1].equals("1") && locationZoneArray[1].equals("2"))
                    {
                        underLİmit = secondCorridorUnderLimit;
                        limit = secondCorridorHighLimit;
                    }


                    List<String> path1 = new ArrayList<>();
                    List<String> path2 = new ArrayList<>();
                    // From left the calculation is:
                    if(locationStoreNumber % 2 == 1) {
                        for (int counter = locationStoreNumber; counter < limit; counter += 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path1.add(sb.toString());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("1.3.0");
                        path1.add(sb.toString());

                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = secondCorridorHighLimit; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 27; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        }
                    }else if( locationStoreNumber % 2 == 0) {
                        for (int counter = locationStoreNumber; counter < limit; counter += 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path1.add(sb.toString());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("1.3.0");
                        path1.add(sb.toString());

                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = secondCorridorHighLimit; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                            // For testing PathZoneList = path1;
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 27; counter >= destinationStoreNumber; counter -= 2) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append((destinationZoneArray[0] + "."));
                                sb2.append((destinationZoneArray[1] + "."));
                                sb2.append(counter);
                                path1.add(sb2.toString());
                            }
                        }
                    }
                    /**
                     * Here path2 is found. After it is found, shorter one will be declared and it will be our main path
                     * destinationZone = "1.1.5";
                     * Zone = "1.2.16";
                     *
                     * destinationZone = "1.1.16";
                     * Zone = "1.2.15";
                     */

                    if(locationStoreNumber % 2 == 1) {
                        for (int counter = locationStoreNumber; counter >= underLİmit; counter -= 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path2.add(sb.toString());
                        }
                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                            // For testing PathZoneList = path2; destinationZone = "1.1.5"; Zone = "1.2.16";
                        }
                    }else if( locationStoreNumber % 2 == 0) {
                        for (int counter = locationStoreNumber; counter >= 0; counter -= 2) {

                            StringBuilder sb = new StringBuilder();
                            sb.append((locationZoneArray[0] + "."));
                            sb.append((locationZoneArray[1] + "."));
                            sb.append(counter);
                            path2.add(sb.toString());
                        }
                        if (destinationStoreNumber % 2 == 0) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        } else if (destinationStoreNumber % 2 == 1) {
                            for (int counter = 0; counter <= destinationStoreNumber; counter += 2) {
                                StringBuilder sb = new StringBuilder();
                                sb.append((destinationZoneArray[0] + "."));
                                sb.append((destinationZoneArray[1] + "."));
                                sb.append(counter);
                                path2.add(sb.toString());
                            }
                        }
                    }
                    /**
                     * This is one of the most important part of the navigation part.
                     * If user wants to change corridor, we look to number of zones which user should pass,
                     * and we show user shorter one. The least Zone number, the shorter path
                     */
                    PathZoneList = path1;
                    if(path1.size() > path2.size())
                    {
                        PathZoneList = path2;
                    }
                   // Log.d("Liste", path1.toString());
                   // Log.d("Liste", path2.toString());
                // For testing PathZoneList = path1;
                }
            }

        }// End of controling Zone and Destionation Zone is null or not.

        Log.d("Liste", PathZoneList.toString());

        if(PathZoneList != null)
        {
            Iterator<String> pathListIterator = PathZoneList.iterator();
            while(pathListIterator.hasNext())
            {

                RelativeLayout rv = (RelativeLayout) findViewById(R.id.OuterRelativeLayout);
                RelativeLayout.LayoutParams params;
                ImageButton image = new ImageButton(getApplicationContext());
                image.setBackgroundResource(R.drawable.blue);


                List<Integer> coordinateList = new ArrayList<>();
                if ((coordinateList = getCoordinates("MarkerCoordinates.txt", getApplicationContext(), pathListIterator.next())) != null) {
                    if (coordinateList.size() > 0) {
                        params = new RelativeLayout.LayoutParams(30, 30);
                        params.leftMargin = coordinateList.get(0);
                        params.topMargin = coordinateList.get(1);
                        rv.addView(image, params);
                    } else {
                        Toast.makeText(getApplicationContext(), Zone + "Is not in MarkerCoordinates.txt", Toast.LENGTH_LONG).show();
                    }
                }

                }
            }
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
}
