
package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rocketteam.passkeeper.GuardarPassword.Agregar_Password;
import com.rocketteam.passkeeper.MainActivity;
import com.rocketteam.passkeeper.R;

public class ViewPassActivity extends AppCompatActivity {

    Button btnYouPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass);

        btnYouPass = findViewById(R.id.botonYouPassViewPass);
        btnYouPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.rocketteam.passkeeper.ViewPassActivity.this, Passwords.class);
                startActivity(intent);
            }
        });
    }

}