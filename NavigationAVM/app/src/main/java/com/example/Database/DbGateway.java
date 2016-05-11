package com.example.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.Database.Repositories.StaticData;

import java.util.ArrayList;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class DbGateway extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "AVM.db";
    private static final String TABLESTORENAME = "Stores";
    private static final String TABLEDISTANCES = "Distances";
    private static final String TABLElOGIN = "Login" ;

    public DbGateway(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public DbGateway(Context context)
    {
        super(context, DATABASE_NAME, null, 1 );
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLESTORENAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "StoreNames TEXT, Zone TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLEDISTANCES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Zone TEXT, BSSID TEXT, NearZones TEXT, FarthestDistance INTEGER, ShortestDistance INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLElOGIN + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME text,SURNAME text,USERNAME text,EMAIL text,PASSWORD text)");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        Log.i("DbGateway:onOpen", "Bağlantı Açıldı");
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {
                alc.set(0,c);
                c.moveToFirst();
                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
