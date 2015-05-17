package com.uvdoha.trelolo.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    private final UriMatcher mUriMatcher;
    private final HashMap<String, String> mNotesProjectionMap;
    private DatabaseHelper mOpenHelper;
    private static final int BOARDS_MAIN = 1;
    private static final int BOARDS_MAIN_ID = 2;
    private static final int LISTS_MAIN = 3;
    private static final int LISTS_MAIN_ID = 4;
    private static final int CARDS_MAIN = 5;
    private static final int CARDS_MAIN_ID = 6;

    public MyContentProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, BoardsTable.TABLE_NAME, BOARDS_MAIN);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, BoardsTable.TABLE_NAME + "/#", BOARDS_MAIN_ID);

        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, ListsTable.TABLE_NAME, LISTS_MAIN);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, ListsTable.TABLE_NAME + "/#", LISTS_MAIN_ID);

        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardsTable.TABLE_NAME, CARDS_MAIN);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardsTable.TABLE_NAME + "/#", CARDS_MAIN_ID);

        mNotesProjectionMap = new HashMap<>();
        mNotesProjectionMap.put(BoardsTable._ID, BoardsTable._ID);
        mNotesProjectionMap.put(BoardsTable.COLUMN_NAME, BoardsTable.COLUMN_NAME);

        mNotesProjectionMap.put(ListsTable._ID, ListsTable._ID);
        mNotesProjectionMap.put(ListsTable.COLUMN_NAME, ListsTable.COLUMN_NAME);

        mNotesProjectionMap.put(CardsTable._ID, CardsTable._ID);
        mNotesProjectionMap.put(CardsTable.COLUMN_NAME, CardsTable.COLUMN_NAME);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                queryBuilder.setTables(BoardsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BoardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case BOARDS_MAIN_ID:
                queryBuilder.setTables(BoardsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                queryBuilder.appendWhere(BoardsTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{ uri.getLastPathSegment() });
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BoardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case LISTS_MAIN:
                queryBuilder.setTables(ListsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ListsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case LISTS_MAIN_ID:
                queryBuilder.setTables(ListsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                queryBuilder.appendWhere(ListsTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{ uri.getLastPathSegment() });
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ListsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case CARDS_MAIN:
                queryBuilder.setTables(CardsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case CARDS_MAIN_ID:
                queryBuilder.setTables(CardsTable.TABLE_NAME);
                queryBuilder.setProjectionMap(mNotesProjectionMap);
                queryBuilder.appendWhere(CardsTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{ uri.getLastPathSegment() });
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                return BoardsTable.CONTENT_TYPE;
            case BOARDS_MAIN_ID:
                return BoardsTable.CONTENT_ITEM_TYPE;
            case LISTS_MAIN:
                return ListsTable.CONTENT_TYPE;
            case LISTS_MAIN_ID:
                return ListsTable.CONTENT_ITEM_TYPE;
            case CARDS_MAIN:
                return CardsTable.CONTENT_TYPE;
            case CARDS_MAIN_ID:
                return CardsTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (mUriMatcher.match(uri) != BOARDS_MAIN &&
            mUriMatcher.match(uri) != LISTS_MAIN &&
            mUriMatcher.match(uri) != CARDS_MAIN) {
            // Can only insert into to main URI.
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;

        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        if (!values.containsKey(BoardsTable.COLUMN_NAME)) {
            values.put(BoardsTable.COLUMN_NAME, "");
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId;
        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                rowId = db.insert(BoardsTable.TABLE_NAME, null, values);
                break;
            case LISTS_MAIN:
                rowId = db.insert(ListsTable.TABLE_NAME, null, values);
                break;
            case CARDS_MAIN:
                rowId = db.insert(CardsTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            Uri noteUri;
            switch (mUriMatcher.match(uri)) {
                case BOARDS_MAIN:
                    noteUri = ContentUris.withAppendedId(BoardsTable.CONTENT_ID_URI_BASE, rowId);
                    break;
                case LISTS_MAIN:
                    noteUri = ContentUris.withAppendedId(ListsTable.CONTENT_ID_URI_BASE, rowId);
                    break;
                case CARDS_MAIN:
                    noteUri = ContentUris.withAppendedId(CardsTable.CONTENT_ID_URI_BASE, rowId);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                count = db.delete(BoardsTable.TABLE_NAME, selection, selectionArgs);
                break;

            case BOARDS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        BoardsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.delete(BoardsTable.TABLE_NAME, finalWhere, selectionArgs);
                break;

            case LISTS_MAIN:
                count = db.delete(ListsTable.TABLE_NAME, selection, selectionArgs);
                break;

            case LISTS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        ListsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.delete(ListsTable.TABLE_NAME, finalWhere, selectionArgs);
                break;

            case CARDS_MAIN:
                count = db.delete(CardsTable.TABLE_NAME, selection, selectionArgs);
                break;

            case CARDS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        CardsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.delete(CardsTable.TABLE_NAME, finalWhere, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                count = db.update(BoardsTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case BOARDS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        BoardsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.update(BoardsTable.TABLE_NAME, values, finalWhere, selectionArgs);
                break;

            case LISTS_MAIN:
                count = db.update(ListsTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case LISTS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        ListsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.update(ListsTable.TABLE_NAME, values, finalWhere, selectionArgs);
                break;

            case CARDS_MAIN:
                count = db.update(CardsTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case CARDS_MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        CardsTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.update(CardsTable.TABLE_NAME, values, finalWhere, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
