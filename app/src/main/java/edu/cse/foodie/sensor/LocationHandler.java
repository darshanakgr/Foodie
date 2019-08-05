package edu.cse.foodie.sensor;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import edu.cse.foodie.model.SensorDataObject;

public class LocationHandler implements LocationListener {
    private final SensorDataObject dataObject;

    public LocationHandler(SensorDataObject dataObject) {
        this.dataObject = dataObject;
    }


    @Override
    public void onLocationChanged(Location location) {
        dataObject.setLat(location.getLatitude());
        dataObject.setLng(location.getLongitude());
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
