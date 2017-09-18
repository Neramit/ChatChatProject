package com.example.chatchatapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

public class ChangeDisplayname extends AppCompatActivity implements jsonBack {

    SearchView displayName;
    String displayname, token, target;
    CircularProgressButton button;
    TextView count;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_dislayname);
        setContentView(R.layout.activity_change_displayname);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        displayName = (SearchView) findViewById(R.id.edittextDisplayname);
        button = (CircularProgressButton) findViewById(R.id.ok_button);
        count = (TextView) findViewById(R.id.countDisplayname);

        button.setProgress(0);
        button.setIndeterminateProgressMode(true);

        displayName.setQuery(displayname, false);

        displayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName.setIconified(false);
            }
        });

        displayName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                count.setText(s.length() + "/20");
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayname = displayName.getQuery().toString();
                if (displayname.trim().length() > 3 && displayname.trim().length() < 21) {
                    Gson sendJson = new Gson();
                    button.setProgress(50);
                    User data = new User();
                    data.setDisplayName(displayname);
                    token = sp.getString("token", null);
                    registerSend send = new registerSend("Other", "profileAccountDisplayName", token, data);
                    String sendJson2 = sendJson.toJson(send);
                    new SimpleHttpTask(ChangeDisplayname.this).execute(sendJson2);
                } else {
                    Toast.makeText(ChangeDisplayname.this, R.string.toast_displayname_minimum, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);

        if (data.getStatus() == 200) {
            button.setProgress(100);
            mEdit1.putString("displayName", displayname);
            mEdit1.commit();
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 700);

        } else {
            button.setProgress(-1);
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setProgress(0);
                }
            }, 700);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
