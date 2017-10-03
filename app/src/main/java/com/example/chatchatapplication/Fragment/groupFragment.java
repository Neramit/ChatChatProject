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

import com.example.chatchatapplication.Activity.FriendChatroom;
import com.example.chatchatapplication.Adapter.groupAdapter;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Group;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.groupListRetrieve;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class groupFragment extends Fragment implements jsonBack {

    ListView groupList;
    private groupAdapter mAdapter;
    private ArrayList<Group> group_list = new ArrayList<Group>();
    TextView noGroup;

    String token;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new groupAdapter(getActivity());
        View view = null;
        view = (LinearLayout)inflater.inflate(R.layout.fragment_group, container, false);
        groupList = (ListView) view.findViewById(R.id.groupList);
        noGroup = (TextView) view.findViewById(R.id.no_group);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit1 = sp.edit();

        Gson sendJson = new Gson();
//        button.setProgress(50);
        final User data = new User();
//        data.setDisplayName(displayname);
        token = sp.getString("token", null);
        registerSend send = new registerSend("Group", "groupTabEnter", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(this).execute(sendJson2);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FriendChatroom.class);
//                mEdit1.putInt("chatroomUid",group_list.get(position).getChatroomUID());
//                mEdit1.putString("groupName",group_list.get(position).getGroupName());
//                mEdit1.putInt("friendStatus",group_list.get(position).getFriendStatus());
//                mEdit1.putString("friendDisplayName",group_list.get(position).getDisplayName());
//                mEdit1.putString("friendDisplayPictureURL",group_list.get(position).getDisplayPictureURL());
//                mEdit1.putString("friendRegistrationID",group_list.get(position).getFriendRegistrationID());
//                mEdit1.commit();
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        groupListRetrieve data = gson.fromJson(output, groupListRetrieve.class);

        if (data.getStatus() == 200) {
            int i = 0;
            while (data.getData().size() > i) {
//                friend_list.add(data.getData().get(i));
                group_list.add(data.getData().get(i));
                mAdapter.addItem(data.getData().get(i));
//                data.getData().remove(i);
                i++;
            }
            if (group_list.size()==0){
                groupList.setVisibility(View.GONE);
                noGroup.setVisibility(View.VISIBLE);
            }else{
                groupList.setAdapter(mAdapter);
//                String json = gson.toJson(data.getData());
//                mEdit1.putString("friendList", json);
//                mEdit1.commit();
            }
        }else{
            groupList.setVisibility(View.GONE);
            noGroup.setVisibility(View.VISIBLE);
        }
    }
}