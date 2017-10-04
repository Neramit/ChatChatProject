package com.example.chatchatapplication.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kosalgeek.android.md5simply.MD5;

import java.net.HttpURLConnection;
import java.util.Objects;

public class Login extends AppCompatActivity implements jsonBack {

    private HttpURLConnection conn;

    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private CircularProgressButton circularProgressButton;
    private CheckBox remember;

    String userName, password, rememberUsername, rememberPassword;
    String salt = "a059a744729dfc7a4b4845109f591029";

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
        setContentView(R.layout.activity_login);

        mEdit1 = sp.edit();

        circularProgressButton = findViewById(R.id.login_button);
        mUserNameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        remember = findViewById(R.id.remember);

        Button buttonRegister = findViewById(R.id.register_button);
        TextView buttonForgot = findViewById(R.id.forgot_button);

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

        rememberUsername = sp.getString("remember_username", "");
        rememberPassword = sp.getString("remember_password", "");
        if (!Objects.equals(rememberPassword, "") && !Objects.equals(rememberUsername, "")) {
//            userName = rememberUsername;
//            password = rememberPassword;
//            connect();
            mUserNameView.setText(rememberUsername);
            mPasswordView.setText(rememberPassword);
            remember.setChecked(true);
        }

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
                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "No internet connection\nplease connect the internet.", Snackbar.LENGTH_INDEFINITE).setAction("X", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    if (checkOnlineState()) {
                        if (snackbar.isShown())
                            snackbar.dismiss();
                        connect();
                    } else {
                        snackbar.show();
                    }
                }
            }
        });

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!remember.isChecked()) {
                    mEdit1.remove("remember_username");
                    mEdit1.remove("remember_password");
                    mEdit1.apply();
                }
            }
        });
    }

    public void connect() {
        Gson sendJson = new Gson();
        String pw = MD5.encrypt(salt + MD5.encrypt(MD5.encrypt(password) + salt));
        User user = new User(userName, pw);
        user.setRegistrationID(FirebaseInstanceId.getInstance().getToken());
        registerSend send = new registerSend("Authentication", "login", user);
        String sendJson2 = sendJson.toJson(send);
        circularProgressButton.setProgress(50);
        new SimpleHttpTask(Login.this).execute(sendJson2);
    }

    public boolean checkOnlineState() {
        ConnectivityManager CManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        return NInfo != null && NInfo.isConnectedOrConnecting();
    }

    @Override
    public void processFinish(String output) {
        if (Objects.equals(output, "")) {
            circularProgressButton.setProgress(0);
            circularProgressButton.setText("Can't connect server");
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Server maintenance or server down\nplease login later", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connect();
                }
            });
            snackbar.show();
        } else {
            Gson gson = new Gson();
            final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);

            if (data.getStatus() == 200) {
                mEdit1.putString("token", data.getData().getToken());
                mEdit1.putString("username", data.getData().getUsername());
                mEdit1.putString("email", data.getData().getEmail());
                mEdit1.putString("displayName", data.getData().getDisplayName());
                mEdit1.putString("displayPictureURL", data.getData().getDisplayPictureURL());
                mEdit1.commit();
//                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
                circularProgressButton.setProgress(100);

                if (remember.isChecked()) {
                    mEdit1.putString("remember_username", userName);
                    mEdit1.putString("remember_password", password);
                    mEdit1.commit();
                }

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
    }

    private boolean isUserNameValid(String userName) {
        //TODO: Replace this with your own logic
        boolean value = true;

        return value;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6 && password.length() <= 20;
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
                finishAndRemoveTask();
//                finishAffinity();
                System.exit(0);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
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
