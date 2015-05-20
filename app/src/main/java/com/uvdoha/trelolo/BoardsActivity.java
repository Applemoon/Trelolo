package com.uvdoha.trelolo;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.uvdoha.trelolo.data.BoardsTable;
import com.uvdoha.trelolo.navigation.DrawerItem;
import com.uvdoha.trelolo.navigation.DrawerListAdapter;
import com.uvdoha.trelolo.utils.Callback;

import java.util.ArrayList;


public class BoardsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter adapter;
    private ArrayList<DrawerItem> drawerItems;
    private DrawerListAdapter navDrawerListAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);

        initNavigationDrawer();

// тута бутер будет
//        findViewById(R.id.ic_drawer).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDrawerLayout.isDrawerOpen(mDrawerList))
//                    mDrawerLayout.closeDrawer(mDrawerList);
//                else
//                    mDrawerLayout.openDrawer(mDrawerList);
//            }
//        });

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

        ServiceHelper.getInstance(this).getBoards(this, callback);
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

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
        }
    }

    private ArrayList<DrawerItem> getNavDrawerItems() {
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        ArrayList<DrawerItem> itemsList = new ArrayList<>();
        for (int i = 0; i < navMenuTitles.length; ++i)
            itemsList.add(new DrawerItem(navMenuTitles[i]));

        return itemsList;
    }

    private void initNavigationDrawer() {
        // load slide menu items
        drawerItems = getNavDrawerItems();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // load slide menu items
        drawerItems = getNavDrawerItems();

        // setting the nav drawer list adapter
        navDrawerListAdapter = new DrawerListAdapter(getApplicationContext(), drawerItems);
        mDrawerList.setAdapter(navDrawerListAdapter);
    }
}
