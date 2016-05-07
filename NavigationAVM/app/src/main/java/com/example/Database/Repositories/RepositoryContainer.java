package com.example.Database.Repositories;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class RepositoryContainer
{
    private static RepositoryContainer rc = null;
    private Context context;

    private ArrayList<IRepository> repositories;

    private RepositoryContainer(Context context)
    {
        repositories = new ArrayList<IRepository>();
        this.context = context;
    }

    // Singleton Design Pattern is needed here.
    public static RepositoryContainer create(Context context)
    {
        if(rc == null)
        {
            rc = new RepositoryContainer(context);
        }
        return rc;
    }

    /**
     * Factory Design Pattern is needed Here
     * It returns instances according to Repository Name
     */

    public IRepository getRepository(int repName)
    {
        IRepository ir = null;
        switch (repName)
        {
            case RepositoryNames.STORENAMES:
                ir = new StoresRepository(context);
                break;
            case RepositoryNames.DISTANCES:
                ir = new DistancesRepository(context);
                break;
        }
        return ir;
    }
}
