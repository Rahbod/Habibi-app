package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.cedarstudios.cedarmapssdk.listeners.OnTilesConfigured;

import java.util.Locale;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.fragment.MapFragment;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;

public class NewMapActivity extends AppCompatActivity {

    private ProgressBar loader;
    private FrameLayout frameLayout;
    private LinearLayout btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);

        loader = findViewById(R.id.loader);
        frameLayout = findViewById(R.id.content);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.getExtrasPref(NewMapActivity.this).getString(PutKey.LAT).isEmpty()){
                    Toast.makeText(NewMapActivity.this, "لطفا روی نقشه کلیک کنید", Toast.LENGTH_LONG).show();
                }else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(PutKey.LAT, SessionManager.getExtrasPref(NewMapActivity.this).getString(PutKey.LAT));
                    returnIntent.putExtra(PutKey.LNG, SessionManager.getExtrasPref(NewMapActivity.this).getString(PutKey.LNG));
                    returnIntent.putExtra(PutKey.ZOOM, 15);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

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