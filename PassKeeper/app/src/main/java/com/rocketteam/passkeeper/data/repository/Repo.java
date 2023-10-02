package com.rocketteam.passkeeper.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.data.model.response.TokenResponse;
import com.rocketteam.passkeeper.data.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repo {
    private ApiInterface apiInterface;

    public Repo() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public LiveData<TokenResponse> autenticarUsuario(UserCredentials credenciales) {
        MutableLiveData<TokenResponse> resultado = new MutableLiveData<>();

        Call<TokenResponse> call = apiInterface.login(credenciales);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(response.body());
                } else {
                    // Manejar errores de autenticación
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                // Manejar errores de red
            }
        });

        return resultado;
    }

}
