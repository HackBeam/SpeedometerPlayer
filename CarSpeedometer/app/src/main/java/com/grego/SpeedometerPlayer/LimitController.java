package com.grego.SpeedometerPlayer;

import com.grego.SpeedometerPlayer.Services.Implementations.Limit.LimitType;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * Controller listening tap events to configure the current limit.
 */
public class LimitController implements IInputListener
{
    /**
     * Starts listening input events.
     */
    public void StartController()
    {
        Core.Services.Input.Subscribe(this);
    }

    /**
     * Stops listening input events.
     * Should be called when controller is no longer used.
     */
    public void StopController()
    {
        Core.Services.Input.Unsubscribe(this);
    }

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
        Core.Services.Limit.ChangeToLimitType(LimitType.ONE_TAP_LIMIT);
    }

    @Override
    public void OnDoubleTap()
    {
        Core.Services.Limit.ChangeToLimitType(LimitType.DOUBLE_TAP_LIMIT);
    }

    //region Unused Input Events
    @Override
    public void OnLongPress()
    {

    }

    @Override
    public void OnSwipeLeft()
    {

    }

    @Override
    public void OnSwipeRight()
    {

    }
    //endregion

    //endregion
}
