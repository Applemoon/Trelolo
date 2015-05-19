package com.uvdoha.trelolo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.uvdoha.trelolo.R;

/**
 * Created by vadim on 06.05.15.
 */
public class BoardAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<Board> itemList;

    public BoardAdapter(Context context, ArrayList<Board> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Board getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.board_item, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.user_name);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.title.setText(getItem(position).getTitle());

        return convertView;
    }


    static class ViewHolder {
        TextView title;
    }
}