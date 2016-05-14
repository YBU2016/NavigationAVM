package com.example.Database.Repositories;

import android.content.Context;

import com.example.Database.DbGateway;
import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.StoresEntity;
import com.example.models.DistanceViewModel;
import com.example.models.StoresViewModel;

import java.util.Vector;

/**
 * Created by Alparslan on 24.4.2016.
 */
public abstract class IRepository
{
    private static final String DATABASE_NAME = "AVM.db";

    protected Context context;
    protected static DbGateway dbg;

    public IRepository(Context context)
    {
        this.context = context;
        dbg = new DbGateway(context,DATABASE_NAME, null, 1);

    }

    public abstract long GetCount();
    public abstract boolean Add(EntityBase e);
    public abstract boolean Update(EntityBase e);
    public abstract boolean Delete(int id);
    public abstract EntityBase GetRecord(int id);
    public abstract Vector<EntityBase> GetResult();

    public Vector<StoresViewModel> GetAllRecords(){
        return null;
    }

    /**
     * These two methods will be crashed in DistancesRepository class.
     * From only DistanceRepository Class
     *
     * @return A null Vector if it is not called from DistanceRepository
     */
    public Vector<DistanceViewModel> getDistanceFromBSSID(String sendedBSSID)
    {
        Vector<DistanceViewModel> s = null;
        return s;
    }

    /**
     * From only DistanceRepository Class
     *
     * @return A boolean [true]
     */
    public boolean isInDatabase(String zone, String bssid, String nearzone, int fathest, int shortest)
    {
        return true;
    }

    /**
     * From only StoreRepository Class
     *
     * @return A boolean [true]
     */
    public boolean isInDatabase(String storeName)
    {
        return true;
    }

    public String getZoneFromStoreName(String storeName)
    {
        return null;
    }
    public String getIdFromStoreName(String storeName)
    {
        return null;
    }
    /**
     * From only LoginRepository Class
     *
     * @return A boolean [true]
     */

    public boolean  isAdminInDatabase(String name, String surname, String username, String email, String password)
    {
        return true;
    }


    public String getSinlgeEntry(String USERNAME)
    {
        return null;
    }

    public  String GetId (String USERNAME) {return null;}


}
