package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 9/22/2017 at 10:26 AM.
 */

public class InviteAdapter2 extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Friend> inviteList;
    private ArrayList<Friend> arraylist = new ArrayList<Friend>();
    private Button inviteButton;

    public InviteAdapter2(Context mContext, List<Friend> inviteList, Button inviteButton) {
        this.mContext = mContext;
        this.inviteList = inviteList;
        this.inflater = LayoutInflater.from(mContext);
        this.inviteButton = inviteButton;

        this.arraylist.addAll(inviteList);
    }

    @Override
    public int getCount() {
        return inviteList.size();
    }

    @Override
    public Friend getItem(int i) {
        return inviteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView name;
        CircleImageView circleImageView;
        CheckBox chk;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.invite_friend_list2, null);
            // Locate the TextViews in listview_item.xml
            holder.circleImageView = (CircleImageView) view.findViewById(R.id.image_user2);
            holder.name = (TextView) view.findViewById(R.id.invite_name2);
            holder.chk = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String pictureURL = inviteList.get(i).getDisplayPictureURL();
        if (pictureURL != null) {
            Glide.with(view)
                    .load(pictureURL)  //Test
                    .into(holder.circleImageView);
        } else
            holder.circleImageView.setImageResource(R.drawable.default_user);
        holder.name.setText(inviteList.get(i).getDisplayName());
        if (inviteList.get(i).getCheckInvite()!=null)
            holder.chk.setChecked(true);
        else
            holder.chk.setChecked(false);
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    inviteList.get(i).setCheckInvite(true);
                else
                    inviteList.get(i).setCheckInvite(false);
                int countCheck = 0;
                for (int j = 0; j < getCount(); j++) {
                    if (inviteList.get(j).getCheckInvite()!=null)
                        countCheck++;
                    else{}
                }
                inviteButton.setText(mContext.getString(R.string.text_invite) +" (" +String.valueOf(countCheck) + ")");
            }
        });
        return view;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        inviteList.clear();
        if (charText.length() == 0) {
            inviteList.addAll(arraylist);
        } else {
            for (Friend wp : arraylist) {
                if (wp.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    inviteList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
