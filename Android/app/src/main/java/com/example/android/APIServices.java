package com.example.android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIServices {

    @POST("heart")
    Call<AuthResponse> getHeartDetails(@Body HeartDetails details);


}
