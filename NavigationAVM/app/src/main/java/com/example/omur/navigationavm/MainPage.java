package com.example.omur.navigationavm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;


import com.example.Adapters.NavListAdapter;
import com.example.DrawerFragments.fragment_about;
import com.example.DrawerFragments.fragment_home;
import com.example.DrawerFragments.fragment_settings;
import com.example.models.NavItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainPage extends AppCompatActivity
{
    public static TreeMap<Double, String> FiveOfResultMap = new TreeMap<Double, String>();
    public static WifiManager wifi;
    public static List<ScanResult> results;
    Context context = this;

    TextView textView;
    DrawerLayout drawerLayout ;
    RelativeLayout drawerPane ;
    ListView lstNav ;

    List<NavItem> listnavitem ;
    List<Fragment> listfragment ;

    ActionBarDrawerToggle actionBarDrawerToggle ;

    public double calculateDistance(double levelInDb, double freqInMHz){
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lstNav= (ListView) findViewById(R.id.nav_list);


        // drawer listesine adapter kullanarak title subtitle ve icon atamak için ;

        listnavitem=new ArrayList<NavItem>() ;
        listnavitem.add(new NavItem("Home","Home Page",R.mipmap.ic_launcher)) ;
        listnavitem.add(new NavItem("Settings","Change something",R.mipmap.ic_launcher)) ;
        listnavitem.add(new NavItem("About","Information",R.mipmap.ic_launcher)) ;

        NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav_list,listnavitem);

        lstNav.setAdapter(navListAdapter);

        // fragment işlemleri için ;

        listfragment = new ArrayList<Fragment>();

        listfragment.add(new fragment_home()) ;
        listfragment.add(new fragment_settings()) ;
        listfragment.add(new fragment_about()) ;

        // ilk fragmentı default olarak ayarlıyoruz
       final FragmentManager fragmentManager = getSupportFragmentManager() ;
        fragmentManager.beginTransaction().replace(R.id.main_content,listfragment.get(0)).commit() ;

        setTitle(listnavitem.get(0).getTitle());
        lstNav.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);

        lstNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //hangi pozisyon tıklanırsa o fragment gelmesi için get(0) ları get(position) yapıp , listener içine koyuyoruz

                FragmentManager fragmentManager = getSupportFragmentManager() ;
                fragmentManager.beginTransaction().replace(R.id.main_content,listfragment.get(position)).commit() ;
                setTitle(listnavitem.get(position).getTitle());

                lstNav.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);

            }
        });

        // drawer layout için listener oluşturuyoruz

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_opened,R.string.drawer_closed)
        {
            @Override
            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {

                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);

            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);



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
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();

                    for (Map.Entry entryMap : FiveOfResultMap.entrySet())
                        editor.putString(entryMap.getKey().toString(), entryMap.getValue().toString());
                        editor.commit();

                    /**
                     * This part is finding zone. We sent nearest 5 modems to Area Class.
                     * In this class, we have several operations.
                     */
                    Area areaClass = new Area(context);
                    areaClass.findZone(FiveOfResultMap);
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
        /**
         * Wifi scan code finishes Here
         */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true ;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }
}