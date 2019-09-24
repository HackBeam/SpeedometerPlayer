package com.grego.SpeedometerPlayer.Services.Implementations.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.ILocationService;
import com.grego.SpeedometerPlayer.Services.Listeners.ILocationListener;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Speed info provider service listening to location changes.
 */
public class DefaultLocationService implements ILocationService
{
    private LocationManager locationManagerAndroid;
    private LinkedList<ILocationListener> subscribers;
    private boolean permissionGranted = false;

    private Location cachedLocationData;
    private int cachedSpeedValue = -1;

    public DefaultLocationService()
    {
        locationManagerAndroid = (LocationManager) Core.ApplicationContext.getSystemService(Context.LOCATION_SERVICE);
        subscribers = new LinkedList<>();
    }

    /**
     * Checks if Location permission is granted
     * - If so, the service starts listening Android for location updates
     * - If not, ask the user to grant permission AND NOTHING MORE.
     *
     * NOTE: The given activity must implement the onRequestPermissionsResult() method to call again
     * this method in order to formally start listening location updates.
     * @param activity Activity needed to ask and check system permissions.
     */
    @Override
    public void StartListening(Activity activity)
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            !permissionGranted)
        {
            locationManagerAndroid.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.1f, this);
            permissionGranted = true;
        }
        else
        {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            // At this point, the activity should call again to the StartListening() method if the requested permission is granted.
        }
    }

    /**
     * Stops listening location updates.
     */
    @Override
    public void StopListening()
    {
        if (permissionGranted)
        {
            locationManagerAndroid.removeUpdates(this);
            permissionGranted = false;
        }
    }

    @Override
    public void Subscribe(ILocationListener listener)
    {
        subscribers.add(listener);

        if (permissionGranted && cachedLocationData != null)
        {
            listener.OnSpeedChanges(cachedSpeedValue);
        }
        else
        {
            listener.OnSpeedChanges(-1);
        }
    }

    @Override
    public void Unsubscribe(ILocationListener listener)
    {
        subscribers.remove(listener);
    }

    //region Android Location Manager Events
    /**
     * Called by the Android Location Manager when location changes.
     * Gets the location data, transforms the speed to the configured metric
     * applying the configured safety margin, and notifies all subscribers
     * with the new speed info.
     * @param location The Location data provided by Android.
     */
    @Override
    public void onLocationChanged(Location location)
    {
        cachedLocationData = location;
        float newSpeed = cachedLocationData.getSpeed();

        if (Core.Data.Preferences.speedUnit.equals("km/h"))
        {
            cachedSpeedValue = (int) (newSpeed * 3.6);
        }
        else if (Core.Data.Preferences.speedUnit.equals("mph"))
        {
            cachedSpeedValue = (int) (newSpeed * 2.23694);
        }
        else
        {
            cachedSpeedValue = (int) newSpeed;
        }

        if (cachedSpeedValue > 20)
        {
            cachedSpeedValue += Core.Data.Preferences.safetyMargin;
        }

        Iterator<ILocationListener> iterator = subscribers.iterator();
        while (iterator.hasNext())
        {
            iterator.next().OnSpeedChanges(cachedSpeedValue);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {

    }

    @Override
    public void onProviderEnabled(String s)
    {

    }

    @Override
    public void onProviderDisabled(String s)
    {

    }
    //endregion
}
