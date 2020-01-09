package ir.rahbod.habibi.controller;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    public static volatile Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
    }
}
