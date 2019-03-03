package ir.rahbod.habibi.controller;

import android.app.Application;
import android.content.Context;

import com.cedarstudios.cedarmapssdk.CedarMaps;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MyApp extends Application {
    public static volatile Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        CedarMaps.getInstance()
                .setClientID("acharchi-14303242984039919475")
                .setClientSecret("bz_FlmFjaGFyY2hpjtizjeh7jdd1Zl2JtKDOfbZ4niyPlnPmOAZDlQuo_ac=")
                .setContext(this);
    }
}
