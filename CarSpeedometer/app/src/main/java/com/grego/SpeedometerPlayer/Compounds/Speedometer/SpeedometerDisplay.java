package com.grego.SpeedometerPlayer.Compounds.Speedometer;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.GlobalEnums.SoundID;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;
import com.grego.SpeedometerPlayer.Services.Listeners.ILocationListener;

/**
 * UI compound to display the current speed provided by the Location service.
 */
public abstract class SpeedometerDisplay extends ConstraintLayout implements ILocationListener, IInputListener
{
    protected TextView speedValueText;
    protected TextView speedUnitsText;
    protected ImageView signalLostIcon;

    protected int speedValue = -1;
    protected boolean active = false;
    protected SoundID lastSoundPlayed = SoundID.NONE;

    public SpeedometerDisplay(Context context)
    {
        super(context);

        InflateXML();

        if (!isInEditMode())
        {
            Initialize();
        }
    }

    public SpeedometerDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        InflateXML();

        if (!isInEditMode())
        {
            Initialize();
        }
    }

    public SpeedometerDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        InflateXML();

        if (!isInEditMode())
        {
            Initialize();
        }
    }

    /**
     * Calls to the inflate() method.
     */
    protected abstract void InflateXML();

    /**
     * Gets all the UI objects and sets all the default values.
     */
    protected abstract void Initialize();

    /**
     * Gets the UI objects for the speed view and sets all it's default values.
     * @param speedValueTextID The ID of the speed value TextView.
     * @param speedUnitsTextID The ID of the speed units value TextView.
     */
    protected void InitializeSpeedometer(int speedValueTextID, int speedUnitsTextID, int signalLostIconID)
    {
        // Get UI object references
        speedValueText = (TextView) findViewById(speedValueTextID);
        speedUnitsText = (TextView) findViewById(speedUnitsTextID);
        signalLostIcon = (ImageView) findViewById(signalLostIconID);

        // Set default preferences
        speedValueText.setTypeface(Core.Data.DefaultFont);
        speedUnitsText.setTypeface(Core.Data.DefaultFont);
        signalLostIcon.setColorFilter(Core.Data.Colors.signalLost);
        signalLostIcon.setAlpha(0f);
    }

    /**
     * Plays the given sound ID if its not the last played sound.
     * To allow to play any sound again, call this method with SoundID.NONE as parameter.
     * @param soundID The sound ID to play.
     */
    protected void PlaySound(SoundID soundID)
    {
        if (lastSoundPlayed != soundID)
        {
            lastSoundPlayed = soundID;
            Core.Services.Sound.PlaySound(soundID);
        }
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
            signalLostIcon.setAlpha(0f);
        }
    }

    /**
     * Applies the given color to all of the UI elements of the compound.
     *
     * @param color The color to apply.
     */
    public void SetColor(int color)
    {
        Core.Helpers.SetColorToTextView(speedValueText, color);
        Core.Helpers.SetColorToTextView(speedUnitsText, color);
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
            Core.Services.Location.Subscribe(this);
            Core.Services.Input.Subscribe(this);
            this.setAlpha(1);
            OnActivate();
        }
        else if (!active && this.active)
        {
            this.active = false;
            Core.Services.Location.Unsubscribe(this);
            Core.Services.Input.Unsubscribe(this);
            this.setAlpha(0);
            OnDeactivate();
        }
    }

    /**
     * Called when the component is being activate.
     */
    protected abstract void OnActivate();

    /**
     * Called when the component is being deactivate.
     */
    protected abstract void OnDeactivate();

    /**
     * Retrieves the current showing speed.
     *
     * @return The current speed.
     */
    public int GetSpeedValue()
    {
        if (speedValue < 0)
        {
            return 0;
        }
        else
        {
            return speedValue;
        }
    }

    //region ILocationListener events

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

    @Override
    public void OnSignalLost()
    {
        speedValue = -1; // Values below 0 means no signal
        UpdateUI();

        signalLostIcon.setAlpha(1f);
        SetColor(Core.Data.Colors.signalLost);
        Core.Services.Sound.PlaySound(SoundID.SIGNAL_LOST);
    }
    //endregion

    //region IInputListener events
    @Override
    public void OnSingleTap()
    {

    }

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
}
