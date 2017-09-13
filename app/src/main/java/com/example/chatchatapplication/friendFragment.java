package com.example.chatchatapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.chatchatapplication.Adapter.friendAdapter;

public class friendFragment extends Fragment {

    ListView friendList;
    private friendAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new friendAdapter(getActivity());
        View view = null;
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        friendList = (ListView) view.findViewById(R.id.friendListview);

        friendList.setAdapter(mAdapter);

//        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // TODO Auto-generated method stub
//
//                view.setBackgroundColor(Color.parseColor("#C5F9D4"));
//
//                Intent intent = null;
//                switch (((HospitalNames)list.getAdapter().getItem(position)).getHospitalName()) {
////                switch (hospitalNameList[position])
//                    case "โรงพยาบาลราษฎร์บูรณะ":
//                        intent = new Intent(Find.this, Hospital_1.class);
//                        break;
//                    case "โรงพยาบาลสุขสวัสดิ์":
//                        intent = new Intent(Find.this, Hospital_2.class);
//                        break;
//                    case "โรงพยาบาลประชาพัฒน์":
//                        intent = new Intent(Find.this, Hospital_3.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก":
//                        intent = new Intent(Find.this, Hospital_4.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก 3":
//                        intent = new Intent(Find.this, Hospital_6.class);
//                        break;
//                    case "โรงพยาบาลบางปะกอก 9":
//                        intent = new Intent(Find.this, Hospital_7.class);
//                        break;
//                    case "โรงพยาบาลเจริญกรุง":
//                        intent = new Intent(Find.this, Hospital_5.class);
//                        break;
//                    case "โรงพยาบาลสมิติเวช":
//                        intent = new Intent(Find.this, Hospital_8.class);
//                        break;
//                    case "โรงพยาบาลบํารุงราษฎร์":
//                        intent = new Intent(Find.this, Hospital_9.class);
//                        break;
//                    case "โรงพยาบาลศิริราช":
//                        intent = new Intent(Find.this, Hospital_10.class);
//                        break;
//                    default:
//                        intent = new Intent(Find.this, Hospital_11.class);
//                        break;
//                }
//                if (intent != null) {
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                    finish();
//                }
//            }
//        });

        return view;
    }
}