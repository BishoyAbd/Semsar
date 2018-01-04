package com.projects.cactus.maskn;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by el on 10/16/2017.
 */

public class SemsarApplication extends Application {


    //applicationn crashes with prelolipop when using vector drawable ... this line solves the problem
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static SemsarApplication instance;

    private static RxBus rxBus = new RxBus();


    public static RxBus getRxBus() {
        return rxBus;
    }


    public SemsarApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeCaligraphy();
        initTimper();
    }

    private void initTimper() {


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Timber.plant(new CrashReportingTree());  //tobe implemented
        }
    }

    private void initializeCaligraphy() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/arabict.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    public static Context getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
