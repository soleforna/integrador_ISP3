package com.rocketteam.passkeeper.data.remote;

import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.data.model.response.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/v1/auth/login")
    Call<TokenResponse> login(@Body UserCredentials credentials);


}
