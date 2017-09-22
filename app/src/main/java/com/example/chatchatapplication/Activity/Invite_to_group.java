package com.example.chatchatapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.chatchatapplication.Adapter.InviteAdapter2;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Invite_to_group extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    ListView friendInviteList;
    android.support.v7.widget.SearchView searchInvite;
    Button ok;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;
    InviteAdapter2 iAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_invite);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_group);

        friendInviteList = (ListView) findViewById(R.id.friendListInvite);
        searchInvite = (android.support.v7.widget.SearchView) findViewById(R.id.search_invite);
        ok = (Button) findViewById(R.id.ok_button_invite);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        Gson gson = new Gson();
        String json = sp.getString("friendList", null);
//        friendListRetrieve friendList = gson.fromJson(json, friendListRetrieve.class);
        Type type = new TypeToken<List<Friend>>() {}.getType();
        ArrayList<Friend> friendList = gson.fromJson(json, type);

        iAdapter2 = new InviteAdapter2(this,friendList);
        friendInviteList.setAdapter(iAdapter2);
        searchInvite.setOnQueryTextListener(Invite_to_group.this);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        iAdapter2.filter(text);
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}