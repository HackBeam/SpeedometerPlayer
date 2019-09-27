package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * UI compound to display the current speed colored according to the cruise desired speed.
 */
public class CruiseSpeedometerDisplay extends ConstraintLayout implements IInputListener
{
    private SpeedometerDisplay speedometer;
    private TextView desiredSpeedText;

    private static final int DESIRED_SPEED_OFFSET = 5;
    private int desiredSpeed = 120;
    private boolean active = false;

    public CruiseSpeedometerDisplay(Context context)
    {
        super(context);
        Initialize();
    }

    public CruiseSpeedometerDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public CruiseSpeedometerDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        Initialize();
    }

    private void Initialize()
    {
        inflate(getContext(), R.layout.compound_cruise_display, this);

        if (!isInEditMode())
        {
            // Get UI object references
            speedometer = (SpeedometerDisplay) findViewById(R.id.cruise_speedometer_display);
            desiredSpeedText = (TextView) findViewById(R.id.cruise_desired_value);

            // Set default preferences
            this.setAlpha(0);
            desiredSpeedText.setTypeface(Core.Data.DefaultFont);
            Core.Helpers.SetColorToTextView(desiredSpeedText, Core.Data.Colors.cruiseDesired);
            SetColor(Core.Data.Colors.normal);
        }
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    public void UpdateUI()
    {
        speedometer.UpdateUI();
        desiredSpeedText.setText(Integer.toString(desiredSpeed));

        if (speedometer.GetSpeedValue() >= desiredSpeed + DESIRED_SPEED_OFFSET) // Above good speed
        {
            SetColor(Core.Data.Colors.cruiseAbove);
        }
        else if (speedometer.GetSpeedValue() <= desiredSpeed - DESIRED_SPEED_OFFSET) // Below good speed
        {
            SetColor(Core.Data.Colors.cruiseBelow);
        }
        else // In good desired speed
        {
            SetColor(Core.Data.Colors.cruiseGood);
        }
    }

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
        desiredSpeed = speedometer.GetSpeedValue();
        UpdateUI();
    }

    //region Unused Input Events
    @Override
    public void OnDoubleTap()
    {

    }

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
     * Enables or disables the compound.
     * @param active Whether or not the compound should be visible and active.
     */
    public void SetActive(boolean active)
    {
        if (active && !this.active)
        {
            this.active = true;
            Core.Services.Input.Subscribe(this);
            this.setAlpha(1);

            OnSingleTap();
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
