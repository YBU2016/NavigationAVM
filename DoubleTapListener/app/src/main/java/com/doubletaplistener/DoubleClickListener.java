package com.doubletaplistener;

import android.view.View;

/**
 * Created by Toshıba on 23.5.2016.
 */
public abstract class DoubleClickListener implements View.OnClickListener
{
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    long lastClickTime = 0;

    @Override
    public void onClick(View v)
    {
        long clickTime = System.currentTimeMillis();
        if(clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA)
        {
            onDoubleClick(v);
        }else{
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }
    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
}
