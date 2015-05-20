package com.uvdoha.trelolo.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uvdoha.trelolo.R;

import java.util.ArrayList;

/**
 * Created by dmitry on 20.05.15.
 */
public class DrawerListAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<DrawerItem> navDrawerItems;

    public DrawerListAdapter(Context mContext, ArrayList<DrawerItem> navDrawerItems) {
        this.mContext = mContext;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.drawer_layout, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.drawer_item_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
    }

}