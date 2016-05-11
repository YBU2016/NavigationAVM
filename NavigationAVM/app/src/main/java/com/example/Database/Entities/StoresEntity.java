package com.example.Database.Entities;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class StoresEntity extends EntityBase
{
    private String storeNames;
    private String Zone;

    public StoresEntity(int id, String storeName, String zone )
    {
        super(id);
        storeNames = storeName;
        Zone = zone;
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
