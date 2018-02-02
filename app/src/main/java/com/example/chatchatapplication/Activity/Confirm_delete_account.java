package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.simpleRetrieve;
import com.example.chatchatapplication.R;
import com.google.gson.Gson;

public class Confirm_delete_account extends AppCompatActivity implements jsonBack {

    CheckBox yesDelete;
    CircularProgressButton deleteButton;

    // Shared preferrence
    SharedPreferences sp;

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
        setContentView(R.layout.activity_confirm_delete_account);
        yesDelete = (CheckBox) findViewById(R.id.chk_delete);
        deleteButton = (CircularProgressButton) findViewById(R.id.delete_button);

        deleteButton.setIndeterminateProgressMode(true);
        deleteButton.setProgress(0);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yesDelete.isChecked()) {
                    deleteButton.setProgress(50);
                    Gson sendJson = new Gson();
                    User user = new User();
                    String token = sp.getString("token", null);
                    registerSend send = new registerSend("Other", "deleteAccount", token, user);
                    String sendJson2 = sendJson.toJson(send);
                    new SimpleHttpTask(Confirm_delete_account.this).execute(sendJson2);
                } else
                    Toast.makeText(Confirm_delete_account.this, R.string.checkbox_before, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        simpleRetrieve data = gson.fromJson(output, simpleRetrieve.class);
        if (data.getStatus() == 200) {
            deleteButton.setProgress(100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Confirm_delete_account.this, Login.class));
                    finishAffinity();
                }
            }, 1000);

        }
    }
}
