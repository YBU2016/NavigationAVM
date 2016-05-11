package com.example.Database.Repositories;

import android.content.Context;
import android.util.Log;

import com.example.Database.Entities.DistancesEntity;
import com.example.Database.Entities.LoginEntity;
import com.example.Database.Entities.StoresEntity;
import com.example.models.DistanceViewModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Alparslan on 7.5.2016.
 * In this class, we entered static stores data to our database. We record this data to a txt file in assets
 * Its name is StoreNamesText.txt. So if one install our program, this data will be his/her mobile phone also
 *
 */
public class StaticData
{
    private RepositoryContainer repositoryContainer;
    private IRepository repository;
    private Vector<DistanceViewModel> model;

    public void insertStoreTableData(String fileName, Context context)
    {
        repositoryContainer = RepositoryContainer.create(context);
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)
            ));
            String line;

            while((line = bufferedReader.readLine()) != null)
            {
                String[] dataArray = line.split("\\+");
                if(dataArray.length == 2)
                {
                    repository = repositoryContainer.getRepository(RepositoryNames.STORENAMES);
                    repository.Add(new StoresEntity(0, dataArray[0], dataArray[1]));
                }

                if(dataArray.length == 5) {
                    repository = repositoryContainer.getRepository(RepositoryNames.DISTANCES);
                    if (!repository.isInDatabase(dataArray[0], dataArray[1], dataArray[2],
                            Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[4]))) {
                        repository.Add(new DistancesEntity(0, dataArray[0], dataArray[1],
                                dataArray[2], Integer.parseInt(dataArray[4]), Integer.parseInt(dataArray[3])));
                    }
                }
                if(dataArray.length == 6) {
                    repository = repositoryContainer.getRepository(RepositoryNames.LOGIN);
                    if (!repository.isAdminInDatabase(dataArray[0], dataArray[1], dataArray[2],
                            dataArray[3], dataArray[4])) {
                        repository.Add(new LoginEntity(0, dataArray[0], dataArray[1],
                                dataArray[2], dataArray[3], dataArray[4]));
                    }
                }

            }
            // Log.d("Text Data Comes", storeHashMap.toString());

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
    }
}
