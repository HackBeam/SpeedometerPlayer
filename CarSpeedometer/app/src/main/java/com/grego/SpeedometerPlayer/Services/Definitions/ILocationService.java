package com.grego.SpeedometerPlayer.Services.Definitions;

import android.app.Activity;
import android.location.LocationListener;

import com.grego.SpeedometerPlayer.Services.Listeners.ILocationListener;

public interface ILocationService extends LocationListener
{
    /**
     * Starts listening android for location changes.
     * @param activity Activity needed to start listening.
     */
    void StartListening(Activity activity);

    /**
     * Stops listening android for location changes.
     */
    void StopListening();

    /**
     * Sets the given object to be notified when location changes.
     * @param listener The object to notify to.
     */
    void Subscribe(ILocationListener listener);

    /**
     * Unset the given object to be notified when location changes.
     * @param listener The object to stop notifying to.
     */
    void Unsubscribe(ILocationListener listener);
}
