package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.Member;
import com.example.chatchatapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 10/5/2017 at 3:39 PM.
 */

public class KickAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Member> membersList;

    public KickAdapter(Context mContext, List<Member> membersList) {
        this.mContext = mContext;
        this.membersList = membersList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Member getItem(int i) {
        return membersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView name;
        CircleImageView circleImageView;
        ImageView status;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        KickAdapter.ViewHolder holder;
        if (view == null) {
            holder = new KickAdapter.ViewHolder();
            view = inflater.inflate(R.layout.member_list, null);
            // Locate the TextViews in listview_item.xml
            holder.circleImageView = (CircleImageView) view.findViewById(R.id.image_member);
            holder.name = (TextView) view.findViewById(R.id.kick_member_name);
            holder.status = (ImageView) view.findViewById(R.id.status_member_image);
            view.setTag(holder);
        } else {
            holder = (KickAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        String displayPictureURL = membersList.get(i).getMemberImageURL();
        int status = membersList.get(i).getMemberStatus();
        if (displayPictureURL != null) {
            Glide.with(mContext)
                    .load(displayPictureURL)  //Test
                    .into(holder.circleImageView);
        } else {
            holder.circleImageView.setImageResource(R.drawable.default_user);
        }
        holder.name.setText(membersList.get(i).getMemberDisplayName());
        if (status == 0)
            holder.status.setImageResource(R.drawable.letter1);
        else if (status == 2)
            holder.status.setImageResource(R.drawable.star);
        else if (status == 3)
            holder.status.setImageResource(R.drawable.kick_member);

        return view;
    }

}
