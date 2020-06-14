package org.techtown.lattefinalnoshared.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.Alarm;
import org.techtown.lattefinalnoshared.VO.Hope;
import org.techtown.lattefinalnoshared.VO.LatteMessage;
import org.techtown.lattefinalnoshared.VO.Room;
import org.techtown.lattefinalnoshared.VO.RoomDetail;
import org.techtown.lattefinalnoshared.VO.Sensor;
import org.techtown.lattefinalnoshared.VO.SensorData;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomCurrentSetting extends Fragment {

    private SingletoneVO vo = SingletoneVO.getInstance();

    // TextView Component
    private TextView userID;
    private TextView sensorThermo;
    private TextView sensorAircon_heater;
    private TextView sensorHumid;
    private TextView sensorLight;
    private TextView sensorBlind;
    private TextView sensorDoor;
    private TextView exitModeTextView;
    private TextView roomName;
    private TextView thermo_sbValue;
    private TextView lightPower;

    // TextView Alarm
    private TextView alarmState;
    private TextView alarmTime;
    private TextView alarmDates;

    // Toggle Button
    private ToggleButton lightOnOff;
    private ToggleButton alarmOnOff;
    private ToggleButton blindHopeOPEN;
    private ToggleButton blindHopeCLOSE;

    // SeekBar Component
    private SeekBar thermo_seekBar;
    private SeekBar hope_light_seekBar;

    // Button Component
    private ToggleButton blindOPEN;
    private ToggleButton blindCLOSE;

    private BroadcastReceiver currentBroadcastReceiver;
    private Map<String, View> componentMap = new HashMap<String, View>();

    private Gson gson = MainActivity.gson;
    //    private Map<View,Integer> componentSet = new HashMap<View,Integer>();
    private String blindState = "";
    private HashMap<String, TextView> views;


    private String alarmHour="";
    private String alarmMin="";

    private SensorData light;
    private int prevPower = 0;
    @Override
    public void onStart() {
        super.onStart();

//        Intent i = new Intent("toService");
//        i.putExtra("request", "RoomCurrentSetting");
//        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);
    }

    public String getStringFromIntent(Intent intent, String code) {
        String result = intent.getExtras().getString(code);

        return result;
    }

    public boolean getStringFromIntent(String data, Intent intent, String code) {
        if ((data = intent.getExtras().getString(code)) != null) {

            return true;
        } else {
            return false;
        }

    }


//    public void setCurrentReceiver(Intent intent,HashMap<String,View> hashMap){
//            for(Map map : hashMap){
//
//            }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_room_current_setting, container, false);

        //        componentSet.put(userID,R.id.userID);
        views = new HashMap<>();

        userID = rootView.findViewById(R.id.userID);
        String[] viewName = {"thermo", "aircon", "humid", "light", "blind", "door"};
        componentMap.put("userID", userID);

        sensorThermo = rootView.findViewById(R.id.sensorThermo);
        views.put(viewName[0], sensorThermo);
        componentMap.put("sensorThermo", sensorThermo);

        sensorAircon_heater = rootView.findViewById(R.id.sensorAircon_heater);
        views.put(viewName[1], sensorAircon_heater);
        componentMap.put("sensorAircon_heater", sensorAircon_heater);

        sensorHumid = rootView.findViewById(R.id.sensorHumid);
        views.put(viewName[2], sensorHumid);
        componentMap.put("sensorHumid", sensorHumid);

        sensorLight = rootView.findViewById(R.id.sensorLight);
        views.put(viewName[3], sensorLight);
        componentMap.put("lightPower", sensorLight);

        sensorBlind = rootView.findViewById(R.id.sensorBlind);
        views.put(viewName[4], sensorBlind);
        componentMap.put("blindState", sensorBlind);

        sensorDoor = rootView.findViewById(R.id.sensorDoor);
        views.put(viewName[5], sensorDoor);
        componentMap.put("sensorDoor", sensorDoor);

        exitModeTextView = rootView.findViewById(R.id.exitModeTextView);
        componentMap.put("exitModeTextView", exitModeTextView);

        roomName = rootView.findViewById(R.id.roomName);

        componentMap.put("roomName", roomName);

        thermo_sbValue = rootView.findViewById(R.id.thermo_sbValue);
        lightPower = rootView.findViewById(R.id.lightPower);


        lightOnOff = rootView.findViewById(R.id.lightOnOff);
        alarmOnOff = rootView.findViewById(R.id.alarmOnOff);

        // Alarm TextView
        alarmState= rootView.findViewById(R.id.alarmState);
        alarmTime= rootView.findViewById(R.id.alarmTime);
        alarmDates= rootView.findViewById(R.id.alarmDates);


        // SeekBar Component
        thermo_seekBar = rootView.findViewById(R.id.thermo_seekBar);
        hope_light_seekBar = rootView.findViewById(R.id.hope_light_seekBar);


        // Button Component
        blindOPEN = rootView.findViewById(R.id.blindHopeOPEN);
        blindCLOSE = rootView.findViewById(R.id.blindHopeCLOSE);




        //  서버에서 받아오는 값 컴포넌트에 지정하는 Broadcast Receiver.
        currentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = "";

                Log.i("BBBBBB", vo.getId());
//                if(getStringFromIntent(data,intent, "Setting")) {
                if ((data = intent.getStringExtra("Setting")) != null) {
                    // 초기 값 셋팅.
                    LatteMessage lmsg = gson.fromJson(data, LatteMessage.class);
                    RoomDetail roomDetail = gson.fromJson(lmsg.getJsonData(), RoomDetail.class);
                    Hope hope = roomDetail.getHope();
                    Alarm alarm = roomDetail.getAlarm();

                    alarmHour = alarm.getHour();
                    alarmMin = alarm.getMin();

                    thermo_sbValue.setText(hope.getTemp()+"ºC");
                    thermo_seekBar.setProgress(Integer.valueOf(hope.getTemp()));

                    lightPower.setText(hope.getLight() + "%");
//                    hope_light_seekBar.setProgress(Integer.valueOf(hope.getLight()));


                    List<Sensor> sensors = roomDetail.getSensorList();
                    for (Sensor sensor : sensors) {
                        String type = sensor.getType();
                        String states = sensor.getStates();
                        TextView textView = views.get(type);
                        if (sensor.getStateDetail() != null) {
                            String value = sensor.getStateDetail();
                            textView.setText(value + "%");

                            if ("On".equals(states)) {
                                lightOnOff.setChecked(true);
                                lightPower.setText(value + "%");
                                hope_light_seekBar.setProgress(Integer.valueOf(value));
                            } else {
                                lightOnOff.setChecked(false);
                                hope_light_seekBar.setProgress(0);
                            }
                        } else {
                            if ("aircon".equals(type) || "heater".equals(type)) {
                                textView.setText(type + ": " + states);
                            } else if("thermo".equals(type)){
                                textView.setText(states+"ºC");
                            }else if("humid".equals(type)){
                                textView.setText(states+"%");
                            }else {
                                textView.setText(states);

                                if ("blind".equals(type)) {

                                    if ("On".equals(states)) {
                                        blindOPEN.setChecked(true);
                                        blindCLOSE.setChecked(false);
                                    } else {
                                        blindOPEN.setChecked(false);
                                        blindCLOSE.setChecked(true);
                                    }


                                }
                            }
                        }
                    }
                    // 알람 초기값 셋팅.
                    if(alarm.isFlag()){
                    alarmState.setText("On");}else{
                        alarmState.setText("Off");
                    }
                    alarmTime.setText(alarm.getHour() + ":" + alarm.getMin());
                    alarmDates.setText(alarm.getWeeks());
                    alarmOnOff.setChecked(alarm.isFlag());

                    userID.setText(vo.getId());
                    roomName.setText(roomDetail.getRoomNo());
                    Log.i("inRoomCurrentSetting", "inFragCurrent: " + data);
                }
//                if ((data = getStringFromIntent(intent, "current")) != null) {
//                if(getStringFromIntent(data,intent, "current")){
//                    sensorDoor.setText(data);
//                    Log.i("inRoomCurrent", "current: " + data);
//                } else if ((data = getStringFromIntent(intent, "lightPower")) != null) {
//                    sensorLight.setText(data);
//                    userID.setText(vo.getId());
//                } else if ((data = getStringFromIntent(intent, "blindState")) != null) {
//                    sensorBlind.setText(data);
//                } else if ((data = getStringFromIntent(intent, "userId")) != null) {
//                    userID.setText(data);
//                }

            }
        };

        LocalBroadcastManager.getInstance((MainActivity) getActivity())
                .registerReceiver(currentBroadcastReceiver, new IntentFilter("currentRoomSetting"));

        // 서버로 설정값 보내는 컴포넌트 이벤트

        thermo_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thermo_sbValue.setText(progress + "°C");
                this.progress = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SensorData sensorData = new SensorData();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                String sendTime=sdf.format(System.currentTimeMillis());
