package com.example.chatchatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAccount extends AppCompatActivity {

    TextView displayName, username;
    LinearLayout topLinear;
    LinearLayout.LayoutParams p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setIcon(R.drawable.profile_icon);
        getSupportActionBar().setTitle(R.string.title_profile);
        setContentView(R.layout.activity_profile_account);

        topLinear = (LinearLayout) findViewById(R.id.topLinear);
        displayName = (TextView) findViewById(R.id.displayname);
        username = (TextView) findViewById(R.id.user_name);

        CircleImageView circleImageView = new CircleImageView(this);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        circleImageView.setAdjustViewBounds(true);
        circleImageView.setImageResource(R.drawable.default_user);
        circleImageView.setLayoutParams(p);
        topLinear.addView(circleImageView);
    }

    public void changeDisplayname(View v) {
        startActivity(new Intent(this,ChangeDisplayname.class));
    }
}
