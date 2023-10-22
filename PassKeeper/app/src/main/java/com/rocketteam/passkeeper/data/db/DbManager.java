package com.rocketteam.passkeeper.data.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rocketteam.passkeeper.data.model.request.PasswordCredentials;
import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.data.model.response.UserResponse;
import com.rocketteam.passkeeper.util.HashUtility;


public class DbManager {
    public static final String TB_PASSWORD = "password";
    public static final String PASSWORD_ID = "id";
    public static final String PASSWORD_USERNAME = "username";
    public static final String PASSWORD_URL = "url";
    public static final String PASSWORD_KEYWORD = "keyword";
    public static final String PASSWORD_DESCRIPTION = "description";
    public static final String PASSWORD_USER = "user_id";
    public static final String PASSWORD_NAME = "name";
    public static final String CREATE_PASSWORD_TABLE = "CREATE TABLE password( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, "+
            "url TEXT, "+
            "keyword TEXT NOT NULL, "+
            "description TEXT, "+
            "name TEXT, "+
            "user_id INTEGER, "+
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), "+
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE)";

    public static final String TB_USER = "user";
    public static final String ID_USER = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String SALT = "salt"; // Nueva columna para almacenar el salt
    public static final string biometric ="biometric";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT UNIQUE, " +
            "password TEXT, " +
            "salt TEXT, " +
            "biometric INTEGER DEFAULT 0,"+
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')) " +
            ")";

    private SharedPreferences sharedPreferences;
    private DbConnection connection;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        this.connection = new DbConnection(context);
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
     * @return true si el registro es exitoso, false si hay un error.
     * @throws HashUtility.HashingException Si ocurre un error durante el hashing de la contraseña.
     * @throws HashUtility.SaltException    Si ocurre un error durante la generación del salt.
     */
    public boolean userRegister(UserCredentials user) throws HashUtility.HashingException, HashUtility.SaltException {
        try {
            // Generar un salt aleatorio
            String salt = HashUtility.generateSalt();
            // Hashear la contraseña con el salt generado
            String hashedPassword = HashUtility.hashPassword(user.getPassword(), salt);

            ContentValues content = new ContentValues();
            content.put(EMAIL, user.getEmail());
            content.put(PASSWORD, hashedPassword); // Guardar el hash en la base de datos
            content.put(SALT, salt); // Guardar el salt en la base de datos

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
    public boolean passwordRegister(PasswordCredentials psw) {

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
        } catch (Exception e) {
            Log.e("Error", "Password registration error: " + e.getMessage());
            return false;
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

    public DbManager(Context context) {
        this.connection = new DbConnection(context);
        this.sharedPreferences = context.getSharedPreferences("Storage", Context.MODE_PRIVATE);
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

            // Verificar si se encontró el usuario en la base de datos
            if (emailIndex != -1 && cursor.moveToFirst()) {
                // Obtener los datos del usuario desde el cursor
                int id = cursor.getInt(idIndex);
                String userEmail = cursor.getString(emailIndex);
                String password = cursor.getString(pwdIndex);
                String sal = cursor.getString(salIndex);

                // Crear un nuevo objeto UserResponse con los datos obtenidos
                user = new UserResponse(id, userEmail, password, sal);
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


}
