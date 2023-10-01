package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rocketteam.passkeeper.GuardarPassword.Agregar_Password;

public class Passwords extends AppCompatActivity {

    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        btnAgregar = findViewById(R.id.btn_agregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Passwords.this, Agregar_Password.class);
                startActivity(intent);
            }
        });


    }
}