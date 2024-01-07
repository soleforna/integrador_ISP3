package com.rocketteam.passkeeper;


import static com.rocketteam.passkeeper.util.ShowAlertsUtility.mostrarSweetAlert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
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
import com.rocketteam.passkeeper.util.ShowAlertsUtility;

import java.util.Objects;

public class RegisterPasswordActivity extends AppCompatActivity {

    // Variables de instancia para manejar la base de datos y las vistas
    private DbManager dbManager;
    private TextInputEditText editTextName;
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUrl;
    private TextInputEditText editTextDescripcion;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUrl;
    private TextInputLayout textInputLayoutPass;

    Button btnAtras;
    Button btnGuardar;

    /**
     * Método llamado cuando se crea la actividad. Se inicializan las vistas y se configuran
     * los Listeners para los botones.
     *
     * @param savedInstanceState Objeto que contiene el estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        // Inicializacion de variables
        dbManager = new DbManager(getApplicationContext());
        editTextName = findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        textInputLayoutName = findViewById(R.id.textInputLayout);
        textInputLayoutUrl = findViewById(R.id.textInputLayout4);
        textInputLayoutPass = findViewById(R.id.textInputLayout3);
        ImageView imageViewGenerar2 = findViewById(R.id.imageViewGenerar2);

        // Agrega TextWatcher a los EditText para validar en tiempo real
        editTextName.addTextChangedListener(new InputTextWatcher(textInputLayoutName));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPass));
        editTextUrl.addTextChangedListener(new InputTextWatcher(textInputLayoutUrl));

        // Método para generar un password aleatorio
        imageViewGenerar2.setOnClickListener(v -> {
            String randomPassword = HashUtility.generateRandomPassword(12);
            editTextPassword.setText(randomPassword);
            // Establece la selección al final del texto
            editTextPassword.setSelection(Objects.requireNonNull(editTextPassword.getText()).length());
            Toast.makeText(RegisterPasswordActivity.this, "Contraseña generada con éxito", Toast.LENGTH_SHORT).show();
        });

        // Configuración del botón para regresar a la actividad ShowPasswordsActivity
        btnAtras = findViewById(R.id.boton_atras_guardar);
        btnAtras.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterPasswordActivity.this, ShowPasswordsActivity.class);
            startActivity(intent);
        });

        // Configuración del botón para guardar la contraseña
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(view -> {
            if (validateInputNewPass()) {
                addPassword();
            }
        });
    }

    /**
     * Método para validar las entradas del usuario al registrar una nueva contraseña.
     *
     * @return true si las entradas son válidas, false si hay errores de validación.
     */
    private boolean validateInputNewPass() {
        String url = Objects.requireNonNull(editTextUrl.getText()).toString();
        String pass = Objects.requireNonNull(editTextPassword.getText()).toString();
        String name = Objects.requireNonNull(editTextName.getText()).toString();

        if (pass.isEmpty()) {
            textInputLayoutPass.setError("Por favor, ingresa una contraseña");
            return false;
        } else if (name.isEmpty()) {
            textInputLayoutName.setError("Por favor, ingresa un nombre");
            return false;
        } else if (!url.isEmpty() && !url.matches("((https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-z]{2,})+(/\\S*)?)")) {
            textInputLayoutUrl.setError("Por favor, ingresa una URL válida");
            return false;
        }
        return true;
    }

    /**
     * Método para registrar una nueva contraseña en la base de datos.
     */
    private void addPassword() {
        String ERROR = "Error al registrar la contraseña";
        try {
            dbManager.open();

            //obtengo el ID del usuario logueado
            SharedPreferences sharedPreferences = getSharedPreferences("Storage", Context.MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", -1);
            Log.i("TAG", "UserId desde addPassword: "+userId);
            PasswordCredentials password = null;
            if (userId != -1) {
                password = new PasswordCredentials(
                        Objects.requireNonNull(editTextUsuario.getText()).toString(),
                        Objects.requireNonNull(editTextUrl.getText()).toString(),
                        Objects.requireNonNull(editTextPassword.getText()).toString(),
                        Objects.requireNonNull(editTextDescripcion.getText()).toString(),
                        Objects.requireNonNull(editTextName.getText()).toString(),
                        userId
                );
            }

            if (dbManager.passwordRegister(password)) {
                // Mostrar un SweetAlertDialog para el registro exitoso de la contraseña
                ShowAlertsUtility.mostrarSweetAlert(this, 2, "Registro exitoso", "El Password ha sido registrado correctamente", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    // Redirigir al usuario a la página de PasswordActivity
                    Intent intent = new Intent(RegisterPasswordActivity.this, ShowPasswordsActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            mostrarSweetAlert(this, 1, ERROR, "Fallo el registro en la base de datos",null);
            e.printStackTrace();
        } catch (HashUtility.HashingException e){
            mostrarSweetAlert(this, 1, ERROR, "Fallo al encriptar la contraseña", null);
        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            mostrarSweetAlert(this, 1, "Error", "Ocurrió un error inesperado.", null);
        } finally {
            dbManager.close();
        }
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso del dispositivo.
     * Este método reemplaza el comportamiento predeterminado del botón de retroceso,
     * redirigiendo al usuario desde la actividad actual ({@code RegisterPasswordActivity})
     * a la actividad principal ({@code ShowPasswordsActivity}) de la aplicación.
     * Una vez que la redirección se ha completado, la actividad actual es finalizada
     * y eliminada de la pila de actividades para mantener una estructura de navegación coherente.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterPasswordActivity.this, ShowPasswordsActivity.class);
        startActivity(intent);
        finish();
    }
}