package com.example.chatchatapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.google.gson.Gson;
import com.kosalgeek.android.md5simply.MD5;

import java.net.HttpURLConnection;

public class Login extends AppCompatActivity implements jsonBack{

    private HttpURLConnection conn;

    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;

    private CircularProgressButton circularProgressButton;
    String userName, password;
    String salt = "a059a744729dfc7a4b4845109f591029";

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        circularProgressButton = (CircularProgressButton) findViewById(R.id.login_button);
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button buttonRegister = (Button) findViewById(R.id.register_button);
        TextView buttonForgot = (TextView) findViewById(R.id.forgot_button);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, FP_EnterEmail.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mUserNameView.setError(null);
                mPasswordView.setError(null);

                userName = mUserNameView.getText().toString();
                password = mPasswordView.getText().toString();
                View focusView = null;
                boolean cancel = false;

                if (TextUtils.isEmpty(userName)) {
                    mUserNameView.setError(getString(R.string.error_field_required));
                    focusView = mUserNameView;
                    cancel = true;
                } else if (!isUserNameValid(userName)) {
                    mUserNameView.setError(getString(R.string.error_invalid_userName));
                    focusView = mUserNameView;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)) {
                    mPasswordView.setError(getString(R.string.error_field_password));
                    focusView = mPasswordView;
                    cancel = true;
                } else if (!isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    Gson sendJson = new Gson();
                    String pw = MD5.encrypt(salt + MD5.encrypt(MD5.encrypt(password) + salt));
                    User user = new User(userName, pw);
                    registerSend send = new registerSend("Authentication", "login", user);
                    String sendJson2 = sendJson.toJson(send);
                    circularProgressButton.setProgress(50);
                    new SimpleHttpTask(Login.this).execute(sendJson2);
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
        if (data.getStatus() == 200) {
            mEdit1.putString("token", data.getData().getToken());
            mEdit1.putString("username", data.getData().getUsername());
            mEdit1.putString("email", data.getData().getEmail());
            mEdit1.putString("displayName", data.getData().getDisplayName());
            mEdit1.putString("displayPictureURL", data.getData().getDisplayPictureURL());
            mEdit1.putString("displayPicturePath", data.getData().getDisplayPicturePath());
            mEdit1.commit();
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            circularProgressButton.setProgress(100);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    startActivity(new Intent(Login.this, MainActivity.class));
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
                    circularProgressButton.setProgress(0);
                }
            }, 1000);

        }
    }

    private boolean isUserNameValid(String userName) {
        //TODO: Replace this with your own logic
        boolean value = true;

        return value;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_exit);
        builder.setIcon(R.drawable.logo2);
        builder.setMessage(R.string.message_exit);

        builder.setPositiveButton(R.string.Yes_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    System.exit(0);
                }
            }
        });
        builder.setNegativeButton(R.string.No_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
