package com.rocketteam.passkeeper.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rocketteam.passkeeper.RegisterUserActivity;

/**
 * Clase que gestiona la conexión y la creación de la base de datos SQLite para PassKeeper.
 */
public class DbConnection extends SQLiteOpenHelper {
    // Nombre de la base de datos y versión
    private static final String DB_NAME = "passkeeper.db";
    private static final int DB_VERSION = 1;

    /**
     * Constructor de la clase.
     *
     * @param context Contexto de la aplicación.
     */
    public DbConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

   public DbConnection(RegisterUserActivity registerUserActivity) {
        super(registerUserActivity, DB_NAME, null, DB_VERSION);
   }


    /**
     * Método llamado cuando se crea la base de datos por primera vez.
     *
     * @param sqLiteDatabase Base de datos SQLite.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d("DbConnection", "onCreate called");
            Log.d("DbConnection", "Creating PASSWORD table...");
            sqLiteDatabase.execSQL(DbManager.CREATE_PASSWORD_TABLE);
            Log.d("DbConnection", "Creating USER table...");
            sqLiteDatabase.execSQL(DbManager.CREATE_USER_TABLE);
            Log.d("DbConnection", "Tables created successfully.");
        } catch (SQLException e) {
            Log.e("DbConnection", "Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Método llamado cuando se actualiza la base de datos.
     *
     * @param sqLiteDatabase Base de datos SQLite.
     * @param i Versión antigua de la base de datos.
     * @param i1 Versión nueva de la base de datos.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_PASSWORD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_USER);
        onCreate(sqLiteDatabase);
    }

}
