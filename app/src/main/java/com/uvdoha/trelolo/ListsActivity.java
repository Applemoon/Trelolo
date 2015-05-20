package com.uvdoha.trelolo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uvdoha.trelolo.data.ListsTable;
import com.uvdoha.trelolo.utils.Callback;


public class ListsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;
    String board_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        board_id = getIntent().getStringExtra("board_id");
        ListView listView = (ListView) findViewById(R.id.list_view);
        String[] from = new String[]{ListsTable.COLUMN_NAME, ListsTable._ID};
        int[] to = new int[]{R.id.list_title, R.id.list_id};
        adapter = new SimpleCursorAdapter(this,
                R.layout.list_item,
                null,
                from,
                to,
                0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idView = (TextView) view.findViewById(R.id.list_id);

                Intent i = new Intent(ListsActivity.this, CardsActivity.class);
                i.putExtra("list_id", idView.getText().toString());
                startActivity(i);
            }
        });

        Callback callback = new Callback() {
            @Override
            public void onSuccess(Bundle data) {
                Toast.makeText(ListsActivity.this,
                        R.string.success_lists_download,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Bundle data) {
                Toast.makeText(ListsActivity.this,
                        R.string.fail_lists_download,
                        Toast.LENGTH_SHORT).show();
            }
        };

        if (savedInstanceState == null) {
            ServiceHelper.getInstance(this).getLists(this, board_id, callback);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ListsTable.CONTENT_URI,
                ListsTable.PROJECTION,
                ListsTable.SELECT,
                new String[] { board_id },
                ListsTable.DEFAULT_SORT_ORDER);
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
