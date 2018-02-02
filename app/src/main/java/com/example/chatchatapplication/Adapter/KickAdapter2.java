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
import com.example.chatchatapplication.Object_json.Member;
import com.example.chatchatapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Neramit777 on 9/22/2017 at 10:26 AM.
 */

public class KickAdapter2 extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Member> inviteList;
    private Button kickButton;
    private int countCheck;
    private ArrayList<Member> arraylist;
    private ArrayList<Member> arraylist2;

    public KickAdapter2(Context mContext, List<Member> inviteList, Button kickButton) {
        this.mContext = mContext;
        this.inviteList = inviteList;
        this.inflater = LayoutInflater.from(mContext);
        this.kickButton = kickButton;

        arraylist = new ArrayList<Member>();
        arraylist2 = new ArrayList<Member>();
        arraylist2.addAll(inviteList);
    }

    @Override
    public int getCount() {
        return inviteList.size();
    }

    @Override
    public Member getItem(int i) {
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

        String pictureURL = inviteList.get(i).getMemberImageURL();
        if (pictureURL != null) {
            Glide.with(view)
                    .load(pictureURL)  //Test
                    .into(circleImageView);
        } else
            circleImageView.setImageResource(R.drawable.default_user);
        name.setText(inviteList.get(i).getMemberDisplayName());
//        chk.setChecked(inviteList.get(i).getCheckInvite());
        if (inviteList.get(i).getMemberStatus()==3)
            chk.setChecked(true);
        else
            chk.setChecked(false);

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                inviteList.get(i).setCheckInvite(b);
                if (b)
                    inviteList.get(i).setMemberStatus(3);
                else
                    inviteList.get(i).setMemberStatus(0);
                countCheck = 0;
                for (int j = 0; j < getCount(); j++) {

                    if (inviteList.get(j).getMemberStatus()==3) {
                        countCheck++;
                    }
                }
                kickButton.setText(mContext.getString(R.string.text_invite) + " (" + String.valueOf(countCheck) + ")");
            }
        });
        return view;
    }

    public List<Member> getInviteList() {
        return inviteList;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        inviteList.clear();
        if (charText.length() == 0) {
            inviteList.addAll(arraylist2);
        } else {
            for (Member wp : arraylist2) {
                if (wp.getMemberDisplayName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    inviteList.add(wp);

                }
            }
        }

        notifyDataSetChanged();
    }
}
