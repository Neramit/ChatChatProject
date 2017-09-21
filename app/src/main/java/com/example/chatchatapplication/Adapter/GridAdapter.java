package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Neramit777 on 9/7/2017.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private View view;
    private String imageUrl;

    public GridAdapter(Context c, String imageURL) {
        mContext = c;
        imageUrl = imageURL;
    }

//    public GridAdapter(View view) {
//        this.view = view;
//    }

    public int getCount() {
        return 2;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        CircleImageView circleImageView = null;

        WindowManager windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int gridSize = width / 2;

        LinearLayout All = new LinearLayout(mContext);
        All.setOrientation(LinearLayout.VERTICAL);
        All.setLayoutParams(new LinearLayout.LayoutParams(gridSize, gridSize));
        All.setWeightSum(1f);
        All.setPadding(10,10,10,10);

        // if it's not recycled, initialize some attributes
        circleImageView = new CircleImageView(mContext);
        circleImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.8f));
//            circleImageView.setLayoutParams(new GridView.LayoutParams(gridSize, gridSize));
        circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView title = new TextView(mContext);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.2f));
//        title.setTextSize(18);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        if (position == 0) {
            if (imageUrl != null) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .into(circleImageView);
            } else
                circleImageView.setImageResource(R.drawable.default_user);
            title.setText(R.string.title_profile_account);
            circleImageView.setPadding(10,10,10,10);
        } else {
            circleImageView.setImageResource(R.drawable.setting_icon);
            title.setText(R.string.title_Setting);
        }

        All.addView(circleImageView);
        All.addView(title);

        return All;
    }
}
