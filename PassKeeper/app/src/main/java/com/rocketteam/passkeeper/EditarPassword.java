package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;

public class EditarPassword extends AppCompatActivity {
    Button btnAtrasGuardar;
    Button btnGuardar;

    EditText editTextName;
    EditText editTextUsuario;
    EditText editTextPassword;
    EditText editTextUrl;
    EditText editTextDescripcion;

    private int columnIndexId; // Obtiene el ID de la contraseña a editar

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        Intent intent = getIntent(); // Obtiene el Intent que inició esta actividad
        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        int columnIndexId = intent.getIntExtra("idColumna", 0);
        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("ViewPassActivity", "Valor de columnIndexId: " + columnIndexId);


        editTextName = findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        // Botón para guardar la contraseña editada
        btnGuardar = findViewById(R.id.btnGuardarPassword);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene los nuevos valores de los campos
                String newName = editTextName.getText().toString();
                String newUser = editTextUsuario.getText().toString();
                String newPassword = editTextPassword.getText().toString();
                String newUrl = editTextUrl.getText().toString();
                String newDescription = editTextDescripcion.getText().toString();

                // Crea un objeto PasswordCredentials con los nuevos valores
                PasswordCredentials updatedPassword = new PasswordCredentials(columnIndexId, newName, newUser, newPassword, newUrl, newDescription);

                // Actualiza la contraseña en la base de datos
                DbManager dbManager = new DbManager(EditarPassword.this);
                dbManager.open();
                int rowsUpdated = dbManager.updatePassword(columnIndexId, updatedPassword);
                dbManager.close();

                if (rowsUpdated > 0) {
                    // mensaje actualización exitosa
                    Toast.makeText(EditarPassword.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();

                } else {
                    //  mensaje de error si la actualización falla
                    Toast.makeText(EditarPassword.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                }
            }

        });


        //-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnAtrasGuardar = findViewById(R.id.boton_atras);
        btnAtrasGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });

    }
}