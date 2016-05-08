package com.example.Database.Repositories;

import android.content.Context;

import com.example.Database.DbGateway;
import com.example.Database.Entities.EntityBase;
import com.example.models.DistanceViewModel;

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
    public abstract Vector<DistanceViewModel> getDistanceFromBSSID(String sendedBSSID);
    public abstract boolean isInDatabase(String zone, String bssid, String nearzone, int fathest, int shortest);


}
