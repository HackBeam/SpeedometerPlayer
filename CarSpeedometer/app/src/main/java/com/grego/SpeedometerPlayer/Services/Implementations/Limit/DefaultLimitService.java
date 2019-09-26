package com.grego.SpeedometerPlayer.Services.Implementations.Limit;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.ILimitService;
import com.grego.SpeedometerPlayer.Services.Listeners.ILimitListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Limit info provider service.
 */
public class DefaultLimitService implements ILimitService
{
    private ArrayList<ILimitListener> subscribers;
    private int currentLimit;

    public DefaultLimitService()
    {
        subscribers = new ArrayList<>();
        currentLimit = Core.Data.Preferences.highLimitOneTap;
    }

    @Override
    public void Subscribe(ILimitListener listener)
    {
        subscribers.add(listener);
        listener.OnLimitChange(currentLimit);
    }

    @Override
    public void Unsubscribe(ILimitListener listener)
    {
        subscribers.remove(listener);
    }

    @Override
    public int GetCurrentLimit()
    {
        return currentLimit;
    }

    @Override
    public void ChangeToLimitType(LimitType newLimitType)
    {
        switch (newLimitType)
        {
            case ONE_TAP_LIMIT:
                if (currentLimit == Core.Data.Preferences.highLimitOneTap)
                {
                    currentLimit = Core.Data.Preferences.lowLimitOneTap;
                }
                else
                {
                    currentLimit = Core.Data.Preferences.highLimitOneTap;
                }
                break;

            case DOUBLE_TAP_LIMIT:
                if (currentLimit == Core.Data.Preferences.highLimitDoubleTap)
                {
                    currentLimit = Core.Data.Preferences.lowLimitDoubleTap;
                }
                else
                {
                    currentLimit = Core.Data.Preferences.highLimitDoubleTap;
                }
                break;
        }

        TriggerSubscribers();
    }

    @Override
    public void ChangeLimitValue(int newLimitValue)
    {
        if (newLimitValue > 0)
        {
            currentLimit = newLimitValue;
            TriggerSubscribers();
        }
    }

    @Override
    public void ResetLimitIfNeeded()
    {
        if (currentLimit != Core.Data.Preferences.highLimitOneTap &&
            currentLimit != Core.Data.Preferences.lowLimitOneTap &&
            currentLimit != Core.Data.Preferences.highLimitDoubleTap &&
            currentLimit != Core.Data.Preferences.lowLimitDoubleTap)
        {
            ChangeLimitValue(Core.Data.Preferences.highLimitOneTap);
        }
    }

    /**
     * Notify all subscribers the limit has changed.
     */
    private void TriggerSubscribers()
    {
        Iterator<ILimitListener> iterator = subscribers.iterator();
        while (iterator.hasNext())
        {
            iterator.next().OnLimitChange(currentLimit);
        }
    }
}
