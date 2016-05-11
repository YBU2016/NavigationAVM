package com.example.omur.navigationavm;

import android.support.multidex.MultiDex;
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

import com.example.Adapters.NavListAdapter;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.DrawerFragments.fragment_about;
import com.example.DrawerFragments.fragment_home;
import com.example.DrawerFragments.fragment_settings;
import com.example.models.NavItem;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity
{
    private RepositoryContainer repositoryContainer;
    DrawerLayout drawerLayout ;
    RelativeLayout drawerPane ;
    ListView lstNav ;

    List<NavItem> listnavitem ;
    List<Fragment> listfragment ;

    ActionBarDrawerToggle actionBarDrawerToggle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MultiDex.install(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repositoryContainer = RepositoryContainer.create(this);

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