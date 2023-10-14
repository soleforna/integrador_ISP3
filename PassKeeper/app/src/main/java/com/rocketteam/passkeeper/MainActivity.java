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
    import com.rocketteam.passkeeper.util.InputTextWatcher;
    import android.text.TextUtils;
    import android.widget.Toast;
    
    public class MainActivity extends AppCompatActivity {
        private DbManager dbManager;
        private TextInputEditText editTextEmail;
        private TextInputEditText editTextPassword;
        private TextInputLayout textInputLayoutEmail;
        private TextInputLayout textInputLayoutPwd;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.i("info", "Iniciado");
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
                        iniciarSesion();
                    }
                }
            });
    
            TextView linkRegister = findViewById(R.id.linkRegister);

            linkRegister.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    
        // Resto del código de MainActivity
    
        // Método para validar las entradas de usuarios
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
    
        // Método para iniciar sesión
        private void iniciarSesion() {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
    
            boolean credentialsValid = dbManager.validateUser(password, email);
    
            if (credentialsValid) {
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Credenciales inválidas, por favor intenta nuevamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
