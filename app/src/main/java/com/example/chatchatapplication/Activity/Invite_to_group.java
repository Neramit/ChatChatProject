package com.example.chatchatapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chatchatapplication.Adapter.InviteAdapter2;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Invite_to_group extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    ListView friendInviteList;
    android.support.v7.widget.SearchView searchInvite;
    Button InviteButton;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;
    InviteAdapter2 iAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();
        String theme = sp.getString("Theme", "Green");
        switch (theme) {
            case "Blue":
                setTheme(R.style.Blue);
                break;
            case "Pink":
                setTheme(R.style.Blue);
                break;
            case "Orange":
                setTheme(R.style.Orange);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
        setContentView(R.layout.activity_invite_to_group);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_invite);
        friendInviteList = (ListView) findViewById(R.id.friendListInvite);
        searchInvite = (android.support.v7.widget.SearchView) findViewById(R.id.search_invite);
        InviteButton = (Button) findViewById(R.id.ok_button_invite);

        Gson gson = new Gson();
        String json = sp.getString("friendList", null);
        String json2 = sp.getString("inviteFriendList", null);
//        friendListRetrieve friendList = gson.fromJson(json, friendListRetrieve.class);
        Type type = new TypeToken<List<Friend>>() {
        }.getType();
        final ArrayList<Friend> friendList = gson.fromJson(json, type);
        final ArrayList<Friend> friendList2 = gson.fromJson(json2, type);

        int countCheck = 0;
        if (friendList2 != null && friendList != null) {
            for (Friend obj : friendList) {
                for (Friend obj2 : friendList2) {
                    if (Objects.equals(obj.getFriendUsername(), obj2.getFriendUsername())) {
                        obj.setCheckInvite(true);
                        countCheck++;
                    }
                }
            }
        }
        InviteButton.setText(getResources().getString(R.string.text_invite) + " (" + String.valueOf(countCheck) + ")");

        iAdapter2 = new InviteAdapter2(this, friendList, InviteButton);
        friendInviteList.setAdapter(iAdapter2);
        searchInvite.setOnQueryTextListener(Invite_to_group.this);

        InviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iAdapter2.getInviteList().size() != 0) {
                    Gson gson = new Gson();
                    List<Friend> sendFriendList = new ArrayList<Friend>();

                    for (Friend obj : iAdapter2.getInviteList()) {
                        if (obj.getCheckInvite())
                            sendFriendList.add(obj);
                    }

                    String json = gson.toJson(sendFriendList);
                    mEdit1.putString("inviteFriendList", json);
                    mEdit1.commit();
//                startActivity(new Intent(Invite_to_group.this,CreateGroup.class));
                    finish();
                } else
                    Toast.makeText(Invite_to_group.this, getResources().getString(R.string.can_not_invite), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        iAdapter2.filter(newText);
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
