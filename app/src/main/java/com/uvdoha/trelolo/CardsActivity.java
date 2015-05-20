package com.uvdoha.trelolo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.uvdoha.trelolo.data.CardsTable;
import com.uvdoha.trelolo.utils.Callback;


public class CardsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;
    String list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        list_id = getIntent().getStringExtra("list_id");
        ListView listView = (ListView) findViewById(R.id.list_view);
        String[] from = new String[]{CardsTable.COLUMN_NAME, CardsTable._ID};
        int[] to = new int[]{R.id.list_title, R.id.list_id};
        adapter = new SimpleCursorAdapter(this,
                R.layout.list_item,
                null,
                from,
                to,
                0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        Callback callback = new Callback() {
            @Override
            public void onSuccess(Bundle data) {
                Log.d("DEBUG", "Get all cards for list_id = " + list_id + " success");
            }

            @Override
            public void onFail(Bundle data) {
                Log.d("DEBUG", "Get all cards for list_id = " + list_id + " fail");
            }
        };
        if (savedInstanceState == null) {
            ServiceHelper.getInstance(this).getCards(this, list_id, callback);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                CardsTable.CONTENT_URI,
                CardsTable.PROJECTION,
                CardsTable.SELECT,
                new String[] { list_id },
                CardsTable.DEFAULT_SORT_ORDER);
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
