package com.grego.SpeedometerPlayer.Services.Implementations;

/**
 * Interface to be implemented for classes which wants to listen battery changes.
 */
public interface IBatteryListener
{
    /**
     * Called when the battery level changes.
     * @param batteryLevel The new battery level received from the service.
     */
    void OnBatteryLevelReceived(int batteryLevel);
}
