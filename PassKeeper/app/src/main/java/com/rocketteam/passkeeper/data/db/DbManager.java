package com.rocketteam.passkeeper.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.time.LocalDateTime;

public class DbManager {
//------------------------------------------Table Password---------------------------------------------------------------------------------------------
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
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), "+
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')))";

    //------------------------------------------End Table Password------------------------------------------------------------------------------------


    //----------------------------------------- Table User--------------------------------------------------------------------------------------------
    public static final String TB_USER="user";
    public static final String ID_USER="id";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    // Creamos la tabla user tabla que este definida en DbConnection.java
public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user ( " +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "email TEXT, " +
        "password TEXT, " +
        "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')), " +
        "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')) " +
        ")";

//---------------------------------------------End Table User-----------------------------------------------------------------------------------------

    private DbConnection connection;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        this.connection = new DbConnection((View.OnClickListener) context);
    }

    public DbManager open() throws SQLException{
        db = connection.getWritableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }


}
