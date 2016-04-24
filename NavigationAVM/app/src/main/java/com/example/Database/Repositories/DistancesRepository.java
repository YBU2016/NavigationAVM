package com.example.Database.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Database.Entities.BlankEntity;
import com.example.Database.Entities.DistancesEntity;
import com.example.Database.Entities.EntityBase;
import com.example.models.DistanceViewModel;

import java.util.Vector;

/**
 * Created by Toshıba on 24.4.2016.
 */
public class DistancesRepository extends IRepository
{
    public static final String ID = "ID";
    public static final String TABLE_NAME = "Distances";
    public static final String ZONE = "Zone";
    public static final String SHORTESTDISTANCE = "ShortestDistance";
    public static final String FARTHESTDISTANCE = "FarthestDistance";
    public static final String NEARZONES = "NearZones";
    public static final String BSSID = "BSSID";

    public DistancesRepository(Context context) {
        super(context);
    }

    @Override
    public long GetCount()
    {
        SQLiteDatabase db = super.dbg.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        long r = cur.getCount();
        db.close();
        return r;
    }

    @Override
    public boolean Add(EntityBase e)
    {
        DistancesEntity de = (DistancesEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ZONE, de.getStore_ID());
        cv.put(BSSID, de.getBSSID());
        cv.put(FARTHESTDISTANCE, de.getFarthestDistance());
        cv.put(SHORTESTDISTANCE, de.getShortestDistance());
        cv.put(NEARZONES, de.getNearZones());
        long r = db.insert(TABLE_NAME, null, cv);
        db.close();
        if (r > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean Update(EntityBase e)
    {
        DistancesEntity de = (DistancesEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ZONE, de.getStore_ID());
        cv.put(BSSID, de.getBSSID());
        cv.put(FARTHESTDISTANCE, de.getFarthestDistance());
        cv.put(SHORTESTDISTANCE, de.getShortestDistance());
        cv.put(NEARZONES, de.getNearZones());
        long r = db.update(TABLE_NAME, cv, ID + " = ?",
                new String [] {String.valueOf(de.getID())});
        db.close();
        if (r > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean Delete(int id)
    {
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        long r = db.delete(TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        if (r > 0)
            return true;
        else
            return false;
    }

    @Override
    public EntityBase GetRecord(int id)
    {
        EntityBase entity = null;
        SQLiteDatabase db = dbg.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + "WHERE" + ID +
        " = ?", new String[] {String.valueOf(id)});

        if(cur.moveToNext())
        {
            entity = new DistancesEntity(id, cur.getString(cur.getColumnIndex(ZONE)), cur.getString(cur.getColumnIndex(BSSID)),
                    cur.getString(cur.getColumnIndex(NEARZONES)), cur.getInt(4), cur.getInt(5));
        }else
        {
            entity = new BlankEntity();
        }

        return entity;
    }

    @Override
    public Vector<EntityBase> GetResult()
    {
        EntityBase entity = null;
        SQLiteDatabase db = dbg.getReadableDatabase();
        Cursor cur = db.query(TABLE_NAME, new String[] {ID, ZONE, BSSID, NEARZONES, FARTHESTDISTANCE, SHORTESTDISTANCE},
        "", null, "", "", "");
        Vector<EntityBase> records = new Vector<EntityBase>(cur.getCount());
        while (cur.moveToNext())
        {
            entity = new DistancesEntity(cur.getInt(0), cur.getString(cur.getColumnIndex(ZONE)),
                    cur.getString(cur.getColumnIndex(BSSID)), cur.getString(cur.getColumnIndex(NEARZONES)),
                    cur.getInt(4), cur.getInt(5));
            records.add(entity);
        }

        return records;
    }

    @Override
    public Vector<DistanceViewModel> getDistanceFromBSSID(String sendedBSSID)
    {
        String[] columns = {ID, ZONE, BSSID, NEARZONES, FARTHESTDISTANCE, SHORTESTDISTANCE };
        DistanceViewModel entity;

        SQLiteDatabase dbReadable = dbg.getReadableDatabase();
        Cursor result = dbReadable.query(TABLE_NAME, columns, "BSSID = ?", new String[] {sendedBSSID},null,null,null,null);
        Vector<DistanceViewModel> records = new Vector<>(result.getCount());
        if(result.moveToNext())
        {// zone bssid nearzone farhtest shortest

            while(result.moveToNext())
            {
                entity = new DistanceViewModel(result.getString(1), result.getString(2), result.getString(3),
                        result.getInt(4), result.getInt(5));
                records.add(entity);
            }
        }

        return records;
    }
}
