package ir.rahbod.habibi.pages;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.listeners.OnTilesConfigured;

import java.util.Locale;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.fragment.MapFragment;

public class NewMapActivity extends AppCompatActivity {

    private ProgressBar loader;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);

        loader = findViewById(R.id.loader);
        frameLayout = findViewById(R.id.content);

        CedarMaps.getInstance().prepareTiles(new OnTilesConfigured() {
            @Override
            public void onSuccess() {
                frameLayout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                MapFragment fragment = new MapFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragment).commit();
            }

            @Override
            public void onFailure(@NonNull String error) {

            }
        });


    }
}