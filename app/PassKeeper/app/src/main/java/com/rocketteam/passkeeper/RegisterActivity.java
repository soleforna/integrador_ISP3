package com.rocketteam.passkeeper;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class RegisterActivity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         // Encontrar el TextView por su ID
         TextView linkLogin = findViewById(R.id.linkLogin);

         // Configurar un OnClickListener para el TextView
         linkLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Navegar de vuelta al MainActivity
                 Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                 startActivity(intent);
             }
         });


         // Configuración del boton que vuelve a home.
         MaterialButton btnHome = findViewById(R.id.btn_home);

         // Configurar un OnClickListener para el botón
         btnHome.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // Navegar de vuelta al MainActivity
                 Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                 startActivity(intent);
             }
         });
     }
}


