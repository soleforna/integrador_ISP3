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
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;

import java.util.Objects;

public class EditPasswordActivity extends AppCompatActivity {

    Button btnPrev;
    Button btnGuardar;

    private DbManager dbManager;
    private TextInputEditText editTextName;
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUrl;
    private TextInputEditText editTextDescripcion;
    private SharedPreferences sharedPreferences;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUrl;
    private TextInputLayout textInputLayoutPass;
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
        dbManager = new DbManager(EditPasswordActivity.this);

        //inicializo las variables
        editTextName = findViewById(R.id.editTextName);
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        textInputLayoutName = findViewById(R.id.textInputLayout);
        textInputLayoutUrl = findViewById(R.id.textInputLayout4);
        textInputLayoutPass = findViewById(R.id.textInputLayout3);

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
                Toast.makeText(EditPasswordActivity.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
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

               if(validateInputNewPass()){
                   try {
                       int userId = sharedPreferences.getInt("userId", -1);
                       Log.i("TAG", "El usuario en EditPasswordActivity es: "+userId);

                       // Obtiene los detalles de la contraseña actual
                       pwd = new PasswordCredentials(newUser,newUrl,newPassword,newDescription,newName, userId);

                       if (dbManager.updatePassword(columnIndexId,pwd, userId)) {
                           // Añade un mensaje de confirmación
                           startActivity(new Intent(EditPasswordActivity.this, ShowPasswordsActivity.class));
                           Toast.makeText(EditPasswordActivity.this, "Modificado con éxito", Toast.LENGTH_SHORT).show();
                           finish();
                       } else {
                           // Mensaje de error si no se encuentra la contraseña
                           Toast.makeText(EditPasswordActivity.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                       }
                   }catch (Exception e){
                       Log.e("TAG", "ERROR: "+ e.getMessage());
                   }
               }
            }
        });

        //-------------------------------- Regresa a la activity PasswordActivity--------------------------------------
        btnPrev = findViewById(R.id.boton_atras);
        btnPrev.setOnClickListener(view -> {
            // Cierra la base de datos y vuelve a la actividad ShowPasswordsActivity
            Intent intent1 = new Intent(EditPasswordActivity.this, ShowPasswordsActivity.class);
            startActivity(intent1);
        });
    }

    /**
     * Método para validar las entradas del usuario al registrar una nueva contraseña.
     *
     * @return true si las entradas son válidas, false si hay errores de validación.
     */
    private boolean validateInputNewPass() {
        String url = Objects.requireNonNull(editTextUrl.getText()).toString();
        String pass = Objects.requireNonNull(editTextPassword.getText()).toString();
        String name = Objects.requireNonNull(editTextName.getText()).toString();

        if (pass.isEmpty()) {
            textInputLayoutPass.setError("Por favor, ingresa una contraseña");
            textInputLayoutName.setError(null);
            textInputLayoutUrl.setError(null);
            return false;
        } else if (name.isEmpty()) {
            textInputLayoutName.setError("Por favor, ingresa un nombre");
            textInputLayoutPass.setError(null);
            textInputLayoutUrl.setError(null);
            return false;
        } else if (!url.isEmpty() && !url.matches("((https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-z]{2,})+(/\\S*)?)")) {
            textInputLayoutUrl.setError("Por favor, ingresa una URL válida");
            textInputLayoutName.setError(null);
            textInputLayoutPass.setError(null);
            return false;
        } else {
            textInputLayoutName.setError(null);
            textInputLayoutUrl.setError(null);
            textInputLayoutPass.setError(null);
            return true;
        }
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso del dispositivo.
     * Este método reemplaza el comportamiento predeterminado del botón de retroceso,
     * redirigiendo al usuario desde la actividad actual ({@code EditPasswordActivity})
     * a la actividad ({@code ShowPasswordsActivity}) de la aplicación.
     * Una vez que la redirección se ha completado, la actividad actual es finalizada
     * y eliminada de la pila de actividades para mantener una estructura de navegación coherente.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditPasswordActivity.this, ShowPasswordsActivity.class);
        startActivity(intent);
        finish();
    }

}