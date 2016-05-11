package com.example.Database.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Database.Entities.BlankEntity;
import com.example.Database.Entities.EntityBase;
import com.example.Database.Entities.LoginEntity;
import com.example.models.DistanceViewModel;

import java.util.Vector;

/**
 * Created by omur on 08.05.2016.
 */
public class LoginRepository extends IRepository {

    public static final String TABLE_NAME = "Login";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    public LoginRepository(Context context) {
        super(context);
    }

    @Override
    public long GetCount() {
        SQLiteDatabase db = super.dbg.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        long r = cur.getCount();
        return r;
    }

    @Override
    public boolean Add(EntityBase e) {

        LoginEntity se = (LoginEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, se.getName());
        cv.put(SURNAME, se.getSurName());
        cv.put(USERNAME, se.getUserName());
        cv.put(EMAIL, se.getEmail());
        cv.put(PASSWORD, se.getPassword());


        long r = db.insert(TABLE_NAME, null, cv);
        db.close();

        if(r > 0)
            return true;
        else
            return false;

    }

    @Override
    public boolean Update(EntityBase e) {

        LoginEntity se = (LoginEntity) e;
        SQLiteDatabase db = super.dbg.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, se.getName());
        cv.put(SURNAME, se.getSurName());
        cv.put(USERNAME, se.getUserName());
        cv.put(EMAIL, se.getEmail());
        cv.put(PASSWORD, se.getPassword());
        long r = db.update(TABLE_NAME, cv, ID + " = ?",
                new String[]{ String.valueOf(se.getID())});
        db.close();

        if (r > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean Delete(int id) {
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
    public EntityBase GetRecord(int id) {

        EntityBase entity = null;
        SQLiteDatabase db = dbg.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + ID + " = ?", new String[] {String.valueOf(id)});

        if (cur.moveToNext())
        {
            entity = new LoginEntity(id, cur.getString(cur.getColumnIndex(NAME)),cur.getString(cur.getColumnIndex(SURNAME)),
                    cur.getString(cur.getColumnIndex(USERNAME)),cur.getString(cur.getColumnIndex(EMAIL)),
                    cur.getString(cur.getColumnIndex(PASSWORD)));
        }
        else
        {
            entity = new BlankEntity();
        }

        cur.close();
        return entity;
    }

    @Override
    public Vector<EntityBase> GetResult() {
        return null;
    }

    @Override
    public String getSinlgeEntry(String USERNAME)
    {

        SQLiteDatabase db = super.dbg.getReadableDatabase();
        Cursor cursor=db.query("LOGIN", null, " Username=?", new String[]{USERNAME}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    @Override
    public boolean isAdminInDatabase(String name, String surname, String username, String email, String password) {
        SQLiteDatabase dbReadable = dbg.getReadableDatabase();

        Cursor cur = dbReadable.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Name = ? AND SurName = ? " +
                "AND Username = ? AND Email = ? AND Password = ?", new String[]
                {name, surname, username, email, password});

        if (cur.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

}
