package com.example.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.Database.Entities.StoresEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.omur.navigationavm.R;

import java.util.ArrayList;


/**
 * Created by omur on 12.03.2016.
 */
public class Fragment2 extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lst;
    private ArrayList<String> StoreList;

    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    int i ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment2,container , false);

       repositoryContainer = RepositoryContainer.create(v.getContext());
        repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);

        int r = (int) repository.GetCount();

        lst = (ListView)v.findViewById(R.id.StorelistView);

        lst.setOnItemClickListener(this);

        StoreList=new ArrayList<>();

        for(i=2 ; i<r ; i++)
        {
            StoresEntity se = (StoresEntity) repository.GetRecord(i);
            StoreList.add(se.getStoreName()) ;
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.store_list, R.id.storetxt, StoreList
        );


        lst.setAdapter(adapter);


        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}