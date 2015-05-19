package com.uvdoha.trelolo.data;


import android.net.Uri;
import android.provider.BaseColumns;


public class BoardsTable implements BaseColumns {
    private BoardsTable() {}

    public static final String TABLE_NAME = "boards";
    public static final Uri CONTENT_URI =  Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + TABLE_NAME);
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + TABLE_NAME + "/");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + MyContentProvider.AUTHORITY + "." + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + MyContentProvider.AUTHORITY + "." + TABLE_NAME;

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CLOSED = "closed";

    public static final String[] PROJECTION = new String[] {
            _ID,
            COLUMN_NAME,
            COLUMN_CLOSED
    };

    public static final String CREATE_TABLE =
            "create table " + TABLE_NAME + " (" +
                    _ID + " text primary key, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_CLOSED + " integer default 0" +
                    ");";

    public static final String DEFAULT_SORT_ORDER = COLUMN_NAME + " COLLATE LOCALIZED ASC";
}
