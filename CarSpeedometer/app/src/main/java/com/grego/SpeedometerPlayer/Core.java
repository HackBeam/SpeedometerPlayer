package com.grego.SpeedometerPlayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.DataContainers.Colors;
import com.grego.SpeedometerPlayer.DataContainers.Preferences;
import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;
import com.grego.SpeedometerPlayer.Services.Definitions.ILocationService;
import com.grego.SpeedometerPlayer.Services.Implementations.Battery.DefaultBatteryService;
import com.grego.SpeedometerPlayer.Services.Implementations.Location.DefaultLocationService;

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
        public static ILocationService Location;
    }

    /**
     * Container class for Helpers
     */
    public static class Helpers
    {
        /**
         * Applies color and shadow to the given text view.
         * @param textView The text to colorize.
         * @param color The color to apply.
         */
        public static void SetColorToTextView(TextView textView, int color)
        {
            textView.setTextColor(color);
            textView.setShadowLayer(20, 0, 0, color);
        }

        /**
         * Sets the brightness of the screen to 100%
         * @param activity The activity calling this method, needed to access to the screen parameters.
         */
        public static void SetMaxScreenBrightness(Activity activity)
        {
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.screenBrightness = 1.0f;
            activity.getWindow().setAttributes(layoutParams);
        }
    }

    /**
     * Container class for Data
     */
    public static class Data
    {
        public static Typeface DefaultFont;
        public static Colors Colors;
        public static Preferences Preferences;
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
        Services.Location = new DefaultLocationService(context);

        // Default Data
        Data.DefaultFont = Typeface.createFromAsset(context.getAssets(), "fonts/digital-7.ttf");
        Data.Colors = new Colors(context);
        Data.Preferences = new Preferences(context);
    }
}
