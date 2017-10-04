package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.simpleRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.net.HttpURLConnection;

public class FP_CheckEmail extends AppCompatActivity implements jsonBack {

    private CircularProgressButton circularProgressButton;
    private HttpURLConnection conn;
    final Handler handler = new Handler();
    private int genNum;
    private String email;
    private EditText mGenNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
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
        setContentView(R.layout.activity_fp__check_email);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.ok_button);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("E-mail", null);

        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);
        mGenNumView = (EditText) findViewById(R.id.enter_email);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mGenNumView.setError(null);

                genNum = Integer.parseInt(mGenNumView.getText().toString());
                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(mGenNumView.getText().toString())) {
                    mGenNumView.setError(getString(R.string.error_field_password));
                    focusView = mGenNumView;
                    cancel = true;
                } else if (!isPasswordValid(mGenNumView.getText().toString())) {
                    mGenNumView.setError(getString(R.string.error_invalid_password));
                    focusView = mGenNumView;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    Gson sendJson = new Gson();
                    User data = new User();
                    data.setEmail(email);
                    data.setGenNum(genNum);
                    registerSend send = new registerSend("Forgot password", "checkEmail", data);
                    String sendJson2 = sendJson.toJson(send);
                    circularProgressButton.setProgress(50);
                    new SimpleHttpTask(FP_CheckEmail.this).execute(sendJson2);
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final simpleRetrieve data = gson.fromJson(output, simpleRetrieve.class);
        if (data.getStatus() == 200) {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            circularProgressButton.setProgress(100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(FP_CheckEmail.this, FP_ResetPassword.class);
                    intent.putExtra("E-mail", email);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }, 1000);
        } else if (data.getStatus() == 500) {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            circularProgressButton.setProgress(-1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    startActivity(new Intent(FP_CheckEmail.this, FP_EnterEmail.class));
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    finish();
                }
            }, 1000);

        } else {
            Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
            circularProgressButton.setProgress(-1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    mGenNumView.setText("");
                    circularProgressButton.setProgress(0);
                }
            }, 1000);
        }
    }

    private boolean isPasswordValid(String genNum) {
        //TODO: Replace this with your own logic
        return genNum.length() >= 6;
    }


}
