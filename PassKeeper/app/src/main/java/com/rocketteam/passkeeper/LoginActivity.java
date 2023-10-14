package com.rocketteam.passkeeper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.util.InputTextWatcher;
import android.text.TextUtils;



public class LoginActivity extends AppCompatActivity {
    private DbManager dbManager;
    //Instancia de la clase encargada de la gestión de la base de datos.

    private TextInputEditText editTextEmail;
    //input en el activity (email)

    private TextInputEditText editTextPassword;
    //input en el activity (password)
    private TextInputLayout textInputLayoutEmail;
    //Contenedor del input email
    private TextInputLayout textInputLayoutPwd;
    //Contenedor del input password


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Estas línea de acá arriba cargan la interfaz del activity que esta dentro (activity_main)


        //Las lineas de acá abajo van a inicializar las variables -->
        dbManager = new DbManager(getApplicationContext());
        //se instancía DbManager.

        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editPassword);
        textInputLayoutEmail = findViewById(R.id.textInputLayout);
        textInputLayoutPwd = findViewById(R.id.textInputLayout2);
        // estas 4 lieneas me permiten ingresar a la interfaz grafica del activiti_main.xml
        // las dos lineas primera me permiten manejar el contenido de los inputs y las otras dos restantes el contenedor de las mismas.


        // Agregamos TextWatcher a los EditText
        editTextEmail.addTextChangedListener(new InputTextWatcher(textInputLayoutEmail));
        editTextPassword.addTextChangedListener(new InputTextWatcher(textInputLayoutPwd));


        //Vinculo el boton de la interfaz grafica y le agrego un lisetener (escuchador)
        Button btnLogin = findViewById(R.id.btn_login_m);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    iniciarSesion();
                }
            }
        });
    }


        //Metodo para validar las entradas de usuarios
        //Estos strings me permiten obtener texto de lo ingresado por los usuarios en los inputs
        private boolean validateInput() {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();


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

            return true;
        }






        // Método para iniciar sesión
        private void iniciarSesion() {
            // Implementa la lógica de inicio de sesión aquí
            // Debes verificar si las credenciales del usuario son válidas en la base de datos
            // Si las credenciales son válidas, puedes redirigir al usuario a la pantalla principal de la aplicación
            // Si no son válidas, muestra un mensaje de error al usuario
        }
    }







