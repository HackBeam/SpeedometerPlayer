package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * UI compound to display the current speed colored according to the cruise desired speed.
 */
public class CruiseSpeedometerDisplay extends ConstraintLayout implements IInputListener
{
    private static final int DESIRED_SPEED_OFFSET = 5;
    private static final int BACKGROUND_DEACTIVATED_ALPHA = 90;

    private SpeedometerDisplay speedometer;
    private TextView desiredSpeedText;
    private ImageView backgroundBelow;
    private ImageView backgroundGood;
    private ImageView backgroundAbove;
    private ImageView arrowIndicator;

    private int desiredSpeed = 120;
    private boolean active = false;

    //region Rotation algorithm cache
    private int minSpeedIndicator;
    private float currentArrowDegrees = 0;
    private static final float INDICATOR_SPEED_RANGE = DESIRED_SPEED_OFFSET * 6;
    private static final int ROTATION_OFFSET = 90;
    private static final int ROTATION_RANGE = ROTATION_OFFSET * 2;
    //endregion

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
            backgroundAbove = (ImageView) findViewById(R.id.cruise_above_background);
            backgroundGood = (ImageView) findViewById(R.id.cruise_desired_background);
            backgroundBelow = (ImageView) findViewById(R.id.cruise_below_background);
            arrowIndicator = (ImageView) findViewById(R.id.cruise_arrow_indicator);

            // Set default preferences
            this.setAlpha(0);
            desiredSpeedText.setTypeface(Core.Data.DefaultFont);
            Core.Helpers.SetColorToTextView(desiredSpeedText, Core.Data.Colors.cruiseDesired);
            backgroundAbove.setColorFilter(Core.Data.Colors.cruiseAbove);
            backgroundGood.setColorFilter(Core.Data.Colors.cruiseGood);
            backgroundBelow.setColorFilter(Core.Data.Colors.cruiseBelow);

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
            backgroundAbove.setImageAlpha(255);
            backgroundGood.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
            backgroundBelow.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
        }
        else if (speedometer.GetSpeedValue() <= desiredSpeed - DESIRED_SPEED_OFFSET) // Below good speed
        {
            SetColor(Core.Data.Colors.cruiseBelow);
            backgroundAbove.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
            backgroundGood.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
            backgroundBelow.setImageAlpha(255);
        }
        else // In good desired speed
        {
            SetColor(Core.Data.Colors.cruiseGood);
            backgroundAbove.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
            backgroundGood.setImageAlpha(255);
            backgroundBelow.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
        }

        UpdateArrow();
    }

    private void UpdateArrow()
    {
        float normalized = (speedometer.GetSpeedValue() - minSpeedIndicator) / INDICATOR_SPEED_RANGE;
        float rotation = normalized * ROTATION_RANGE - ROTATION_OFFSET;
        arrowIndicator.setRotation(rotation);
    }

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
        SetDesiredSpeed();
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
     * Sets the desired speed to the current speed
     * and updates the rotation algorithm cache vars.
     */
    private void SetDesiredSpeed()
    {
        desiredSpeed = speedometer.GetSpeedValue();
        minSpeedIndicator = desiredSpeed - DESIRED_SPEED_OFFSET - DESIRED_SPEED_OFFSET - DESIRED_SPEED_OFFSET;
        UpdateUI();
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

            SetDesiredSpeed();
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
