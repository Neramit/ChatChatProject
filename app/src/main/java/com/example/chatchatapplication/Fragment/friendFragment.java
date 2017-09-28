package com.example.chatchatapplication.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatchatapplication.Activity.AddFriend;
import com.example.chatchatapplication.Activity.FriendChatroom;
import com.example.chatchatapplication.Adapter.friendAdapter;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.friendListRetrieve;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class friendFragment extends Fragment implements jsonBack {

    ListView friendList;
    private friendAdapter mAdapter;
    private String token;
    private ArrayList<Friend> friend_list = new ArrayList<Friend>();
    TextView addFriend;
    LinearLayout noFriend;

    friendListRetrieve data;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new friendAdapter(getActivity());
        View view = null;
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        friendList = (ListView) view.findViewById(R.id.friendListview);
        addFriend = (TextView) view.findViewById(R.id.add_friend);
        noFriend = (LinearLayout) view.findViewById(R.id.no_friend);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit1 = sp.edit();

        Gson sendJson = new Gson();
//        button.setProgress(50);
        final User data = new User();
//        data.setDisplayName(displayname);
        token = sp.getString("token", null);
        registerSend send = new registerSend("Friend", "friendTabEnter", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(this).execute(sendJson2);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddFriend.class));
            }
        });

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FriendChatroom.class);
                mEdit1.putInt("chatroomUid",friend_list.get(position).getChatroomUID());
                mEdit1.putString("friendUsername",friend_list.get(position).getFriendUsername());
                mEdit1.putString("friendStatus",friend_list.get(position).getFriendStatus());
                mEdit1.putString("friendDisplayName",friend_list.get(position).getDisplayName());
                mEdit1.putString("friendDisplayPictureURL",friend_list.get(position).getDisplayPictureURL());
                mEdit1.putString("friendRegistrationID",friend_list.get(position).getFriendRegistrationID());
                mEdit1.commit();
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
//        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.friend_tab) + " (" + friend_list.size() + ")");
        super.onStart();
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        data = gson.fromJson(output, friendListRetrieve.class);

        if (data.getStatus() == 200) {
            int i = 0;
            while (data.getData().size() > i) {
//                friend_list.add(data.getData().get(i));
                friend_list.add(data.getData().get(i));
                mAdapter.addItem(data.getData().get(i));
//                data.getData().remove(i);
                i++;
            }
            friendList.setAdapter(mAdapter);
            String json = gson.toJson(data.getData());
            mEdit1.putString("friendList", json);
            mEdit1.commit();
        }else{
            friendList.setVisibility(View.GONE);
            noFriend.setVisibility(View.VISIBLE);
        }
    }
}