package com.rocketteam.passkeeper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
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
import cn.pedant.SweetAlert.SweetAlertDialog;



public class RegisterActivity extends AppCompatActivity {
    private DbConnection dbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


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
                dbConnection = new DbConnection(RegisterActivity.this);
                registrarUsuario(dbConnection);
            }
        });
        //-------------------------------End onclick Registrar----------------------------------------------------------------------------------
         }
         //------------------------------Registrar Usuario-----------------------------------------------------------------------------------
        private void registrarUsuario(DbConnection dbConnection){


            TextInputLayout textInputLayoutEmail = findViewById(R.id.textInputLayoutReg);
            TextInputEditText editTextEmail = findViewById(R.id.editTextUsernameReg);

            TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayout2Reg);
            TextInputEditText editTextPassword = findViewById(R.id.editPasswordReg);

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            // Realiza las validaciones necesarias
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                   // Muestra Sweet Alert, en este caso es una alerta de warning
               SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Advertencia");//Muestra un titulo
                sweetAlertDialog.setContentText("Rellene todos los campos.");// Muestra un texto
                sweetAlertDialog.show();
                return;
            }

            SQLiteDatabase db = null;

            try {
                // Abre la conexión a la base de datos
                db = dbConnection.getWritableDatabase();

                // Insertar el usuario en la base de datos SQLite
                ContentValues values = new ContentValues();
                values.put(DbManager.EMAIL, email);
                values.put(DbManager.PASSWORD, password);

                long newRowId = db.insert(DbManager.TB_USER, null, values);
                if (newRowId != -1) {
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
                if (db != null) {
                    // Cierra la conexión a la base de datos si está abierta
                    db.close();
                }
            }
        }

}








