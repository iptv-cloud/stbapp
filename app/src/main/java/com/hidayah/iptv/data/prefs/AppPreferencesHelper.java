/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.hidayah.iptv.data.prefs;


import android.content.Context;

import com.hidayah.iptv.BuildConfig;

/**
 * Created by janisharali on 27/01/17.
 */


public class AppPreferencesHelper implements PreferencesHelper {
    private static final String KEY_LAST_CHANNEL="last_played_channel";
    private static final String KEY_USER_NAME="user_name";
    private static final String KEY_TOKEN="user_access_token";
    private static final String KEY_REFRESH_TOKEN="user_refresh_token";
    private SharedPreferencesUtils sharedPreferencesUtils;
    public static final String TOKEN_NOT_SET = "token_not_set";
    private static final String USER_LOGIN_STATUS = "key_user_login_status";
    private static final String NORMAL_UPDATE = "Normal Update";
    private static final String KEY_SELECTED_LOCALE = "key_selectedLang";
    private static final String KEY_REFRESH_INTERVAL = "key_refresh_interval";
    private static final String KEY_APP_CURRENT_BASEURL = "key_app_current_base_url";

    public AppPreferencesHelper(Context context) {
        this.sharedPreferencesUtils = new SharedPreferencesUtils(context);
    }



    @Override
    public String getUserAccessToken() {
        return sharedPreferencesUtils.getStringValue(KEY_TOKEN,TOKEN_NOT_SET);
    }

    @Override
    public long getAccessTokenInterval() {
        return sharedPreferencesUtils.getIntegerValue(KEY_REFRESH_INTERVAL, 5*60*1000);
    }


    @Override
    public void setUserAccessToken(String token) {
       sharedPreferencesUtils.editStringValue(KEY_TOKEN,token);
    }

    @Override
    public void setUserRefreshToken(String refreshToken) {
        sharedPreferencesUtils.editStringValue(KEY_REFRESH_TOKEN,refreshToken);
    }


    @Override
    public void saveLastChannel(int channelId) {
        sharedPreferencesUtils.editIntegerValue(KEY_LAST_CHANNEL,channelId);
    }

    @Override
    public int getLastChannel() {
        return sharedPreferencesUtils.getIntegerValue(KEY_LAST_CHANNEL,-1);
    }

    @Override
    public String getAppCurrentBaseUrl() {
        return sharedPreferencesUtils.getStringValue(KEY_APP_CURRENT_BASEURL,null);
    }

    public String getBaseUrl() {
        if(getAppCurrentBaseUrl() != null) return getAppCurrentBaseUrl();
        return BuildConfig.BASE_URL;
    }
}
