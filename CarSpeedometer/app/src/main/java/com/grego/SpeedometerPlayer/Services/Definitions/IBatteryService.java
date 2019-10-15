package com.grego.SpeedometerPlayer.Services.Definitions;

import com.grego.SpeedometerPlayer.Services.Listeners.IBatteryListener;

/**
 * Interface to be implemented by a Battery info provider service.
 */
public interface IBatteryService
{
    /**
     * Starts listening android for battery changes.
     */
    void StartListening();

    /**
     * Stops listening android for battery changes.
     */
    void StopListening();

    /**
     * Sets the given object to be notified when battery changes.
     * @param listener The object to notify to.
     */
    void Subscribe(IBatteryListener listener);

    /**
     * Unsets the given object to be notified when battery changes.
     * @param listener The object to stop notifying to.
     */
    void Unsubscribe(IBatteryListener listener);
}
