package com.rocketteam.passkeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;

import java.util.Objects;

public class EditarPassword extends AppCompatActivity {

    Button btnPrev;
    Button btnGuardar;

    private DbManager dbManager;
    private TextInputEditText editTextName;
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUrl;
    private TextInputEditText editTextDescripcion;
    private SharedPreferences sharedPreferences;
    private int columnIndexId; // Almacena el ID de la contraseña que se está editando

    /**
     * Método llamado cuando se crea la actividad. Realiza la inicialización y configuración de la interfaz de usuario.
     *
     * @param savedInstanceState Instancia anterior del estado de la actividad (puede ser nula).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);
        // Obtiene el Intent que inició esta actividad
        Intent intent = getIntent();

        // Utiliza 0 como valor predeterminado en el caso que no venga ningun valor
        columnIndexId = intent.getIntExtra("idColumna", 0);

        // Proporciona una etiqueta ("TAG") donde le paso la activity y el mensaje
        Log.i("TAG", "Valor de columnIndexId: " + columnIndexId);

        //Se instancia dbmanager para acceder a su lógica
        dbManager = new DbManager(EditarPassword.this);

        //inicializo las variables
        editTextName = findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);


        int userId = sharedPreferences.getInt("userId", -1);
        // Obtiene los detalles de la contraseña que se va a editar
        try {
            PasswordResponse updatedPasswordDetails = dbManager.getPasswordDetails(columnIndexId, userId);
            if (updatedPasswordDetails != null) {
                // Rellena los campos de texto con los detalles de la contraseña a editar
                editTextName.setText(updatedPasswordDetails.getName());
                editTextUsuario.setText(updatedPasswordDetails.getUsername());

                editTextPassword.setText(updatedPasswordDetails.getKeyword());
                editTextUrl.setText(updatedPasswordDetails.getUrl());
                editTextDescripcion.setText(updatedPasswordDetails.getDescription());
            } else {
                // Muestra un mensaje de error si la actualización falla
                Toast.makeText(EditarPassword.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("TAG", "ERROR: "+e.getMessage());
        }

        // Configuración del botón para guardar la contraseña editada
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            PasswordCredentials pwd = null;

            @Override
            public void onClick(View view) {
                // Obtiene los nuevos valores de los campos
                String newName = Objects.requireNonNull(editTextName.getText()).toString();
                String newUser = Objects.requireNonNull(editTextUsuario.getText()).toString();
                String newPassword = Objects.requireNonNull(editTextPassword.getText()).toString();
                String newUrl = Objects.requireNonNull(editTextUrl.getText()).toString();
                String newDescription = Objects.requireNonNull(editTextDescripcion.getText()).toString();

               try {
                   int userId = sharedPreferences.getInt("userId", -1);
                   Log.i("TAG", "El usuario en EditarPassword es: "+userId);

                   // Obtiene los detalles de la contraseña actual
                   pwd = new PasswordCredentials(newUser,newUrl,newPassword,newDescription,newName, userId);

                   if (dbManager.updatePassword(columnIndexId,pwd, userId)) {
                       // Añade un mensaje de confirmación
                       startActivity(new Intent(EditarPassword.this, PasswordsActivity.class));
                       Toast.makeText(EditarPassword.this, "Modificado con éxito", Toast.LENGTH_SHORT).show();
                       finish();
                   } else {
                       // Mensaje de error si no se encuentra la contraseña
                       Toast.makeText(EditarPassword.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                   }
               }catch (Exception e){
                   Log.e("TAG", "ERROR: "+ e.getMessage());
               }

            }

        });

        //-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnPrev = findViewById(R.id.boton_atras);
        btnPrev.setOnClickListener(view -> {
            // Cierra la base de datos y vuelve a la actividad PasswordsActivity
            Intent intent1 = new Intent(EditarPassword.this, PasswordsActivity.class);
            startActivity(intent1);
        });

    }

}