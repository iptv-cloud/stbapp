package com.hidayah.iptv;

import android.app.Application;



public class HidayahTvApplication extends Application {
    public static String countryCode = null;
    private static HidayahTvApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static HidayahTvApplication getInstance() {
        return mInstance;
    }
}
