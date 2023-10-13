package com.rocketteam.passkeeper.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rocketteam.passkeeper.data.model.request.UserCredentials;
import com.rocketteam.passkeeper.util.HashUtility;

public class DbManager {
    public static final String TB_PASSWORD = "password";
    public static final String PASSWORD_ID = "id";
    public static final String PASSWORD_USERNAME = "username";
    public static final String PASSWORD_URL = "url";
    public static final String PASSWORD_KEYWORD = "keyword";
    public static final String PASSWORD_DESCRIPTION = "description";
    public static final String PASSWORD_CATEGORY = "category";
    public static final String CREATE_PASSWORD_TABLE = "CREATE TABLE password( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, "+
            "url TEXT, "+
            "keyword TEXT NOT NULL, "+
            "description TEXT, "+
            "category TEXT, "+
            "user_id INTEGER, "+
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), "+
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE)";

    public static final String TB_USER = "user";
    public static final String ID_USER = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String SALT = "salt"; // Nueva columna para almacenar el salt
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT UNIQUE, " +
            "password TEXT, " +
            "salt TEXT, " +
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')) " +
            ")";

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
            Log.i("DbManager", "newRowId: "+newRowId);
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



}
