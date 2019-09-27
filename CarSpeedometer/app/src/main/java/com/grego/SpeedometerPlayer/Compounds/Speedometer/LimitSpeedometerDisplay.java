package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * UI compound to display the current speed colored according to the configured limit.
 */
public class LimitSpeedometerDisplay extends ConstraintLayout implements IInputListener
{
    private SpeedometerDisplay speedometer;
    private TextView limitText;
    private int currentLimit;
    private boolean active = false;

    public LimitSpeedometerDisplay(Context context)
    {
        super(context);
        Initialize();
    }

    public LimitSpeedometerDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public LimitSpeedometerDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        Initialize();
    }

    private void Initialize()
    {
        inflate(getContext(), R.layout.compound_limit_display, this);

        if (!isInEditMode())
        {
            // Get UI object references
            speedometer = (SpeedometerDisplay) findViewById(R.id.limit_speedometer_display);
            limitText = (TextView) findViewById(R.id.limit_value);

            // Set default preferences
            currentLimit = Core.Data.Preferences.highLimitOneTap;
            limitText.setTypeface(Core.Data.DefaultFont);
            Core.Helpers.SetColorToTextView(limitText, Core.Data.Colors.limitDanger);
            SetActive(true);
        }
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    public void UpdateUI()
    {
        speedometer.UpdateUI();
        UpdateDisplayingLimit();

        if (speedometer.GetSpeedValue() >= currentLimit) // Above limit
        {
            if (speedometer.GetSpeedValue() >= currentLimit + Core.Data.Preferences.veryAboveLimitOffset) // VERY above limit
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

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
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

    @Override
    public void OnSwipeUp()
    {

    }

    @Override
    public void OnSwipeDown()
    {

    }
    //endregion

    //endregion

    /**
     * Checks if the current limit is a valid one.
     * If not, sets it to the one tap high limit.
     * Then changes the limit text to display the current limit.
     */
    private void UpdateDisplayingLimit()
    {
        if (currentLimit != Core.Data.Preferences.highLimitOneTap && currentLimit != Core.Data.Preferences.lowLimitOneTap && currentLimit != Core.Data.Preferences.highLimitDoubleTap && currentLimit != Core.Data.Preferences.lowLimitDoubleTap)
        {
            currentLimit = Core.Data.Preferences.highLimitOneTap;
        }

        limitText.setText(Integer.toString(currentLimit));
    }

    /**
     * Enables or disables the compound.
     *
     * @param active Whether or not the compound should be visible and active.
     */
    public void SetActive(boolean active)
    {
        if (active && !this.active)
        {
            this.active = true;
            Core.Services.Input.Subscribe(this);
            this.setAlpha(1);
            UpdateUI();
        }
        else if (!active && this.active)
        {
            this.active = false;
            Core.Services.Input.Unsubscribe(this);
            this.setAlpha(0);
        }
    }

    /**
     * Applies the given color to all of the UI elements of the compound.
     *
     * @param color The color to apply.
     */
    public void SetColor(int color)
    {
        speedometer.SetColor(color);
    }
}
