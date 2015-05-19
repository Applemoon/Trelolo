package com.uvdoha.trelolo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uvdoha.trelolo.adapters.Board;
import com.uvdoha.trelolo.adapters.BoardAdapter;
import com.uvdoha.trelolo.R;
import java.util.ArrayList;

/**
 * Created by dmitry on 19.05.15.
 */
public class BoardsItemFragment extends Fragment {
    public BoardsItemFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.board_item, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.itemsListView);
        BoardAdapter itemAdapter = new BoardAdapter(getActivity(), new ArrayList<Board>());
        listView.setAdapter(itemAdapter);
        return rootView;
    }

}
