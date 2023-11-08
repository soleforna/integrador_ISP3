package com.rocketteam.passkeeper;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.util.BiometricUtils;
import com.rocketteam.passkeeper.util.HashUtility;
import com.rocketteam.passkeeper.util.InputTextWatcher;
import com.rocketteam.passkeeper.util.ShowAlertsUtility;

import java.util.Objects;


/**
 * Activity para registrar un nuevo usuario.
 */
public class RegisterUserActivity extends AppCompatActivity {
    private DbManager dbManager;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPassword2;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPwd;
    private TextInputLayout textInputLayoutPwd2;

    private SwitchCompat switchBiometric;
    private boolean biometricEnabled;
    private boolean userWhitBiometrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Verificar si la autenticación biométrica está habilitada en este dispositivo
        biometricEnabled = BiometricUtils.isBiometricPromptEnabled(this);
        Log.i("TAG", "la biometria esta: " + biometricEnabled);
        switchBiometric = findViewById(R.id.switch1);

        // Inicialización de las variables
        dbManager = new DbManager(getApplicationContext());
        userWhitBiometrics = dbManager.userWhitBiometrics();
        Log.i("TAG", "Existe usuario con biometria habilitada: " + userWhitBiometrics);
        editTextEmail = findViewById(R.id.editTextUsernameReg);
        editTextPassword = findViewById(R.id.editPasswordReg);
        editTextPassword2 = findViewById(R.id.editPasswordReg2);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutReg);
        textInputLayoutPwd = findViewById(R.id.textInputLayout2Reg);
        textInputLayoutPwd2 = findViewById(R.id.textInputLayout2Reg2);

        // Agrega TextWatcher a los EditText para validación en tiempo real
        editTextEmail.addTextChangedListener(new InputTextWatcher(textInputLayoutEmail));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd));
        editTextPassword2.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd2));

        //En esta Linea el swicht sera visible si tanto la biometria esta activa o si ningun usuario la tiene activada
        switchBiometric.setVisibility(biometricEnabled && !userWhitBiometrics ? View.VISIBLE : View.GONE);

        // Configuración del enlace para regresar al MainActivity
        TextView linkLogin = findViewById(R.id.linkLogin);
        linkLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Configuración del botón para volver al MainActivity
        MaterialButton btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Configuración del botón de registro
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> {
            if (validateInput()) {
                registerUser();
            }
        });
    }

    /**
     * Método para validar las entradas del usuario.
     *
     * @return true si las entradas son válidas, false si hay errores de validación.
     */
    private boolean validateInput() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString();
        String password2 = Objects.requireNonNull(editTextPassword2.getText()).toString();

        //si no hay biometria ó hay un usuario ya utilizando la biometria pone el switch en false
        if (!biometricEnabled || userWhitBiometrics) {
            switchBiometric.setChecked(false);
        }
        // Validación del correo electrónico
        if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError("El email es necesario");
            return false;
        } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            textInputLayoutEmail.setError("Por favor, ingresa un correo electrónico válido");
            return false;
        }
        // Validación de las contraseñas
        if (TextUtils.isEmpty(password)) {
            textInputLayoutPwd.setError("La contraseña es necesaria");
            return false;
        } else if (!password.matches(".{8,}")) {
            textInputLayoutPwd.setError("Mínimo 8 caracteres");
            return false;
        }

        if (TextUtils.isEmpty(password2)) {
            textInputLayoutPwd2.setError("Por favor, confirma tu contraseña");
            return false;
        } else if (!password.equals(password2)) {
            textInputLayoutPwd2.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    /**
     * Método para registrar al usuario en la base de datos.
     */
    private void registerUser() {
        String MSGERROR = "Error al registrar el usuario";
        try {
            dbManager.open();
            UserCredentials user = new UserCredentials(Objects.requireNonNull(editTextEmail.getText()).toString(), Objects.requireNonNull(editTextPassword.getText()).toString());

            if (dbManager.userRegister(user, switchBiometric.isChecked() ? 1 : 0)) {
                ShowAlertsUtility.mostrarSweetAlert(this, 2, "Registro exitoso", "El usuario ha sido registrado correctamente", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    // Redirigir al usuario a la página de inicio de sesión
                    Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                // Mostrar un SweetAlertDialog para el error de registro
                ShowAlertsUtility.mostrarSweetAlert(this, 1, "Error en el registro", "El email " + user.getEmail() + " ya se encuentra registrado", null);
            }
        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            ShowAlertsUtility.mostrarSweetAlert(this, 1, MSGERROR, "No se pudo registrar el usuario en la base de datos.", null);
            e.printStackTrace();
        } catch (HashUtility.SaltException e) {
            // Mostrar un SweetAlertDialog para errores de generación de salt
            ShowAlertsUtility.mostrarSweetAlert(this, 1, MSGERROR, "Error al generar el salt para la contraseña.", null);
        } catch (HashUtility.HashingException e) {
            // Mostrar un SweetAlertDialog para errores de hash
            ShowAlertsUtility.mostrarSweetAlert(this, 1, MSGERROR, "Error al hashear la contraseña.", null);
        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            ShowAlertsUtility.mostrarSweetAlert(this, 1, "Error", "Ocurrió un error inesperado.", null);
        } finally {
            dbManager.close();
        }
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso del dispositivo.
     * Este método reemplaza el comportamiento predeterminado del botón de retroceso,
     * redirigiendo al usuario desde la actividad actual ({@code RegisterUserActivity})
     * a la actividad principal ({@code MainActivity}) de la aplicación.
     * Una vez que la redirección se ha completado, la actividad actual es finalizada
     * y eliminada de la pila de actividades para mantener una estructura de navegación coherente.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

