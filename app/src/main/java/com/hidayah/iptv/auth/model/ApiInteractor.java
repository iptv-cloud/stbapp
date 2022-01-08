package com.hidayah.iptv.auth.model;

import android.util.Log;

import com.hidayah.iptv.HidayahTvApplication;
import com.hidayah.iptv.auth.LoginContract;
import com.hidayah.iptv.auth.endpoint.ApiEndpoint;
import com.hidayah.iptv.data.NetworkHelper;
import com.hidayah.iptv.data.prefs.AppPreferencesHelper;
import com.hidayah.iptv.player.model.epg.EpgCurrentGuideRes;
import com.hidayah.iptv.player.model.epg.EpgGuideRes;
import com.hidayah.iptv.player.view.ExoPlayerActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiInteractor {
    private static final String TAG = "ApiInteractor";


    public void getToken(Object reqbody, LoginContract.LoginCompleteListener listener) {
//        Log.d(TAG, "getToken: ");
        // TODO put registration stuffs here

        ApiEndpoint apiService = NetworkHelper.createApiService(ApiEndpoint.class);

        Call<LoginResponse> call = apiService.getRefreshToken(reqbody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if(response.code() == 404){
                        listener.onLoginFailed("Not found");
                        return;
                    }
                    if(response.code() == 401 ){
                        listener.onLoginFailed("Unauthorized user");
                        return;
                    }
                    LoginResponse loginResponse = response.body();
                    Data data = loginResponse.getData();
//                    Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.body().getData().getChannelToken()+ "]");
                    listener.onLoginSuccess(data);
//                    AppPreferencesHelper preferencesHelper = new AppPreferencesHelper(HidayahTvApplication.getInstance());
//                    preferencesHelper.setAccessTokenInterval((long) data.getRefreshInterval()*1000);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    listener.onLoginFailed("Failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: t = [" + t.getMessage() + "]");
                listener.onLoginFailed(t.getMessage());
            }
        });

    }

    public void getAccessToken(LoginContract.LoginCompleteListener listener) {
        AppPreferencesHelper preferencesHelper = new AppPreferencesHelper(HidayahTvApplication.getInstance());

        Log.d(TAG, "getToken: ");
        // TODO put registration stuffs here
        ApiEndpoint apiService = NetworkHelper.createApiService(ApiEndpoint.class);

        Call<LoginResponse> call = apiService.getAccessToken();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    final int code = response.code();
                    if(code == 200){
                        Data loginResponse = response.body().getData();
                        preferencesHelper.setUserAccessToken(loginResponse.getChannelToken());
//                        Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.body().getData().getToken() + "]");
                       // listener.onLoginSuccess("", loginResponse.getAccessToken(),loginResponse.getUserid());
                    }else if(code == 401){
                        listener.onLoginFailed("UN_AUTHORIZED");
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                   listener.onLoginFailed(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure() called with: t = [" + t.getMessage() + "]");
                listener.onLoginFailed(t.getMessage());
            }
        });

    }
    public static final long HOUR = 3600*1000; // in milli-seconds.
    public void getEpgguideData(String token, ExoPlayerActivity.EpgGuideListener epgGuideListener){
        ApiEndpoint apiService = NetworkHelper.createApiService(ApiEndpoint.class, token);

        String start_date_time = null;
        String end_date_time = null;
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + 7 * 24 * HOUR);
            System.out.println(startDate);
            start_date_time = format.format(startDate);
            end_date_time = format.format(endDate);
//            Log.d(TAG, "getEpgguideData() called with: dateString = [" + start_date_time + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<EpgGuideRes> call = apiService.getEpgGuideData(start_date_time,end_date_time);
        call.enqueue(new Callback<EpgGuideRes>() {
            @Override
            public void onResponse(Call<EpgGuideRes> call, Response<EpgGuideRes> response) {
                //Toast.makeText(context,response.body().toString(),Toast.LENGTH_LONG).show();
                epgGuideListener.onGetEpgData(response.body().getChannels());
//                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<EpgGuideRes> call, Throwable t) {
//                Log.d(TAG, "onFailure() called with: t = [" + t.getMessage() + "]");
                epgGuideListener.onErorr(t.getMessage());
            }
        });
    }

    public void getCurrentEpgData(int channelId, ExoPlayerActivity.EpgCurrentGuideListener epgGuideListener){

        ApiEndpoint apiService = NetworkHelper.createApiService(ApiEndpoint.class);
        Call<EpgCurrentGuideRes> call = apiService.getCurrentEpgData(channelId);
        call.enqueue(new Callback<EpgCurrentGuideRes>() {
            @Override
            public void onResponse(Call<EpgCurrentGuideRes> call, Response<EpgCurrentGuideRes> response) {
                //Toast.makeText(context,response.body().toString(),Toast.LENGTH_LONG).show();
                epgGuideListener.onGetCurrentEpgData(response.body().getData());
//                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<EpgCurrentGuideRes> call, Throwable t) {
//                Log.d(TAG, "onFailure() called with: t = [" + t.getMessage() + "]");
                epgGuideListener.onErorr(t.getMessage());
            }
        });
    }
}