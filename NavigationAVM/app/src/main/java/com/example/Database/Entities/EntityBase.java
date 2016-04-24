package com.example.Database.Entities;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class EntityBase
{
    private int ID;

    public EntityBase()
    {

    }

    public EntityBase(int id)
    {
        this();
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
