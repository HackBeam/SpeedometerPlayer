package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.os.IInterface;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * UI compound to display the current speed colored according to the configured limit.
 */
public class LimitSpeedometerDisplay extends SpeedometerDisplay
{
    private TextView limitText;
    private int currentLimit;
    private int forcedLimit = -1;

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

    @Override
    protected void InflateXML()
    {
        inflate(getContext(), R.layout.compound_limit_display, this);
    }

    @Override
    protected void Initialize()
    {
        // Get UI object references
        InitializeSpeedometer(R.id.limit_speed_value, R.id.limit_speed_units);
        limitText = (TextView) findViewById(R.id.limit_value);

        // Set default preferences
        currentLimit = Core.Data.Preferences.highLimitOneTap;
        limitText.setTypeface(Core.Data.DefaultFont);
        Core.Helpers.SetColorToTextView(limitText, Core.Data.Colors.limitDanger);
        SetActive(true);
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    @Override
    public void UpdateUI()
    {
        super.UpdateUI();
        UpdateDisplayingLimit();

        if (this.speedValue > currentLimit) // Above limit
        {
            if (this.speedValue > currentLimit + Core.Data.Preferences.veryAboveLimitOffset) // VERY above limit
            {
                SetColor(Core.Data.Colors.limitDanger);
            }
            else
            {
                SetColor(Core.Data.Colors.limitWarning);
            }
        }
        else
        {
            SetColor(Core.Data.Colors.normal);
        }
    }

    @Override
    protected void OnActivate()
    {
        UpdateUI();
    }

    @Override
    protected void OnDeactivate()
    {

    }

    /**
     * Forces to set the given limit.
     *
     * @param limitValue The new limit.
     */
    public void ForceLimit(int limitValue)
    {
        forcedLimit = limitValue;
    }

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
        forcedLimit = -1;

        if (currentLimit == Core.Data.Preferences.highLimitOneTap)
        {
            currentLimit = Core.Data.Preferences.lowLimitOneTap;
        }
        else
        {
            currentLimit = Core.Data.Preferences.highLimitOneTap;
        }

        UpdateUI();
    }

    @Override
    public void OnDoubleTap()
    {
        forcedLimit = -1;

        if (currentLimit == Core.Data.Preferences.highLimitDoubleTap)
        {
            currentLimit = Core.Data.Preferences.lowLimitDoubleTap;
        }
        else
        {
            currentLimit = Core.Data.Preferences.highLimitDoubleTap;
        }

        UpdateUI();
    }

    //endregion

    /**
     * Checks if the current limit is a valid one.
     * If not, sets it to the one tap high limit.
     * Then changes the limit text to display the current limit.
     */
    private void UpdateDisplayingLimit()
    {
        if (forcedLimit < 0)
        {
            if (currentLimit != Core.Data.Preferences.highLimitOneTap && currentLimit != Core.Data.Preferences.lowLimitOneTap && currentLimit != Core.Data.Preferences.highLimitDoubleTap && currentLimit != Core.Data.Preferences.lowLimitDoubleTap)
            {
                currentLimit = Core.Data.Preferences.highLimitOneTap;
            }
        }
        else
        {
            currentLimit = forcedLimit;
        }

        limitText.setText(Integer.toString(currentLimit));
    }
}