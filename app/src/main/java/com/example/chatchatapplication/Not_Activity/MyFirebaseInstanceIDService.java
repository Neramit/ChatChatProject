package com.example.chatchatapplication.Not_Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

/**
 * Created by Neramit777 on 9/22/2017 at 3:14 PM.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements jsonBack{
    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences sp;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Gson sendJson = new Gson();
        User data = new User();
        data.setDisplayPictureURL(token);
        token = sp.getString("token", null);
        registerSend send = new registerSend("Authentication", "firebase", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(MyFirebaseInstanceIDService.this).execute(sendJson2);

    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);
        Log.d("Status -------------------------------->",data.getMessage());
    }
}
