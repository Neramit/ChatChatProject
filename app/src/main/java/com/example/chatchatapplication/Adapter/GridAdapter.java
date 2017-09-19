package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

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
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            circleImageView = new CircleImageView(mContext);
            circleImageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
//            circleImageView.setAdjustViewBounds(true);
            circleImageView.setPadding(8, 8, 8, 8);
        } else {
            circleImageView = new CircleImageView(mContext);
            circleImageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
//            circleImageView.setAdjustViewBounds(true);
            circleImageView.setPadding(8, 8, 8, 8);
        }

        if (position == 0) {
            if (imageUrl != null) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .into(circleImageView);
            } else
                circleImageView.setImageResource(R.drawable.default_user);
        } else {
            circleImageView.setImageResource(R.drawable.setting_icon);
        }
        return circleImageView;
    }
}
