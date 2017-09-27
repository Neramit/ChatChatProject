package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.example.chatchatapplication.Object_json.simpleRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.Objects;

public class FP_ResetPassword extends AppCompatActivity implements jsonBack {

    private EditText mPasswordView;
    private EditText mRePasswordView;

    private CircularProgressButton circularProgressButton;
    private HttpURLConnection conn;

    String salt = "a059a744729dfc7a4b4845109f591029";
    String password, rePassword, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sp.getInt("theme", 0);
        if (theme != 0) {
            setTheme(theme);
        }
        setContentView(R.layout.activity_fp__reset_password);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.ok_button);

        mPasswordView = (EditText) findViewById(R.id.password);
        mRePasswordView = (EditText) findViewById(R.id.retype_password);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("E-mail", null);

        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mPasswordView.setError(null);
                mRePasswordView.setError(null);

                password = mPasswordView.getText().toString();
                rePassword = mRePasswordView.getText().toString();
                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(password)) {
                    mPasswordView.setError(getString(R.string.error_field_password));
                    focusView = mPasswordView;
                    cancel = true;
                } else if (!isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                if (TextUtils.isEmpty(rePassword)) {
                    mRePasswordView.setError(getString(R.string.error_field_password));
                    focusView = mRePasswordView;
                    cancel = true;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Objects.equals(rePassword, password)) {
                        mRePasswordView.setError(getString(R.string.error_re_password));
                        focusView = mRePasswordView;
                        cancel = true;
                    }
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    circularProgressButton.setProgress(50);
                    new SimpleHttpTask(FP_ResetPassword.this).execute();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final simpleRetrieve data = gson.fromJson(output, simpleRetrieve.class);
        if (data.getStatus() == 200) {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_LONG).show();
            circularProgressButton.setProgress(100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    startActivity(new Intent(FP_ResetPassword.this, Login.class));
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
                    mPasswordView.setText("");
                    mRePasswordView.setText("");
                    circularProgressButton.setProgress(0);
                }
            }, 1000);

        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6 && password.length() <= 4;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
