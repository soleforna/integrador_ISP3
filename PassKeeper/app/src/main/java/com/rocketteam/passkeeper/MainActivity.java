package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info", "Iniciado");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//-------------------------------- LLeva a la activity RegisterActivity--------------------------------------
        TextView linkRegister = findViewById(R.id.linkRegister);
        linkRegister.setOnClickListener(view -> {
            // Navegar a otra activity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
//-------------------------------- LLeva a la activity PasswordActivity--------------------------------------
        btnLogin = findViewById(R.id.btn_login_m);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });

    }

}