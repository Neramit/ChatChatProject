package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriend extends AppCompatActivity implements jsonBack {

    SearchView searchView;
    LinearLayout extendView;
    CircularProgressButton button;
    String userName, token, displayName, target;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    LinearLayout.LayoutParams p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_friends);
        setContentView(R.layout.activity_add_friend);

        searchView = (SearchView) findViewById(R.id.friend_search);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        extendView = (LinearLayout) findViewById(R.id.extend_view);
//        mUserNameView = (EditText) findViewById(R.id.friend_search);
        button = (CircularProgressButton) findViewById(R.id.search_button);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                if (extendView.getChildCount() > 0) {
                    extendView.setVisibility(View.INVISIBLE);
//                    extendView.removeAllViews();
                    button.setProgress(0);
                }
            }
        });

        button.setIndeterminateProgressMode(true);
        button.setProgress(0);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                userName = searchView.getQuery().toString();
//                    searchView.setError(null);
                View focusView = null;
                boolean cancel = false;
                if (TextUtils.isEmpty(userName)) {
                    focusView = searchView;
                    cancel = true;
                } else if (!isUserNameValid(userName)) {
                    focusView = searchView;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    Gson sendJson = new Gson();
                    button.setProgress(50);
                    User data = new User();
                    data.setUsername(userName);
                    token = sp.getString("token", null);
                    registerSend send = new registerSend("Friend", "addFriendSearchButton", token, data);
                    String sendJson2 = sendJson.toJson(send);
                    target = "search";
                    new SimpleHttpTask(AddFriend.this).execute(sendJson2);
                }
                return false;
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Gson sendJson = new Gson();
                    button.setProgress(50);
                    User data = new User();
                    data.setUsername(userName);
                    token = sp.getString("token", null);
                    registerSend send = new registerSend("Friend", "addFriendAddButton", token, data);
                    String sendJson2 = sendJson.toJson(send);
                    target = "add";
                    new SimpleHttpTask(AddFriend.this).execute(sendJson2);
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);

        if (Objects.equals(target, "add")) {
            button.setProgress(0);
            if (data.getStatus() == 200) {
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(AddFriend.this, MainActivity.class));
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        finish();
                    }
                }, 700);
            } else if (data.getStatus() == 201) {
                button.setProgress(0);
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            extendView.setPadding(10, 10, 10, 10);
            p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p.weight = 0.4f;
            p.gravity = Gravity.CENTER_HORIZONTAL;
            extendView.setLayoutParams(p);
            extendView.setWeightSum(100);

            CircleImageView circleImageView = new CircleImageView(AddFriend.this);
            p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p.weight = 76;
            p.gravity = Gravity.CENTER_HORIZONTAL;
            if (data.getData().getDisplayPictureURL() == null) {
                circleImageView.setImageResource(R.drawable.default_user);
            } else {
                Glide.with(AddFriend.this)
                        .load(data.getData().getDisplayPictureURL())
                        .into(circleImageView);
            }
            circleImageView.setLayoutParams(p);
            extendView.addView(circleImageView);

            if (data.getStatus() == 200) {
                button.setProgress(100);
                searchNormal(data);
            } else if (data.getStatus() == 201) {
                button.setProgress(0);
                searchYouAlready(data);
            } else if (data.getStatus() == 202) {
                button.setProgress(100);
                searchUserAlready(data);
            } else if (data.getStatus() == 203) {
                button.setProgress(0);
                searchYouAlready(data);
            } else if (data.getStatus() == 204) {
                button.setProgress(100);
                searchNormal(data);
            } else {
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
                button.setProgress(-1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
//                    mUserNameView.setText("");
                        button.setProgress(0);
                    }
                }, 700);
            }
        }
    }

    private void searchNormal(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        p.weight = 50;
        p.gravity = Gravity.CENTER_HORIZONTAL;
        name.setLayoutParams(p);
        displayName = data.getData().getDisplayName();
        name.setText(displayName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        name.setTextSize(16);
        extendView.addView(name);
    }

    private void searchYouAlready(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 14;
        p.gravity = Gravity.CENTER_HORIZONTAL;
        name.setLayoutParams(p);
        displayName = data.getData().getDisplayName();
        name.setText(displayName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        name.setTextSize(16);
        extendView.addView(name);
        TextView message = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 10;
        p.gravity = Gravity.CENTER_HORIZONTAL;
        message.setLayoutParams(p);
        message.setText(data.getMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            message.setTextColor(getColor(R.color.dark_text));
        }
        message.setTextSize(16);
        extendView.addView(message);
        LinearLayout buttonView = (LinearLayout) findViewById(R.id.button_view);
        LinearLayout bigView = (LinearLayout) findViewById(R.id.big_view);
        bigView.removeView(buttonView);
    }

    private void searchUserAlready(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 14;
        p.gravity = Gravity.CENTER_HORIZONTAL;
        name.setLayoutParams(p);
        displayName = data.getData().getDisplayName();
        name.setText(data.getData().getDisplayName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        name.setTextSize(16);
        extendView.addView(name);
        TextView message = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 10;
        p.gravity = Gravity.CENTER_HORIZONTAL;
        message.setLayoutParams(p);
        message.setText(data.getMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        name.setTextSize(16);
        extendView.addView(name);
    }

    private boolean isUserNameValid(String userName) {
        //TODO: Replace this with your own logic
        return userName.length() >= 4 && userName.length() <= 20;
    }

    @Override
    public void onBackPressed() {
        finish();
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
}
