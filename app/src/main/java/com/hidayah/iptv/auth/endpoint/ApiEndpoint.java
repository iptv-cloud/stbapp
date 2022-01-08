package com.hidayah.iptv.auth.endpoint;

import com.hidayah.iptv.auth.model.LoginResponse;
import com.hidayah.iptv.player.model.epg.EpgCurrentGuideRes;
import com.hidayah.iptv.player.model.epg.EpgGuideRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiEndpoint {

    @POST("rtv-api/create-session")
    Call<LoginResponse> getRefreshToken(@Body Object requestBody);

    @GET("create-session")
    Call<LoginResponse> getAccessToken();


//    @GET("rtv-epg-api/guide-data")
//    Call<EpgGuideRes> getEpgGuideData(@Query("start_time") String start_time, @Query("end_time") String end_time);
//
//    @GET("rtv-epg-api/guide-summary")
//    Call<EpgCurrentGuideRes> getCurrentEpgData(@Query("start_time") String start_time, @Query("end_time") String end_time);

    @GET("epg/android-guide-data")
    Call<EpgGuideRes> getEpgGuideData(@Query("start_time") String start_time, @Query("end_time") String end_time);

    @GET("epg/current-channel-detail")
    Call<EpgCurrentGuideRes> getCurrentEpgData(@Query("channel_id") int channel_id);
}
