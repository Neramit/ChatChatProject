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
import com.example.chatchatapplication.Object_json.Member;
import com.example.chatchatapplication.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroup extends AppCompatActivity {

    StorageReference storageRef;

    ExpandedGridView gridView;
    InviteAdapter iAdapter;
    RelativeLayout groupImage;
    ProgressBar progrssImage;
    CircleImageView circleImageGroup;

    Uri resultUri;
    ArrayList<Member> memberList = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sp.getInt("Theme", 0);
        if (theme != 0) {
            Log.d("Test",Integer.toString(theme));
            setTheme(theme);
        }
        getSupportActionBar().setTitle(R.string.title_create_group);
        setContentView(R.layout.activity_create_group);

        storageRef = FirebaseStorage.getInstance().getReference();

        circleImageGroup = (CircleImageView) findViewById(R.id.group_circle);
        groupImage = (RelativeLayout) findViewById(R.id.group_picture);
        progrssImage = (ProgressBar) findViewById(R.id.progress_image);
        gridView = (ExpandedGridView) findViewById(R.id.ex_grid_view);

        Member add = new Member("Add Button",4);
        memberList.add(add);
        iAdapter = new InviteAdapter(this,memberList);
        gridView.setAdapter(iAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(position==0){
                    startActivity(new Intent(CreateGroup.this,Invite_to_group.class));
                }
            }
        });
    }

    public void setGroupPicture(View v) {
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
                resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    circleImageGroup.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                File file = new File(resultUri.toString().replace("file://", "" ));
//
//                Bitmap b = null;
//                try {
//                    b = new ImageZipper(this)
//                            .setQuality(100)
//                            .setMaxWidth(200)
//                            .setMaxHeight(200)
//                            .compressToBitmap(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), b, "Title", null);
//                resultUri = Uri.parse(path);
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
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
