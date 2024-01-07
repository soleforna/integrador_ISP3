package com.rocketteam.passkeeper;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;

import java.util.Objects;

public class ViewPassActivity extends AppCompatActivity {

    Button btnYouPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass);

        Intent intent = getIntent(); // Obtiene el Intent que inició esta actividad
        // Almacena el ID de la contraseña
        int columnIndexId1 = intent.getIntExtra("idColumna", 0);

        TextView txtName = findViewById(R.id.editTextName);
        TextView txtUsername = findViewById(R.id.editTextUsuario);
        @SuppressLint("CutPasteId") TextView txtKeyword = findViewById(R.id.editTextPassword);
        TextView txtUrl = findViewById(R.id.editTextUrl);
        TextView txtDescription = findViewById(R.id.editTextDescripcion);
        btnYouPass = findViewById(R.id.botonYouPassViewPass);


        //Inicializar el dbManager
        DbManager dbManager = new DbManager(this);
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", -1);
        // Obtiene los detalles de la contraseña que se va a mostrar
        try {
            PasswordResponse updatedPasswordDetails = dbManager.getPasswordDetails(columnIndexId1, userId);
            if (updatedPasswordDetails != null) {
                // Rellena los campos de texto con los detalles de la contraseña a mostrar
                txtName.setText(updatedPasswordDetails.getName());
                txtUsername.setText(updatedPasswordDetails.getUsername());
                txtKeyword.setText(updatedPasswordDetails.getKeyword());
                txtUrl.setText(updatedPasswordDetails.getUrl());
                txtDescription.setText(updatedPasswordDetails.getDescription());
            } else {
                // Muestra un mensaje de error
                Toast.makeText(getApplicationContext(), "Error al mostrar los datos de la contraseña", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TAG", "ERROR: " + e.getMessage());
        }


        // Proporciona una etiqueta ("TAG") donde le paso la actividad y el mensaje
        Log.i("ViewPassActivity", "Valor de passwordId: " + columnIndexId1);

        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        int columnIndexId = intent.getIntExtra("idColumna", 0);
        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("ViewPassActivity", "Valor de columnIndexId: " + columnIndexId);

        ImageView imageViewCopy = findViewById(R.id.imageViewCopy);
        @SuppressLint("CutPasteId") TextInputEditText editTextPassword = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);


        //copiar la contraseña al portapapeles
        imageViewCopy.setOnClickListener(v -> {
            String password = Objects.requireNonNull(textInputLayout3.getEditText()).getText().toString();

            if (!password.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(clip);
                // Muestra un mensaje de confirmación
                Toast.makeText(getApplicationContext(), "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        btnYouPass = findViewById(R.id.botonYouPassViewPass);
        // Regresa a la activity PasswordActivity
        btnYouPass.setOnClickListener(view -> {
            Intent intent1 = new Intent(ViewPassActivity.this, ShowPasswordsActivity.class);
            startActivity(intent1);
        });
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso del dispositivo.
     * Este método reemplaza el comportamiento predeterminado del botón de retroceso,
     * redirigiendo al usuario desde la actividad actual ({@code ViewPassActivity})
     * a la actividad ({@code ShowPasswordsActivity}) de la aplicación.
     * Una vez que la redirección se ha completado, la actividad actual es finalizada
     * y eliminada de la pila de actividades para mantener una estructura de navegación coherente.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewPassActivity.this, ShowPasswordsActivity.class);
        startActivity(intent);
        finish();
    }

}