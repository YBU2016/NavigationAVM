package com.example.Database.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.Database.Entities.BlankEntity;
import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.StoresEntity;
import com.example.models.DistanceViewModel;
import com.example.models.StoresViewModel;

import java.util.Vector;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class StoresRepository extends IRepository
{
    public static final String TABLE_NAME = "Stores";
    public static final String ID = "ID";
    public static final String STORENAMES = "StoreNames";
    public static final String ZONE = "Zone";
    public static final String COUNT = "Count";

    public StoresRepository(Context context)
    {
        super(context);
    }

    @Override
    public long GetCount()
    {
        SQLiteDatabase db = super.dbg.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        long r = cur.getCount();
        cur.close();
        return r;
    }

    @Override
    public boolean Add(EntityBase e)
    {
        StoresEntity se = (StoresEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STORENAMES, se.getStoreName());
        cv.put(ZONE, se.getZone());
        cv.put(COUNT, se.getCount());
        long r = db.insert(TABLE_NAME, null, cv);
        db.close();

        if(r > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean Update(EntityBase e)
    {
        StoresEntity se = (StoresEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

      //  cv.put(STORENAMES, se.getStoreName());
     //   cv.put(ZONE, se.getZone());
        cv.put(COUNT, se.getCount());
        long r = db.update(TABLE_NAME, cv, ID + " = ?",
                new String[]{ String.valueOf(se.getID())});
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
                new String[] {String.valueOf(id)});
        db.close();
        if(r > 0)
            return true;
        else
            return false;
    }

    @Override
    public EntityBase GetRecord(int id)
    {
        EntityBase entity = null;
        SQLiteDatabase db = dbg.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
            + ID + " = ?", new String[] {String.valueOf(id)});

        if (cur.moveToNext())
        {
            entity = new StoresEntity(id, cur.getString(cur.getColumnIndex(STORENAMES)),
                    cur.getString(cur.getColumnIndex(ZONE)), cur.getInt(cur.getColumnIndex(COUNT)));
        }
        else
        {
            entity = new BlankEntity();
        }

        cur.close();
        return entity;
    }

    @Override
    public Vector<EntityBase> GetResult()
    {
        EntityBase entity = null;
        SQLiteDatabase db = dbg.getReadableDatabase();

        Cursor cur = db.query(TABLE_NAME, new String[] {ID, STORENAMES, ZONE}, "", null,"", "", "");
        Vector<EntityBase> records = new Vector<EntityBase>(cur.getCount());
        while (cur.moveToFirst())
        {
            entity = new StoresEntity(cur.getInt(0), cur.getString(cur.getColumnIndex(STORENAMES)),
                    cur.getString(cur.getColumnIndex(ZONE)), cur.getInt(cur.getColumnIndex(COUNT)));
            records.add(entity);
        }
        cur.close();
        return records;
    }

    @Override
    public Vector<StoresViewModel> GetAllRecords() {
        StoresViewModel entity;
        SQLiteDatabase db = dbg.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT " + STORENAMES + ", " +ZONE + " FROM " + TABLE_NAME, null);

        Vector<StoresViewModel> records = new Vector<StoresViewModel>(cur.getCount());
        while (cur.moveToNext())
        {
            entity = new StoresViewModel(cur.getString(0), cur.getString(1));
            records.add(entity);
        }
        cur.close();
        return records;

    }

    @Override
    public boolean isInDatabase(String storeName) {
        SQLiteDatabase dbReadable = dbg.getReadableDatabase();

        Cursor cur = dbReadable.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE StoreNames = ? ",new String[]
                {storeName});

        if (cur.moveToFirst())
        {
            cur.close();
            return true;
        }else{
            cur.close();
            return false;
        }
    }

    @Override
    public String getIdFromStoreName(String storeName) {
        SQLiteDatabase db = dbg.getReadableDatabase();

        String id = null;
        Cursor cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE StoreNames = ? ",new String[]
                {storeName});

        if(cur.moveToNext())
        {
            id = cur.getString(0);
        }

        cur.close();
        return id;
    }


    @Override
    public String getZoneFromStoreName(String storeName) {
        SQLiteDatabase db = dbg.getReadableDatabase();

        String Zone = null;
        Cursor cur = db.rawQuery("SELECT " + ZONE + " FROM " + TABLE_NAME + " WHERE StoreNames = ? ",new String[]
                {storeName});

        if(cur.moveToNext())
        {
            Zone = cur.getString(0);
        }

        cur.close();
        return Zone;
    }
}
