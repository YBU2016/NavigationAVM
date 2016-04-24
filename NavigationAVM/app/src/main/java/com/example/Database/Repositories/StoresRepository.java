package com.example.Database.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Database.Entities.BlankEntity;
import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.StoresEntity;
import com.example.models.DistanceViewModel;

import java.util.Vector;

/**
 * Created by Toshıba on 24.4.2016.
 */
public class StoresRepository extends IRepository
{
    public static final String TABLE_NAME = "Stores";
    public static final String ID = "ID";
    public static final String STORENAMES = "StoreNames";
    public static final String STOREID = "Store_ID";

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
        return r;
    }

    @Override
    public boolean Add(EntityBase e)
    {
        StoresEntity se = (StoresEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STORENAMES, se.getStoreNames());
        cv.put(STOREID, se.getStore_ID());
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

        cv.put(STORENAMES, se.getStoreNames());
        cv.put(STOREID, se.getStore_ID());

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
                    cur.getString(cur.getColumnIndex(STOREID)));
        }
        else
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

        Cursor cur = db.query(TABLE_NAME, new String[] {ID, STORENAMES, STOREID}, "", null,"", "", "");
        Vector<EntityBase> records = new Vector<EntityBase>(cur.getCount());
        while (cur.moveToFirst())
        {
            entity = new StoresEntity(cur.getInt(0), cur.getString(cur.getColumnIndex(STORENAMES)),
                    cur.getString(cur.getColumnIndex(STOREID)));
            records.add(entity);
        }
        return records;
    }

    @Override
    public Vector<DistanceViewModel> getDistanceFromBSSID(String sendedBSSID) {
        return null;
    }
}
