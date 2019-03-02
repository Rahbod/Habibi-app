package ir.rahbod.habibi.pages;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

class MyLocListener implements LocationListener {

    private Location myLocation = null;

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
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

    public LatLng getDeviceLocation() {
        LatLng latLng = new LatLng();
        if (myLocation != null) {
            latLng.setLatitude(myLocation.getLatitude());
            latLng.setLongitude(myLocation.getLongitude());
        }
        Log.e("qqqq", "getDeviceLocation: " + latLng.getLatitude() );
        return latLng;
    }
}
