package com.grego.SpeedometerPlayer.Compounds;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.ILocationListener;

/**
 * UI compound to display the current speed provided by the Location service.
 */
public class SpeedometerDisplay extends ConstraintLayout implements ILocationListener
{
    private TextView speedValueText;
    private TextView speedUnitsText;

    private int speedValue = -1;

    public SpeedometerDisplay(Context context)
    {
        super(context);
        Initialize();
    }

    public SpeedometerDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public SpeedometerDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        Initialize();
    }

    /**
     * Initializes the component.
     * - Inflates it's XML.
     * - Gets the UI objects.
     * - Establish all the default values.
     */
    private void Initialize()
    {
        inflate(getContext(), R.layout.compound_speedometer, this);

        if (!isInEditMode())
        {
            // Get UI object references
            speedValueText = (TextView) findViewById(R.id.speed_value);
            speedUnitsText = (TextView) findViewById(R.id.speed_units);

            // Set default preferences
            speedValueText.setTypeface(Core.Data.DefaultFont);
            speedUnitsText.setTypeface(Core.Data.DefaultFont);
            SetColor(Core.Data.Colors.normal);
        }
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
            Core.Services.Location.Subscribe(this);
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
            Core.Services.Location.Unsubscribe(this);
        }
    }

    /**
     * Called by the LocationService when the speed changes.
     * Saves the given speed value and updates the compound UI.
     *
     * @param speed The new speed value.
     */
    @Override
    public void OnSpeedChanges(float speed)
    {
        speedValue = (int) speed;
        UpdateUI();
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    public void UpdateUI()
    {
        speedUnitsText.setText(Core.Data.Preferences.speedUnit);

        if (speedValue < 0)
        {
            speedValueText.setText("---");
        }
        else
        {
            speedValueText.setText(Integer.toString(speedValue));
        }

        if (speedValue >= Core.Services.Limit.GetCurrentLimit()) // Above limit
        {
            if (speedValue >= Core.Services.Limit.GetCurrentLimit() + Core.Data.Preferences.veryAboveLimitIncrement) // VERY above limit
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

    /**
     * Applies the given color to all of the UI elements of the compound.
     *
     * @param color
     */
    public void SetColor(int color)
    {
        Core.Helpers.SetColorToTextView(speedValueText, color);
        Core.Helpers.SetColorToTextView(speedUnitsText, color);
    }
}
