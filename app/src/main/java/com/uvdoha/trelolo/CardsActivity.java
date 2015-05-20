package com.uvdoha.trelolo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uvdoha.trelolo.data.CardsTable;
import com.uvdoha.trelolo.utils.Callback;


public class CardsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;
    String list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        if (getIntent() != null) {
            list_id = getIntent().getStringExtra("list_id");
            ListView listView = (ListView) findViewById(R.id.list_view);
            String[] from = new String[]{CardsTable.COLUMN_NAME, CardsTable._ID};
            int[] to = new int[]{R.id.card_title, R.id.card_id};
            adapter = new SimpleCursorAdapter(this,
                    R.layout.card_item,
                    null,
                    from,
                    to,
                    0);
            listView.setAdapter(adapter);
            getLoaderManager().initLoader(0, null, this);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView idView = (TextView) view.findViewById(R.id.card_id);
                    TextView titleView = (TextView) view.findViewById(R.id.card_title);
                    Intent i = new Intent(CardsActivity.this, CardViewActivity.class);

                    i.putExtra("card_id", idView.getText().toString());
                    i.putExtra("card_title", titleView.getText().toString());
                    startActivity(i);
                }
            });

            Callback callback = new Callback() {
                @Override
                public void onSuccess(Bundle data) {
                    Toast.makeText(CardsActivity.this,
                            R.string.success_cards_download,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Bundle data) {
                    Toast.makeText(CardsActivity.this,
                            R.string.fail_cards_download,
                            Toast.LENGTH_SHORT).show();
                }
            };
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("offline", true)) {
                ServiceHelper.getInstance(this).getCards(this, list_id, callback);
            }
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
