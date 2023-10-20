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
    import android.content.SharedPreferences;
    import android.content.Context;



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


            /**
             * Busca el botón de inicio de sesión en la interfaz de usuario y agrega un escuchador
             * para manejar el evento de clic.
             */
            Button btnLogin = findViewById(R.id.btn_login_m);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Verifica si la entrada del usuario es válida antes de intentar iniciar sesión.
                    if (validateInput()) {
                        try {
                            // Intenta iniciar sesión, manejando posibles excepciones de Hashing y NoSuchAlgorithmException.
                            iniciarSesion();
                        } catch (HashUtility.HashingException | NoSuchAlgorithmException e) {
                            // Lanza una RuntimeException en caso de excepción para propagar el error.
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

        /**
         * Inicia sesión en la aplicación, verificando las credenciales del usuario.
         *
         * @throws HashUtility.HashingException si se produce un error al realizar el hashing de la contraseña.
         * @throws NoSuchAlgorithmException si no se encuentra el algoritmo de hashing especificado.
         */
        private void iniciarSesion() throws HashUtility.HashingException, NoSuchAlgorithmException {
            // Obtiene el correo electrónico ingresado por el usuario.
            String email = editTextEmail.getText().toString();
            // Obtiene la contraseña ingresada por el usuario.
            String password = editTextPassword.getText().toString();
            // Abre la conexión con la base de datos.
            dbManager.open();


            // Valida las credenciales del usuario en la base de datos.
            if (dbManager.validateUser(password, email)) {

                ObtenerID(userId);
                // Si las credenciales son válidas, se inicia la actividad de contraseñas.
                Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
                startActivity(intent);

                // Cierra la conexión con la base de datos.
                dbManager.close();
            } else {
                // Si las credenciales son inválidas, muestra un mensaje de error.
                mostrarSweetAlert(SweetAlertDialog.ERROR_TYPE, "Credenciales inválidas", "usuario o contraseña incorrecto");
                // También podría usar Toast para mostrar un mensaje de error alternativo.
                //Toast.makeText(this, "Credenciales inválidas, por favor intenta nuevamente", Toast.LENGTH_SHORT).show();
                // Cierra la conexión con la base de datos.
                dbManager.close();
            }

            private void ObtenerID(String userId) {
                // Obtiene una instancia de SharedPreferences.
                SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Guarda el ID del usuario en SharedPreferences.
                editor.putString("user_id", userId);
                editor.apply();
            }
        }


        /**
         * Muestra un diálogo emergente SweetAlertDialog con un título, mensaje y botón de aceptar personalizado.
         *
         * @param tipo     El tipo de diálogo SweetAlertDialog (por ejemplo, SweetAlertDialog.ERROR_TYPE).
         * @param titulo   El título que se mostrará en el diálogo.
         * @param mensaje  El mensaje que se mostrará en el diálogo.
         */
        private void mostrarSweetAlert(int tipo, String titulo, String mensaje) {
            // Registra un mensaje de depuración para indicar el tipo de SweetAlertDialog.
            Log.d("TAG", "Mostrando SweetAlertDialog de tipo: " + tipo);

            // Crea una instancia de SweetAlertDialog con el contexto actual y el tipo especificado.
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, tipo);
            // Establece el título del diálogo.
            sweetAlertDialog.setTitleText(titulo);
            // Establece el mensaje del diálogo.
            sweetAlertDialog.setContentText(mensaje);
            // Establece el texto del botón de confirmación como "Aceptar".
            sweetAlertDialog.setConfirmText("Aceptar");
            // Establece un escuchador para el botón de confirmación.
            sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                // Cierra el diálogo con animación.
                sweetAlertDialog1.dismissWithAnimation();

                // Realiza acciones adicionales en función del tipo de diálogo.
                if (tipo == 2) {
                    finish(); // Cerrar la actividad en caso de un error de registro
                }
            });
            // Muestra el diálogo SweetAlertDialog.
            sweetAlertDialog.show();
        }
    }
