package com.example.chatchatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.dd.CircularProgressButton;

public class ChangeDisplayname extends AppCompatActivity {

    SearchView displayName;
    String displayname;
    CircularProgressButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_displayname);

        displayName = (SearchView) findViewById(R.id.edittextDisplayname);
        button = (CircularProgressButton) findViewById(R.id.ok_button);

        button.setProgress(0);
        button.setIndeterminateProgressMode(true);

        displayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName.setIconified(false);
//                if (extendView.getChildCount() > 0) {
//                    extendView.setVisibility(View.INVISIBLE);
////                    extendView.removeAllViews();
//                    button.setProgress(0);
//                }
            }
        });

        // Remove icon search
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) displayName.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);

    }
}
