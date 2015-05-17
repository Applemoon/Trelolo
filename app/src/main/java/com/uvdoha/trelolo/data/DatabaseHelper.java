package com.uvdoha.trelolo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TODO работа со всеми таблицами
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getName();
    public static final String DB_NAME = "trelolodb";
    public static final String AUTHORITY = "tp.uvdoha.Trelolo";
    public static final int DB_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BoardsTable.CREATE_TABLE);

        // TODO временно, убрать
        ContentValues cv = new ContentValues();
        for (int i = 1; i < 5; i++) {
            cv.put(BoardsTable.COLUMN_NAME, "Доска " + i);
            db.insert(BoardsTable.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + BoardsTable.TABLE_NAME);
        onCreate(db);
    }
}
