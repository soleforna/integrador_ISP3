package com.rocketteam.passkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activuty_about);
//-------------------------------- Regresa la activity PasswordActivity--------------------------------------
        // Configuración del boton que vuelve a home.
        MaterialButton btn_about = findViewById(R.id.btn_about);

        // Configurar un OnClickListener para el botón
        btn_about.setOnClickListener(view -> {
            // Navegar de vuelta al Passwords
            Intent intent = new Intent(AboutActivity.this, PasswordsActivity.class);
            startActivity(intent);
        });

    }


}
