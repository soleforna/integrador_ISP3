package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;
import com.rocketteam.passkeeper.util.InputTextWatcher;

public class EditarPassword extends AppCompatActivity {

    Button btnPrev;
    Button btnGuardar;

    private DbManager dbManager;
    private TextInputEditText editTextName;
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUrl;
    private TextInputEditText editTextDescripcion;
    private int columnIndexId; // Almacena el ID de la contraseña que se está editando

    /**
     * Método llamado cuando se crea la actividad. Realiza la inicialización y configuración de la interfaz de usuario.
     *
     * @param savedInstanceState Instancia anterior del estado de la actividad (puede ser nula).
     */


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        // Obtiene el Intent que inició esta actividad
        Intent intent = getIntent();

        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        columnIndexId = intent.getIntExtra("idColumna", 0);

        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("EditarPassword", "Valor de columnIndexId: " + columnIndexId);

        //Se instancia dbmanager para acceder a su lógica
        dbManager = new DbManager(EditarPassword.this);
        dbManager.open();
        // Obtiene los detalles de la contraseña que se va a editar
        PasswordResponse updatedPasswordDetails = dbManager.getPasswordDetails(columnIndexId);


        if (updatedPasswordDetails != null) {
            // Rellena los campos de texto con los detalles de la contraseña a editar
            editTextName.setText(updatedPasswordDetails.getName());
            editTextUsuario.setText(updatedPasswordDetails.getUsername());
            editTextPassword.setText(updatedPasswordDetails.getPassword());
            editTextUrl.setText(updatedPasswordDetails.getUrl());
            editTextDescripcion.setText(updatedPasswordDetails.getDescription());
        } else {
            // Muestra un mensaje de error si la actualización falla
            Toast.makeText(EditarPassword.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
        }


        // Configuración del botón para guardar la contraseña editada
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene los nuevos valores de los campos
                String newName = editTextName.getText().toString();
                String newUser = editTextUsuario.getText().toString();
                String newPassword = editTextPassword.getText().toString();
                String newUrl = editTextUrl.getText().toString();
                String newDescription = editTextDescripcion.getText().toString();

                // Obtiene los detalles de la contraseña actual
                /*
                PasswordCredentials existingPassword  = dbManager.getPasswordDetails(columnIndexId);

                if (existingPassword != null) {
                    // Actualiza los detalles actuales con los nuevos valores
                    existingPassword.setName(newName);
                    existingPassword.setUser(newUser);
                    existingPassword.setPassword(newPassword);
                    existingPassword.setUrl(newUrl);
                    existingPassword.setDescription(newDescription);

                    // Llama a la función para actualizar la contraseña en la base de datos
                    dbManager.updatePassword(columnIndexId, existingPassword);

                    // Añade un mensaje de confirmación
                    Toast.makeText(EditarPassword.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // Mensaje de error si no se encuentra la contraseña
                    Toast.makeText(EditarPassword.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                }
*/
            }

        });




        //-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnPrev = findViewById(R.id.boton_atras);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cierra la base de datos y vuelve a la actividad PasswordsActivity
                dbManager.close();
                Intent intent = new Intent(EditarPassword.this, PasswordsActivity.class);
                startActivity(intent);
            }
        });


    }

}