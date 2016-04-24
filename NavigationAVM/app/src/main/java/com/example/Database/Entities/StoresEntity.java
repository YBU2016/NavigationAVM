package com.example.Database.Entities;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class StoresEntity extends EntityBase
{
    private String StoreName;
    private String Store_ID;

    public StoresEntity(int id, String storeName, String store_ID )
    {
        super(id);
        StoreName = storeName;
        Store_ID = store_ID;
    }

    public String getStore_ID() {
        return Store_ID;
    }

    public void setStore_ID(String store_ID) {
        Store_ID = store_ID;
    }

    public String getStoreNames() {
        return StoreName;
    }

    public void setStoreNames(String storeNames) {
        StoreName = storeNames;
    }
}
