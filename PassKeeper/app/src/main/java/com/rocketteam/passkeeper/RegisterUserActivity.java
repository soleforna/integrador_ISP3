package com.rocketteam.passkeeper;

import static com.rocketteam.passkeeper.util.ShowAlertsUtility.mostrarSweetAlert;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.util.BiometricUtils;
import com.rocketteam.passkeeper.util.HashUtility;
import com.rocketteam.passkeeper.util.InputTextWatcher;

import java.util.Objects;


/**
 * Activity para registrar un nuevo usuario.
 */
public class RegisterUserActivity extends AppCompatActivity {
    private final String USER_ERROR = "Error al registrar el usuario";
    private DbManager dbManager;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPassword2;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPwd;
    private TextInputLayout textInputLayoutPwd2;
    private Switch switchBiometric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Verificar si la autenticación biométrica está habilitada en este dispositivo
        boolean biometricEnabled = BiometricUtils.isBiometricPromptEnabled(this);
        switchBiometric = findViewById(R.id.switch1);
        switchBiometric.setVisibility(biometricEnabled ? View.VISIBLE : View.GONE);
        Log.i("TAG", "switch biometrico "+ switchBiometric.getVisibility());

        // Inicialización de las variables
        dbManager = new DbManager(getApplicationContext());
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
                registrarUsuario();
            }
        });
    }

    /**
     * Método para validar las entradas del usuario.
     * @return true si las entradas son válidas, false si hay errores de validación.
     */
    private boolean validateInput() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString();
        String password2 = Objects.requireNonNull(editTextPassword2.getText()).toString();

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
    private void registrarUsuario() {
        try {
            dbManager.open();
            UserCredentials user = new UserCredentials(Objects.requireNonNull(editTextEmail.getText()).toString(), Objects.requireNonNull(editTextPassword.getText()).toString());
            //envia 1 o 0 segun la posicion del switch biometrico
            if (dbManager.userRegister(user, switchBiometric.isChecked() ? 1 : 0)) {
                // Mostrar un SweetAlertDialog para el registro exitoso
                mostrarSweetAlert(this, 2, "Registro exitoso", "El usuario ha sido registrado correctamente.");
                // Cerrar la actividad actual y volver a la actividad anterior
                finish();
            } else {
                // Mostrar un SweetAlertDialog para el error de registro
                mostrarSweetAlert(this, 1, USER_ERROR, "El email " + user.getEmail() + " ya se encuentra registrado");
            }
        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            mostrarSweetAlert(this, 1, USER_ERROR, "Falló el registro en la base de datos.");
            e.printStackTrace();
        } catch (HashUtility.SaltException e) {
            // Mostrar un SweetAlertDialog para errores de generación de salt
            mostrarSweetAlert(this, 1, USER_ERROR, "Error al generar el salt para la contraseña.");
        } catch (HashUtility.HashingException e) {
            // Mostrar un SweetAlertDialog para errores de hash
            mostrarSweetAlert(this, 1, USER_ERROR, "Error al hashear la contraseña.");
        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            mostrarSweetAlert(this, 1, "Error", "Ocurrió un error inesperado.");
        } finally {
            dbManager.close();
        }
    }

}
