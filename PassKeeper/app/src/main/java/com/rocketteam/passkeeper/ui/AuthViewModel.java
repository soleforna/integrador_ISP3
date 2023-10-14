package com.rocketteam.passkeeper.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.data.repository.Repo;


public class AuthViewModel extends ViewModel {
    private Repo repo;

    public AuthViewModel() {
        repo = new Repo();

    }

    public LiveData<TokenResponse> autenticarUsuario(UserCredentials credenciales) {
        LiveData<TokenResponse> resultado = repo.autenticarUsuario(credenciales);
        return resultado;
    }

    // Métodos para manejar excepciones específicas, si es necesario
    public LiveData<String> getErrorMessage() {
        return repo.getErrorMessage();
    }
}
