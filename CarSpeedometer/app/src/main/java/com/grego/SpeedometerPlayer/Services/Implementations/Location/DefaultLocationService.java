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

    @Override
    public void StartListening(Activity activity)
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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
