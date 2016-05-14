package com.example.Database.Entities;

import java.util.Collections;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class StoresEntity extends EntityBase
{
    private String storeNames;
    private String Zone;
    private int Count;

    public StoresEntity(int id, String storeName, String zone, int count )
    {
        super(id);
        storeNames = storeName;
        Zone = zone;
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getStoreName() {
        return storeNames;
    }

    public void setStoreName(String storeName) {
        storeNames = storeName;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

}
