package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EditarPassword extends AppCompatActivity {
    Button btnAtrasGuardar;
    Button btnGuardar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        Intent intent = getIntent(); // Obtiene el Intent que inició esta actividad
        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        int columnIndexId = intent.getIntExtra("idColumna", 0);
        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("ViewPassActivity", "Valor de columnIndexId: " + columnIndexId);

        //-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnAtrasGuardar = findViewById(R.id.boton_atras_guardar);
        btnAtrasGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
//-------------------------------- LLeva a la activity PasswordActivity--------------------------------------
        btnGuardar = findViewById(R.id.btnGuardarPassword);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
    }
}