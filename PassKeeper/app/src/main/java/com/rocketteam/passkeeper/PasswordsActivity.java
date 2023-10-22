package com.rocketteam.passkeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rocketteam.passkeeper.data.db.DbManager;

import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PasswordsActivity extends AppCompatActivity {

    FloatingActionButton btnA;
    private ImageView imageView;
    private ImageButton imageButton;
    private DbManager dbManager;
    private TableLayout tableLayout;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);
        // Inicializa el DbManager y otros elementos de la actividad.
        dbManager = new DbManager(this);
        dbManager.open();
        tableLayout = findViewById(R.id.tableLayout); //busca el id del tablelayout

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fabAgregar = findViewById(R.id.btn_agregar);

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PasswordsActivity.this, RegisterPasswordActivity.class);
                startActivity(intent);
            }
        });
        MostrarPasswords(1);
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

    private void MostrarPasswords(int userId) {
        Cursor cursor = dbManager.getPasswordsForUser(userId);
        // Definimos un cursor y vamos a la función en el DbManajer y le pasamos un parametro de tipo int userId, osea el usuario logueado
        TableLayout tableLayout = findViewById(R.id.tableLayout);//Obtenemos el tableLayout
        TextView noPasswordsText = findViewById(R.id.txtNoPassword);//Obtenemos el textView
        ImageView circleExclamation = findViewById(R.id.imageView);//Obtenemos el imageView

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                // Verificamos si el numero de filas en el cursor es 0
                // Si no hay filas en el cursor, muestra el texto y oculta el TableLayout
                noPasswordsText.setVisibility(View.VISIBLE);
                circleExclamation.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.GONE);
            } else {
                // Si hay filas en el cursor, muestra el TableLayout y oculta el texto
                noPasswordsText.setVisibility(View.GONE);
                circleExclamation.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);

                LayoutInflater inflater = LayoutInflater.from(this);

                while (cursor.moveToNext()) {
                    // Esto crea un nuevo tableRow para cada contraseña,el tableRow esta en row_password.xml
                    TableRow row = (TableRow) inflater.inflate(R.layout.row_password, null);

                    ImageButton iconEye = row.findViewById(R.id.icon_eye);
                    ImageButton iconPen = row.findViewById(R.id.icon_pen);
                    ImageButton iconTrash = row.findViewById(R.id.icon_trash);

                    try {
                        String columnId = DbManager.PASSWORD_ID;
                        String columnName = DbManager.PASSWORD_NAME;
                        int columnIndexName = cursor.getColumnIndex(columnName);
                        int columnIndexId = cursor.getColumnIndex(columnId);

                        // verifica que las columnas de password y name existen en el resultado del cursor
                        if (columnIndexId != -1 && columnIndexName != -1) {
                            TextView nombreTextView = row.findViewById(R.id.textView);
                            nombreTextView.setText(cursor.getString(columnIndexName)); //Setea el PASSWORD_NAME AL textView
                            int getId = cursor.getInt(columnIndexId);
                            iconEye.setOnClickListener(new View.OnClickListener() {
                                //onclick para abrir la actividad del ViewPassActivity
                                @Override
                                public void onClick(View v) {

                                    // Crea un intent para abrir la actividad ViewPassActivity

                                    Intent intent = new Intent(PasswordsActivity.this, ViewPassActivity.class);
                                    // Agrega el id como un extra en el intent
                                    intent.putExtra("idColumna", getId);
                                    // utlizamos el putExtra para pasar información con el intent

                                    // Inicia la actividad ViewPassActivity
                                    startActivity(intent);
                                }
                            });

                            // onclick del el editar
                            iconPen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int idColumnaSeleccionada = cursor.getInt(columnIndexId);

                                    // Crea un intent para abrir la actividad ViewPassActivity

                                    Intent intent = new Intent(PasswordsActivity.this, EditarPassword.class);
                                    // Agrega el id como un extra en el intent
                                    intent.putExtra("idColumna", getId);
                                    // utlizamos el putExtra para pasar información con el intent

                                    // Inicia la actividad ViewPassActivity
                                    startActivity(intent);

                                }
                            });
                            // onclick para borrar un password
                            iconTrash.setOnClickListener(new View.OnClickListener() {
                                //onclick borrar
                                @Override
                                public void onClick(View v) {

                                }
                            });

                        } else {
                            Log.e("PasswordActivity", "Column not found: " + columnName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Agrega el TableRow al TableLayout
                    tableLayout.addView(row);
                }
            }

            cursor.close();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        dbManager.close(); // Cierra la base de datos al destruir la actividad.
    }

}
