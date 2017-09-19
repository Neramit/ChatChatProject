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
import android.widget.GridView;

import com.example.chatchatapplication.Adapter.GridAdapter;
import com.example.chatchatapplication.Activity.ProfileAccount;
import com.example.chatchatapplication.R;
import com.example.chatchatapplication.Activity.Setting;

public class settingFragment extends Fragment {
    GridView gridview;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String displayPictureURL = sp.getString("displayPictureURL",null);

        gridview = (GridView) view.findViewById(R.id.grid_view);
        gridview.setAdapter(new GridAdapter(getActivity(),displayPictureURL));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getActivity(), ProfileAccount.class));
                } else if (position == 1) {
                    startActivity(new Intent(getActivity(), Setting.class));
                }
            }
        });

        return view;
    }

}