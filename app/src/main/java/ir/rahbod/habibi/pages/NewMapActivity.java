package ir.rahbod.habibi.pages;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.MapView;
import com.cedarstudios.cedarmapssdk.listeners.OnTilesConfigured;
import com.cedarstudios.cedarmapssdk.listeners.ReverseGeocodeResultListener;
import com.cedarstudios.cedarmapssdk.model.geocoder.reverse.ReverseGeocode;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.light.Position;

import ir.rahbod.habibi.R;

public class NewMapActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageView myLocation;
    private MyLocListener myLocListener;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean locationPermissionGranted = false;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myLocListener = new MyLocListener();
        LocationManager locationManager = (LocationManager) NewMapActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocListener);

        CedarMaps.getInstance()
                .setClientID("acharchi-14303242984039919475")
                .setClientSecret("bz_FlmFjaGFyY2hpjtizjeh7jdd1Zl2JtKDOfbZ4niyPlnPmOAZDlQuo_ac=")
                .setContext(this);
        CedarMaps.getInstance().prepareTiles(new OnTilesConfigured() {
            @Override
            public void onSuccess() {
                setContentView(R.layout.activity_new_map);
                mapView = findViewById(R.id.mapView);

                myLocation = findViewById(R.id.myLocation);
                myLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLocationPermission();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull String errorMessage) {
                finish();
            }
        });
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(NewMapActivity.this, FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(NewMapActivity.this, COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(NewMapActivity.this,
                        permission,
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(NewMapActivity.this,
                    permission,
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void initMap() {

        CedarMaps.getInstance().reverseGeocode(myLocListener.getDeviceLocation(),new ReverseGeocodeResultListener() {
            @Override
            public void onSuccess(@NonNull ReverseGeocode result) {
                Toast.makeText(NewMapActivity.this, "" + result.getLocality(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull String errorMessage) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                    initMap();
                }
            }

        }
    }
}
