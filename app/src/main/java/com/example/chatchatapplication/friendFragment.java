package com.example.chatchatapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class friendFragment extends Fragment {

    ListView friendList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        friendList = (ListView) view.findViewById(R.id.friendListview);
        
        return view;
    }

}