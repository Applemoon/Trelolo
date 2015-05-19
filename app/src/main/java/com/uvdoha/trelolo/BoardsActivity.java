package com.uvdoha.trelolo;

import android.app.Activity;
import android.app.ListActivity;
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

import com.uvdoha.trelolo.data.BoardsTable;
import com.uvdoha.trelolo.utils.Callback;


public class BoardsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);



        String token = getIntent().getExtras().getString("token", null);

        ListView listView = (ListView) findViewById(R.id.list_view);

        String[] from = new String[] { BoardsTable.COLUMN_NAME, BoardsTable._ID };
        int[] to = new int[] { R.id.board_title, R.id.board_id };
        adapter = new SimpleCursorAdapter(BoardsActivity.this,
                R.layout.board_item,
                null,
                from,
                to,
                0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, BoardsActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idView = (TextView) view.findViewById(R.id.board_id);

                Intent i = new Intent(BoardsActivity.this, ListsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", idView.getText().toString());
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        Callback callback = new Callback() {
            @Override
            public void onSuccess(Bundle data) {


            }

            @Override
            public void onFail(Bundle data) {}
        };

        if (token == null) {
            ServiceHelper.getInstance(this).getBoards(this, callback);
        } else {
            ServiceHelper.getInstance(this).getBoards(token, this, callback);
        }
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
