package com.grego.SpeedometerPlayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.DataContainers.Colors;
import com.grego.SpeedometerPlayer.DataContainers.Preferences;
import com.grego.SpeedometerPlayer.DataContainers.SoundIdClipPairs;
import com.grego.SpeedometerPlayer.Services.Definitions.IAnimationService;
import com.grego.SpeedometerPlayer.Services.Definitions.IBatteryService;
import com.grego.SpeedometerPlayer.Services.Definitions.IInputService;
import com.grego.SpeedometerPlayer.Services.Definitions.ILocationService;
import com.grego.SpeedometerPlayer.Services.Definitions.ISoundService;
import com.grego.SpeedometerPlayer.Services.Definitions.IVibrationService;
import com.grego.SpeedometerPlayer.Services.Implementations.Animation.DefaultAnimationService;
import com.grego.SpeedometerPlayer.Services.Implementations.Battery.DefaultBatteryService;
import com.grego.SpeedometerPlayer.Services.Implementations.Input.DefaultInputService;
import com.grego.SpeedometerPlayer.Services.Implementations.Location.DefaultLocationService;
import com.grego.SpeedometerPlayer.Services.Implementations.Sound.DefaultSoundService;
import com.grego.SpeedometerPlayer.Services.Implementations.Vibration.DefaultVibrationService;

/**
 * Container class for Services, Helpers and Global Data
 */
public class Core
{
    public static Context ApplicationContext;

    /**
     * Container class for Services
     */
    public static class Services
    {
        public static IBatteryService Battery;
        public static ILocationService Location;
        public static IInputService Input;
        public static IAnimationService Animation;
        public static IVibrationService Vibration;
        public static ISoundService Sound;
    }

    /**
     * Container class for Helpers
     */
    public static class Helpers
    {
        /**
         * Applies color and shadow to the given text view.
         *
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
         *
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
        public static SoundIdClipPairs Sounds;
    }

    /**
     * Container class for all request codes used for asking
     * system permissions to the user.
     */
    public static class PermissionRequestCodes
    {
        public static final int LocationServiceStart = 1;
    }

    /**
     * Instantiates all the needed services and loads the default data.
     * Should be called by the very first activity onCreate.
     *
     * @param appContext The context calling this method. Used only to get the ApplicationContext.
     */
    public static void Initialize(Context appContext)
    {
        ApplicationContext = appContext.getApplicationContext();

        // Default Data
        Data.DefaultFont = Typeface.createFromAsset(ApplicationContext.getAssets(), "fonts/digital-7.ttf");
        Data.Colors = new Colors();
        Data.Preferences = new Preferences();
        Data.Sounds = new SoundIdClipPairs();

        // Services
        Services.Battery = new DefaultBatteryService();
        Services.Location = new DefaultLocationService();
        Services.Input = new DefaultInputService();
        Services.Animation = new DefaultAnimationService();
        Services.Vibration = new DefaultVibrationService();
        Services.Sound = new DefaultSoundService();
    }
}
