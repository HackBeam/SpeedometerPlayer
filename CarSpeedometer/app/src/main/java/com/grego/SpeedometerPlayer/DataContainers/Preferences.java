package com.grego.SpeedometerPlayer.DataContainers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;

public class Preferences
{
    //region Hardcoded preferences
    /**
     * Offset at which the speed is considered very above limit, so
     * {@link com.grego.SpeedometerPlayer.Compounds.Speedometer.LimitSpeedometerDisplay}
     * will turns in danger color (red by default)
     */
    public final int veryAboveLimitOffset = 10;

    /**
     * Milliseconds that {@link android.location.LocationManager}
     * will wait until serve the new location data.
     */
    public final int locationUpdatesMinMilliseconds = 1;

    /**
     * Meters that {@link android.location.LocationManager}
     * will wait to travel until serve the new location data.
     */
    public final float locationUpdatesMinMeters = 1;

    public final int swipeThreshold = 100;
    public final int swipeVelocityThreshold = 100;
    //endregion

    //region Preferences from XML
    public boolean mirrorMode;
    public float safetyMargin;
    public String speedUnit;
    public int highLimitOneTap;
    public int lowLimitOneTap;
    public int highLimitDoubleTap;
    public int lowLimitDoubleTap;
    //endregion

    public Preferences()
    {
        LoadPreferences();
    }

    public void LoadPreferences()
    {
        // If no preferences are saved, create the default ones
        PreferenceManager.setDefaultValues(Core.ApplicationContext, R.xml.preferences, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Core.ApplicationContext);
        mirrorMode = preferences.getBoolean("mirror_mode", false);
        safetyMargin = Integer.parseInt(preferences.getString("safety_margin", "0"));
        speedUnit = preferences.getString("speed_unit", "km/h");
        highLimitOneTap = Integer.parseInt(preferences.getString("high_limit_one_tap", "120"));
        lowLimitOneTap = Integer.parseInt(preferences.getString("low_limit_one_tap", "100"));
        highLimitDoubleTap = Integer.parseInt(preferences.getString("high_limit_double_tap", "80"));
        lowLimitDoubleTap = Integer.parseInt(preferences.getString("low_limit_double_tap", "60"));
    }

    /* TODO: Implement SavePreferences if needed
    public void SavePreferences(Context context)
    {
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.
    }
    */
}
