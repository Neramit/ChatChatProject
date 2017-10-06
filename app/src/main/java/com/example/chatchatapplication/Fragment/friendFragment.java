package com.example.chatchatapplication.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatchatapplication.Activity.AddFriend;
import com.example.chatchatapplication.Activity.FriendChatroom;
import com.example.chatchatapplication.Activity.MainActivity;
import com.example.chatchatapplication.Adapter.friendAdapter;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.friendListRetrieve;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class friendFragment extends Fragment implements jsonBack {

    ListView friendList;
    private friendAdapter mAdapter;
    private String token, chkResponse;
    private ArrayList<Friend> friend_list = new ArrayList<Friend>();
    TextView addFriend;
    LinearLayout noFriend;

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

        final String username = sp.getString("username", null);

        Gson sendJson = new Gson();
        final User data = new User();
        token = sp.getString("token", null);
        registerSend send = new registerSend("Friend", "friendTabEnter", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(this).execute(sendJson2);
        chkResponse = "enterFriend";

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddFriend.class));
            }
        });

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                // TODO Auto-generated method stub
                if (friend_list.get(position).getFriendStatus() == 0 && !Objects.equals(friend_list.get(position).getOwnerUsername(), username)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.title_accect);
                    builder.setIcon(R.drawable.letter1);
                    builder.setMessage(R.string.message_accept);

                    builder.setPositiveButton(R.string.Yes_accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gson sendJson = new Gson();
                            User data = new User();
                            data.setUsername(friend_list.get(position).getOwnerUsername());
                            token = sp.getString("token", null);
                            registerSend send = new registerSend("Friend", "acceptFriendRequest", token, data);
                            String sendJson2 = sendJson.toJson(send);
                            new SimpleHttpTask(friendFragment.this).execute(sendJson2);
                            chkResponse = "acceptFriend";

                        }
                    });
                    builder.setNegativeButton(R.string.No_decline, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gson sendJson = new Gson();
                            User data = new User();
                            data.setUsername(friend_list.get(position).getOwnerUsername());
                            token = sp.getString("token", null);
                            registerSend send = new registerSend("Friend", "declineFriendRequest", token, data);
                            String sendJson2 = sendJson.toJson(send);
                            new SimpleHttpTask(friendFragment.this).execute(sendJson2);
                            chkResponse = "declineFriend";
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton(R.string.Neutral_close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else if (friend_list.get(position).getFriendStatus() == 1) {
                    Intent intent = new Intent(getActivity(), FriendChatroom.class);
                    mEdit1.putInt("chatroomUid", friend_list.get(position).getChatroomUID());
                    mEdit1.putString("friendUsername", friend_list.get(position).getFriendUsername());
                    mEdit1.putInt("friendStatus", friend_list.get(position).getFriendStatus());
                    mEdit1.putString("friendDisplayName", friend_list.get(position).getDisplayName());
                    mEdit1.putString("friendDisplayPictureURL", friend_list.get(position).getDisplayPictureURL());
                    mEdit1.putBoolean("friendFavorite", friend_list.get(position).getFavorite());
                    mEdit1.putString("friendRegistrationID", friend_list.get(position).getFriendRegistrationID());
                    mEdit1.commit();
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "You already request this user\nWait for this user accept request.", Toast.LENGTH_SHORT).show();
                }
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
        if (Objects.equals(chkResponse, "enterFriend")) {
            Gson gson = new Gson();
            friendListRetrieve data = gson.fromJson(output, friendListRetrieve.class);
            if (data.getStatus() == 200) {
                int i = 0;
                while (data.getData().size() > i) {
                    friend_list.add(data.getData().get(i));
                    mAdapter.addItem(data.getData().get(i));
                    i++;
                }
                if (friend_list.size() == 0) {
                    friendList.setVisibility(View.GONE);
                    noFriend.setVisibility(View.VISIBLE);
                } else {
                    friendList.setAdapter(mAdapter);
                    String json = gson.toJson(data.getData());
                    mEdit1.putString("friendList", json);
                    mEdit1.commit();
                    friendList.setVisibility(View.VISIBLE);
                    noFriend.setVisibility(View.GONE);
                }
            } else {
                friendList.setVisibility(View.GONE);
                noFriend.setVisibility(View.VISIBLE);
            }
        } else if (Objects.equals(chkResponse, "acceptFriend")) {
            Gson gson = new Gson();
            searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
            if (data.getStatus() == 200) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_accept_success), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (Objects.equals(chkResponse, "declineFriend")) {
            Gson gson = new Gson();
            searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
            if (data.getStatus() == 200) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_decline_success), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}