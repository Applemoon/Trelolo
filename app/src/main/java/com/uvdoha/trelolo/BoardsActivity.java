package com.uvdoha.trelolo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uvdoha.trelolo.utils.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BoardsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_boards);


        Callback callback = new Callback() {
            @Override
            public void onSuccess(Bundle data) {

                String boardsString = data.getString("result", null);

                List<String> values = new ArrayList<>();

                if (boardsString != null) {
                    try {
                        JSONArray boards = new JSONArray(boardsString);

                        for (int i = 0; i < boards.length(); ++i) {
                            JSONObject board = boards.getJSONObject(i);

                            values.add(board.getString("name"));
                        }
                    }
                    catch (Exception e) {

                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BoardsActivity.this, android.R.layout.simple_list_item_1, values);
                setListAdapter(adapter);
            }

            @Override
            public void onFail(Bundle data) {

            }
        };

        Bundle data = getIntent().getExtras();
        String token = data.getString("token", null);
        if (token == null) {
            ServiceHelper.getInstance(this).getBoards(this, callback);
        } else {
            ServiceHelper.getInstance(this).getBoards(token, this, callback);
        }


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
