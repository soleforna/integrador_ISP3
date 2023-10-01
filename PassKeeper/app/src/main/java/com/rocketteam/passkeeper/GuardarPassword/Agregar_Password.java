package com.rocketteam.passkeeper.GuardarPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rocketteam.passkeeper.MainActivity;
import com.rocketteam.passkeeper.Passwords;
import com.rocketteam.passkeeper.R;

public class Agregar_Password extends AppCompatActivity {

    Button btnAtrasGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_password);

        btnAtrasGuardar = findViewById(R.id.boton_atras_guardar);
        btnAtrasGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agregar_Password.this, Passwords.class);
                startActivity(intent);
            }
        });
    }
}