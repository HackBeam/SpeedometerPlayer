package com.grego.SpeedometerPlayer.DataContainers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;

public class Preferences
{
    //region Hardcoded preferences
    public final int veryAboveLimitIncrement = 10;
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
