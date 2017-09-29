package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 9/21/2017 at 4:35 PM.
 */

public class InviteAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Friend> membersList;

    public InviteAdapter(Context mContext, List<Friend> membersList) {
        this.mContext = mContext;
        this.membersList = membersList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Friend getItem(int i) {
        return membersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView name;
        CircleImageView circleImageView;
        ImageView x;
        RelativeLayout all;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.invite_friend_list, null);
            // Locate the TextViews in listview_item.xml
            holder.circleImageView = (CircleImageView) view.findViewById(R.id.picture_invite);
            holder.name = (TextView) view.findViewById(R.id.name_invite);
            holder.x = (ImageView) view.findViewById(R.id.x);
            holder.all = (RelativeLayout) view.findViewById(R.id.All);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (Objects.equals(membersList.get(i).getDisplayName(), "AddButton271137")) {
            holder.name.setText(R.string.text_add);
            holder.circleImageView.setImageResource(R.drawable.add_invite);
            holder.all.removeView(holder.x);
        } else {
            // Set the results into TextViews
            String displayPictureURL = membersList.get(i).getDisplayPictureURL();
            if (displayPictureURL != null) {
                Glide.with(mContext)
                        .load(membersList.get(i).getDisplayPictureURL())  //Test
                        .into(holder.circleImageView);
            }else{
                holder.circleImageView.setImageResource(R.drawable.default_user);
            }
            holder.name.setText(membersList.get(i).getDisplayName());

        }
        return view;
    }


}
