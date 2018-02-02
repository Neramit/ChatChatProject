package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatchatapplication.Adapter.KickAdapter;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.Object_json.Member;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.detailRecieve;
import com.example.chatchatapplication.Object_json.registerSend;
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

public class Member_group_detail extends AppCompatActivity implements jsonBack {

    StorageReference storageRef;

    CircleImageView circleImageGroup;
    TextView groupName, member_num;
    ListView listView;

    ArrayList<Member> member_list = new ArrayList<Member>();
    KickAdapter iAdapter;
    List<Member> sendFriendList;
    Uri resultUri;
    String token;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_group_detail));
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = sp.edit();
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
        setContentView(R.layout.activity_member_group_detail);
        storageRef = FirebaseStorage.getInstance().getReference();

        mEdit.putString("kickFriendList", null);
        mEdit.commit();

        groupName = (TextView) findViewById(R.id.group_name);
        circleImageGroup = (CircleImageView) findViewById(R.id.group_circle);
        listView = (ListView) findViewById(R.id.list_view);
        member_num = (TextView) findViewById(R.id.member_num);

        groupName.setText(sp.getString("groupName",""));  // Not sure
    }

    public void gotoKickList(View view) {
        startActivity(new Intent(this, kick_list.class));
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
        inflater.inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close_group) {

//            if (groupName.getQuery().toString().isEmpty()) {
//                Toast.makeText(this, getResources().getString(R.string.toast_group_name_null), Toast.LENGTH_SHORT).show();
//            } else if (groupPassword.getText().toString().isEmpty()) {
//                Toast.makeText(this, getResources().getString(R.string.toast_group_password_null), Toast.LENGTH_SHORT).show();
//            } else if (sendFriendList == null) {
//                Toast.makeText(this, getResources().getString(R.string.toast_group_member_null), Toast.LENGTH_SHORT).show();
//            } else if (groupName.getQuery().toString().length() < 4) {
//                Toast.makeText(this, getResources().getString(R.string.toast_group_name_more), Toast.LENGTH_SHORT).show();
//            } else if (groupPassword.getText().toString().length() < 6) {
//                Toast.makeText(this, getResources().getString(R.string.toast_group_password_more), Toast.LENGTH_SHORT).show();
//            } else {
//
//                Gson sendJson = new Gson();
//                progress.setVisibility(View.VISIBLE);
//                Group data = new Group();
//                data.setGroupName(groupName.getQuery().toString());
////                data.setGroupMember(memberList);
//                data.setGroupOwner(sp.getString("username", null));
//
//                String encryptPassword = Encoder.BuilderAES()
//                        .message(groupPassword.getText().toString())
//                        .method(AES.Method.AES_CBC_PKCS5PADDING)
//                        .key("mit&24737")
//                        .keySize(AES.Key.SIZE_128)
//                        .iVector(sp.getString("userName", "m"))
//                        .encrypt();
//
//                data.setGroupPassword(encryptPassword);
////                data.setGroupMemberNum(memberList.size() - 1);
//                token = sp.getString("token", null);
//                GroupSend send = new GroupSend("Group", "getGroupUID", token, data);
//                String sendJson2 = sendJson.toJson(send);
//                checkSend = "save";  // not sure
//                new SimpleHttpTask(Member_group_detail.this).execute(sendJson2);
//            }
//            StorageReference imagesRef = storageRef.child("GroupImage/" + groupName.getQuery().toString() + ".jpg");
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

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        detailRecieve data = gson.fromJson(output, detailRecieve.class);
        if (data.getStatus() == 200) {
            member_list.clear();
            int i = 0;
            while (data.getData().getMemberList().size() > i) {
                member_list.add(data.getData().getMemberList().get(i));
                i++;
            }
            if (member_list.size() == 0) {
//                friendList.setVisibility(View.GONE);
            } else {
                iAdapter = new KickAdapter(this, member_list);
                listView.setAdapter(iAdapter);
                String json = gson.toJson(data.getData().getMemberList());
                mEdit.putString("memberList", json);
                mEdit.commit();
            }

            if (member_list.size() != 0)
                member_num.setText(getResources().getString(R.string.text_member) + " " + member_list.size());
            else
                member_num.setText(getResources().getString(R.string.text_member) + " 0");
        } else {
//            friendList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sp.getString("kickFriendList", null);
        Type type = new TypeToken<List<Friend>>() {
        }.getType();
        sendFriendList = gson.fromJson(json, type);

        Gson sendJson = new Gson();
        final User data = new User();
        data.setGenNum(sp.getInt("groupUid", 0));
        token = sp.getString("token", null);
        registerSend send = new registerSend("GroupDetail", "enterGroupDetail", token, data);
        String sendJson2 = sendJson.toJson(send);
        new SimpleHttpTask(this).execute(sendJson2);

//        if (sendFriendList != null) {
//            for (int i = 0; sendFriendList.size() > i; i++) {
//                if (sendFriendList.get(i).getCheckInvite())
//                    memberList.add(sendFriendList.get(i));
//            }
//        }

        iAdapter = new KickAdapter(this, member_list);
        listView.setAdapter(iAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = sp.edit();

        mEdit.putString("kickFriendList", null);
        mEdit.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
