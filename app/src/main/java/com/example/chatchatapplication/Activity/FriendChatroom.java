package com.example.chatchatapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.chatchatapplication.Adapter.MessageAdapter;
import com.example.chatchatapplication.Object_json.messages;
import com.example.chatchatapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

public class FriendChatroom extends AppCompatActivity {

    DatabaseReference mMessagesRef;
    ToggleButton send_bt;
    private ListView listview;
    public static ArrayList<messages> listMessage;

    EditText text;

    String User;
    ArrayAdapter<messages> adapter;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onStart() {
        getSupportActionBar().setTitle(User);
        super.onStart();

        mMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMessage.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    messages user = userSnapshot.getValue(messages.class);
                    listMessage.add(user);
                    adapter = new MessageAdapter(FriendChatroom.this, R.layout.user_message_list, listMessage, User);
                }
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chatroom);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        User = sp.getString("displayName", null);
        if (User == null)
            User = sp.getString("username", null);

        int chatroomUid = getIntent().getExtras().getInt("chatroomUid");

//        sp = PreferenceManager.getDefaultSharedPreferences(this);
//        mEdit1 = sp.edit();

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mMessagesRef = mRootRef.child(String.valueOf(chatroomUid));

        listMessage = new ArrayList<messages>();
        listview = (ListView) findViewById(R.id.list_chat);
        text = (EditText) findViewById(R.id.editText);
        send_bt = (ToggleButton) findViewById(R.id.send_bt);

//        User = getIntent().getExtras().getString("user_name", "Anonymous");
//        User = sp.getString("displayName", null);
//        if(User==null)
//            User = sp.getString("username", null);
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessagesRef.setValue(listMessage);
                if (text.getText().toString().trim().isEmpty()) {
                } else {
                    sendNewMessage(text.getText().toString(), User);
                    text.setText("");                      //Clear input edittext panel
                    scrollMyListViewToBottom();
                }
            }
        });
    }

    private void sendNewMessage(String message, String userName) {
        String id = mMessagesRef.push().getKey();
        String encrypt = Encoder.BuilderAES()
                .message(message)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("mit&24737")
                .keySize(AES.Key.SIZE_128)
                .iVector(userName)
                .encrypt();
        messages messages = new messages(encrypt, userName);
        mMessagesRef.child(id).setValue(messages);
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
}
