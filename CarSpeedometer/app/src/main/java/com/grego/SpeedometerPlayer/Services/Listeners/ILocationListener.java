package com.grego.SpeedometerPlayer.Services.Listeners;

/**
 * Interface to be implemented for classes which wants to listen location changes.
 */
public interface ILocationListener
{
    /**
     * Called when the speed changes.
     * @param speed The new speed value.
     */
    void OnSpeedChanges(float speed);

    /**
     * Called when the location signal is lost.
     */
    void OnSignalLost();
}
