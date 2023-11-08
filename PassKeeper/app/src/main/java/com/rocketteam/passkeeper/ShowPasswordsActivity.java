package com.rocketteam.passkeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocketteam.passkeeper.data.db.DbManager;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;
import com.rocketteam.passkeeper.util.InputTextWatcher;
import com.rocketteam.passkeeper.util.ShowAlertsUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowPasswordsActivity extends AppCompatActivity {

    FloatingActionButton btnA;
    private ImageView imageView;
    private ImageButton imageButton;
    private DbManager dbManager;
    private ScrollView scrollView;

    private ImageButton iconEye;
    private ImageButton iconPen;
    private ImageButton iconTrash;
    private List<PasswordResponse> passwords;
    private int userId;
    private List<PasswordResponse> passwordList; // lista de objetos PasswordResponse
    private TextInputEditText textSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        // Inicializa el DbManager y otros elementos de la actividad.
        dbManager = new DbManager(this);

        TableLayout tableLayout = findViewById(R.id.tableLayout); //busca el id del tablelayout
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        EditText editTextSearch = findViewById(R.id.editTextSearch);

        // Agrega el TextWatcher al EditText para filtrar contraseñas
        editTextSearch.addTextChangedListener(new InputTextWatcher(textInputLayout) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Llama al método filterPasswords para obtener la lista filtrada
                String searchText = s.toString().trim();
                List<PasswordResponse> filteredPasswords = filterPasswords(passwords, searchText);

                // Muestra las contraseñas filtradas
                showPasswords(filteredPasswords, userId);
            }
        });

        // Nombre del SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Storage", MODE_PRIVATE);

        //Comprobar si la clave "userId" existe
        if (sharedPreferences.contains("userId")) {
            // Obtener el valor de "userId" de SharedPreferences
            int userId = sharedPreferences.getInt("userId", -1);
            Log.i("TAG", "Mostrando las contraseñas del usuario Id: " + userId);
            passwords = dbManager.getPasswordsListForUserId(userId);
            if (passwords.size() > 0) {
                Log.i("TAG", "La lista tiene: " + passwords.size());
            } else {
                Log.i("TAG", "La lista llega VACIA");
            }

            showPasswords(passwords, userId);

        } else {
            // La clave "userId" no existe
            Log.e("ShowPasswordsActivity", "La clave 'userId' no existe");
        }
        MaterialButton fabAgregar = findViewById(R.id.btn_agregar);

        fabAgregar.setOnClickListener(view -> {
            Intent intent = new Intent(ShowPasswordsActivity.this, RegisterPasswordActivity.class);
            startActivity(intent);
            finish();
        });
        imageView = findViewById(R.id.menu_view);
        imageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(ShowPasswordsActivity.this, imageView);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.option_1) {
                    // Abre la actividad AboutActivity
                    Intent intent1 = new Intent(ShowPasswordsActivity.this, AboutActivity.class);
                    startActivity(intent1);
                    return true;
                } else if (itemId == R.id.option_2) {
                    this.onBackPressed();
                    return true;
                } else {
                    return false;
                }
            });

            // Mostrar el menú emergente
            popupMenu.show();
        });
        ScrollView scrollView = findViewById(R.id.scrollView);
        ImageView scrollIndicator = findViewById(R.id.scrollIndicator);
        scrollView.post(() -> {
            if (scrollView.getChildAt(0).getHeight() > scrollView.getHeight()) {
                // El contenido es más grande que la vista, muestra el indicador
                scrollIndicator.setVisibility(View.VISIBLE);
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
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
        });
    }

    private void showPasswords(List<PasswordResponse> passwords, int userId) {
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
                    // Si el texto es más largo que 10 caracteres muestra 10 caracteres y 3 puntos al final
                    if (pwd.getName().length() > 10) {
                        String shortenedText = pwd.getName().substring(0, 10) + "...";
                        nombreTextView.setText(shortenedText);
                    } else {
                        nombreTextView.setText(pwd.getName());
                    }
                    assignButtons(pwd.getId(), userId);

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
        finish();
    }

    private void assignButtons(int id, int userId) {
        iconTrash.setTag(id); // Donde 'id' es el ID de la contraseña correspondiente

        // onclick para abrir la actividad del ViewPassActivity
        iconEye.setOnClickListener(v -> {

            // Crea un intent para abrir la actividad ViewPassActivity
            Intent intent = new Intent(ShowPasswordsActivity.this, ViewPassActivity.class);
            // Agrega el id como un extra en el intent
            intent.putExtra("idColumna", id);
            // Agrega userId como un extra en el intent
            intent.putExtra("userId", userId);
            // Inicia la actividad ViewPassActivity
            startActivity(intent);
            finish();
        });

        // onclick del el editar
        iconPen.setOnClickListener(v -> {
            // Crea un intent para abrir la actividad ViewPassActivity
            Intent intent = new Intent(ShowPasswordsActivity.this, EditPasswordActivity.class);
            // Agrega el id como un extra en el intent
            intent.putExtra("idColumna", id);
            // Agrega userId como un extra en el intent
            intent.putExtra("userId", userId);
            // Inicia la actividad ViewPassActivity
            startActivity(intent);
            finish();
        });

        // Onclick Borrar
        iconTrash.setOnClickListener(v -> {
            int idToDelete = (int) v.getTag();

            // Muestra la alerta para el borrado de la contraseña.
            ShowAlertsUtility.mostrarSweetAlertDeletePassword(ShowPasswordsActivity.this, 3, "¿Estás seguro de que deseas eliminar esta contraseña?", "Si borras la contraseña, ya no podrás revertirlo",
                    sweetAlertDialog -> {
                        // Acción de confirmación (Aceptar)
                        try {
                            dbManager.deletePassword(idToDelete);
                            passwords = dbManager.getPasswordsListForUserId(userId);
                            showPasswords(passwords, userId);

                            // Muestra una segunda alerta de confirmación (Tipo 2) después de eliminar la contraseña
                            ShowAlertsUtility.mostrarSweetAlert(ShowPasswordsActivity.this, 2, "Operación Completada", "La contraseña se ha eliminado correctamente", SweetAlertDialog::dismissWithAnimation);
                        } catch (Exception e) {
                            Log.e("Error al borrar contraseña", Objects.requireNonNull(e.getMessage()));
                            ShowAlertsUtility.mostrarSweetAlert(ShowPasswordsActivity.this, 1, "Error", "Se ha producido un error", SweetAlertDialog::dismissWithAnimation);
                        }
                    },
                    sweetAlertDialog -> {
                        // Acción de cancelación (Cancelar)
                        ShowAlertsUtility.mostrarSweetAlert(ShowPasswordsActivity.this, 1, "Operación Cancelada", "Se ha cancelado la operación", SweetAlertDialog::dismissWithAnimation);
                    }
            );
        });

    }

    public List<PasswordResponse> filterPasswords(List<PasswordResponse> passwords, String
            word) {
        List<PasswordResponse> filterpass = new ArrayList<>();

        for (PasswordResponse password : passwords) {
            // Cambia a minúsculas tanto el nombre de la contraseña como la palabra buscada
            String passwordName = password.getName().toLowerCase();
            String searchWord = word.toLowerCase();

            // Verifica si el nombre de la contraseña contiene la palabra buscada
            if (passwordName.contains(searchWord)) {
                filterpass.add(password); // Agrega el objeto coincidente a la lista filterpass
            }
        }
        return filterpass;
    }

    /**
     * Método llamado cuando se presiona el botón de retroceso del dispositivo.
     * Este método reemplaza el comportamiento predeterminado del botón de retroceso,
     * redirigiendo al usuario desde la actividad actual ({@code ShowPasswordsActivity})
     * a la actividad principal ({@code MainActivity}) de la aplicación.
     * Una vez que la redirección se ha completado, la actividad actual es finalizada
     * y eliminada de la pila de actividades para mantener una estructura de navegación coherente.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowPasswordsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}


