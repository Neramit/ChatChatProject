package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;

import java.util.ArrayList;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 9/13/2017.
 */

public class friendAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    //    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<Friend> mData = new ArrayList<Friend>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public friendAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Friend item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Friend item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Friend getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.friend_listview, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.friendname);
                    String friendDisplayName = mData.get(position).getDisplayName();
                    if (friendDisplayName != null)
                        holder.textView.setText(friendDisplayName);
                    else holder.textView.setText(mData.get(position).getFriendUsername());

                    String friendDisplayPictureURL = mData.get(position).getDisplayPictureURL();
                    if (friendDisplayPictureURL != null) {
                        holder.circleImageView = (CircleImageView) convertView.findViewById(R.id.image_user);
                        Glide.with(convertView)
                                .load(mData.get(position).getDisplayPictureURL())  //Test
                                .into(holder.circleImageView);
                    }
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.section_listview, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSequence);
                    holder.textView.setText(mData.get(position).getFriendUsername());
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
        CircleImageView circleImageView;
    }
}
