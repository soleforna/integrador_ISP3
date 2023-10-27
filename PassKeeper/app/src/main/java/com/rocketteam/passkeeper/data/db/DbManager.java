package com.rocketteam.passkeeper.data.db;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.data.model.response.PasswordResponse;
import com.rocketteam.passkeeper.data.model.response.UserResponse;
import com.rocketteam.passkeeper.util.HashUtility;

import java.util.List;
import java.util.ArrayList;

public class DbManager {
    public static final String TB_PASSWORD = "password";
    //public static final String PASSWORD_ID = "id";
    public static final String PASSWORD_USERNAME = "username";
    public static final String PASSWORD_URL = "url";
    public static final String PASSWORD_KEYWORD = "keyword";
    public static final String PASSWORD_DESCRIPTION = "description";
    public static final String PASSWORD_USER = "user_id";
    public static final String PASSWORD_NAME = "name";
    public static final String CREATE_PASSWORD_TABLE = "CREATE TABLE password( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, " +
            "url TEXT, " +
            "keyword TEXT NOT NULL, " +
            "description TEXT, " +
            "name TEXT, " +
            "user_id INTEGER, " +
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE)";

    public static final String TB_USER = "user";
    //public static final String ID_USER = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String SALT = "salt"; // Columna para almacenar el salt
    public static final String BIO = "biometric"; // Columna para almacenar la opcion biometrica
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT UNIQUE, " +
            "password TEXT, " +
            "salt TEXT, " +
            "biometric INTEGER DEFAULT 0,"+
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')) " +
            ")";

    private final DbConnection connection;
    private SQLiteDatabase db;
    private final SharedPreferences sharedPreferences;

    public DbManager(Context context) {
        this.connection = new DbConnection(context);
        this.sharedPreferences = context.getSharedPreferences("Storage", Context.MODE_PRIVATE);
    }

    public DbManager open() throws SQLException {
        db = connection.getWritableDatabase();
        return this;
    }

    public void close() {
        connection.close();
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Objeto UserCredentials que contiene la información del usuario.
     * @param bio  Valor Entero que representa la selección de utilizar Huella del usuario
     * @return true si el registro es exitoso, false si hay un error.
     * @throws HashUtility.HashingException Si ocurre un error durante el hashing de la contraseña.
     * @throws HashUtility.SaltException    Si ocurre un error durante la generación del salt.
     */
    public boolean userRegister(UserCredentials user, int bio) throws HashUtility.HashingException, HashUtility.SaltException {
        try {
            // Generar un salt aleatorio
            String salt = HashUtility.generateSalt();
            // Hashear la contraseña con el salt generado
            String hashedPassword = HashUtility.hashPassword(user.getPassword(), salt);

            ContentValues content = new ContentValues();
            content.put(EMAIL, user.getEmail());
            content.put(PASSWORD, hashedPassword); // Guardar el hash en la base de datos
            content.put(SALT, salt); // Guardar el salt en la base de datos
            content.put(BIO, bio); //guardar la seleccion biometrica

            // Evitar el conflicto de email duplicado y no realizar el registro, pero devolver -1
            long newRowId = db.insertWithOnConflict(TB_USER, null, content, SQLiteDatabase.CONFLICT_IGNORE);
            // Si newRowId es -1, indica que hubo un conflicto y no se pudo insertar el nuevo usuario
            return newRowId != -1;
        } catch (HashUtility.SaltException e) {
            // Manejar la excepción de generación de salt
            Log.e("Error", "Salt generation error: " + e.getMessage());
            throw e; // Re-lanzar la excepción para que sea manejada en un nivel superior si es necesario
        } catch (HashUtility.HashingException e) {
            // Manejar la excepción de hashing de contraseña
            Log.e("Error", "Hashing password error: " + e.getMessage());
            throw e; // Re-lanzar la excepción para que sea manejada en un nivel superior si es necesario
        }
    }

    //Método que se utiliza para obtener el salt del usuario según el userId
    public String getSaltById(int userId) {
        String salt = null;
        Cursor cursor = null;

        try {
            String query = "SELECT salt FROM user WHERE id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            int saltIndex = cursor.getColumnIndex("salt");
            if (saltIndex != -1 && cursor.moveToFirst()) {
                salt = cursor.getString(saltIndex);
            } else {
                // La columna "salt" no existe en el conjunto de resultados o el cursor está vacío
                Log.e("Error", "No se pudo encontrar la columna 'salt'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return salt;
    }
    /**
     * Registra una nueva contraseña en la base de datos.
     *
     * @param psw La información de la contraseña a registrar.
     * @return true si la contraseña se registró correctamente, false si ocurrió un error.
     */
    public boolean passwordRegister(PasswordCredentials psw) throws HashUtility.HashingException {

        if (psw == null) {
            throw new IllegalArgumentException("PasswordCredentials no puede ser null");
        }

        try {
            //Obtengo el Salt para hashear la contraseña
            String salt = getSaltById(psw.getUserId());
            if (salt != null) {

                ContentValues content = new ContentValues();
                content.put(PASSWORD_USER, psw.getUserId());
                content.put(PASSWORD_NAME, psw.getName());
                content.put(PASSWORD_USERNAME, psw.getUser());
                content.put(PASSWORD_URL, psw.getUrl());
                content.put(PASSWORD_DESCRIPTION, psw.getDescription());
                String hashedPassword = HashUtility.hashPassword(psw.getPassword(), salt);
                content.put(PASSWORD_KEYWORD, hashedPassword); // Guardar la contraseña hasheada

                long newRowId = db.insert(TB_PASSWORD, null, content);
                return newRowId != -1;
            } else {
                // El usuario con el ID especificado no existe
                Log.e("Error", "No existe el usuario");
                return false;
            }
        } catch (SQLException e) {
            Log.e("Error", "Error de SQL al registrar la contraseña: " + e.getMessage());
            throw e;
        } catch (HashUtility.HashingException e) {
            Log.e("Error", "Error al hashear la contraseña: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            Log.e("Error", "Password registration error: " + e.getMessage());
            throw e;
        }finally {
            db.close();
        }
    }

    /**
     * Valida las credenciales del usuario.
     *
     * @param pwd   Contraseña ingresada por el usuario.
     * @param email Correo electrónico ingresado por el usuario.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validateUser(String pwd, String email) throws HashUtility.HashingException {
        // Obtener el usuario por correo electrónico
        UserResponse user = this.getUserByEmail(email);

        if (user != null && HashUtility.checkPassword(pwd, user.getPassword(), user.getSalt())) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt("userId", user.getId());  // userId es el ID del usuario autenticado
            editor.putInt("biometric", user.getBiometric());
            editor.apply();
            Log.i("TAG", "userID guardado en DbManayer: "+sharedPreferences.getInt("userId", -1));
            Log.i("TAG", "biometric guardado en DbManayer: "+sharedPreferences.getInt("biometric",-1));
            return true; // Las credenciales son válidas
        }
        return false; // Las credenciales son inválidas
    }

    /**
     * Obtiene un usuario por su dirección de correo electrónico desde la base de datos.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return Objeto UserResponse si se encuentra el usuario, o null si no se encuentra.
     */
    public UserResponse getUserByEmail(String email) {
        UserResponse user = null;

        try {
            // Define la consulta SQL para seleccionar el usuario por email
            String query = "SELECT * FROM user WHERE email = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});
            int idIndex = cursor.getColumnIndex("id");
            int emailIndex = cursor.getColumnIndex("email");
            int pwdIndex = cursor.getColumnIndex("password");
            int salIndex = cursor.getColumnIndex("salt");
            int bioIndex = cursor.getColumnIndex("biometric");

            // Verificar si se encontró el usuario en la base de datos
            if (emailIndex != -1 && cursor.moveToFirst()) {
                // Obtener los datos del usuario desde el cursor
                int id = cursor.getInt(idIndex);
                String userEmail = cursor.getString(emailIndex);
                String password = cursor.getString(pwdIndex);
                String sal = cursor.getString(salIndex);
                int biometric = cursor.getInt(bioIndex);

                // Crear un nuevo objeto UserResponse con los datos obtenidos
                user = new UserResponse(id, userEmail, password, sal, biometric);
            }
            // Cerrar el cursor y la base de datos
            cursor.close();
            db.close();

        } catch (Exception e) {
            Log.e("TAG", "Error al obtener el usuario por email", e);
            e.printStackTrace();
        }

        // Devolver el usuario encontrado (o null si no se encontró)
        return user;
    }

    public Cursor getPasswordsForUser(int userId) {
        String[] columns = {PASSWORD_USER,PASSWORD_NAME};
        String selection = PASSWORD_USER + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        return getDb().query(TB_PASSWORD, columns, selection, selectionArgs, null, null, null);
    }
    /* Pasa a lista, las contraseñas obtenidas para un usuario especifico en getPasswordForUser */

    public List<PasswordResponse> getPasswordsListForUserId(int userId) {
        List<PasswordResponse> passwords = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = getPasswordsForUser(userId);
            int idIndex= cursor.getColumnIndex( "id");
            int nameIndex =cursor.getColumnIndex(PASSWORD_NAME);
            int userIndex =cursor.getColumnIndex(PASSWORD_USERNAME);
            int keywordIndex =cursor.getColumnIndex(PASSWORD_KEYWORD);
            int urlIndex =cursor.getColumnIndex(PASSWORD_URL);
            int descriptionIndex =cursor.getColumnIndex(PASSWORD_DESCRIPTION);


            if (nameIndex != -1) {
                while (cursor.moveToNext()) {
                    int id= cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String user = cursor.getString(userIndex);
                    String keyword = cursor.getString(keywordIndex);
                    String url = cursor.getString(urlIndex);
                    String description = cursor.getString(descriptionIndex);

                    PasswordResponse passwordResponse = new PasswordResponse(id,name, user,keyword,url,description);
                    passwords.add(passwordResponse);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "Error al crear lista de contraseñas", e);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return passwords;
    }





    public SQLiteDatabase getDb() {
        return db;
    }


}
