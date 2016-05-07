package com.example.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Database.Entities.DistancesEntity;
import com.example.Database.Entities.EntityBase;

import org.w3c.dom.Text;

import java.util.Vector;

/**
 * Created by Alparslan on 24.4.2016.
 */
public class DistancesAdapter extends BaseAdapter
{
    private Context context;
    private Vector<EntityBase> model;

    public DistancesAdapter(Context context, Vector<EntityBase> model)
    {
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((DistancesEntity) getItem(position)).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DistancesEntity de = (DistancesEntity) getItem(position);
        TextView tID = new TextView(context);
        tID.setHeight(90);
        return tID;
    }
}
