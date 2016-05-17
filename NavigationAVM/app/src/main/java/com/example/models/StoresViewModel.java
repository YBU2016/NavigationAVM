package com.example.models;

import com.example.Database.Entities.EntityBase;

/**
 * Created by Toshıba on 12.5.2016.
 */
public class StoresViewModel extends EntityBase
{
    public String StoreNames;
    public String Zone;

    public StoresViewModel(String storeNames, String zone) {
        super();
        StoreNames = storeNames;
        Zone = zone;

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
