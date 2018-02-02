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

import com.example.chatchatapplication.Adapter.KickAdapter2;
import com.example.chatchatapplication.Object_json.Member;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class kick_list extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{

    ListView memberKickList;
    android.support.v7.widget.SearchView searchKick;
    Button KickButton;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    KickAdapter2 iAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.button_kick_meber));

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();
        String theme = sp.getString("Theme", "Green");
        switch (theme) {
            case "Blue":
                setTheme(R.style.Blue);
                break;
            case "Pink":
                setTheme(R.style.Pink);
                break;
            case "Orange":
                setTheme(R.style.Orange);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
        setContentView(R.layout.activity_kick_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_invite);
        memberKickList = (ListView) findViewById(R.id.friendListKick);
        searchKick = (android.support.v7.widget.SearchView) findViewById(R.id.search_kick);
        KickButton = (Button) findViewById(R.id.ok_button_kick);

        Gson gson = new Gson();
        String json = sp.getString("memberList", null);
        String json2 = sp.getString("kickMemberList", null);
//        friendListRetrieve friendList = gson.fromJson(json, friendListRetrieve.class);
        Type type = new TypeToken<List<Member>>() {
        }.getType();
        final ArrayList<Member> friendList = gson.fromJson(json, type);
        final ArrayList<Member> friendList2 = gson.fromJson(json2, type);

        int countCheck = 0;
        if (friendList2 != null && friendList != null) {
            for (Member obj : friendList) {
                for (Member obj2 : friendList2) {
                    if (Objects.equals(obj.getMemberUsername(), obj2.getMemberUsername())) {
                        obj.setCheckInvite(true);
                        countCheck++;
                    }
                }
            }
        }
        KickButton.setText(getResources().getString(R.string.text_invite) + " (" + String.valueOf(countCheck) + ")");

        iAdapter2 = new KickAdapter2(kick_list.this, friendList, KickButton);
        memberKickList.setAdapter(iAdapter2);
        searchKick.setOnQueryTextListener(kick_list.this);

        KickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iAdapter2.getInviteList().size() != 0) {
                    Gson gson = new Gson();
                    List<Member> sendFriendList = new ArrayList<Member>();

                    for (Member obj : iAdapter2.getInviteList()) {
                        if (obj.getMemberStatus()==3)
                            sendFriendList.add(obj);
                    }

                    String json = gson.toJson(sendFriendList);
                    mEdit1.putString("kickMemberList", json);
                    mEdit1.commit();
//                startActivity(new Intent(Invite_to_group.this,CreateGroup.class));
                    finish();
                } else
                    Toast.makeText(kick_list.this, getResources().getString(R.string.can_not_kick), Toast.LENGTH_SHORT).show();
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
