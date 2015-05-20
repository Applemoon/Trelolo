package com.uvdoha.trelolo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// TODO работа со всеми таблицами
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "trelolodb";
    public static final String AUTHORITY = "tp.uvdoha.Trelolo";
    public static final int DB_VERSION = 4;

    DatabaseHelper(Context context) {
        super(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BoardsTable.CREATE_TABLE);
        db.execSQL(ListsTable.CREATE_TABLE);
        db.execSQL(CardsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
