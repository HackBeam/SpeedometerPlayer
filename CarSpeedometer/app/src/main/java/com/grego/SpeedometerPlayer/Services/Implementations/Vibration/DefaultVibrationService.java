package com.grego.SpeedometerPlayer.Services.Implementations.Vibration;

import android.content.Context;
import android.os.Vibrator;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.IVibrationService;

/**
 * Provides and handles easy access to vibration functions.
 */
public class DefaultVibrationService implements IVibrationService
{
    private Vibrator androidVibrator;

    public DefaultVibrationService()
    {
        androidVibrator = (Vibrator) Core.ApplicationContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void Vibrate()
    {
        if (Core.Data.Preferences.vibrateEnabled)
        {
            androidVibrator.vibrate(Core.Data.Preferences.vibrationMilliseconds);
        }
    }
}
