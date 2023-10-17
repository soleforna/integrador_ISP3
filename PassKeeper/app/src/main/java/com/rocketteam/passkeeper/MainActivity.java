    package com.rocketteam.passkeeper;
    
    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;
    import android.content.Intent;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.android.material.textfield.TextInputLayout;
    import com.google.android.material.textfield.TextInputLayout;
    import com.rocketteam.passkeeper.data.db.DbManager;
    import com.rocketteam.passkeeper.util.HashUtility;
    import com.rocketteam.passkeeper.util.InputTextWatcher;
    import android.text.TextUtils;
    import android.widget.Toast;

    import java.security.NoSuchAlgorithmException;

    import cn.pedant.SweetAlert.SweetAlertDialog;

    public class MainActivity extends AppCompatActivity {
        private DbManager dbManager;
        private TextInputEditText editTextEmail;
        private TextInputEditText editTextPassword;
        private TextInputLayout textInputLayoutEmail;
        private TextInputLayout textInputLayoutPwd;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.i("TAG", "PasKeeper Iniciado");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    
            dbManager = new DbManager(getApplicationContext());
            editTextEmail = findViewById(R.id.editTextUsername);
            editTextPassword = findViewById(R.id.editPassword);
            textInputLayoutEmail = findViewById(R.id.textInputLayout);
            textInputLayoutPwd = findViewById(R.id.textInputLayout2);
    
            // Agregamos TextWatcher a los EditText
            editTextEmail.addTextChangedListener(new InputTextWatcher(textInputLayoutEmail));
            editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd));
    
            Button btnLogin = findViewById(R.id.btn_login_m);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput()) {
                        try {
                            iniciarSesion();
                        } catch (HashUtility.HashingException | NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
    
            TextView linkRegister = findViewById(R.id.linkRegister);

            linkRegister.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    
        private boolean validateInput() {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
    
            // Validación del correo electrónico
            if (TextUtils.isEmpty(email)) {
                textInputLayoutEmail.setError("El email es necesario");
                return false;
            } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                textInputLayoutEmail.setError("Por favor, ingresa un correo electrónico válido");
                return false;
            }
    
            // Validación de la contraseña
            if (TextUtils.isEmpty(password)) {
                textInputLayoutPwd.setError("La contraseña es necesaria");
                return false;
            }

            return true && !email.isEmpty() && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

        }
    
        private void iniciarSesion() throws HashUtility.HashingException, NoSuchAlgorithmException {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            dbManager.open();
            if (dbManager.validateUser(password, email)) {
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);
                dbManager.close();
            } else {
                mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Credenciales inválidas", "usuario o contraseña incorrecto");
                //Toast.makeText(this, "Credenciales inválidas, por favor intenta nuevamente", Toast.LENGTH_SHORT).show();
                dbManager.close();
            }

        }

        private void mostrarSweetAlert(int tipo, String titulo, String mensaje) {
            Log.d("TAG", "Mostrando SweetAlertDialog de tipo: " + tipo);
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
