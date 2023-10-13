package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info", "Iniciado");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btn_login_m); //boton login
        TextView linkRegister = findViewById(R.id.linkRegister); //link registro

        linkRegister.setOnClickListener(view -> { // LLeva a la activity RegisterActivity
            // Navegar a otra activity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() { // LLeva a la activity PasswordActivity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
    }
}