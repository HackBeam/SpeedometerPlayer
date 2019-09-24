package com.grego.SpeedometerPlayer.Services.Implementations.Limit;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.ILimitService;
import com.grego.SpeedometerPlayer.Services.Listeners.ILimitListener;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Limit info provider service.
 */
public class DefaultLimitService implements ILimitService
{
    private LinkedList<ILimitListener> subscribers;
    private int currentLimit;

    public DefaultLimitService()
    {
        subscribers = new LinkedList<>();
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
