package com.example.chatchatapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Not_Activity.SimpleHttpTask;
import com.example.chatchatapplication.Not_Activity.jsonBack;
import com.example.chatchatapplication.Object_json.User;
import com.example.chatchatapplication.Object_json.registerSend;
import com.example.chatchatapplication.Object_json.searchRetrieve;
import com.example.chatchatapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAccount extends AppCompatActivity implements jsonBack {

    TextView displayName, userName, Email;
    LinearLayout topLinear, displaynameButton;
    LinearLayout.LayoutParams p;
    String displayname, username, email, displayImage, token;
    CircleImageView userImage;

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    StorageReference storageRef;
    UploadTask mUploadTask;
    ProgressBar progressBar;

    private static int RESULT_LOAD_IMAGE = 2;

    @Override
    protected void onStart() {
        super.onStart();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();

        displayname = sp.getString("displayName", null);
        username = sp.getString("username", null);
        email = sp.getString("email", null);
        displayImage = sp.getString("displayPictureURL", null);

        if (displayName != null) {
            displayName.setText(displayname);
        } else {
            displayName.setText(username);
        }

        if (displayImage != null) {
            Glide.with(ProfileAccount.this)
                    .load(displayImage)
                    .into(userImage);
        }

        userName.setText(username);
        Email.setText(email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();
        String theme = sp.getString("Theme", "Green");
        switch (theme) {
            case "Blue":
                setTheme(R.style.Blue);
                break;
            case "Pink":
                setTheme(R.style.Blue);
                break;
            case "Orange":
                setTheme(R.style.Orange);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
        setContentView(R.layout.activity_profile_account);

        getSupportActionBar().setIcon(R.drawable.profile_icon);
        getSupportActionBar().setTitle(R.string.title_profile);
        storageRef = FirebaseStorage.getInstance().getReference();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_user_image);
        userImage = (CircleImageView) findViewById(R.id.user_image);
        topLinear = (LinearLayout) findViewById(R.id.topLinear);
        displaynameButton = (LinearLayout) findViewById(R.id.displaynameButton);
        displayName = (TextView) findViewById(R.id.displayname);
        userName = (TextView) findViewById(R.id.user_name);
        Email = (TextView) findViewById(R.id.Email);
    }

    public void changeDisplayname(View v) {
        startActivity(new Intent(this, ChangeDisplayname.class));
    }

    public void changeUserPicture(View v) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setInitialCropWindowPaddingRatio(0)
                .setAspectRatio(1, 1)
                .setRequestedSize(350,350)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();

                StorageReference imagesRef = storageRef.child("ProfileImage/" + username + ".jpg");
                imagesRef.putFile(resultUri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        userImage.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String pictureURL = taskSnapshot.getDownloadUrl().toString();

                        Gson sendJson = new Gson();
                        User data = new User();
                        data.setDisplayPictureURL(pictureURL);
                        token = sp.getString("token", null);
                        mEdit1.putString("displayPictureURL",pictureURL);
                        mEdit1.commit();
                        registerSend send = new registerSend("Other", "profileAccountDisplayPicture", token, data);
                        String sendJson2 = sendJson.toJson(send);
                        new SimpleHttpTask(ProfileAccount.this).execute(sendJson2);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileAccount.this.getContentResolver(), resultUri);
                            userImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        userImage.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        userImage.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        final searchRetrieve data = gson.fromJson(output, searchRetrieve.class);

        if (data.getStatus() == 200) {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
            mEdit1.putString("displayPictureURL", data.getData().getDisplayPictureURL());
            mEdit1.commit();
        } else {
            Toast.makeText(this, data.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAccount(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete_account);
        builder.setIcon(R.drawable.logo2);
        builder.setMessage(R.string.text_sure_to_delete_account);

        builder.setPositiveButton(R.string.but_next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ProfileAccount.this,Confirm_delete_account.class));
            }
        });
        builder.setNegativeButton(R.string.but_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
