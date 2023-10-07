package com.rocketteam.passkeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbConnection;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterActivity extends AppCompatActivity {
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbManager = new DbManager(getApplicationContext());
        Log.d("DB", "Instanciado");

        //-------------------------------- LLeva a la activity MainActivity--------------------------------------
        TextView linkLogin = findViewById(R.id.linkLogin);

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar de vuelta al MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //-------------------------------- Regresa a la activity MainActivity--------------------------------------
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

// ----------------------------------------------------Onclick Registrar
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Boton", "de registrar");
                registrarUsuario();
            }
        });
    }

    //------------------------------Registrar Usuario-----------------------------------------------------------------------------------
    private void registrarUsuario() {

        TextInputLayout textInputLayoutEmail = findViewById(R.id.textInputLayoutReg);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayout2Reg);
        TextInputEditText editTextEmail = findViewById(R.id.editTextUsernameReg);
        TextInputEditText editTextPassword = findViewById(R.id.editPasswordReg);
        TextInputEditText editTextPassword2 = findViewById(R.id.editPasswordReg2);
        Log.d("email", editTextEmail.getText().toString());

        // Realiza las validaciones necesarias
        // TODO Realizar validaciones, expresiones regulares y validar que pasword sea igual a password2
        if (TextUtils.isEmpty(editTextEmail.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString())) {
            // Muestra Sweet Alert, en este caso es una alerta de warning



            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("Advertencia");//Muestra un titulo
            sweetAlertDialog.setContentText("Rellene todos los campos.");// Muestra un texto
            sweetAlertDialog.show();
            return;
        }

        try {
            dbManager.open();
            UserCredentials user = new UserCredentials(editTextEmail.getText().toString(), editTextPassword.getText().toString());

            if (dbManager.userRegister(user)) {
                // Registro exitoso
                // Muestra Sweet Alert, en este caso es una alerta de success
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Registro exitoso");//Muestra un titulo
                sweetAlertDialog.setContentText("El usuario ha sido registrado correctamente.");//Muestro un texto
                //sweetAlertDialog.setConfirmText("Aceptar"); // Boton Aceptar
                sweetAlertDialog.show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Error en el registro
                // Muestra Sweet Alert de error
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error en el registro");
                sweetAlertDialog.setContentText("No se pudo registrar el usuario.");
                //sweetAlertDialog.setConfirmText("Aceptar"); //Boton Aceptar
                sweetAlertDialog.show();
            }
        } catch (SQLiteException e) {
            // Error al insertar el usuario en la base de datos
            // Muestra Sweet Alert de error
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText("Error al registrar el usuario");//Muestra un titulo
            sweetAlertDialog.setContentText("No se pudo registrar el usuario.");//Muestra un texto
            // sweetAlertDialog.setConfirmText("Aceptar"); //Boton aceptar
            sweetAlertDialog.show();
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}









