package com.grego.SpeedometerPlayer.Services.Definitions;

import com.grego.SpeedometerPlayer.Services.Implementations.Limit.LimitType;
import com.grego.SpeedometerPlayer.Services.Listeners.ILimitListener;

/**
 * Interface to be implemented by a Limit info provider service.
 */
public interface ILimitService
{
    /**
     * Sets the given object to be notified when limit changes.
     * @param listener The object to notify to.
     */
    void Subscribe(ILimitListener listener);

    /**
     * Unsets the given object to be notified when limit changes.
     * @param listener The object to stop notifying to.
     */
    void Unsubscribe(ILimitListener listener);

    /**
     * Gets the current limit stored.
     * @return
     */
    int GetCurrentLimit();

    /**
     * Changes the current limit to the next limit of the given type.
     * @param newLimitType
     */
    void ChangeToLimitType(LimitType newLimitType);

    /**
     * Changes the current limit to the given value.
     * @param newLimitValue The new limit value.
     */
    void ChangeLimitValue(int newLimitValue);

    /**
     * Checks if the current limit is a valid value.
     * If not, will reset the limit to a valid value.
     */
    void ResetLimitIfNeeded();
}
