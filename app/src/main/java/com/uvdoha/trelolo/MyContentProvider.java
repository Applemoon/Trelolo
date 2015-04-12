package com.uvdoha.trelolo;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        // TODO
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO
        return null;
    }

    @Override
    public String getType(Uri uri) {
        // TODO
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO
        return 0;
    }
}
