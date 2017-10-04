package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chatchatapplication.R;

public class SetPasscode_2 extends AppCompatActivity {

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    int input, now, passcode;

    ImageView c1, c2, c3, c4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();
        String theme = sp.getString("Theme", "Green");
        switch (theme) {
            case "Blue":
                setTheme(R.style.Blue_NoActionBar);
                break;
            case "Pink":
                setTheme(R.style.Pink_NoActionBar);
                break;
            case "Orange":
                setTheme(R.style.Orange_NoActionBar);
                break;
            default:
                setTheme(R.style.AppTheme_NoActionBar);
                break;
        }
        setContentView(R.layout.activity_set_passcode_2);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.enter_passcode_layout);
        switch (theme) {
            case "Blue":
                linearLayout.setBackgroundColor(getColor(R.color.colorPrimary_blue));
                break;
            case "Pink":
                linearLayout.setBackgroundColor(getColor(R.color.colorPrimary_pink));
                break;
            case "Orange":
                linearLayout.setBackgroundColor(getColor(R.color.colorPrimary_orange));
                break;
            default:
                linearLayout.setBackgroundColor(getColor(R.color.colorPrimary));
                break;
        }

        c1 = (ImageView) findViewById(R.id.circle_21);
        c2 = (ImageView) findViewById(R.id.circle_22);
        c3 = (ImageView) findViewById(R.id.circle_23);
        c4 = (ImageView) findViewById(R.id.circle_24);

        passcode = getIntent().getIntExtra("Passcode", 0);
    }

    public void check(int a) {
        if (input == 0) {
            input = 1;
            now = a;
            c1.setBackground(getResources().getDrawable(R.drawable.circle_en));
        } else if (input == 1) {
            input = 2;
            now = now * 10 + a;
            c2.setBackground(getResources().getDrawable(R.drawable.circle_en));
        } else if (input == 2) {
            input = 3;
            now = now * 10 + a;
            c3.setBackground(getResources().getDrawable(R.drawable.circle_en));
        } else if (input == 3) {
            c4.setBackground(getResources().getDrawable(R.drawable.circle_en));
            input = 4;
            now = now * 10 + a;
            if (passcode == now+1) {
                mEdit1.putInt("Passcode", now+1);
                mEdit1.apply();
                finish();
            } else {
                Toast.makeText(this, R.string.not_match_passcode, Toast.LENGTH_SHORT).show();
                c1.setBackground(getResources().getDrawable(R.drawable.circle_dis));
                c2.setBackground(getResources().getDrawable(R.drawable.circle_dis));
                c3.setBackground(getResources().getDrawable(R.drawable.circle_dis));
                c4.setBackground(getResources().getDrawable(R.drawable.circle_dis));
                input = 0;
                now = 0;
            }
        }
    }

    public void one(View view) {
        check(1);
    }

    public void two(View view) {
        check(2);
    }

    public void three(View view) {
        check(3);
    }

    public void four(View view) {
        check(4);
    }

    public void five(View view) {
        check(5);
    }

    public void six(View view) {
        check(6);
    }

    public void seven(View view) {
        check(7);
    }

    public void eight(View view) {
        check(8);
    }

    public void nine(View view) {
        check(9);
    }

    public void zero(View view) {
        check(0);
    }

    public void cancel(View view) {
        startActivity(new Intent(this, SetPasscode_1.class));
        finish();
    }

    public void backspace(View view) {
        if (input == 0) {
            // Nothing
        } else if(input == 1) {
            now = now / 10;
            input--;
            c1.setBackground(getResources().getDrawable(R.drawable.circle_dis));
        }else if(input == 2) {
            now = now / 10;
            input--;
            c2.setBackground(getResources().getDrawable(R.drawable.circle_dis));
        }else if(input == 3) {
            now = now / 10;
            input--;
            c3.setBackground(getResources().getDrawable(R.drawable.circle_dis));
        }
    }

}
