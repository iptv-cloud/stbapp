package com.hidayah.iptv.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hidayah.iptv.BuildConfig;
import com.hidayah.iptv.HidayahTvApplication;
import com.hidayah.iptv.data.prefs.AppPreferencesHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {


    private static final String TAG = "ApiClient";

    public static Retrofit getRetrofit(String host) {

        return new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public static Retrofit getAuthClient(String token) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(25, TimeUnit.SECONDS);
        httpClient.connectTimeout(25, TimeUnit.SECONDS);
        httpClient.addInterceptor(new AddHeaderInterceptor(token));
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(getLoggingInterceptor());
        }
        AppPreferencesHelper preferencesHelper = new AppPreferencesHelper(HidayahTvApplication.getInstance());
        return new Retrofit.Builder()
                .baseUrl(preferencesHelper.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(httpClient.build())
                .build();
    }

    private static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static class AddHeaderInterceptor implements Interceptor {
        private String token;

        public AddHeaderInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            if (token!=null) builder.addHeader("Authorization", token);
            builder.addHeader("Content-Type","application/json");
           // builder.addHeader("Access-Code", HidayahTvApplication.getCountryCode());
            return chain.proceed(builder.build());

        }
    }

    public static <S> S createApiService(Class<S> serviceClass, String token) {
        return NetworkHelper.getAuthClient(token).create(serviceClass);
    }

    public static <S> S createApiService(Class<S> serviceClass) {
        AppPreferencesHelper preferencesHelper = new AppPreferencesHelper(HidayahTvApplication.getInstance());
        return NetworkHelper.getAuthClient(preferencesHelper.getUserAccessToken()).create(serviceClass);
    }
}
