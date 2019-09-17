package com.grego.SpeedometerPlayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;
import com.grego.SpeedometerPlayer.Services.Implementations.DefaultBatteryService;

public class Core
{
    public static class Services
    {
        public static IBatteryService Battery;
    }

    public static class Helpers
    {
        public static void SetColorToTextView(TextView textView, int color)
        {
            textView.setTextColor(color);
            textView.setShadowLayer(20, 0, 0, color);
        }
    }

    public static class Data
    {
        public static Typeface DefaultFont;

        public static class Colors
        {
            // Make always sure to match every color defined on colors.xml
            public static int normal;
            public static int limitDanger;
            public static int limitWarning;
            public static int cruiseGood;
            public static int cruiseAbove;
            public static int cruiseBelow;

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

    public static void Initialize(Context context)
    {
        // Services
        Services.Battery = new DefaultBatteryService();

        // Default Data
        Data.DefaultFont = Typeface.createFromAsset(context.getAssets(), "fonts/digital-7.ttf");
        Data.Colors.LoadDefaults(context);
    }
}
