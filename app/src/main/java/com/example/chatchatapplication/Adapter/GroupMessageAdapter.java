package com.example.chatchatapplication.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatchatapplication.Object_json.messages;
import com.example.chatchatapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

/**
 * Created by Neramit777 on 10/4/2017 at 5:35 PM.
 */

public class GroupMessageAdapter extends ArrayAdapter<messages> {
    private Context mContext;
    private ArrayList<messages> items;
    private String user, friendDisplayPictureURL,memberDisplayName;

    public GroupMessageAdapter(Context context, int textViewResourceId, ArrayList<messages> items, String user) {
        super(context, textViewResourceId, items);

        this.user = user;
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (items.get(position).getMessageUser().equals(user)) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.user_message_list, null);
        } else {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.othermember_message_list, null);

            friendDisplayPictureURL = items.get(position).getMessagePictureURL();
            memberDisplayName = items.get(position).getMessageDisplayName();

            CircleImageView otherImage = (CircleImageView) v.findViewById(R.id.other_image);
            TextView memberName = (TextView) v.findViewById(R.id.member_name);

            if (friendDisplayPictureURL != null) {
                Glide.with(mContext)
                        .load(friendDisplayPictureURL)  //Test
                        .into(otherImage);
            }

            if (memberDisplayName != null){
                memberName.setText(memberDisplayName);
            }
        }
        messages o = items.get(position);
        if (o != null) {
            TextView ms = (TextView) v.findViewById(R.id.message);
            TextView dt = (TextView) v.findViewById(R.id.date);

            String decrypted = Encoder.BuilderAES()
                    .message(items.get(position).getMessageText())
                    .method(AES.Method.AES_CBC_PKCS5PADDING)
                    .key("mit&24737")
                    .keySize(AES.Key.SIZE_128)
                    .iVector(items.get(position).getMessageUser())
                    .decrypt();

//            un.setText(items.get(position).getMessageUser());
            ms.setText(String.valueOf(decrypted));
//            dt.setText(String.valueOf(DateFormat.format("HH:mm",items.get(position).getMessageTime())));
            dt.setText(String.valueOf(DateFormat.format("dd/MM/yy\nHH:mm", items.get(position).getMessageTime())));
        }
        return v;
    }
}
