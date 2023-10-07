package com.rocketteam.passkeeper.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rocketteam.passkeeper.RegisterActivity;


public class DbConnection extends SQLiteOpenHelper {

    private static final String DB_NAME = "passkeeper.db";
    private static final int DB_VERSION = 1;

    public DbConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

   public DbConnection(RegisterActivity registerActivity) {
        super(registerActivity, DB_NAME, null, DB_VERSION);
   }


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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_PASSWORD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_USER);
        onCreate(sqLiteDatabase);
    }

}
