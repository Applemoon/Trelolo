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
    public static final String AUTHORITY = "tp.uvdoha.Trelolo";
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
        mUriMatcher.addURI(AUTHORITY, BoardsTable.TABLE_NAME, BOARDS_MAIN);
        mUriMatcher.addURI(AUTHORITY, BoardsTable.TABLE_NAME + "/#", BOARDS_MAIN_ID);

        mUriMatcher.addURI(AUTHORITY, ListsTable.TABLE_NAME, LISTS_MAIN);
        mUriMatcher.addURI(AUTHORITY, ListsTable.TABLE_NAME + "/#", LISTS_MAIN_ID);

        mUriMatcher.addURI(AUTHORITY, CardsTable.TABLE_NAME, CARDS_MAIN);
        mUriMatcher.addURI(AUTHORITY, CardsTable.TABLE_NAME + "/#", CARDS_MAIN_ID);

        mNotesProjectionMap = new HashMap<>();
        mNotesProjectionMap.put(BoardsTable._ID, BoardsTable._ID);
        mNotesProjectionMap.put(BoardsTable.COLUMN_NAME, BoardsTable.COLUMN_NAME);
        mNotesProjectionMap.put(BoardsTable.COLUMN_CLOSED, BoardsTable.COLUMN_CLOSED);

        mNotesProjectionMap.put(ListsTable._ID, ListsTable._ID);
        mNotesProjectionMap.put(ListsTable.COLUMN_NAME, ListsTable.COLUMN_NAME);
        mNotesProjectionMap.put(ListsTable.COLUMN_BOARD_ID, ListsTable.COLUMN_BOARD_ID);
        mNotesProjectionMap.put(ListsTable.COLUMN_CLOSED, ListsTable.COLUMN_CLOSED);

        mNotesProjectionMap.put(CardsTable._ID, CardsTable._ID);
        mNotesProjectionMap.put(CardsTable.COLUMN_NAME, CardsTable.COLUMN_NAME);
        mNotesProjectionMap.put(CardsTable.COLUMN_LIST_ID, CardsTable.COLUMN_LIST_ID);
        mNotesProjectionMap.put(CardsTable.COLUMN_DESCRIPTION, CardsTable.COLUMN_DESCRIPTION);
        mNotesProjectionMap.put(CardsTable.COLUMN_CLOSED, CardsTable.COLUMN_CLOSED);
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
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BoardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case BOARDS_MAIN_ID:
                queryBuilder.setTables(BoardsTable.TABLE_NAME);
                queryBuilder.appendWhere(BoardsTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{ uri.getLastPathSegment() });
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BoardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case LISTS_MAIN:
                queryBuilder.setTables(ListsTable.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ListsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case LISTS_MAIN_ID:
                queryBuilder.setTables(ListsTable.TABLE_NAME);
                queryBuilder.appendWhere(ListsTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{ uri.getLastPathSegment() });
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ListsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case CARDS_MAIN:
                queryBuilder.setTables(CardsTable.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CardsTable.DEFAULT_SORT_ORDER;
                }
                break;

            case CARDS_MAIN_ID:
                queryBuilder.setTables(CardsTable.TABLE_NAME);
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

        queryBuilder.setProjectionMap(mNotesProjectionMap);
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

        // Проверка обязательных полей
        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                if (!values.containsKey(BoardsTable.COLUMN_NAME)) {
                    throw new IllegalArgumentException("No " + BoardsTable.COLUMN_NAME + " key");
                }
                break;
            case LISTS_MAIN:
                if (!values.containsKey(ListsTable.COLUMN_NAME)) {
                    throw new IllegalArgumentException("No " + ListsTable.COLUMN_NAME + " key");
                } else if (!values.containsKey(ListsTable.COLUMN_BOARD_ID)) {
                    throw new IllegalArgumentException("No " + ListsTable.COLUMN_BOARD_ID + " key");
                }
                break;
            case CARDS_MAIN:
                if (!values.containsKey(CardsTable.COLUMN_NAME)) {
                    throw new IllegalArgumentException("No " + CardsTable.COLUMN_NAME + " key");
                } else if (!values.containsKey(CardsTable.COLUMN_LIST_ID)) {
                    throw new IllegalArgumentException("No " + CardsTable.COLUMN_LIST_ID + " key");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String tableName;
        switch (mUriMatcher.match(uri)) {
            case BOARDS_MAIN:
                tableName = BoardsTable.TABLE_NAME;
                break;
            case LISTS_MAIN:
                tableName = ListsTable.TABLE_NAME;
                break;
            case CARDS_MAIN:
                tableName = CardsTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        final long rowId = db.insert(tableName, null, values);

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            Uri contentIdUri;
            switch (mUriMatcher.match(uri)) {
                case BOARDS_MAIN:
                    contentIdUri = BoardsTable.CONTENT_ID_URI_BASE;
                    break;
                case LISTS_MAIN:
                    contentIdUri = ListsTable.CONTENT_ID_URI_BASE;
                    break;
                case CARDS_MAIN:
                    contentIdUri = CardsTable.CONTENT_ID_URI_BASE;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            final Uri noteUri = ContentUris.withAppendedId(contentIdUri, rowId);
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
