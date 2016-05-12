package com.example.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.StoresEntity;
import com.example.Database.Repositories.IRepository;
import com.example.Database.Repositories.RepositoryContainer;
import com.example.Database.Repositories.RepositoryNames;
import com.example.models.StoresViewModel;
import com.example.omur.navigationavm.DatabaseScreen;
import com.example.omur.navigationavm.LocationScreen;
import com.example.omur.navigationavm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Fragment1 extends Fragment
{
    Button goDatabaseButton, goLocationScreenButton;
    private AutoCompleteTextView autoCompleteTextView;

    private RepositoryContainer repositoryContainer;
    private IRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1,container , false);
        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.chooseStore);
        repositoryContainer = RepositoryContainer.create(getActivity());
        repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);

        List<StoresViewModel> StoreList;
        ArrayAdapter<String> adapter = null;
        if((StoreList = (repository.GetAllRecords())) != null)
        {
            List<String> storeStringList = new ArrayList<>();
            for (int counter = 0; counter < StoreList.size(); counter++)
            {
                storeStringList.add(StoreList.get(counter).StoreNames);
            }
            adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, storeStringList);
        }


        autoCompleteTextView.setAdapter(adapter);

        goDatabaseButton = (Button) v.findViewById(R.id.AddData);
        goDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatabaseScreen.class);
                startActivity(intent);
            }
        });

        goLocationScreenButton = (Button) v.findViewById(R.id.LocationScreen);
        goLocationScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationScreen.class);
                startActivity(intent);
            }
        });


        return v;
    }
}