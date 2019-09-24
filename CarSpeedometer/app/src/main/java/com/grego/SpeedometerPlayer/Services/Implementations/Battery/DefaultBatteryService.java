package com.grego.SpeedometerPlayer.Services.Implementations.Battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;
import com.grego.SpeedometerPlayer.Services.Listeners.IBatteryListener;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Battery info provider service listening to battery level changes.
 */
public class DefaultBatteryService extends BroadcastReceiver implements IBatteryService
{
    private final static String BATTERY_LEVEL_KEY = "level";

    private LinkedList<IBatteryListener> subscribers;
    private int cachedBatteryLevel = -1;

    public DefaultBatteryService()
    {
        subscribers = new LinkedList<>();
    }

    /**
     * Called by Android through the Intent.
     * Notifies all subscribers with the new battery info.
     * @param context Unused context.
     * @param intent The Intent provided by Android with the battery info.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        cachedBatteryLevel = intent.getIntExtra(BATTERY_LEVEL_KEY, 0);

        Iterator<IBatteryListener> iterator = subscribers.iterator();
        while (iterator.hasNext())
        {
            iterator.next().OnBatteryLevelReceived(cachedBatteryLevel);
        }
    }

    /**
     * Registers the service as a receiver for the ACTION_BATTERY_CHANGED intent.
     */
    @Override
    public void StartListening()
    {
        Core.ApplicationContext.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     * Unregisters the service as a receiver for intents.
     */
    @Override
    public void StopListening()
    {
        Core.ApplicationContext.unregisterReceiver(this);
    }

    @Override
    public void Subscribe(IBatteryListener listener)
    {
        subscribers.add(listener);

        // Auto-serve cached level
        if (cachedBatteryLevel != -1)
        {
            listener.OnBatteryLevelReceived(cachedBatteryLevel);
        }
    }

    @Override
    public void Unsubscribe(IBatteryListener listener)
    {
        subscribers.remove(listener);
    }
}
