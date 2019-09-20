package com.grego.SpeedometerPlayer.Services.Definitions;

import android.content.Context;
import android.location.LocationListener;

import com.grego.SpeedometerPlayer.Services.Listeners.ILocationListener;

public interface ILocationService extends LocationListener
{
    /**
     * Starts listening android for location changes.
     * @param context Context needed to start the listener.
     */
    void StartListening(Context context);

    /**
     * Stops listening android for location changes.
     * @param context Context needed to stop the listener.
     */
    void StopListening(Context context);

    /**
     * Sets the given object to be notified when location changes.
     * @param listener The object to notify to.
     */
    void Subscribe(ILocationListener listener);

    /**
     * Unsets the given object to be notified when location changes.
     * @param listener The object to stop notifying to.
     */
    void Unsubscribe(ILocationListener listener);
}
