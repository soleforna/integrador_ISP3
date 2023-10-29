
package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;
import com.rocketteam.passkeeper.util.HashUtility;

public class ViewPassActivity extends AppCompatActivity {
    private ImageView imageViewCopy;
    private TextInputEditText editTextPassword;
    private DbManager dbManager;
    private SharedPreferences sharedPreferences;

    Button btnYouPass;

    private TextView txtName, txtUsername, txtKeyword, txtUrl, txtDescription;
    private int columnIndexId; // Almacena el ID de la contraseña


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass);

        Intent intent = getIntent(); // Obtiene el Intent que inició esta actividad
        columnIndexId = intent.getIntExtra("idColumna", 0);

        txtName = findViewById(R.id.editTextName);
        txtUsername = findViewById(R.id.editTextUsuario);
        txtKeyword = findViewById(R.id.editTextPassword);
        txtUrl = findViewById(R.id.editTextUrl);
        txtDescription = findViewById(R.id.editTextDescripcion);
        btnYouPass = findViewById(R.id.botonYouPassViewPass);


        //Inicializar el dbManager
        dbManager = new DbManager(this);
        sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", -1);
        // Obtiene los detalles de la contraseña que se va a mostrar
        try {
            PasswordResponse updatedPasswordDetails = dbManager.getPasswordDetails(columnIndexId, userId);
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
        }catch (Exception e){
            Log.e("TAG", "ERROR: "+e.getMessage());
        }









        // Proporciona una etiqueta ("TAG") donde le paso la actividad y el mensaje
        Log.i("ViewPassActivity", "Valor de passwordId: " + columnIndexId);

        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        int columnIndexId = intent.getIntExtra("idColumna", 0);
        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("ViewPassActivity", "Valor de columnIndexId: " + columnIndexId);

        imageViewCopy = findViewById(R.id.imageViewCopy);
        editTextPassword = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);







        imageViewCopy.setOnClickListener(new View.OnClickListener() { //copiar la contraseña al portapapeles
            @Override
            public void onClick(View v) {
                String password = textInputLayout3.getEditText().getText().toString();

                if (!password.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("password", password);
                    clipboard.setPrimaryClip(clip);
                    // Muestra un mensaje de confirmación
                    Toast.makeText(getApplicationContext(), "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnYouPass = findViewById(R.id.botonYouPassViewPass);
        btnYouPass.setOnClickListener(new View.OnClickListener() {
            // Regresa a la activity PasswordActivity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.rocketteam.passkeeper.ViewPassActivity.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });
    }

}