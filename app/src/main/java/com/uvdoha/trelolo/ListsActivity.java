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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uvdoha.trelolo.data.BoardsTable;
import com.uvdoha.trelolo.data.ListsTable;
import com.uvdoha.trelolo.utils.Callback;


public class ListsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        ListView listView = (ListView) findViewById(R.id.list_view);
        String[] from = new String[] { ListsTable.COLUMN_NAME, ListsTable._ID };
        int[] to = new int[] { R.id.board_title, R.id.board_id };
        adapter = new SimpleCursorAdapter(ListsActivity.this,
                R.layout.board_item,
                null,
                from,
                to,
                0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, ListsActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idView = (TextView) view.findViewById(R.id.board_id);

                Intent i = new Intent(ListsActivity.this, CardsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", idView.getText().toString());
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String boardId = bundle.getString("id");

        ServiceHelper.getInstance(this).getLists(this, boardId, new Callback() {
            @Override
            public void onSuccess(Bundle data) {

            }

            @Override
            public void onFail(Bundle data) {

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ListsTable.CONTENT_URI,
                ListsTable.PROJECTION,
                null,
                null,
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
