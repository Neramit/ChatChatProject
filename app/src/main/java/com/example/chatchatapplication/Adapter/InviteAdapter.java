package com.example.chatchatapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatchatapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 9/21/2017 at 4:35 PM.
 */

public class InviteAdapter extends BaseAdapter{

    private LayoutInflater inflater;

    public InviteAdapter() {}

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView name;
        CircleImageView circleImageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.invite_friend_list, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name_invite);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
//        holder.name.setText();
        return view;
    }


}
