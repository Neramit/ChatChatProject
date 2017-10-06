package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.Group;
import com.example.chatchatapplication.R;

import java.util.ArrayList;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 10/3/2017 at 4:18 PM.
 */

public class groupAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Group> mData = new ArrayList<Group>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public groupAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Group item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Group item) {
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
    public Group getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        groupAdapter.ViewHolder holder;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new groupAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.group_listview, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.groupname);
                    holder.statusImage = (ImageView) convertView.findViewById(R.id.status_image);
                    holder.circleImageView = (CircleImageView) convertView.findViewById(R.id.image_group);

                    String GroupName = mData.get(position).getGroupName();
                    holder.textView.setText(GroupName + " (" + mData.get(position).getGroupMemberNum() + ")");

                    if (mData.get(position).getGroupStatus() == 0) {
                        holder.statusImage.setImageResource(R.drawable.letter1);
                    }

                    String groupImageURL = mData.get(position).getGroupImageURL();
                    if (groupImageURL!= null) {
                        Glide.with(convertView)
                                .load(mData.get(position).getGroupImageURL())  //Test
                                .into(holder.circleImageView);
                    }
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.section_listview, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSequence);
//                    holder.textView.setText(mData.get(position).getFriendUsername());
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (groupAdapter.ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        CircleImageView circleImageView;
        ImageView statusImage;
    }
}
