package ir.rahbod.habibi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cedarstudios.cedarmapssdk.MapView;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.controller.MyApp;
import ir.rahbod.habibi.helper.Constants;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MapFragment extends Fragment implements LocationEngineListener {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private LocationEngine mLocationEngine = null;
    private ProgressBar loader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapView);
        loader = view.findViewById(R.id.loader);

        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(mapboxMap -> {
            mMapboxMap = mapboxMap;

            mMapboxMap.setMaxZoomPreference(17);
            mMapboxMap.setMinZoomPreference(6);
            mMapboxMap.setCameraPosition(
                    new CameraPosition.Builder()
                            .target(Constants.QOM)
                            .zoom(15)
                            .build());

            if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
                enableLocationComponent();
            }

//            //Add marker to map
//            addMarkerToMapViewAtPosition(Constants.QOM);

            //Set a touch event listener on the map
            mMapboxMap.addOnMapClickListener(point -> {
                removeAllMarkersFromMapView();
                addMarkerToMapViewAtPosition(point);
            });

            setupCurrentLocationButton();
        });
    }

//    private void reverseGeocode(CameraPosition position) {
//        loader.setVisibility(View.VISIBLE);
//        location.setVisibility(View.GONE);
//        CedarMaps.getInstance().reverseGeocode(
//                position.target,
//                new ReverseGeocodeResultListener() {
//                    @Override
//                    public void onSuccess(@NonNull ReverseGeocode result) {
//                        Toast.makeText(MyApp.context, "" + result.getAddress(), Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
//                        location.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull String errorMessage) {
//
//                    }
//                });
//    }

    public boolean statusCheck() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("برای کار با نقشه باید GPS دستگاه خود را روشن کنید، آیا مایل به روشن کردن آن هستید؟")
                .setCancelable(false)
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        loader.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Add a marker to the map
    private void addMarkerToMapViewAtPosition(LatLng coordinate) {
        mMapboxMap.addMarker(new MarkerOptions().position(coordinate));
        SessionManager.getExtrasPref(MyApp.context).putExtra(PutKey.LAT, String.valueOf(coordinate.getLatitude()));
        SessionManager.getExtrasPref(MyApp.context).putExtra(PutKey.LNG, String.valueOf(coordinate.getLongitude()));
    }

    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    //Clear all markers on the map
    private void removeAllMarkersFromMapView() {
        mMapboxMap.clear();
    }

    private void animateToCoordinate(LatLng coordinate, int zoomLevel) {
        CameraPosition position = new CameraPosition.Builder()
                .target(coordinate)
                .zoom(zoomLevel)
                .build();
        mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @SuppressLint("MissingPermission")
    private void setupCurrentLocationButton() {
        FloatingActionButton fb = getView().findViewById(R.id.showCurrentLocationButton);
        fb.setOnClickListener(v -> {
            if (!statusCheck()) {
                buildAlertMessageNoGps();
            }else {
                enableLocationComponent();
                toggleCurrentLocationButton();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void toggleCurrentLocationButton() {
        if (!mMapboxMap.getLocationComponent().isLocationComponentEnabled()) {
            return;
        }
        Location location = mMapboxMap.getLocationComponent().getLastKnownLocation();
        if (location != null) {
            animateToCoordinate(new LatLng(location.getLatitude(), location.getLongitude()), 16);
        }

        switch (mMapboxMap.getLocationComponent().getRenderMode()) {
            case RenderMode.NORMAL:
                mMapboxMap.getLocationComponent().setRenderMode(RenderMode.COMPASS);
                break;
            case RenderMode.GPS:
                mMapboxMap.getLocationComponent().setRenderMode(RenderMode.NORMAL);
                break;
            case RenderMode.COMPASS:
                mMapboxMap.getLocationComponent().setRenderMode(RenderMode.NORMAL);
                break;
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        if (getActivity() == null) {
            return;
        }
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            // Create a location engine instance
            initializeLocationEngine();

            mMapboxMap.getLocationComponent().activateLocationComponent(getActivity());
            mMapboxMap.getLocationComponent().setLocationComponentEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION_LOCATION_REQUEST_CODE);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeLocationEngine() {
        mLocationEngine = new LocationEngineProvider(getContext()).obtainBestLocationEngineAvailable();
        mLocationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        mLocationEngine.activate();

        mLocationEngine.addLocationEngineListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            if (mLocationEngine != null) {
                mLocationEngine.activate();
            }
        }
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationEngine != null) {
            mLocationEngine.removeLocationUpdates();
            mLocationEngine.removeLocationEngineListener(this);
        }
        mMapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
        if (mLocationEngine != null) {
            mLocationEngine.deactivate();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mMapView = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_LOCATION_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED)) {
                    Toast.makeText(getActivity(), "qqqq", Toast.LENGTH_LONG).show();
                } else {
                    enableLocationComponent();
                    toggleCurrentLocationButton();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        if (mLocationEngine != null && PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            mLocationEngine.requestLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
