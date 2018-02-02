package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatchatapplication.Adapter.InviteAdapter;
import com.example.chatchatapplication.Not_Activity.ExpandedGridView;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.Friend;
import com.example.chatchatapplication.Object_json.Group2;
import com.example.chatchatapplication.Object_json.GroupSend;
import com.example.chatchatapplication.Object_json.groupUidRetrieve;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

public class CreateGroup extends AppCompatActivity implements jsonBack {

    StorageReference storageRef;

    InviteAdapter iAdapter;
    List<Friend> sendFriendList;
    String token;
    boolean checkSend;
    Uri resultUri;
    ArrayList<Friend> memberList = new ArrayList<Friend>();

    ExpandedGridView gridView;
    RelativeLayout groupImage, progress;
    SearchView groupName;
    ProgressBar progrssImage;
    CircleImageView circleImageGroup;
    TextView count, member_num;
    EditText groupPassword;

    SharedPreferences sp;
    SharedPreferences.Editor mEdit;

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
        getSupportActionBar().setTitle(R.string.title_create_group);
        setContentView(R.layout.activity_create_group);

        storageRef = FirebaseStorage.getInstance().getReference();

        mEdit = sp.edit();
        mEdit.putString("inviteFriendList", null);
        mEdit.commit();

        progress = (RelativeLayout) findViewById(R.id.wait_send);
        groupPassword = (EditText) findViewById(R.id.group_password);
        count = (TextView) findViewById(R.id.count_group_name);
        groupName = (SearchView) findViewById(R.id.group_name);
        circleImageGroup = (CircleImageView) findViewById(R.id.group_circle);
        groupImage = (RelativeLayout) findViewById(R.id.group_picture);
        progrssImage = (ProgressBar) findViewById(R.id.progress_image);
        gridView = (ExpandedGridView) findViewById(R.id.ex_grid_view);
        member_num = (TextView) findViewById(R.id.member_num);

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

        groupName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                count.setText(s.length() + "/20");
                if (s.length() > 20)
                    groupName.setQuery(s.substring(0, 20), false);
                return false;
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

            if (groupName.getQuery().toString().isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.toast_group_name_null), Toast.LENGTH_SHORT).show();
            } else if (groupPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.toast_group_password_null), Toast.LENGTH_SHORT).show();
            } else if (sendFriendList == null) {
                Toast.makeText(this, getResources().getString(R.string.toast_group_member_null), Toast.LENGTH_SHORT).show();
            } else if (groupName.getQuery().toString().length() < 4){
                Toast.makeText(this, getResources().getString(R.string.toast_group_name_more), Toast.LENGTH_SHORT).show();
            }else if (groupPassword.getText().toString().length() < 6){
                Toast.makeText(this, getResources().getString(R.string.toast_group_password_more), Toast.LENGTH_SHORT).show();
            } else {

                Gson sendJson = new Gson();
                progress.setVisibility(View.VISIBLE);
                Group2 data = new Group2();
                data.setGroupName(groupName.getQuery().toString());
                data.setGroupMember(memberList);
                data.setGroupOwner(sp.getString("username", null));

                String encryptPassword = Encoder.BuilderAES()
                        .message(groupPassword.getText().toString())
                        .method(AES.Method.AES_CBC_PKCS5PADDING)
                        .key("mit&24737")
                        .keySize(AES.Key.SIZE_128)
                        .iVector(sp.getString("username", "m"))
                        .encrypt();

                data.setGroupPassword(encryptPassword);
                data.setGroupMemberNum(memberList.size() - 1);
                token = sp.getString("token", null);
                GroupSend send = new GroupSend("Group", "getGroupUID", token, data);
                String sendJson2 = sendJson.toJson(send);
                checkSend = false;
                new SimpleHttpTask(CreateGroup.this).execute(sendJson2);
            }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String output) {

        if (!checkSend) {
            Gson gson = new Gson();
            final groupUidRetrieve data = gson.fromJson(output, groupUidRetrieve.class);
            if (data.getStatus() == 200) {
                if (resultUri == null) {
                    Gson sendJson = new Gson();
                    Group2 data2 = new Group2();
                    data2.setGroupImageURL("null");
                    data2.setGroupUID(data.getData().getGroupUID());
                    token = sp.getString("token", null);
                    GroupSend send = new GroupSend("Group", "createGroup", token, data2);
                    String sendJson2 = sendJson.toJson(send);
                    checkSend = true;
                    new SimpleHttpTask(CreateGroup.this).execute(sendJson2);
                } else {
                    StorageReference imagesRef = storageRef.child("GroupImage/" + data.getData().getGroupUID() + ".jpg");
                    imagesRef.putFile(resultUri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CreateGroup.this, "Failed upload group picture", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String pictureURL = taskSnapshot.getDownloadUrl().toString();
                            Gson sendJson = new Gson();
                            Group2 data2 = new Group2();
                            data2.setGroupImageURL(pictureURL);
                            data2.setGroupUID(data.getData().getGroupUID());
                            token = sp.getString("token", null);
                            GroupSend send = new GroupSend("Group", "createGroup", token, data2);
                            String sendJson2 = sendJson.toJson(send);
                            checkSend = true;
                            new SimpleHttpTask(CreateGroup.this).execute(sendJson2);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CreateGroup.this, "Uploading group picture ...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else
                Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();
            final searchRetrieve data3 = gson.fromJson(output, searchRetrieve.class);
            if (data3.getStatus() == 200) {
                finish();
                Toast.makeText(this, "Create group successful", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Failed to create group", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sp.getString("inviteFriendList", null);
        Type type = new TypeToken<List<Friend>>() {
        }.getType();
        sendFriendList = gson.fromJson(json, type);

        memberList.clear();
        Friend add = new Friend();
        add.setDisplayName("AddButton271137");
        memberList.add(add);

        if (sendFriendList != null) {
            for (int i = 0; sendFriendList.size() > i; i++) {
                if (sendFriendList.get(i).getCheckInvite())
                    memberList.add(sendFriendList.get(i));
            }
        }

        iAdapter = new InviteAdapter(this, memberList);
        gridView.setAdapter(iAdapter);
        gridView.setExpanded(true);

        if (memberList.size() != 0)
            member_num.setText(getResources().getString(R.string.text_member) + " " + (memberList.size()-1));
        else
            member_num.setText(getResources().getString(R.string.text_member) + " 0");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = sp.edit();

        mEdit.putString("inviteFriendList", null);
        mEdit.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
