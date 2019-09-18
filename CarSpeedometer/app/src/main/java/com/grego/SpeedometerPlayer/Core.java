package com.grego.SpeedometerPlayer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;
import com.grego.SpeedometerPlayer.Services.Implementations.DefaultBatteryService;

/**
 * Container class for Services, Helpers and Global Data
 */
public class Core
{
    /**
     * Container class for Services
     */
    public static class Services
    {
        public static IBatteryService Battery;
    }

    /**
     * Container class for Helpers
     */
    public static class Helpers
    {
        public static void SetColorToTextView(TextView textView, int color)
        {
            textView.setTextColor(color);
            textView.setShadowLayer(20, 0, 0, color);
        }
    }

    /**
     * Container class for Data
     */
    public static class Data
    {
        public static Typeface DefaultFont;

        /**
         * Container class for default Color data.
         * This provides faster access than always read from colors.xml
         */
        public static class Colors
        {
            // Make always sure to match every color defined on colors.xml
            public static int normal;
            public static int limitDanger;
            public static int limitWarning;
            public static int cruiseGood;
            public static int cruiseAbove;
            public static int cruiseBelow;

            /**
             * Initializes all the colors variables with their default values stored in colors.xml.
             * @param context Context needed to read colors.xml
             */
            private static void LoadDefaults(Context context)
            {
                normal = ContextCompat.getColor(context, R.color.normal);
                limitDanger = ContextCompat.getColor(context, R.color.limit_danger);
                limitWarning = ContextCompat.getColor(context, R.color.limit_warning);
                cruiseGood = ContextCompat.getColor(context, R.color.cruise_good);
                cruiseAbove = ContextCompat.getColor(context, R.color.cruise_above);
                cruiseBelow = ContextCompat.getColor(context, R.color.cruise_below);
            }
        }
    }

    /**
     * Instantiates all the needed services and loads the default data.
     * Should be called by the very first activiy onCreate.
     * @param context Context needed for some services and data to be initialized.
     */
    public static void Initialize(Context context)
    {
        // Services
        Services.Battery = new DefaultBatteryService();

        // Default Data
        Data.DefaultFont = Typeface.createFromAsset(context.getAssets(), "fonts/digital-7.ttf");
        Data.Colors.LoadDefaults(context);
    }
}
