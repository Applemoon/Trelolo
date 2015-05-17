package com.uvdoha.trelolo;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.uvdoha.trelolo.data.BoardsTable;
//import com.uvdoha.trelolo.data.DB;
import com.uvdoha.trelolo.utils.Callback;


public class BoardsActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceHelper.getInstance(this).getBoards(this, new Callback() {
            @Override
            public void onSuccess(Bundle data) {
                Log.d("res", data.getString("result", "sas"));
                String[] from = new String[] { BoardsTable.COLUMN_NAME };
                int[] to = new int[] { android.R.id.text1 };
                adapter = new SimpleCursorAdapter(BoardsActivity.this,
                        android.R.layout.simple_list_item_1,
                        null,
                        from,
                        to,
                        0);
                setListAdapter(adapter);
                getLoaderManager().initLoader(0, null, BoardsActivity.this);
            }

            @Override
            public void onFail(Bundle data) {}
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO
        Intent i = new Intent(BoardsActivity.this, ListsActivity.class);
        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                BoardsTable.CONTENT_URI,
                BoardsTable.PROJECTION,
                null,
                null,
                BoardsTable.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
