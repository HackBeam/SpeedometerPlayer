package com.grego.SpeedometerPlayer.Services.Definitions;

import android.content.Context;
import com.grego.SpeedometerPlayer.Services.Implementations.IBatteryListener;

public interface IBatteryService
{
    void StartListening(Context context);
    void StopListening(Context context);
    void Subscribe(IBatteryListener listener);
    void Unsubscribe(IBatteryListener listener);
}
