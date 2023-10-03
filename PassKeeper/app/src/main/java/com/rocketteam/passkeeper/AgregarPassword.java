package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AgregarPassword extends AppCompatActivity {

    Button btnAtrasGuardar;
    Button btnGuardar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_password);
//-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnAtrasGuardar = findViewById(R.id.boton_atras_guardar);
        btnAtrasGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
//-------------------------------- LLeva a la activity PasswordActivity--------------------------------------
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });

    }
}