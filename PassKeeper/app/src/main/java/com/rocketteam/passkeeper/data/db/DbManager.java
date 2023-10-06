package com.rocketteam.passkeeper.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;

public class DbManager {

    public static final String TB_PASSWORD = "password";
    public static final String PASSWORD_ID = "id";
    public static final String PASSWORD_USERNAME = "username";
    public static final String PASSWORD_URL = "url";
    public static final String PASSWORD_KEYWORD = "keyword";
    public static final String PASSWORD_DESCRIPTION = "description";
    public static final String PASSWORD_CATEGORY = "category";
    public static final String CREATE_TABLE = "CREATE TABLE password( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, "+
            "url TEXT, "+
            "keyword TEXT NOT NULL, "+
            "description TEXT, "+
            "category TEXT, "+
            "created_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime'), "+
            "updated_at TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime') )";

    private DbConnection connection;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        this.connection = new DbConnection(context);
    }

    public DbManager open() throws SQLException{
        db = connection.getWritableDatabase();
        return this;
    }

    public void close(){
        connection.close();
    }


}
