package com.grego.SpeedometerPlayer.DataContainers;

import android.support.v4.content.ContextCompat;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;

/**
 * Container class for default Color data.
 * This provides faster access than always read from colors.xml
 */
public class Colors
{
    // Make always sure to match every color defined on colors.xml
    public int normal;
    public int limitDanger;
    public int limitWarning;
    public int cruiseGood;
    public int cruiseAbove;
    public int cruiseBelow;
    public int cruiseDesired;

    public Colors()
    {
        normal = ContextCompat.getColor(Core.ApplicationContext, R.color.normal);
        limitDanger = ContextCompat.getColor(Core.ApplicationContext, R.color.limit_danger);
        limitWarning = ContextCompat.getColor(Core.ApplicationContext, R.color.limit_warning);
        cruiseGood = ContextCompat.getColor(Core.ApplicationContext, R.color.cruise_good);
        cruiseAbove = ContextCompat.getColor(Core.ApplicationContext, R.color.cruise_above);
        cruiseBelow = ContextCompat.getColor(Core.ApplicationContext, R.color.cruise_below);
        cruiseDesired =  ContextCompat.getColor(Core.ApplicationContext, R.color.cruise_desired);
    }
}
