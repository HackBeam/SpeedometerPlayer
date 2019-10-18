package com.grego.SpeedometerPlayer.Services.Implementations.Location;

import android.os.Handler;

import com.grego.SpeedometerPlayer.Core;

class LocationSignalChecker implements Runnable
{
    private DefaultLocationService parent;
    private Handler handler;
    private int currentUpdateCount;
    private int lastUpdateCount;
    private boolean currentlyChecking;

    LocationSignalChecker(DefaultLocationService parent)
    {
        this.parent = parent;
        handler = new Handler();
        currentUpdateCount = 0;
        lastUpdateCount = 0;
        currentlyChecking = false;
    }

    void StartChecking()
    {
        if (!currentlyChecking)
        {
            currentUpdateCount = 0;
            lastUpdateCount = currentUpdateCount;
            currentlyChecking = handler.postDelayed(this, Core.Data.Preferences.locationSignalCheckingMilliseconds);
        }
    }

    void NotifyLocationUpdateReceived()
    {
        if (currentlyChecking)
        {
            currentUpdateCount++;
        }
        else
        {
            StartChecking();
        }
    }

    void StopChecking()
    {
        handler.removeCallbacks(this);
        currentlyChecking = false;
    }

    @Override
    public void run()
    {
        if (currentlyChecking)
        {
            if (currentUpdateCount == lastUpdateCount)
            {
                // Signal lost
                parent.OnLocationSignalLost();
                currentlyChecking = false;
            }
            else
            {
                // Update variables and check again
                lastUpdateCount = currentUpdateCount;
                currentlyChecking = handler.postDelayed(this, Core.Data.Preferences.locationSignalCheckingMilliseconds);
            }
        }
    }
}
