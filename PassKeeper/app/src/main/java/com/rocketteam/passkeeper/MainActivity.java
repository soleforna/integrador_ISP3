    package com.rocketteam.passkeeper;
    
    import static com.rocketteam.passkeeper.util.ShowAlertsUtility.mostrarSweetAlert;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.biometric.BiometricPrompt;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.content.Intent;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.android.material.textfield.TextInputLayout;
    import com.rocketteam.passkeeper.data.db.DbManager;
    import com.rocketteam.passkeeper.util.BiometricUtils;
    import com.rocketteam.passkeeper.util.HashUtility;
    import com.rocketteam.passkeeper.util.InputTextWatcher;
    import android.text.TextUtils;
    import android.widget.Toast;

    import java.security.NoSuchAlgorithmException;
    import java.util.Objects;

    import cn.pedant.SweetAlert.SweetAlertDialog;

    public class MainActivity extends AppCompatActivity {
        private final String TITLE = "Credenciales inválidas";
        private final String MSG = "Usuario o contraseña incorrectos";
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

            SharedPreferences sharedPreferences = getSharedPreferences("Storage", Context.MODE_PRIVATE);
            dbManager = new DbManager(getApplicationContext());
            editTextEmail = findViewById(R.id.editTextUsername);
            editTextPassword = findViewById(R.id.editPassword);
            textInputLayoutEmail = findViewById(R.id.textInputLayout);
            textInputLayoutPwd = findViewById(R.id.textInputLayout2);
    
            // Agregamos TextWatcher a los EditText
            editTextEmail.addTextChangedListener(new InputTextWatcher(textInputLayoutEmail));
            editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd));

            int bio = sharedPreferences.getInt("biometric",-1);
            Log.i("TAG", "Login Biometric: "+bio);
            Button btnBiometric = findViewById(R.id.fingerprint); //asigno el boton de la huella
            boolean biometricFinger = BiometricUtils.isBiometricPromptEnabled(MainActivity.this);

            btnBiometric.setVisibility(biometricFinger ? View.VISIBLE : View.GONE); // y lo hago visible si existe la biometria
            // Si el dispositivo es compatible con la autenticación biométrica
            btnBiometric.setOnClickListener(view -> {
                this.BiometricAuth();
            });

            if(bio == 1) {
                // Si el usuario ha configurado la preferencia para usar la autenticación biométrica
                if (biometricFinger) {
                    // Mostrar el cuadro de diálogo de autenticación biométrica
                    this.BiometricAuth();
                }
            }

            /*
              Busca el botón de inicio de sesión en la interfaz de usuario y agrega un escuchador
              para manejar el evento de clic.
             */
            Button btnLogin = findViewById(R.id.btn_login_m);
            btnLogin.setOnClickListener(v -> {
                if (validateInput()) {// Verifica si la entrada del usuario es válida antes de intentar iniciar sesión.
                    try {
                        // Intenta iniciar sesión, manejando posibles excepciones de Hashing y NoSuchAlgorithmException.
                        iniciarSesion();
                    } catch (HashUtility.HashingException | NoSuchAlgorithmException e) {
                        // Lanza una RuntimeException en caso de excepción para propagar el error.
                        throw new RuntimeException(e);
                    }
                }
            });
    
            TextView linkRegister = findViewById(R.id.linkRegister);

            linkRegister.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            });
        }
    
        private boolean validateInput() {
            String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
    
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

            return !email.isEmpty() && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

        }

        /**
         * Inicia sesión en la aplicación, verificando las credenciales del usuario.
         *
         * @throws HashUtility.HashingException si se produce un error al realizar el hashing de la contraseña.
         * @throws NoSuchAlgorithmException si no se encuentra el algoritmo de hashing especificado.
         */
        private void iniciarSesion() throws HashUtility.HashingException, NoSuchAlgorithmException {
            // Obtiene el correo electrónico ingresado por el usuario.
            String email = Objects.requireNonNull(editTextEmail.getText()).toString();
            // Obtiene la contraseña ingresada por el usuario.
            String password = Objects.requireNonNull(editTextPassword.getText()).toString();
            // Abre la conexión con la base de datos.
            dbManager.open();

            // Valida las credenciales del usuario en la base de datos.
            if (dbManager.validateUser(password, email)) {
                // Si las credenciales son válidas, se inicia la actividad de contraseñas.
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);

                // Cierra la conexión con la base de datos.
                dbManager.close();
            } else {
                // Si las credenciales son inválidas, muestra un mensaje de error.
                mostrarSweetAlert(this,2,TITLE,MSG,null);
                // También podría usar Toast para mostrar un mensaje de error alternativo.
                //Toast.makeText(this, "Credenciales inválidas, por favor intenta nuevamente", Toast.LENGTH_SHORT).show();
                // Cierra la conexión con la base de datos.
                dbManager.close();
            }

        }

        private void BiometricAuth() {
            BiometricUtils.showBiometricPrompt(MainActivity.this, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    // La autenticación biométrica fue exitosa
                    startActivity(new Intent(MainActivity.this, PasswordsActivity.class));
                    Toast.makeText(MainActivity.this, "Autenticado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }


    }
