
package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ViewPassActivity extends AppCompatActivity {
    private ImageView imageViewCopy;
    private TextInputEditText editTextPassword;
    Button btnYouPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass);

        Intent intent = getIntent(); // Obtiene el Intent que inició esta actividad
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
                Intent intent = new Intent(com.rocketteam.passkeeper.ViewPassActivity.this, ShowPasswordsActivity.class);
                startActivity(intent);
            }
        });
    }

}