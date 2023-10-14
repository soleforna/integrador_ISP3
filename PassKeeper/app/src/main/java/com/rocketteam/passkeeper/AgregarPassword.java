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
<<<<<<< Updated upstream
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
=======

        // -------inicializacion de variables----------
        dbManager = new DbManager(getApplicationContext());
        editTextName= findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword =findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        textInputLayoutName= findViewById(R.id.textInputLayout);
        textInputLayoutUrl = findViewById(R.id.textInputLayout4);
        textInputLayoutPass = findViewById(R.id.textInputLayout3);

        // Agrega TextWatcher a los EditText
        editTextName.addTextChangedListener(new InputTextWatcher(textInputLayoutName));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPass));
        editTextUrl.addTextChangedListener(new InputTextWatcher(textInputLayoutUrl));


//-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnAtras = findViewById(R.id.boton_atras_guardar);
        btnAtras.setOnClickListener(new View.OnClickListener() {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    }

}
=======

    }
    // -------------------- validar campos obligatorios vacios---------------

    private boolean validateInputNewPass() {
        String url = editTextUrl.getText().toString();
        String pass = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();

        if (pass.isEmpty() ) {
            textInputLayoutName.setError("Por favor, ingresa una contraseña");
            return false;}
        else if(name.isEmpty()){
            textInputLayoutPass.setError("Por favor, ingresa un nombre");
            return false;
        } else if (!url.isEmpty() && !url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~|!:,.;]*[-a-zA-Z0-9+&@#/%=~|]")){
            textInputLayoutUrl.setError("Por favor, ingresa una url válida");
            return false;
        }
        return true;
    }


    // ----------------------Método para registrar nueva contraseña--------------------

    private void addPassword() {

        try {
            dbManager.open();
            PasswordCredentials password = new PasswordCredentials(1,editTextName.getText().toString(), editTextPassword.getText().toString(), editTextUsuario.getText().toString(), editTextUrl.getText().toString(), editTextDescripcion.getText().toString());
            if (dbManager.passwordRegister(password)) {
                // Mostrar un SweetAlertDialog para el registro exitoso de contraseña
                mostrarSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Registro de contraseña exitoso", "La contraseña ha sido registrada correctamente.");
            } else {
                // Mostrar un SweetAlertDialog para el error de registro
                mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error en el registro de contraseña", "Error");// TODO
            }
        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error al registrar la contraseña", "No se pudo registrar la contraseña en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error", "Ocurrió un error inesperado.");
        } finally {
            dbManager.close();
        }
    }

    //------------------- Método para mostrar SweetAlertDialog------------------------------------------
    private void mostrarSweetAlert ( int tipo, String titulo, String mensaje){
        Log.d("AgregarPassword", "Mostrando SweetAlertDialog de tipo: " + tipo);
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, tipo);
        sweetAlertDialog.setTitleText(titulo);
        sweetAlertDialog.setContentText(mensaje);
        sweetAlertDialog.setConfirmText("Aceptar"); // Botón aceptar
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            sweetAlertDialog1.dismissWithAnimation();

            if (tipo == 2) {
                finish(); // Cerrar la actividad en caso de un error de registro de contraseña
            }
        });
        sweetAlertDialog.show();
    }
}
>>>>>>> Stashed changes
