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

public class FP_EnterEmail extends AppCompatActivity implements jsonBack {

    private CircularProgressButton circularProgressButton;
    private HttpURLConnection conn;
    final Handler handler = new Handler();
    private String email;
    private EditText mEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sp.getInt("theme", 0);
        if (theme != 0) {
            setTheme(theme);
        }
        setContentView(R.layout.activity_fp__enter_email);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.ok_button);

        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);
        mEmailView = (EditText) findViewById(R.id.enter_email);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mEmailView.setError(null);

                email = mEmailView.getText().toString();
                View focusView = null;
                boolean cancel = false;

                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
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
                    registerSend send = new registerSend("Forgot password", "enterEmail", data);
                    String sendJson2 = sendJson.toJson(send);
                    circularProgressButton.setProgress(50);
                    new SimpleHttpTask(FP_EnterEmail.this).execute();
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
                    Intent intent = new Intent(FP_EnterEmail.this, FP_CheckEmail.class);
                    intent.putExtra("E-mail", email);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }, 1000);
        } else {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            circularProgressButton.setProgress(-1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    mEmailView.setText("");
                    circularProgressButton.setProgress(0);
                }
            }, 1000);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
