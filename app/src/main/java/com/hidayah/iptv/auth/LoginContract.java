package com.hidayah.iptv.auth;


import com.hidayah.iptv.auth.model.Data;

public interface LoginContract {

    interface LoginCompleteListener {
        void onLoginFailed(String msg);
        void onLoginSuccess(Data data);
    }
}
