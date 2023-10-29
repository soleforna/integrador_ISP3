package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;

import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PasswordsActivity extends AppCompatActivity {

    FloatingActionButton btnA;
    private ImageView imageView;
    private ImageButton imageButton;
    private DbManager dbManager;
    private TableLayout tableLayout;
    private ScrollView scrollView;

    private ImageButton iconEye;
    private ImageButton iconPen;
    private ImageButton iconTrash;
    private  List<PasswordResponse> passwords;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);
        // Inicializa el DbManager y otros elementos de la actividad.
        dbManager = new DbManager(this);

        tableLayout = findViewById(R.id.tableLayout); //busca el id del tablelayout




        // Nombre del SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

        //Comprobar si la clave "userId" existe
        if (sharedPreferences.contains("userId")) {
            // Obtener el valor de "userId" de SharedPreferences
            int userId = sharedPreferences.getInt("userId", -1);
            Log.i("TAG", "Mostrando las contraseñas del usuario Id: " + userId);
            passwords = dbManager.getPasswordsListForUserId(userId);

            if(passwords.size()>0){
                Log.i("TAG", "La lista tiene: "+passwords.size());
            }else {
                Log.i("TAG", "La lista llega VACIA");
            }

            MostrarPasswords(passwords, userId);

        } else {
            // La clave "userId" no existe
            Log.e("PasswordsActivity", "La clave 'userId' no existe");
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fabAgregar = findViewById(R.id.btn_agregar);

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordsActivity.this, RegisterPasswordActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.menu_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PasswordsActivity.this, imageView);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.option_1) {
                            // Abre la actividad AboutActivity
                            Intent intent1 = new Intent(PasswordsActivity.this, AboutActivity.class);
                            startActivity(intent1);
                            return true;
                        } else if (itemId == R.id.option_2) {
                            // Abre la actividad MainActivity (
                            Intent intent2 = new Intent(PasswordsActivity.this, MainActivity.class);
                            startActivity(intent2);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                // Mostrar el menú emergente
                popupMenu.show();
            }
        });
        ScrollView scrollView = findViewById(R.id.scrollView);
        ImageView scrollIndicator = findViewById(R.id.scrollIndicator);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                if (scrollView.getChildAt(0).getHeight() > scrollView.getHeight()) {
                    // El contenido es más grande que la vista, muestra el indicador
                    scrollIndicator.setVisibility(View.VISIBLE);
                }
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getChildAt(0).getBottom()
                        <= (scrollView.getHeight() + scrollView.getScrollY())) {
                    // El contenido se encuentra al final, gira el indicador 180 grados
                    scrollIndicator.animate().rotation(180).start();
                } else {
                    // El contenido se puede desplazar, muestra el indicador
                    scrollIndicator.setVisibility(View.VISIBLE);
                    // El contenido se puede desplazar, restaura la rotación a 0 grados
                    scrollIndicator.animate().rotation(0).start();
                }
            }
        });
    }

    /*private void MostrarPasswords(int userId) {
        try {

            TableLayout tableLayout = findViewById(R.id.tableLayout);//Obtenemos el tableLayout
            TextView noPasswordsText = findViewById(R.id.txtNoPassword);//Obtenemos el textView
            ImageView circleExclamation = findViewById(R.id.imageView);//Obtenemos el imageView

            //si la lista esta vacia muestra cartel de advertencia
            if (passwords.isEmpty()) {
                noPasswordsText.setVisibility(View.VISIBLE); //NO hay contraseñas
                circleExclamation.setVisibility(View.VISIBLE); //Signo de Admiracion
                tableLayout.setVisibility(View.GONE);//oculta la tabla
            } else { //sino completa la tabla de password
                noPasswordsText.setVisibility(View.GONE);
                circleExclamation.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);

                LayoutInflater inflater = LayoutInflater.from(this);

                for (int i = 0; i < passwords.size(); i++) {
                    PasswordResponse pwd = passwords.get(i);

                    // Esto crea un nuevo tableRow para cada contraseña,el tableRow esta en row_password.xml
                    TableRow row = (TableRow) inflater.inflate(R.layout.row_password, null);

                    iconEye = row.findViewById(R.id.icon_eye);
                    iconPen = row.findViewById(R.id.icon_pen);
                    iconTrash = row.findViewById(R.id.icon_trash);

                    TextView nombreTextView = row.findViewById(R.id.textView);
                    nombreTextView.setText(pwd.getName()); //Setea el PASSWORD_NAME AL textView

                    asignarBotones(pwd.getId());
                    // Agrega el TableRow al TableLayout
                    tableLayout.addView(row);
                }

            }

    }*/



    private void MostrarPasswords(List<PasswordResponse> passwords, int userId) {
        try {
            TableLayout tableLayout = findViewById(R.id.tableLayout);
            TextView noPasswordsText = findViewById(R.id.txtNoPassword);
            ImageView circleExclamation = findViewById(R.id.imageView);
            tableLayout.removeAllViews();

            if (passwords.isEmpty()) {
                noPasswordsText.setVisibility(View.VISIBLE);
                circleExclamation.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
            } else {
                noPasswordsText.setVisibility(View.GONE);
                circleExclamation.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);

                LayoutInflater inflater = LayoutInflater.from(this);

                for (int i = 0; i < passwords.size(); i++) {
                    PasswordResponse pwd = passwords.get(i);
                    TableRow row = (TableRow) inflater.inflate(R.layout.row_password, null);

                    iconEye = row.findViewById(R.id.icon_eye);
                    iconPen = row.findViewById(R.id.icon_pen);
                    iconTrash = row.findViewById(R.id.icon_trash);

                    TextView nombreTextView = row.findViewById(R.id.textView);
                    nombreTextView.setText(pwd.getName());
                    asignarBotones(pwd.getId(), userId);

                    tableLayout.addView(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            Log.e("TAG", "ERROR: " + e.getMessage());
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        dbManager.close(); // Cierra la base de datos al destruir la actividad.
    }

    private void asignarBotones(int id, int userId) {
        iconTrash.setTag(id); // Donde 'id' es el ID de la contraseña correspondiente

        iconEye.setOnClickListener(new View.OnClickListener() {

            // onclick para abrir la actividad del ViewPassActivity
            @Override
            public void onClick(View v) {

                // Crea un intent para abrir la actividad ViewPassActivity
                Intent intent = new Intent(PasswordsActivity.this, ViewPassActivity.class);
                // Agrega el id como un extra en el intent
                intent.putExtra("idColumna", id);
                // Agrega userId como un extra en el intent
                intent.putExtra("userId", userId);
                // Inicia la actividad ViewPassActivity
                startActivity(intent);
            }
        });

        // onclick del el editar
        iconPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un intent para abrir la actividad ViewPassActivity
                Intent intent = new Intent(PasswordsActivity.this, EditarPassword.class);
                // Agrega el id como un extra en el intent
                intent.putExtra("idColumna", id);
                // Agrega userId como un extra en el intent
                intent.putExtra("userId", userId);
                // Inicia la actividad ViewPassActivity
                startActivity(intent);

            }
        });

        // Onclick Borrar
        iconTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idToDelete = (int) v.getTag();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PasswordsActivity.this);
                alertDialogBuilder.setTitle("Confirmar Eliminación");
                alertDialogBuilder.setMessage("¿Estás seguro de que deseas eliminar esta contraseña?");

                alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbManager.deletePassword(idToDelete);

                        // Actualiza la vista después de eliminar la contraseña
                        passwords = dbManager.getPasswordsListForUserId(userId);
                        MostrarPasswords(passwords, userId);


                        dialog.dismiss();
                    }
                });

                alertDialogBuilder.create().show();
            }
        });
    }


    public List<PasswordResponse> filterPasswords(List<PasswordResponse> passwords) {
        List<PasswordResponse> filterpass= new ArrayList<>();

        return filterpass;
    }
}


