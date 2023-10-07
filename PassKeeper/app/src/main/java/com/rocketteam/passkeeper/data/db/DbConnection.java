package com.rocketteam.passkeeper.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import com.rocketteam.passkeeper.RegisterActivity;


public class DbConnection extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_pwd";
    private static final int DB_VERSION = 1;

    public DbConnection(View.OnClickListener context) {

        super((Context) context,DB_NAME, null, DB_VERSION);
    }

   public DbConnection(RegisterActivity registerActivity) {
        super(registerActivity, "passkeeper.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbManager.CREATE_PASSWORD_TABLE);
        sqLiteDatabase.execSQL(DbManager.CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_PASSWORD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DbManager.TB_USER);
    }

}
