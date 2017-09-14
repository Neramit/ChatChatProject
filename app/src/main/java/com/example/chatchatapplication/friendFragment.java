package com.example.chatchatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chatchatapplication.Adapter.friendAdapter;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.friendListRetrieve;
import com.example.chatchatapplication.Object_json.registerSend;
import com.google.gson.Gson;

import java.util.ArrayList;

public class friendFragment extends Fragment implements jsonBack {

    ListView friendList;
    private friendAdapter mAdapter;
    private String token;
    private ArrayList<Friend> friend_list = new ArrayList<Friend>();

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

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FriendChatroom.class);
                intent.putExtra("chatroomUid",friend_list.get(position).getChatroomUID());
                startActivity(intent);
//                switch (((HospitalNames)list.getAdapter().getItem(position)).getHospitalName()) {
////                switch (hospitalNameList[position])
//                    case "โรงพยาบาลราษฎร์บูรณะ":
//                        intent = new Intent(Find.this, Hospital_1.class);
//                        break;
//                    case "โรงพยาบาลสุขสวัสดิ์":
//                        intent = new Intent(Find.this, Hospital_2.class);
//                        break;
//                    case "โรงพยาบาลประชาพัฒน์":
//                        intent = new Intent(Find.this, Hospital_3.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก":
//                        intent = new Intent(Find.this, Hospital_4.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก 3":
//                        intent = new Intent(Find.this, Hospital_6.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก 9":
//                        intent = new Intent(Find.this, Hospital_7.class);
//                        break;
//                    case "โรงพยาบาลเจริญกรุง":
//                        intent = new Intent(Find.this, Hospital_5.class);
//                        break;
//                    case "โรงพยาบาลสมิติเวช":
//                        intent = new Intent(Find.this, Hospital_8.class);
//                        break;
//                    case "โรงพยาบาลบํารุงราษฎร์":
//                        intent = new Intent(Find.this, Hospital_9.class);
//                        break;
//                    case "โรงพยาบาลศิริราช":
//                        intent = new Intent(Find.this, Hospital_10.class);
//                        break;
//                    default:
//                        intent = new Intent(Find.this, Hospital_11.class);
//                        break;
//                }
//                if (intent != null) {
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();
//                }
            }
        });

        return view;
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
        }

    }
}