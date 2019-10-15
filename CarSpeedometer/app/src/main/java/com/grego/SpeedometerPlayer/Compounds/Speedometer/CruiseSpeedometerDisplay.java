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
public class CruiseSpeedometerDisplay extends SpeedometerDisplay
{
    private static final int DESIRED_SPEED_OFFSET = 5;
    private static final int DANGER_SPEED_OFFSET = DESIRED_SPEED_OFFSET * 3;
    private static final int BACKGROUND_DEACTIVATED_ALPHA = 90;

    private TextView desiredSpeedText;
    private ImageView backgroundBelow;
    private ImageView backgroundGood;
    private ImageView backgroundAbove;
    private ImageView arrowIndicator;

    private int desiredSpeed = 0;
    private boolean active = false;

    //region Rotation algorithm cache
    private int minSpeedIndicator;
    private float currentArrowDegrees = 0;
    private static final float INDICATOR_SPEED_RANGE = DESIRED_SPEED_OFFSET * 6;
    private static final int ROTATION_OFFSET = 110;
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

    @Override
    protected void InflateXML()
    {
        inflate(getContext(), R.layout.compound_cruise_display, this);
    }

    protected void Initialize()
    {
        // Get UI object references
        InitializeSpeedometer(R.id.cruise_speed_value, R.id.cruise_speed_units);
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
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    public void UpdateUI()
    {
        super.UpdateUI();
        desiredSpeedText.setText(Integer.toString(desiredSpeed));

        if (this.speedValue > desiredSpeed + DESIRED_SPEED_OFFSET) // Above good speed
        {
            if (this.speedValue > desiredSpeed + DANGER_SPEED_OFFSET) // VERY Above good speed
            {
                SetColor(Core.Data.Colors.cruiseVeryAbove);
            }
            else
            {
                SetColor(Core.Data.Colors.cruiseAbove);
            }

            backgroundAbove.setImageAlpha(255);
            backgroundGood.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
            backgroundBelow.setImageAlpha(BACKGROUND_DEACTIVATED_ALPHA);
        }
        else if (this.speedValue < desiredSpeed - DESIRED_SPEED_OFFSET) // Below good speed
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
        float normalized = (GetSpeedValue() - minSpeedIndicator) / INDICATOR_SPEED_RANGE;
        float rotation = normalized * ROTATION_RANGE - ROTATION_OFFSET;

        if (rotation < -ROTATION_OFFSET)
        {
            rotation = -ROTATION_OFFSET;
        }
        else if (rotation > ROTATION_OFFSET)
        {
            rotation = ROTATION_OFFSET;
        }

        arrowIndicator.setRotation(rotation);
    }

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {
        SetDesiredSpeed(GetSpeedValue());
    }

    @Override
    public void OnSwipeUp()
    {
        SetDesiredSpeed(desiredSpeed + 1);
    }

    @Override
    public void OnSwipeDown()
    {
        SetDesiredSpeed(desiredSpeed - 1);
    }
    //endregion

    /**
     * Sets the desired speed to the current speed
     * and updates the rotation algorithm cache vars.
     */
    private void SetDesiredSpeed(int desiredSpeed)
    {
        if (desiredSpeed < 0)
        {
            this.desiredSpeed = 0;
        }
        else
        {
            this.desiredSpeed = desiredSpeed;
        }

        minSpeedIndicator = this.desiredSpeed - DESIRED_SPEED_OFFSET - DESIRED_SPEED_OFFSET - DESIRED_SPEED_OFFSET;
        UpdateUI();
    }

    @Override
    protected void OnActivate()
    {
        if (desiredSpeed == 0)
        {
            SetDesiredSpeed(GetSpeedValue());
        }
    }

    @Override
    protected void OnDeactivate()
    {

    }
}
