package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.chatchatapplication.Adapter.InviteAdapter;
import com.example.chatchatapplication.Not_Activity.ExpandedGridView;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroup extends AppCompatActivity {

    StorageReference storageRef;

    ExpandedGridView gridView;
    InviteAdapter iAdapter;
    RelativeLayout groupImage;
    ProgressBar progrssImage;
    CircleImageView circleImageGroup;

    Uri resultUri;
    ArrayList<Friend> memberList = new ArrayList<Friend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sp.getInt("Theme", 0);
        if (theme != 0) {
            Log.d("Test", Integer.toString(theme));
            setTheme(theme);
        }
        getSupportActionBar().setTitle(R.string.title_create_group);
        setContentView(R.layout.activity_create_group);

        storageRef = FirebaseStorage.getInstance().getReference();

        circleImageGroup = (CircleImageView) findViewById(R.id.group_circle);
        groupImage = (RelativeLayout) findViewById(R.id.group_picture);
        progrssImage = (ProgressBar) findViewById(R.id.progress_image);
        gridView = (ExpandedGridView) findViewById(R.id.ex_grid_view);

        Gson gson = new Gson();
        String json = sp.getString("inviteFriendList", null);
//        friendListRetrieve friendList = gson.fromJson(json, friendListRetrieve.class);
        Type type = new TypeToken<List<Friend>>() {
        }.getType();
        List<Friend> sendFriendList = gson.fromJson(json, type);

        Friend add = new Friend();
        add.setDisplayName("AddButton271137");
        memberList.add(add);

        if (sendFriendList != null) {
            for (int i = 0; sendFriendList.size() > i; i++) {
                    if (sendFriendList.get(i).getCheckInvite())
                        memberList.add(sendFriendList.get(i));
            }
//            for (Friend wp : sendFriendList) {
//                if (sendFriendList.get(pos).getCheckInvite())
//                    memberList.add(wp);
//            }
        }

        iAdapter = new InviteAdapter(this, memberList);
        gridView.setAdapter(iAdapter);
        gridView.setExpanded(true);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    startActivity(new Intent(CreateGroup.this, Invite_to_group.class));
                }
            }
        });

//        List<Friend> sendFriendList = iAdapter2.getInviteList();
//        Gson sendJson = new Gson();
//        inviteSend data = new inviteSend();
//        data = ;
//        inviteSend2 send = new inviteSend2("Forgot password", "enterEmail", data);
//        String sendJson2 = sendJson.toJson(send);
//        new SimpleHttpTask(Invite_to_group.this).execute(sendJson2);
    }

    public void setGroupPicture(View v) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setInitialCropWindowPaddingRatio(0)
                .setAspectRatio(1, 1)
                .setRequestedSize(350, 350)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    circleImageGroup.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_group) {
//            StorageReference imagesRef = storageRef.child("ProfileImage/" + username + ".jpg");
//            imagesRef.putFile(resultUri).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    groupImage.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String pictureURL = taskSnapshot.getDownloadUrl().toString();
//                    Gson sendJson = new Gson();
//                    User data = new User();
//                    data.setDisplayPictureURL(pictureURL);
//                    token = sp.getString("token", null);
//                    registerSend send = new registerSend("Other", "profileAccountDisplayPicture", token, data);
//                    String sendJson2 = sendJson.toJson(send);
//                    new SimpleHttpTask(ProfileAccount.this).execute(sendJson2);
//
//                    userImage.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    userImage.setVisibility(View.GONE);
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//            });
//            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//
//
//    }
}