//                Log.i("sendTime",sendTime);
//                Date time = (Date) new java.util.Date();
//                sensorData.setTime(Date.valueOf(sendTime));
                Date date = new Date(System.currentTimeMillis());
                sensorData.setTime(date);
                sensorData.setStates(""+progress);

                String jsonData = gson.toJson(sensorData);
                LatteMessage lmsg =  new LatteMessage("Control","thermo",jsonData);
                lmsg.setRoomNo(vo.getRoomNo());
                sendToService("toService", "Control", gson.toJson(lmsg));
            }
        });

        hope_light_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


//                sendToService("toService", "Control", "" + progress);
                lightPower.setText(progress+"%");
                SensorData sensorData = new SensorData();

                light = sensorData;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                String sendTime=sdf.format(System.currentTimeMillis());
//                Log.i("sendTime",sendTime);
//                Date time = (Date) new java.util.Date();
//                sensorData.setTime(Date.valueOf(sendTime));
                Date date = new Date(System.currentTimeMillis());
                sensorData.setTime(date);
                if(progress!=0){
                    sensorData.setStates("On");
                    lightOnOff.setChecked(true);
                }else{
                    sensorData.setStates("Off");
                    lightOnOff.setChecked(false);
                }
                sensorData.setStateDetail(""+progress);
                String jsonData = gson.toJson(sensorData);
                LatteMessage lmsg =  new LatteMessage("Control","light",jsonData);
                lmsg.setRoomNo(vo.getRoomNo());
                sendToService("toService", "Control", gson.toJson(lmsg));
