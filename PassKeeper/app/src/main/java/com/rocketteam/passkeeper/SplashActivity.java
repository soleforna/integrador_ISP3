package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt; //importación para la autentificación biometrica
import androidx.core.content.ContextCompat;//importación para el contextCompat

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageViewFinger; //se instancia el imagenViewFinger
    private BiometricPrompt biometricPrompt; //se instancia el BiometricPromt

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageViewFinger = findViewById(R.id.imageViewFinger);
        boolean isLoguedIn = true;

        if (isLoguedIn) {
            // si el usuario esta autentificado te pide la autentificación biometrica
            mostrarDialogoDeAutenticacion();
        } else {
            // El usuario no está autenticado, redirige a la actividad de inicio de sesión
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual para que el usuario no pueda volver atrás
        }
        imageViewFinger.setOnClickListener(new View.OnClickListener() { //onclik para mostrar el DialogoDeAutenticacion
            @Override
            public void onClick(View v) {
                mostrarDialogoDeAutenticacion();
            }
        });

    }

    private void mostrarDialogoDeAutenticacion() {
        biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            // Autentificación Biometrica
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                // La autenticación fue exitosa
                startActivity(new Intent(SplashActivity.this, PasswordsActivity.class));
                Toast.makeText(SplashActivity.this, "Aunticado con exito", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    // En el caso de que no haya huellas dactilares registradas o la autenticación biométrica no es posible
                    //Todo hacer algo en este caso
                } else {
                    // Otro error de autenticación biométrica
                    Toast.makeText(SplashActivity.this, errString, Toast.LENGTH_SHORT).show();
                }
            }
                /*@Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    // Se requiere ayuda para autenticar
                    Toast.makeText(MainActivity.this, helpString, Toast.LENGTH_SHORT).show();
                    //en el caso de que se desee agregar alguna opción de ayuda al usuario
                }*/
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Autenticar para acceder a la aplicación").setDescription("Coloca tu dedo en el sensor de huella digital para autenticar.").setNegativeButtonText("Cancelar").build();

        biometricPrompt.authenticate(promptInfo);
    }

}
