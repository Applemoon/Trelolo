package com.uvdoha.trelolo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.uvdoha.trelolo.utils.Callback;


public class BoardsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_boards);

//        String[] values = new String[] { "Доска 1", "Доска 2", "Доска 3" };

        ServiceHelper.getInstance(this).getBoards(this, new Callback() {
            @Override
            public void onSuccess(Bundle data) {
                Log.d("res", data.getString("result", "sas"));
                String[] values = new String[] { "Доска 1", "Доска 2", "Доска 3" };
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BoardsActivity.this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
            }

            @Override
            public void onFail(Bundle data) {

            }
        });

        // TODO

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO
        Intent i = new Intent(BoardsActivity.this, ListsActivity.class);
        startActivity(i);
    }
}
