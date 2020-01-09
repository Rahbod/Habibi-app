package ir.rahbod.habibi.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import ir.map.sdk_map.MapirStyle;
import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class MapActivity extends FragmentActivity {

    MapboxMap map;
    Style mapStyle;
    MapView mapView;
    LatLng qomPoint = new LatLng(34.642678, 50.879720);

    SymbolManager symbolManager;
    Symbol selectedPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        initMap();

        CardView btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPoint == null) {
                    Toast.makeText(MapActivity.this, "لطفا مکان مورد نظر را از روی نقشه انتخاب کنید.", Toast.LENGTH_LONG).show();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(PutKey.LAT, selectedPoint.getLatLng().getLatitude());
                    returnIntent.putExtra(PutKey.LNG, selectedPoint.getLatLng().getLongitude());
                    returnIntent.putExtra(PutKey.ZOOM, map.getCameraPosition().zoom);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private void initMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapStyle = style;

                        // Change camera position to qom
                        map.setCameraPosition(new CameraPosition.Builder()
                                .target(qomPoint)
                                .zoom(11)
                                .build());

                        // Set map click listener
                        map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @SuppressLint("ShowToast")
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                addSymbolToMap(point);
                                return false;
                            }
                        });
                    }
                });
            }
        });
    }

    private void addSymbolToMap(LatLng point) {
        mapStyle.addImage("sample_image_id", getResources().getDrawable(R.drawable.mapbox_marker_icon_default));

        // create symbol manager object
        if (symbolManager != null)
            symbolManager.delete(selectedPoint);

        symbolManager = new SymbolManager(mapView, map, mapStyle);

        /*symbolManager.addClickListener(new OnSymbolClickListener() {
            @Override
            public void onAnnotationClick(Symbol symbol) {
                Toast.makeText(MapActivity.this, "This is CLICK_EVENT"+symbol.getLatLng().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        symbolManager.addLongClickListener(new OnSymbolLongClickListener() {
            @Override
            public void onAnnotationLongClick(Symbol symbol) {
                Toast.makeText(MapActivity.this, "This is LONG_CLICK_EVENT", Toast.LENGTH_SHORT).show();
            }
        });*/

        // set non-data-driven properties, such as:
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setIconRotationAlignment(ICON_ROTATION_ALIGNMENT_VIEWPORT);

        // Add symbol at specified lat/lon
        SymbolOptions symbolOptions = new SymbolOptions();
        symbolOptions.withLatLng(point);
        symbolOptions.withIconImage("sample_image_id");
        symbolOptions.withIconSize(1.0f);

        selectedPoint = symbolManager.create(symbolOptions);
    }
}