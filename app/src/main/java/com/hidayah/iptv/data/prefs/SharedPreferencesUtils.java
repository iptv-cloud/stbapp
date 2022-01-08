package com.hidayah.iptv.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public String preferenceName = "RALLY_IPTV";
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;

    public static SharedPreferencesUtils preferenceUtils;
//
//    public static SharedPreferencesUtils getInstance() {
//        if(preferenceUtils == null) {
//            preferenceUtils = new SharedPreferencesUtils();
//        }
//
//        return preferenceUtils;
//    }

    public SharedPreferencesUtils(Context context) {
        this.mPreferences = context.getSharedPreferences(this.preferenceName, 0);
        this.mEditor = this.mPreferences.edit();
    }

    public void editStringValue(String key, String value) {
        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    public String getStringValue(String key, String defaultValue) {
        return this.mPreferences.getString(key, defaultValue);
    }

    public void editIntegerValue(String key, int value) {
        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public int getIntegerValue(String key, int defaultValue) {
        return this.mPreferences.getInt(key, defaultValue);
    }

    public void editBooleanValue(String key, boolean value) {
        this.mEditor.putBoolean(key, value);
        this.mEditor.commit();
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return this.mPreferences.getBoolean(key, defaultValue);
    }
}
