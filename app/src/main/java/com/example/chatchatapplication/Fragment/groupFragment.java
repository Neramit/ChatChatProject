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

import com.example.chatchatapplication.Activity.GroupChatroom;
import com.example.chatchatapplication.Adapter.groupAdapter;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Group;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.groupListRetrieve;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class groupFragment extends Fragment implements jsonBack {

    ListView groupList;
    private groupAdapter mAdapter;
    private ArrayList<Group> group_list = new ArrayList<Group>();
    TextView noGroup;

    String token, chkResponse;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = (LinearLayout) inflater.inflate(R.layout.fragment_group, container, false);
        groupList = (ListView) view.findViewById(R.id.groupList);
        noGroup = (TextView) view.findViewById(R.id.no_group);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit1 = sp.edit();

        token = sp.getString("token", null);

//        Gson sendJson = new Gson();
//        final User data = new User();
//        registerSend send = new registerSend("Group", "groupTabEnter", token, data);
//        String sendJson2 = sendJson.toJson(send);
//        new SimpleHttpTask(this).execute(sendJson2);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                if (group_list.get(position).getGroupStatus() == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.title_join);
                    builder.setIcon(R.drawable.letter1);
                    builder.setMessage(R.string.message_join);

                    builder.setPositiveButton(R.string.Yes_join, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gson sendJson = new Gson();
                            User data = new User();
                            data.setGenNum(group_list.get(position).getGroupUID());
                            token = sp.getString("token", null);
                            registerSend send = new registerSend("Group", "joinGroupRequest", token, data);
                            String sendJson2 = sendJson.toJson(send);
                            new SimpleHttpTask(groupFragment.this).execute(sendJson2);
                            chkResponse = "joinGroup";

                        }
                    });
                    builder.setNegativeButton(R.string.No_ignore, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Gson sendJson = new Gson();
                            User data = new User();
                            data.setGenNum(group_list.get(position).getGroupUID());
                            token = sp.getString("token", null);
                            registerSend send = new registerSend("Group", "ignoreGroupRequest", token, data);
                            String sendJson2 = sendJson.toJson(send);
                            new SimpleHttpTask(groupFragment.this).execute(sendJson2);
                            chkResponse = "ignoreGroup";
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
                } else {
                    Intent intent = new Intent(getActivity(), GroupChatroom.class);
                    mEdit1.putInt("groupUid", group_list.get(position).getGroupUID());
                    mEdit1.putString("groupName", group_list.get(position).getGroupName());
                    mEdit1.putString("groupOwner", group_list.get(position).getGroupOwner());
                    mEdit1.putString("groupImageURL", group_list.get(position).getGroupImageURL());
                    mEdit1.putInt("groupStatus", group_list.get(position).getGroupStatus());
                    mEdit1.putInt("groupMemberNum", group_list.get(position).getGroupMemberNum());
                    mEdit1.commit();
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter = new groupAdapter(getActivity());
        Gson sendJson = new Gson();
        final User data = new User();
        registerSend send = new registerSend("Group", "groupTabEnter", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(this).execute(sendJson2);
        chkResponse = "enterGroupFragment";
    }

    @Override
    public void processFinish(String output) {

        if (chkResponse.equals("enterGroupFragment")) {
            Gson gson = new Gson();
            groupListRetrieve data = gson.fromJson(output, groupListRetrieve.class);

            if (data.getStatus() == 200) {
                int i = 0;
                group_list.clear();
                while (data.getData().size() > i) {
                    group_list.add(data.getData().get(i));
                    mAdapter.addItem(data.getData().get(i));
                    i++;
                }
                if (group_list.size() == 0) {
                    groupList.setVisibility(View.GONE);
                    noGroup.setVisibility(View.VISIBLE);
                } else {
                    groupList.setAdapter(mAdapter);
                    groupList.setVisibility(View.VISIBLE);
                    noGroup.setVisibility(View.GONE);
                }
            } else {
                groupList.setVisibility(View.GONE);
                noGroup.setVisibility(View.VISIBLE);
            }
        }else if (chkResponse.equals("joinGroup")){
            Gson gson = new Gson();
            searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
            if (data.getStatus() == 200) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_join_success), Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), groupFragment.class));
//                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if (chkResponse.equals("ignoreGroup")){
            Gson gson = new Gson();
            searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
            if (data.getStatus() == 200) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_ignore_success), Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), groupFragment.class));
//                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}