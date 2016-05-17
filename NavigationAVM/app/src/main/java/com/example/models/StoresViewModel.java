package com.example.models;

import com.example.Database.Entities.EntityBase;

/**
 * Created by Toshıba on 12.5.2016.
 */
public class StoresViewModel extends EntityBase
{
    public String StoreNames;
    public String Zone;
    public int Count;
    public StoresViewModel(String storeNames, String zone, int count) {
        super();
        StoreNames = storeNames;
        Zone = zone;
        Count=count ;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public StoresViewModel(){}

    public String getStoreNames() {
        return StoreNames;
    }

    public void setStoreNames(String storeNames) {
        StoreNames = storeNames;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

}
