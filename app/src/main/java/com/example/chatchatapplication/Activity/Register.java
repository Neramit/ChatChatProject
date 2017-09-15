package com.example.chatchatapplication.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.simpleRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;
import com.kosalgeek.android.md5simply.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private AutoCompleteTextView mUserNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;

    private CircularProgressButton circularProgressButton;
    private HttpURLConnection conn;

    String userName, email, password;
    String salt = "a059a744729dfc7a4b4845109f591029";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        linearLayoutAll = (LinearLayout) findViewById(R.id.email_login_form);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.register_button);
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user_name);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRePasswordView = (EditText) findViewById(R.id.retype_password);

        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mUserNameView.setError(null);
                mEmailView.setError(null);
                mPasswordView.setError(null);

                userName = mUserNameView.getText().toString();
                email = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                String rePassword = mRePasswordView.getText().toString();
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
                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailAlready(email)) {
                    mEmailView.setError(getString(R.string.error_already_email));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    new SimpleTask().execute();
                }
            }
        });
    }

    private class SimpleTask extends AsyncTask<String, Void, String> {
//        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
//            linearLayoutAll.setGravity(Gravity.CENTER);
//            Toast.makeText(RegisterActivity.this, "Loading ...", Toast.LENGTH_SHORT).show();
//            progressBar = new ProgressBar(Register.this);
//            progressBar.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
//            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#1f3f99"), PorterDuff.Mode.MULTIPLY);
//            linearLayoutAll.addView(progressBar);
            circularProgressButton.setProgress(50);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();

            try {
                Gson sendJson = new Gson();

                String pw = MD5.encrypt(MD5.encrypt(password) + salt);

                User data = new User(userName, pw, email);
                registerSend send = new registerSend("Authentication", "register", data);
                String sendJson2 = sendJson.toJson(send);
                //HttpURLconnection methods
                URL url = new URL(getString(R.string.URL));
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("json", sendJson2));

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return result.toString();
        }

        protected void onPostExecute(final String jsonString) {
            // Dismiss ProgressBar
//            linearLayoutAll.setGravity(Gravity.NO_GRAVITY);
//            linearLayoutAll.removeView(progressBar);
            showData(jsonString);
        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private void showData(String jsonString) {

        Gson gson = new Gson();
        final simpleRetrieve data = gson.fromJson(jsonString, simpleRetrieve.class);
        if (data.getStatus() == 200) {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_LONG).show();
            circularProgressButton.setProgress(100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    startActivity(new Intent(Register.this, Login.class));
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                    mUserNameView.setText("");
                    mEmailView.setText("");
                    mPasswordView.setText("");
                    mRePasswordView.setText("");
                    circularProgressButton.setProgress(0);
                }
            }, 1000);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isEmailAlready(String email) {
        //TODO: Replace this with your own logic
        boolean value = true;

        return value;
    }

    private boolean isUserNameValid(String userName) {
        //TODO: Replace this with your own logic
        boolean value = true;

        return userName.length() >= 4 && userName.length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