//                sendToService("toService", "light_seekBar", "" + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(hope_light_seekBar.getProgress()!=0) {
                    prevPower = hope_light_seekBar.getProgress();
                }
            }
        });


//        sendEvent(thermoOnOff,"onOffState","" + thermoOnOff.isChecked());
//        thermoOnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("onOffState", "" + thermoOnOff.isChecked());
//            }
//        });

//        sendEvent(lightOnOff, "onOffState", "" + lightOnOff.isChecked());
        lightOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lightOnOff.isChecked()){
                    light.setStates("On");
                    lightPower.setText(light.getStateDetail()+"%");
                    hope_light_seekBar.setProgress(prevPower);
                    String jsonData = gson.toJson(light);
                    LatteMessage lmsg =  new LatteMessage("Control","light",jsonData);
                    lmsg.setRoomNo(vo.getRoomNo());
                    sendToService("toService", "Control", gson.toJson(lmsg));
                }else{
                    light.setStates("Off");
                    prevPower = hope_light_seekBar.getProgress();
                    lightPower.setText("0%");
                    hope_light_seekBar.setProgress(0);
                    String jsonData = gson.toJson(light);
                    LatteMessage lmsg =  new LatteMessage("Control","light",jsonData);
                    lmsg.setRoomNo(vo.getRoomNo());
                    sendToService("toService", "Control", gson.toJson(lmsg));
                }
            }
        });

        // 알람 On/Off 서버로 전송
        sendEvent(alarmOnOff, "onOffState", "" + alarmOnOff.isChecked());
//        alarmOnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("onOffState", "" + alarmOnOff.isChecked());
//            }
//        });

        // 블라인드 상태 전송
//        sendEvent(blindOPEN, "blindState", "OPEN");
//        sendEvent(blindCLOSE, "blindState", "CLOSE");
        blindOPEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindCLOSE.setChecked(false);

                sendToService("toService", "blindState", "OPEN");
            }
        });

        blindCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindOPEN.setChecked(false);
                sendToService("toService", "blindState", "CLOSE");
            }
        });

//        blindHopeOPEN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                blindState = blindHopeOPEN.getTextOn().toString();
//                blindHopeCLOSE.setChecked(false);
//            }
//        });
//        blindHopeCLOSE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                blindState = blindHopeCLOSE.getTextOn().toString();
//                blindHopeOPEN.setChecked(false);
//            }
//        });

        if (vo.getAuthority()) {
            thermo_seekBar.setEnabled(true);
            hope_light_seekBar.setEnabled(true);
            blindOPEN.setEnabled(true);
            blindCLOSE.setEnabled(true);
        } else {
            thermo_seekBar.setEnabled(false);
            hope_light_seekBar.setEnabled(false);
            blindOPEN.setEnabled(false);
            blindCLOSE.setEnabled(false);
        }

        // 초기셋팅 위한 요청 메세지 작성

        Room room = new Room();
        vo.setRoomNo("R000123");
        //여기서 날짜 비교해서 룸객체 보내기.

        room.setRoomNo(vo.getRoomNo());

        LatteMessage lmsg = new LatteMessage(vo.getUserNo(), "RoomDetail", null, gson.toJson(room));


        sendToService("toService", "request", gson.toJson(lmsg));

        return rootView;
    }

    public void sendToService(String broadName, String code, String data) {
        Intent i = new Intent(broadName);
        i.putExtra(code, data);
        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);
    }


    public void sendEvent(View view, String code, String data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToService("toService", code, data);
            }
        });
    }


}
