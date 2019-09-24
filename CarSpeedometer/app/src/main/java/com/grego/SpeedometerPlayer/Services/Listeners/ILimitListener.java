package com.grego.SpeedometerPlayer.Services.Listeners;

/**
 * Interface to be implemented for classes which wants to listen limit changes.
 */
public interface ILimitListener
{
    /**
     * Called when the limit value changes.
     * @param newLimit The new limit value.
     */
    void OnLimitChange(int newLimit);
}
