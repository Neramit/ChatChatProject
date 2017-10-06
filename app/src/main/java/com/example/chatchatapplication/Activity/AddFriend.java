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
import android.widget.ProgressBar;
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
    LinearLayout.LayoutParams p;
    ProgressBar progressBarAdd;

    String userName, token, displayName, target;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
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
        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_friends);
        searchView = (SearchView) findViewById(R.id.friend_search);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        extendView = (LinearLayout) findViewById(R.id.extend_view);
//        mUserNameView = (EditText) findViewById(R.id.friend_search);
        button = (CircularProgressButton) findViewById(R.id.search_button);
        progressBarAdd = (ProgressBar) findViewById(R.id.progress_bar_add);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                if (extendView.getChildCount() > 0) {
//                    extendView.setVisibility(View.GONE);
                    button.setProgress(0);
                }
            }
        });

        button.setIndeterminateProgressMode(true);
        button.setProgress(0);
        progressBarAdd.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                userName = searchView.getQuery().toString();
                View focusView = null;
                boolean cancel = false;
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(AddFriend.this, "Please type username.", Toast.LENGTH_SHORT).show();
                    focusView = searchView;
                    cancel = true;
                } else if (!isUserNameValid(userName)) {
                    Toast.makeText(AddFriend.this, "Must have more than 3 letters", Toast.LENGTH_SHORT).show();
                    focusView = searchView;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    Gson sendJson = new Gson();
//                    button.setProgress(50);
                    extendView.removeAllViews();
                    progressBarAdd.setVisibility(View.VISIBLE);
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
            progressBarAdd.setVisibility(View.GONE);
            if (data.getStatus() == 200||data.getStatus() == 203) {
                button.setProgress(100);
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
            } else {
                button.setProgress(-1);
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            extendView.setPadding(10, 10, 10, 10);
            p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            p.weight = 0.7f;
            p.gravity = Gravity.CENTER_HORIZONTAL;
            extendView.setLayoutParams(p);
            extendView.setWeightSum(1);

            progressBarAdd.setVisibility(View.GONE);

            if (data.getStatus() == 200) {
                getCircleImage(data);
                searchNormal(data);
            } else if (data.getStatus() == 201) {
                getCircleImage(data);
                searchNormal(data);
            } else if (data.getStatus() == 202) {
                getCircleImage(data);
                searchUserAlready(data);
            } else if (data.getStatus() == 203) {
                getCircleImage(data);
                searchYouAdd(data);
            } else if (data.getStatus() == 204) {
                getCircleImage(data);
                searchYouAdd(data);
            } else if (data.getStatus() == 401) {
                getCircleImage(data);
                searchNormal(data);
            } else {
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCircleImage (searchRetrieve data){
        CircleImageView circleImageView = new CircleImageView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        p.weight = 0.7f;
        p.gravity = Gravity.CENTER;
        String targetDisplayPictureURL = data.getData().getDisplayPictureURL();
        if (targetDisplayPictureURL == null) {
            circleImageView.setImageResource(R.drawable.default_user);
        } else {
            Glide.with(AddFriend.this)
                    .load(targetDisplayPictureURL)
                    .into(circleImageView);
        }
        circleImageView.setLayoutParams(p);
        extendView.addView(circleImageView);
    }

    private void searchNormal(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER;
        p.weight = 0.1f;
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
        p.weight = 0.2f;
        p.gravity = Gravity.CENTER;
        message.setLayoutParams(p);
        message.setText(data.getMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            message.setTextColor(getColor(R.color.dark_text));
        }
        message.setTextSize(16);
        extendView.addView(message);
        button.setVisibility(View.INVISIBLE);
    }

    private void searchYouAdd(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 0.3f;
        p.gravity = Gravity.CENTER;
        name.setLayoutParams(p);
        displayName = data.getData().getDisplayName();
        name.setText(displayName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        name.setTextSize(16);
        extendView.addView(name);
        button.setVisibility(View.VISIBLE);
    }

    private void searchUserAlready(searchRetrieve data) {
        TextView name = new TextView(AddFriend.this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 0.1f;
        p.gravity = Gravity.CENTER;
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
        p.weight = 0.2f;
        p.gravity = Gravity.CENTER;
        message.setLayoutParams(p);
        message.setText(data.getMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextColor(getColor(R.color.dark_text));
        }
        message.setTextSize(16);
        extendView.addView(message);
        button.setVisibility(View.VISIBLE);
    }

    private boolean isUserNameValid(String userName) {
        //TODO: Replace this with your own logic
        return userName.length() >= 3 && userName.length() <= 20;
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
