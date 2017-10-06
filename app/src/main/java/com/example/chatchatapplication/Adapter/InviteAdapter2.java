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
    private Button inviteButton;
    private int countCheck;
    private ArrayList<Friend> arraylist;
    private ArrayList<Friend> arraylist2;

    public InviteAdapter2(Context mContext, List<Friend> inviteList, Button inviteButton) {
        this.mContext = mContext;
        this.inviteList = inviteList;
        this.inflater = LayoutInflater.from(mContext);
        this.inviteButton = inviteButton;

        arraylist = new ArrayList<Friend>();
        arraylist2 = new ArrayList<Friend>();
        arraylist2.addAll(inviteList);
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

        view = inflater.inflate(R.layout.invite_friend_list2, null);
        // Locate the TextViews in listview_item.xml
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.image_user2);
        TextView name = (TextView) view.findViewById(R.id.invite_name2);
        CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);

        String pictureURL = inviteList.get(i).getDisplayPictureURL();
        if (pictureURL != null) {
            Glide.with(view)
                    .load(pictureURL)  //Test
                    .into(circleImageView);
        } else
            circleImageView.setImageResource(R.drawable.default_user);
        name.setText(inviteList.get(i).getDisplayName());

        chk.setChecked(inviteList.get(i).getCheckInvite());
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                inviteList.get(i).setCheckInvite(b);
                countCheck = 0;
                for (int j = 0; j < getCount(); j++) {

                    if (inviteList.get(j).getCheckInvite()) {
                        countCheck++;
                    }

                }
                inviteButton.setText(mContext.getString(R.string.text_invite) + " (" + String.valueOf(countCheck) + ")");
            }
        });
        return view;
    }

    public List<Friend> getInviteList() {
        return inviteList;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        inviteList.clear();
        if (charText.length() == 0) {
            inviteList.addAll(arraylist2);
        } else {
            for (Friend wp : arraylist2) {
                if (wp.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    inviteList.add(wp);

                }
            }
        }

        notifyDataSetChanged();
    }
}
