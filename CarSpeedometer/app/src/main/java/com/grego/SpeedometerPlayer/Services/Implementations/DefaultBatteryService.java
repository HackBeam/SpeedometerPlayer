package com.grego.SpeedometerPlayer.Services.Implementations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;

import java.util.Iterator;
import java.util.LinkedList;

public class DefaultBatteryService extends BroadcastReceiver implements IBatteryService
{
    private final static String BATTERY_LEVEL_KEY = "level";

    private LinkedList<IBatteryListener> subscribers;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int level = intent.getIntExtra(BATTERY_LEVEL_KEY, 0);

        Iterator<IBatteryListener> iterator = subscribers.iterator();
        while (iterator.hasNext())
        {
            iterator.next().OnBatteryLevelReceived(level);
        }
    }

    @Override
    public void StartListening(Context context)
    {
        context.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void StopListening(Context context)
    {
        context.unregisterReceiver(this);
    }

    @Override
    public void Subscribe(IBatteryListener listener)
    {
        if (subscribers == null)
        {
            subscribers = new LinkedList<>();
        }

        subscribers.add(listener);
    }

    @Override
    public void Unsubscribe(IBatteryListener listener)
    {
        subscribers.remove(listener);
    }
}
