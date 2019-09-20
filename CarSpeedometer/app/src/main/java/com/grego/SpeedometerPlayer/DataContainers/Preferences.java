package com.grego.SpeedometerPlayer.DataContainers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences
{
    public boolean mirrorMode;
    public float safetyMargin;
    public String speedUnit;
    public int highLimitOneTap;
    public int lowLimitOneTap;
    public int highLimitDoubleTap;
    public int lowLimitDoubleTap;

    public Preferences(Context context)
    {
        LoadPreferences(context);
    }

    public void LoadPreferences(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mirrorMode = preferences.getBoolean("mirror_mode", false);
        safetyMargin = preferences.getInt("safety_margin", 0);
        speedUnit = preferences.getString("speed_unit", "km/h");
        highLimitOneTap = preferences.getInt("high_limit_one_tap", 120);
        lowLimitOneTap = preferences.getInt("low_limit_one_tap", 100);
        highLimitDoubleTap = preferences.getInt("high_limit_double_tap", 80);
        lowLimitDoubleTap = preferences.getInt("low_limit_double_tap", 60);
    }

    /* TODO: Implement SavePreferences if needed
    public void SavePreferences(Context context)
    {
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.
    }
    */
}
