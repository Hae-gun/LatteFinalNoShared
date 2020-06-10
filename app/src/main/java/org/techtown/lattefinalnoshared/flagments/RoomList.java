package org.techtown.lattefinalnoshared.flagments;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.RoomListData;
import org.techtown.lattefinalnoshared.adapter.RecyclerAdapter;

import java.util.Arrays;
import java.util.List;

public class RoomList extends Fragment {



    private RecyclerAdapter adapter;


//    private ImageView roomListImg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_room_list, container, false);
//        roomListImg = rootView.findViewById(R.id.roomListImg);
        Log.i("roomList","onCreateView() start |||"+this.hashCode());
//        Glide.with(this).load("https://i.imgur.com/T7KCxZj.jpg").into(roomListImg);

        RecyclerView recyclerView =rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        getData();


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent i = new Intent("toService");
        i.putExtra("request","roomList");
        LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(i);
        Log.i("roomList","onStart() end |||" + this.hashCode());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("roomList","onDestroy() start |||"+this.hashCode());
    }

    public void getData(){
        List<String> listTitle = Arrays.asList("1","2","3","4");
        List<String> listDate = Arrays.asList("11","22","33","44");
        List<String> listContent = Arrays.asList("111","222","333","444");
        List<String> listImgUrl = Arrays.asList("https://i.imgur.com/4LQp6RH.jpg",
                "https://i.imgur.com/T7KCxZj.jpg",
                "https://i.imgur.com/4LQp6RH.jpg",
                "https://i.imgur.com/T7KCxZj.jpg");
        for(int i=0; i<listTitle.size();i++){
            RoomListData data = new RoomListData(
                    listTitle.get(i),
                    listDate.get(i),
                    listContent.get(i),
                    listImgUrl.get(i)

            );
            adapter.addItem(data);

        }
        adapter.notifyDataSetChanged();
    }
}
