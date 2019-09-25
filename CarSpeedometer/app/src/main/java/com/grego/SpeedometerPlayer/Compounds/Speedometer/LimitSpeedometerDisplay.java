package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.util.AttributeSet;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Listeners.ILimitListener;

/**
 * UI compound to display the current speed colored according to the configured limit.
 */
public class LimitSpeedometerDisplay extends SpeedometerDisplay implements ILimitListener
{
    private int cachedLimit = 120;

    public LimitSpeedometerDisplay(Context context)
    {
        super(context);
    }

    public LimitSpeedometerDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LimitSpeedometerDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Called when the compound starts displaying.
     * Subscribes the compound to the LocationService to listen location changes.
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (!isInEditMode())
        {
            Core.Services.Limit.Subscribe(this);
        }
    }

    /**
     * Called when the compound is no longer visible.
     * Unsubscribe the compound to the LocationService to stop listen location changes.
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        if (!isInEditMode())
        {
            Core.Services.Limit.Unsubscribe(this);
        }
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    @Override
    public void UpdateUI()
    {
        super.UpdateUI();

        if (speedValue >= cachedLimit) // Above limit
        {
            if (speedValue >= cachedLimit + Core.Data.Preferences.veryAboveLimitOffset) // VERY above limit
            {
                SetColor(Core.Data.Colors.limitWarning);
            }
            else
            {
                SetColor(Core.Data.Colors.limitDanger);
            }
        }
        else
        {
            SetColor(Core.Data.Colors.normal);
        }
    }

    @Override
    public void OnLimitChange(int newLimit)
    {
        cachedLimit = newLimit;
        UpdateUI();
    }
}
