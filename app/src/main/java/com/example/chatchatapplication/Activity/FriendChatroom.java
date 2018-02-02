package com.example.chatchatapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.chatchatapplication.Adapter.MessageAdapter;
import com.example.chatchatapplication.Object_json.messages;
import com.example.chatchatapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

public class FriendChatroom extends AppCompatActivity {
    private static final String AUTH_KEY = "key=AAAAM12Rtoo:APA91bEi2_JxY1-rYm1NpotgzNxQ7jZ2H6Jbsmnrw5-KUG0uJk5DkeXzXx1zAHS5KaBysTaUDyW0Xo7rCd4PmRlScZN2zfJ00WPLKATawVwdk46fWjE6RV6imtRpgvqtf8nNN6xBnOMi";

    ToggleButton send_bt;
    private ListView listview;
    LinearLayout progress_message;
    EditText text;

    public static ArrayList<messages> listMessage;
    DatabaseReference mMessagesRef;
    int chatroomUid;
    String friendUsername, friendDisplayName, username, friendDisplayPictureURL,friendRegistrationID,displayName,displayPictureURL;
    ArrayAdapter<messages> adapter;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onStart() {
        getSupportActionBar().setTitle(friendDisplayName);
        super.onStart();

        mMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMessage.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    messages user = userSnapshot.getValue(messages.class);
                    listMessage.add(user);
                    adapter = new MessageAdapter(FriendChatroom.this, R.layout.user_message_list, listMessage, username, friendDisplayPictureURL);
                }
                listview.setAdapter(adapter);

                progress_message.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
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
        setContentView(R.layout.activity_friend_chatroom);

        mEdit1 = sp.edit();

        username = sp.getString("username", null);
        displayName = sp.getString("displayName",null);
        displayPictureURL = sp.getString("displayPictureURL",null);

        chatroomUid = sp.getInt("chatroomUid",0);
        friendUsername = sp.getString("friendUsername",null);
        friendDisplayName = sp.getString("friendDisplayName",null);
        friendDisplayPictureURL = sp.getString("friendDisplayPictureURL",null);
        friendRegistrationID = sp.getString("friendRegistrationID", null);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mMessagesRef = mRootRef.child("Private Chat").child(String.valueOf(chatroomUid));

        progress_message = (LinearLayout) findViewById(R.id.progress_bar_message);
        listMessage = new ArrayList<messages>();
        listview = (ListView) findViewById(R.id.list_chat);
        text = (EditText) findViewById(R.id.editText);
        send_bt = (ToggleButton) findViewById(R.id.send_bt);

//        if(friendUsername==null)
//            friendDisplayName = friendUsername;
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessagesRef.setValue(listMessage);
                if (!text.getText().toString().trim().isEmpty()) {
                    sendNewMessage(text.getText().toString(), username);
                    text.setText("");                      //Clear input edittext panel
                    scrollMyListViewToBottom();
                } else {
                    Toast.makeText(FriendChatroom.this, "Must type any character first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNewMessage(String message, String userName) {
//        String id = mMessagesRef.push().getKey();
        String id = mMessagesRef.push().getKey();
        String encrypt = Encoder.BuilderAES()
                .message(message)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("mit&24737")
                .keySize(AES.Key.SIZE_128)
                .iVector(userName)
                .encrypt();
        messages messages = new messages(encrypt, userName);
        messages messages2 = new messages(encrypt, userName, displayName,displayPictureURL);
        sendWithOtherThread(messages2);
        mMessagesRef.child(id).setValue(messages);
    }

    private void sendWithOtherThread(final messages type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(messages type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", type.getMessageDisplayName());
            jNotification.put("body", type.getMessageText());
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_launcher");

//            jData.put("picture_url", "http://cdn.collider.com/wp-content/uploads/2017/04/my-little-pony-friendship-is-magic-season-7-image-5.png");

//            switch(type) {
//                case "tokens":
//                    JSONArray ja = new JSONArray();
//                    ja.put("dHiCHYYMo6o:APA91bE-Mpfrnd-rJyWmtnLtQdaop1xvaBhdvsv6FPQ0K_egENNCtLRft7jgU4W7wPYd8lhM1f-5kGzGnpbPUsbptBBgV9E2Nm_lZXBJ_Y8SnNr7VF4TtefPKpa9w_OFIYatnC0oqlnB");
//                    ja.put(FirebaseInstanceId.getInstance().getToken());
//                    jPayload.put("registration_ids", ja);
//                    break;
//                case "topic":
//                    jPayload.put("to", "/topics/news");
//                    break;
//                case "condition":
//                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
////                    break;
//                default:
            jPayload.put("to", friendRegistrationID);
//            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

//            Handler h = new Handler(Looper.getMainLooper());
//            h.post(new Runnable() {
//                @Override
//                public void run() {
//                    mTextView.setText(resp);
//                }
//            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this,MainActivity.class));
//        finishAffinity();
        finish();
    }
}
