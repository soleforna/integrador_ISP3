package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.util.HashUtility;

public class AgregarPassword extends AppCompatActivity {
    private ImageView imageViewCopy;
    private ImageView imageViewGenerar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_password);
        /**
         *
         * @param findViewById Obtengo los id.
         */
        imageViewCopy = findViewById(R.id.imageViewCopy);
        imageViewGenerar = findViewById(R.id.imageViewGenerar);
        Button btnGuardar = findViewById(R.id.btn_guardar);
        Button btnAtrasGuardar = findViewById(R.id.boton_atras_guardar);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);
        EditText editText = findViewById(R.id.editTextPassword);
        btnAtrasGuardar.setOnClickListener(new View.OnClickListener() { //Regresa a la activity PasswordActivity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() { //LLeva a la activity PasswordActivity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
        /**
         * Evento onclick que llama a una clase donde genera una contraseña aleatoria segura con letras mayúsculas, minúsculas, números y caracteres especiales.
         *
         * @param GenerateRandomPassword La cantidad de caracteres de la contraseña.
         * @param setText Setea el password en el editText de password.
         * @param Toast muestro un mensaje de susses.
         */
        imageViewGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String randomPassword = HashUtility.generateRandomPassword(12);
                editText.setText(randomPassword);
                Toast.makeText(AgregarPassword.this, "Contraseña generada con éxito", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewCopy.setOnClickListener(new View.OnClickListener() { //copiar la contraseña al portapapeles
            @Override
            public void onClick(View v) {
                String password = textInputLayout3.getEditText().getText().toString();

                if (!password.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("password", password);
                    clipboard.setPrimaryClip(clip);
                    // Muestra un mensaje de confirmación
                    Toast.makeText(getApplicationContext(), "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}