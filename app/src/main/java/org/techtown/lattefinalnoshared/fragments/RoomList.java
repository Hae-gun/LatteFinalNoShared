package org.techtown.lattefinalnoshared.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.LatteMessage;
import org.techtown.lattefinalnoshared.VO.Reservation;
import org.techtown.lattefinalnoshared.VO.RoomListData;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;
import org.techtown.lattefinalnoshared.adapter.RecyclerAdapter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomList extends Fragment {



    private RecyclerAdapter adapter;
    private SingletoneVO vo = SingletoneVO.getInstance();
    private BroadcastReceiver listBroadCast;
    private TextView userID;
    private RecyclerView recyclerView;

    private Gson gson =  MainActivity.gson;
//    private Gson dateGson = new Gson();

//    private ImageView roomListImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_room_list, container, false);
//        roomListImg = rootView.findViewById(R.id.roomListImg);
//        Log.i("roomList","onCreateView() start |||"+this.hashCode());
//        Glide.with(this).load("https://i.imgur.com/T7KCxZj.jpg").into(roomListImg);
        userID = rootView.findViewById(R.id.userID);



        getData(this,rootView);





        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        LatteMessage lmsg = new LatteMessage(vo.getUserNo(),"ReserveList",null,null);
        sendToService("toService", "request", gson.toJson(lmsg));

//        LatteMessage lmsg = new LatteMessage(vo.getUserNo(),"ReserveList",null,null);
//
//
//
//        Intent i = new Intent("toService");
//        i.putExtra("request","roomList");
//        LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(i);
//        Log.i("roomList","onStart() end |||" + this.hashCode());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("roomList","onDestroy() start |||"+this.hashCode());
    }

    public void getData(Fragment frag,ViewGroup rootView){

        Fragment fragment = frag;
        recyclerView =rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter(frag);
        recyclerView.setAdapter(adapter);

        listBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("Setting")!=null){
                    String data = intent.getStringExtra("Setting");
                    LatteMessage lmsg = gson.fromJson(data,LatteMessage.class);
//                    Log.i("inList","Msg : "+data);
//                    Log.i("inList","json : "+lmsg.getJsonData());

//                    Reservation[] reservations = dateGson.fromJson(lmsg.getJsonData(),Reservation[].class);
//                    ArrayList<Reservation> list = dateGson.fromJson(lmsg.getJsonData(),
//                            new TypeToken<ArrayList<Reservation>>(){}.getType());
                    Reservation[] rlist =  gson.fromJson(lmsg.getJsonData(),Reservation[].class);
                    List<Reservation> list = Arrays.asList(rlist);
                    userID.setText(vo.getId());
                    for(Reservation res : list){
//                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                        int result = (int)((res.getEndDate().getTime()- res.getStartDate().getTime())/(3600000*24));

//                        Date dResult = new Date(result);

//                        Log.i("calDate","start: "+res.getStartDate());
//                        Log.i("calDate","end: "+res.getEndDate());
//                        Log.i("calDate",""+result);
//                        Log.i("calDate",""+dResult);

                        RoomListData roomList = new RoomListData(
                                res.getRoomName(),
                                res.getStartDate()+"~"+res.getEndDate(),
                                result+"박"+(result+1)+"일",
                                res.getImgUrl()
                        );

                        adapter.addItem(roomList);
//                        Log.i("inList",roomList.toString());
//                        Log.i("inList",res.toString());
                        //날짜 비교
                        Date today = new Date(System.currentTimeMillis());
//                        Log.i("canControlRoom","AllList] "+res.toString());

                        long startDate = res.getStartDate().getTime()/(3600000*24);
                        long endDate = res.getEndDate().getTime()/(3600000*24);
                        long toDay = today.getTime()/(3600000*24);
//                        Log.i("canControlRoom","startDate] "+startDate);
//                        Log.i("canControlRoom","endDate] "+endDate);
//                        Log.i("canControlRoom","toDay] "+toDay);

                        if(startDate<=toDay &&
                                toDay<=endDate){
                            vo.setRoomName(res.getRoomName());
                            vo.setRoomNo(res.getRoomNo());
//                            Log.i("canControlRoom","CanControl] "+res.toString());
                        }
//                        if(vo.getRoomNo()==null){
//                            vo.setRoomNo("noRoom");
//                        }

                    }


//                    Log.i("inList",""+list.size());



                }
            }
        };


//        // 아래 내용을 DB에서 받은값으로 대체함.
//        List<String> listTitle = Arrays.asList("1","2","3","4");
//        List<String> listDate = Arrays.asList("11","22","33","44");
//        List<String> listContent = Arrays.asList("111","222","333","444");
//        List<String> listImgUrl = Arrays.asList("https://i.imgur.com/4LQp6RH.jpg",
//                "https://i.imgur.com/T7KCxZj.jpg",
//                "https://i.imgur.com/4LQp6RH.jpg",
//                "https://i.imgur.com/T7KCxZj.jpg");
//        for(int i=0; i<listTitle.size();i++){
//            RoomListData data = new RoomListData(
//                    listTitle.get(i),
//                    listDate.get(i),
//                    listContent.get(i),
//                    listImgUrl.get(i)
//
//            );
//
//             adapter.addItem(data);
//        }
        adapter.notifyDataSetChanged();
        LocalBroadcastManager.getInstance((MainActivity) getActivity())
                .registerReceiver(listBroadCast, new IntentFilter("roomListSetting"));
    }
    public void sendToService(String broadName, String code, String data) {
        Intent i = new Intent(broadName);
        i.putExtra(code, data);
        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);
    }
}
