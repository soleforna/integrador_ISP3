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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.util.HashUtility;
import com.rocketteam.passkeeper.util.InputTextWatcher;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    private DbManager dbManager;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPassword2;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPwd;
    private TextInputLayout textInputLayoutPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicialización de las variables
        dbManager = new DbManager(getApplicationContext());
        editTextEmail = findViewById(R.id.editTextUsernameReg);
        editTextPassword = findViewById(R.id.editPasswordReg);
        editTextPassword2 = findViewById(R.id.editPasswordReg2);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutReg);
        textInputLayoutPwd = findViewById(R.id.textInputLayout2Reg);
        textInputLayoutPwd2 = findViewById(R.id.textInputLayout2Reg2);

        // Agrega TextWatcher a los EditText
        editTextEmail.addTextChangedListener(new InputTextWatcher(textInputLayoutEmail));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd));
        editTextPassword2.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd2));

        // Configuración del enlace para regresar al MainActivity
        TextView linkLogin = findViewById(R.id.linkLogin);
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configuración del botón para volver al MainActivity
        MaterialButton btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configuración del botón de registro
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    registrarUsuario();
                }
            }
        });
    }

    // Método para validar las entradas del usuario
    private boolean validateInput() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String password2 = editTextPassword2.getText().toString();

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

    // Método para registrar al usuario
    private void registrarUsuario() {
        try {
            dbManager.open();
            UserCredentials user = new UserCredentials(editTextEmail.getText().toString(), editTextPassword.getText().toString());

            if (dbManager.userRegister(user)) {
                // Mostrar un SweetAlertDialog para el registro exitoso
                mostrarSweetAlert(SweetAlertDialog.SUCCESS_TYPE, "Registro exitoso", "El usuario ha sido registrado correctamente.");
            } else {
                // Mostrar un SweetAlertDialog para el error de registro
                mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error en el registro", "El email "+user.getEmail()+" ya se encuentra registrado");
            }
        } catch (SQLiteException e) {
            // Mostrar un SweetAlertDialog para errores de base de datos
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error al registrar el usuario", "No se pudo registrar el usuario en la base de datos.");
            e.printStackTrace();
        } catch (HashUtility.SaltException e) {
            // Mostrar un SweetAlertDialog para errores de generación de salt
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error al registrar el usuario", "Error al generar el salt para la contraseña.");
        } catch (HashUtility.HashingException e) {
            // Mostrar un SweetAlertDialog para errores de hash
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error al registrar el usuario", "Error al hashear la contraseña.");
        } catch (Exception e) {
            // Mostrar un SweetAlertDialog para errores inesperados
            e.printStackTrace();
            mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Error", "Ocurrió un error inesperado.");
        } finally {
            dbManager.close();
        }
    }

    // Método para mostrar SweetAlertDialog
    private void mostrarSweetAlert(int tipo, String titulo, String mensaje) {
        Log.d("RegisterActivity", "Mostrando SweetAlertDialog de tipo: " + tipo);
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, tipo);
        sweetAlertDialog.setTitleText(titulo);
        sweetAlertDialog.setContentText(mensaje);
        sweetAlertDialog.setConfirmText("Aceptar"); // Botón aceptar
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            sweetAlertDialog1.dismissWithAnimation();
            if (tipo == 2) {
                finish(); // Cerrar la actividad en caso de un error de registro
            }
        });
        sweetAlertDialog.show();
    }
}
