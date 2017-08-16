package com.grego.SpeedometerPlayer;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Grego on 03/08/2017.
 */

public class MiLocationListener implements LocationListener {
    private MainActivity act;
    public float velocidad;

    MiLocationListener(MainActivity ma) {
        velocidad = -1;
        act = ma;
    }

    @Override
    public void onLocationChanged(Location location) {
        velocidad = location.getSpeed();
        act.actualizarKM(velocidad);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
