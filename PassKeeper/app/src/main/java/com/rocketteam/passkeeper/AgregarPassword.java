package com.rocketteam.passkeeper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;

import com.rocketteam.passkeeper.util.HashUtility;
import com.rocketteam.passkeeper.util.InputTextWatcher;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.rocketteam.passkeeper.util.ShowAlertsUtility;


public class AgregarPassword extends AppCompatActivity {

    private DbManager dbManager;
    private TextInputEditText editTextName;
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUrl;
    private TextInputEditText editTextDescripcion;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUrl;
    private ImageView imageViewGenerar2;
    private TextInputLayout textInputLayoutPass;


    Button btnAtras;
    Button btnGuardar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        // -------inicializacion de variables----------
        dbManager = new DbManager(getApplicationContext());
        editTextName = findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        textInputLayoutName = findViewById(R.id.textInputLayout);
        textInputLayoutUrl = findViewById(R.id.textInputLayout4);
        textInputLayoutPass = findViewById(R.id.textInputLayout3);
        imageViewGenerar2 = findViewById(R.id.imageViewGenerar2);

        // Agrega TextWatcher a los EditText
        editTextName.addTextChangedListener(new InputTextWatcher(textInputLayoutName));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPass));
        editTextUrl.addTextChangedListener(new InputTextWatcher(textInputLayoutUrl));

        //------------------- Método para generar un password aleatorio------------------------------------------
        imageViewGenerar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String randomPassword = HashUtility.generateRandomPassword(12);
                editTextPassword.setText(randomPassword);
                // Establece la selección al final del texto
                editTextPassword.setSelection(editTextPassword.getText().length());
                Toast.makeText(AgregarPassword.this, "Contraseña generada con éxito", Toast.LENGTH_SHORT).show();
            }
        });
//-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnAtras = findViewById(R.id.boton_atras_guardar);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
//-------------------------------- Guarda contraseña--------------------------------------
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputNewPass()) {
                    addPassword();
                }
            }
        });

    }
    // -------------------- validar campos obligatorios vacios---------------

    private boolean validateInputNewPass() {
        String url = editTextUrl.getText().toString();
        String pass = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();

        if (pass.isEmpty()) {
            textInputLayoutName.setError("Por favor, ingresa una contraseña");
            return false;
        } else if (name.isEmpty()) {
            textInputLayoutPass.setError("Por favor, ingresa un nombre");
            return false;

        } else if (!url.isEmpty() && !url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~|!:,.;]*[-a-zA-Z0-9+&@#/%=~|]")) {
            textInputLayoutUrl.setError("Por favor, ingresa una url válida");
            return false;
        }
        return true;
    }


    // ----------------------Método para registrar nueva contraseña--------------------

    private void addPassword() {

        try {
            dbManager.open();
            SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

            // Obtener el valor de "userId" de SharedPreferences
            int userId = sharedPreferences.getInt("userId", -1); // -1 es un valor predeterminado en caso de que no se encuentre la clave "userId"
            PasswordCredentials password = new PasswordCredentials(userId, editTextName.getText().toString(), editTextPassword.getText().toString(), editTextUsuario.getText().toString(), editTextUrl.getText().toString(), editTextDescripcion.getText().toString());
            Log.i("PasswordActivity", "Mostrando Id" + userId);
            if (dbManager.passwordRegister(password)) {
                // Mostrar un SweetAlertDialog para el registro exitoso de contraseña
                ShowAlertsUtility.mostrarSweetAlert(this, 2, "Registro exitoso", "El Password ha sido registrado correctamente", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        // Redirigir al usuario a la página de PasswordActivity
                        Intent intent = new Intent(AgregarPassword.this, PasswordsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                // Mostrar un SweetAlertDialog para el error de registro
                ShowAlertsUtility.mostrarSweetAlert(this, SweetAlertDialog.ERROR_TYPE, "Error en el registro de contraseña", "Error", null); // TODO


            }

        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            ShowAlertsUtility.mostrarSweetAlert(this, SweetAlertDialog.ERROR_TYPE, "Error al registrar la contraseña", "No se pudo registrar la contraseña en la base de datos.", null);

            e.printStackTrace();

        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            ShowAlertsUtility.mostrarSweetAlert(this, SweetAlertDialog.ERROR_TYPE, "Error", "Ocurrió un error inesperado.", null);

        } finally {
            dbManager.close();
        }
    }


}
