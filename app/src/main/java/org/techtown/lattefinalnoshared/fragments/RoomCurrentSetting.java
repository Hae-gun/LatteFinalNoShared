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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RoomCurrentSetting extends Fragment {

    private SingletoneVO vo = SingletoneVO.getInstance();

    // TextView Component
    private TextView userID;
    private TextView sensorThermo;
    private TextView sensorAircon;
    private TextView sensorHeater;
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
    private HashMap<String, String> sensorTypeID;

    private String alarmHour = "";
    private String alarmMin = "";

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

    // 지울것들
    private String latteMsg = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_room_current_setting, container, false);

        //        componentSet.put(userID,R.id.userID);
        views = new HashMap<>();

        userID = rootView.findViewById(R.id.userID);
        String[] viewName = {"TEMP", "COOL", "HEAT", "HUMI", "LIGHT", "BLIND", "DOOR"}; //HEAT 추가
        componentMap.put("userID", userID);

        sensorThermo = rootView.findViewById(R.id.sensorThermo);
        views.put(viewName[0], sensorThermo);
        componentMap.put("sensorThermo", sensorThermo);

        sensorAircon = rootView.findViewById(R.id.sensorAircon);
        views.put(viewName[1], sensorAircon);
        componentMap.put("sensorAircon", sensorAircon);

        sensorHeater = rootView.findViewById(R.id.sensorHeater);
        views.put(viewName[2], sensorHeater);
        componentMap.put("sensorHeater", sensorHeater);

        sensorHumid = rootView.findViewById(R.id.sensorHumid);
        views.put(viewName[3], sensorHumid);
        componentMap.put("sensorHumid", sensorHumid);

        sensorLight = rootView.findViewById(R.id.sensorLight);
        views.put(viewName[4], sensorLight);
        componentMap.put("lightPower", sensorLight);

        sensorBlind = rootView.findViewById(R.id.sensorBlind);
        views.put(viewName[5], sensorBlind);
        componentMap.put("blindState", sensorBlind);

//        sensorDoor = rootView.findViewById(R.id.sensorDoor);
//        views.put(viewName[6], sensorDoor);
//        componentMap.put("sensorDoor", sensorDoor);

        exitModeTextView = rootView.findViewById(R.id.exitModeTextView);
        componentMap.put("exitModeTextView", exitModeTextView);

        roomName = rootView.findViewById(R.id.roomName);

        componentMap.put("roomName", roomName);

        thermo_sbValue = rootView.findViewById(R.id.thermo_sbValue);
        lightPower = rootView.findViewById(R.id.lightPower);


        lightOnOff = rootView.findViewById(R.id.lightOnOff);
        alarmOnOff = rootView.findViewById(R.id.alarmOnOff);

        // Alarm TextView
        alarmState = rootView.findViewById(R.id.alarmState);
        alarmTime = rootView.findViewById(R.id.alarmTime);
        alarmDates = rootView.findViewById(R.id.alarmDates);


        // SeekBar Component
        thermo_seekBar = rootView.findViewById(R.id.thermo_seekBar);
        hope_light_seekBar = rootView.findViewById(R.id.hope_light_seekBar);


        // Button Component
        blindOPEN = rootView.findViewById(R.id.blindHopeOPEN);
        blindCLOSE = rootView.findViewById(R.id.blindHopeCLOSE);

//
//        // 지울 컴포넌트
        ToggleButton deleteBed0 = rootView.findViewById(R.id.deleteBed0);
        ToggleButton deleteBed45 = rootView.findViewById(R.id.deleteBed45);
        ToggleButton deleteBed90 = rootView.findViewById(R.id.deleteBed90);
//
//        Button deleteBtn = rootView.findViewById(R.id.deleteBtn);
//
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                latteMsg = "\"\\\"{\\\\\\\"clientNo\\\\\\\":\\\\\\\"TESTER0001\\\\\\\",\\\\\\\"code1\\\\\\\":\\\\\\\"ROOMDETAIL\\\\\\\",\\\\\\\"code2\\\\\\\":\\\\\\\"SUCCESS\\\\\\\",\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"\\\\\\\"jsonData\\\\\\\":\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"\\\\\\\"{\\\\\\\\\\\\\\\"roomNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"ROOM0002\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"sensorList\\\\\\\\\\\\\\\":\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"[{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02101\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"TEMP\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE021\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"70\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02102\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"HUMI\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE021\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"0\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02103\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"HEAT\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE021\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"off\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02104\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"COOL\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE021\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"off\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02201\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"LIGHT\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE022\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"on\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"stateDetail\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"70\\\\\\\\\\\\\\\"}\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\",{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02202\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DOOR\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE022\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"close\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02301\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"BED\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE023\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"0\\\\\\\\\\\\\\\"},\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"{\\\\\\\\\\\\\\\"sensorNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"SN02401\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"BLIND\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"deviceNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"DEVICE024\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"states\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"0\\\\\\\\\\\\\\\"}],\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\"\\\\\\\\\\\\\\\"hope\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"hopeNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"TESTER0001\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"temp\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"26\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"light\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"70\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"bed\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"0\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"blind\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"OPEN\\\\\\\\\\\\\\\"}\\\\n\\\" +\\n\" +\n" +
////                        "\"\\\",\\\\\\\\\\\\\\\"alarm\\\\\\\\\\\\\\\":{\\\\\\\\\\\\\\\"alarmNo\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"TESTER0001\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"hour\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"06\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"min\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"00\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"flag\\\\\\\\\\\\\\\":\\\\\\\\\\\\\\\"N\\\\\\\\\\\\\\\"}}\\\\\\\"}\\\"\";";
//                latteMsg = "{\"clientNo\":\"TESTER0001\",\"code1\":\"ROOMDETAIL\",\"code2\":\"SUCCESS\",\r\n" +
//                        "\"jsonData\":\r\n" +
//                        "\"{\\\"roomNo\\\":\\\"ROOM0002\\\",\\\"sensorList\\\":\r\n" +
//                        "[{\\\"sensorNo\\\":\\\"SN02101\\\",\\\"type\\\":\\\"TEMP\\\",\\\"deviceNo\\\":\\\"DEVICE021\\\",\\\"states\\\":\\\"70\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02102\\\",\\\"type\\\":\\\"HUMI\\\",\\\"deviceNo\\\":\\\"DEVICE021\\\",\\\"states\\\":\\\"0\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02103\\\",\\\"type\\\":\\\"HEAT\\\",\\\"deviceNo\\\":\\\"DEVICE021\\\",\\\"states\\\":\\\"off\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02104\\\",\\\"type\\\":\\\"COOL\\\",\\\"deviceNo\\\":\\\"DEVICE021\\\",\\\"states\\\":\\\"off\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02201\\\",\\\"type\\\":\\\"LIGHT\\\",\\\"deviceNo\\\":\\\"DEVICE022\\\",\\\"states\\\":\\\"on\\\",\\\"stateDetail\\\":\\\"70\\\"}\r\n" +
//                        ",{\\\"sensorNo\\\":\\\"SN02202\\\",\\\"type\\\":\\\"DOOR\\\",\\\"deviceNo\\\":\\\"DEVICE022\\\",\\\"states\\\":\\\"close\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02301\\\",\\\"type\\\":\\\"BED\\\",\\\"deviceNo\\\":\\\"DEVICE023\\\",\\\"states\\\":\\\"0\\\"},\r\n" +
//                        "{\\\"sensorNo\\\":\\\"SN02401\\\",\\\"type\\\":\\\"BLIND\\\",\\\"deviceNo\\\":\\\"DEVICE024\\\",\\\"states\\\":\\\"0\\\"}],\r\n" +
//                        "\\\"hope\\\":{\\\"hopeNo\\\":\\\"TESTER0001\\\",\\\"temp\\\":\\\"26\\\",\\\"light\\\":\\\"70\\\",\\\"bed\\\":\\\"0\\\",\\\"blind\\\":\\\"OPEN\\\"}\r\n" +
//                        ",\\\"alarm\\\":{\\\"alarmNo\\\":\\\"TESTER0001\\\",\\\"hour\\\":\\\"06\\\",\\\"min\\\":\\\"00\\\",\\\"flag\\\":\\\"N\\\"}}\"}";
//                try {
////                    StringBuilder sb = new StringBuilder(latteMsg);
////                    sb.
//                    LatteMessage lmsg = gson.fromJson(latteMsg, LatteMessage.class);
//                    sendToService("currentRoomSetting", "Setting", latteMsg);
//                } catch (Exception e) {
//                    Log.i("latteMsg", "Error");
//                }
//                Log.i("latteMsg", latteMsg);
//            }
//        });
//
        deleteBed0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteBed0.isChecked()) {
                    deleteBed45.setChecked(false);
                    deleteBed90.setChecked(false);

                    SensorData sensorData = new SensorData();
                    Date date = new Date(System.currentTimeMillis());
                    sensorData.setTime(date);
                    sensorData.setDataNo(vo.getRoomNo());
                    sensorData.setStates("0");
                    sensorData.setSensorNo("DEVICE023");
                    LatteMessage lmsg = new LatteMessage(vo.getRoomNo(), "CONTROL", "BED", gson.toJson(sensorData));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                    sendToService("toService", "blindSet", gson.toJson(lmsg));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                }
            }
        });
        deleteBed45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteBed45.isChecked()) {
                    deleteBed0.setChecked(false);
                    deleteBed90.setChecked(false);

                    SensorData sensorData = new SensorData();
                    Date date = new Date(System.currentTimeMillis());
                    sensorData.setTime(date);
                    sensorData.setDataNo(vo.getRoomNo());
                    sensorData.setStates("45");
                    sensorData.setSensorNo("DEVICE023");
                    LatteMessage lmsg = new LatteMessage(vo.getRoomNo(), "CONTROL", "BED", gson.toJson(sensorData));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                    sendToService("toService", "blindSet", gson.toJson(lmsg));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                }
            }
        });
        deleteBed90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteBed90.isChecked()) {
                    deleteBed45.setChecked(false);
                    deleteBed0.setChecked(false);

                    SensorData sensorData = new SensorData();
                    Date date = new Date(System.currentTimeMillis());
                    sensorData.setTime(date);
                    sensorData.setDataNo(vo.getRoomNo());
                    sensorData.setStates("90");
                    sensorData.setSensorNo("DEVICE023");
                    LatteMessage lmsg = new LatteMessage(vo.getRoomNo(), "CONTROL", "BED", gson.toJson(sensorData));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                    sendToService("toService", "blindSet", gson.toJson(lmsg));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                }
            }
        });


        //  서버에서 받아오는 값 컴포넌트에 지정하는 Broadcast Receiver.
        currentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = "";
//                vo.setId("asdf");

//                Log.i("BBBBBB", vo.getId());
//                if(getStringFromIntent(data,intent, "Setting")) {
                if (intent.getStringExtra("Setting") != null) {
                    Log.i("broadCast", "BroadCastSetting");
                    data = intent.getStringExtra("Setting");
                    // 초기 값 셋팅.
                    Log.i("SettingFrag", data);
                    LatteMessage lmsg = gson.fromJson(data, LatteMessage.class);

                    String code1 = lmsg.getCode1();
                    String code2 = lmsg.getCode2();
                    try {
                        if ("RoomDetail".toUpperCase().equals(code1) && "SUCCESS".equals(code2)) {
                            RoomDetail roomDetail = gson.fromJson(lmsg.getJsonData(), RoomDetail.class);
                            Hope hope = roomDetail.getHope();
                            Alarm alarm = roomDetail.getAlarm();
                            vo.setAlarm(alarm);
                            alarmHour = alarm.getHour();
                            alarmMin = alarm.getMin();

                            thermo_sbValue.setText(hope.getTemp() + "ºC");
                            thermo_seekBar.setProgress(Integer.valueOf(hope.getTemp()));

                            lightPower.setText(hope.getLight() + "%");
//                    hope_light_seekBar.setProgress(Integer.valueOf(hope.getLight()));

                            LinkedList<String> sensorNoSet = new LinkedList<>();
                            LinkedList<String> sensorTypeSet = new LinkedList<>();
                            sensorTypeID = new HashMap<>();


                            List<Sensor> sensors = roomDetail.getSensorList();
                            for (Sensor sensor : sensors) {
                                sensorTypeID.put(sensor.getType(), sensor.getSensorNo());
//                            sensorNoSet.addLast(sensor.getSensorNo());
//                            sensorTypeSet.addLast(sensor.getType());


                                String type = sensor.getType();
                                String states = sensor.getStates();
                                TextView textView = views.get(type);
                                if ("LIGHT".equals(type)) {
                                    String value = sensor.getStateDetail();
                                    textView.setText(value + "%");

                                    if ("ON".equals(states)) {
                                        lightOnOff.setChecked(true);
                                        lightPower.setText(value + "%");
                                        hope_light_seekBar.setProgress(Integer.valueOf(value));
                                    } else {
                                        lightOnOff.setChecked(false);
                                        hope_light_seekBar.setProgress(0);
                                    }
                                } else {
                                    if ("cool".toUpperCase().equals(type)) {// || "heat".toUpperCase().equals(type)
                                        textView.setText(type + ": " + states.toUpperCase());
                                    }else if("HEAT".equals(type)){
                                        textView.setText(type + ": "+states.toUpperCase());
                                    } else if ("temp".toUpperCase().equals(type)) {

                                        textView.setText(states + "ºC");
                                    } else if ("humi".toUpperCase().equals(type)) {
                                        textView.setText(states + "%");
                                    } else if ("blind".toUpperCase().equals(type)) {
                                        if ("1".toUpperCase().equals(states)) {
                                            textView.setText("OPEN");
                                            blindOPEN.setChecked(true);
                                            blindCLOSE.setChecked(false);
                                        } else {
                                            textView.setText("CLOSE");
                                            blindOPEN.setChecked(false);
                                            blindCLOSE.setChecked(true);
                                        }
                                    }
                                }
                            }

                            Log.i("sensorListSet", sensorNoSet.toString());
                            Log.i("sensorListSet", sensorTypeSet.toString());
                            // 알람 초기값 셋팅.
                            boolean flag = true;
                            if ("Y".equals(alarm.getFlag())) {
                                alarmState.setText("On".toUpperCase());
                                flag = true;
                            } else if ("N".equals(alarm.getFlag())) {
                                alarmState.setText("Off".toUpperCase());
                                flag = false;
                            }
                            alarmTime.setText(alarm.getHour() + ":" + alarm.getMin());
                            alarmDates.setText(alarm.getWeeks());
                            alarmOnOff.setChecked(flag);

                            userID.setText(vo.getId());
                            roomName.setText(vo.getRoomName()+"호");
                            Log.i("inRoomCurrentSetting", "inFragCurrent: " + data);
                        } else if ("Update".equals(code1)) {
                            Log.i("Update", data);
                            Sensor sensor = gson.fromJson(lmsg.getJsonData(),Sensor.class);
//                            SensorData sensorData = gson.fromJson(lmsg.getJsonData(), SensorData.class);

                            String type = sensor.getType();
                            String states = sensor.getStates();
                            TextView textView = views.get(type);

//                            String states = sensorData.getStates();
                            if ("LIGHT".equals(type)) {
                                String value = sensor.getStateDetail();
                                textView.setText(value + "%");

                                if ("ON".equals(states)) {
                                    lightOnOff.setChecked(true);
                                    lightPower.setText(value + "%");
                                    hope_light_seekBar.setProgress(Integer.valueOf(value));
                                } else {
                                    lightOnOff.setChecked(false);
                                    hope_light_seekBar.setProgress(0);
                                }
                            } else {
                                if ("cool".toUpperCase().equals(type)) {// || "heat".toUpperCase().equals(type)
                                    textView.setText(type + ": " + states.toUpperCase());
                                }else if("HEAT".equals(type)){
                                    textView.setText(type + ": "+states.toUpperCase());
                                } else if ("temp".toUpperCase().equals(type)) {

                                    textView.setText(states + "ºC");
                                } else if ("humi".toUpperCase().equals(type)) {
                                    textView.setText(states + "%");
                                } else if ("blind".toUpperCase().equals(type)) {
                                    if ("1".toUpperCase().equals(states)) {
                                        textView.setText("OPEN");
                                        blindOPEN.setChecked(true);
                                        blindCLOSE.setChecked(false);
                                    } else {
                                        textView.setText("CLOSE");
                                        blindOPEN.setChecked(false);
                                        blindCLOSE.setChecked(true);
                                    }
                                }
                            }
//                        else if("HUMI".equals(code2)){
//
//                        }


//                        sensorBlind.setText(sensorData.getStates());
                        } else if ("CONTROL".equals(code1) || "Control".equals(code1)) {
                            SensorData sensorData = gson.fromJson(lmsg.getJsonData(), SensorData.class);
                            String states = sensorData.getStates();
                            String statesDetail = "";
                            if (sensorData.getStateDetail() != null) {
                                statesDetail = sensorData.getStateDetail();
                            }
                            if ("COOL".equals(code2)) {
                                sensorAircon.setText("COOL: " + states);
                            } else if ("HEAT".equals(code2)) {
                                sensorHeater.setText("HEAT: " + states);
                            } else if ("DOOR".equals(code2)) {
                                sensorDoor.setText(states);
                            } else if ("BLIND".equals(code2)) {
                                sensorBlind.setText(states);
                            } else if ("TEMP".equals(code2)) {
                                Log.i("data", "why?");
                                sensorThermo.setText(states + "-");
                            }
                        }
                    } catch (Exception e) {
                        Log.i("eeeeeeeee","start"+e);
                        sendToService("fromService","noRoom","noRoom");
                        Log.i("eeeeeeeee","end"+e);
                    }
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
        }

        ;

        LocalBroadcastManager.getInstance((MainActivity)

                getActivity())
                .

                        registerReceiver(currentBroadcastReceiver, new IntentFilter("currentRoomSetting"));

        // 서버로 설정값 보내는 컴포넌트 이벤트

        thermo_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensorThermo.setText(progress + "°C");
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
                try {
                    sensorData.setTime(date);
                    sensorData.setStates("" + progress);
                    sensorData.setSensorNo(sensorTypeID.get("TEMP"));

                    String jsonData = gson.toJson(sensorData);
                    LatteMessage lmsg = new LatteMessage("Control", "TEMP", jsonData);
                    lmsg.setRoomNo(vo.getRoomNo());
                    sendToService("toService", "Control", gson.toJson(lmsg));
                } catch (Exception e) {
                    sensorData.setTime(date);
                    sensorData.setStates("" + progress);
                    sensorData.setSensorNo("SN01101");

                    String jsonData = gson.toJson(sensorData);
                    LatteMessage lmsg = new LatteMessage("Control", "TEMP", jsonData);
                    lmsg.setRoomNo("11111111");
                    sendToService("toService", "Control", gson.toJson(lmsg));
                }
            }
        });

        hope_light_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                sensorLight.setText("" + progress + "%");
//                sendToService("toService", "Control", "" + progress);
                lightPower.setText(progress + "%");
                SensorData sensorData = new SensorData();

                light = sensorData;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//                String sendTime=sdf.format(System.currentTimeMillis());
//                Log.i("sendTime",sendTime);
//                Date time = (Date) new java.util.Date();
//                sensorData.setTime(Date.valueOf(sendTime));
                Date date = new Date(System.currentTimeMillis());
                sensorData.setTime(date);
                if (progress != 0) {
                    sensorData.setStates("On");
                    lightOnOff.setChecked(true);
                } else {
                    sensorData.setStates("Off");
                    lightOnOff.setChecked(false);
                }
                sensorData.setStateDetail("" + progress);
                sensorData.setSensorNo(sensorTypeID.get("LIGHT"));
                String jsonData = gson.toJson(sensorData);
                LatteMessage lmsg = new LatteMessage("Control", "light", jsonData);
                lmsg.setClientNo(vo.getRoomNo());
                sendToService("toService", "Control", gson.toJson(lmsg));
//                sendToService("toService", "light_seekBar", "" + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (hope_light_seekBar.getProgress() != 0) {
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
            public String sendData() {
                if (lightOnOff.isChecked()) {
                    light.setStates("On");
                    lightPower.setText(light.getStateDetail() + "%");
                    hope_light_seekBar.setProgress(prevPower);
                } else {
                    light.setStates("Off");
                    lightPower.setText("0%");
                    hope_light_seekBar.setProgress(0);
                }
                Date date = new Date(System.currentTimeMillis());
                light.setTime(date);
                light.setSensorNo(sensorTypeID.get("LIGHT"));
                String jsonData = gson.toJson(light);
                LatteMessage lmsg = new LatteMessage("Control", "light", jsonData);

                return gson.toJson(lmsg);
            }

            @Override
            public void onClick(View v) {
//                if (lightOnOff.isChecked()) {
//                    light.setStates("On");
//                    lightPower.setText(light.getStateDetail() + "%");
//                    hope_light_seekBar.setProgress(prevPower);
//                    Date date = new Date(System.currentTimeMillis());
//                    light.setTime(date);
//                    String jsonData = gson.toJson(light);
//                    LatteMessage lmsg = new LatteMessage("Control", "light", jsonData);
//                    lmsg.setClientNo(vo.getRoomNo());
                sendToService("toService", "Control", gson.toJson(sendData()));
//                } else {
//                    light.setStates("Off");
//                    prevPower = hope_light_seekBar.getProgress();
//                    lightPower.setText("0%");
//                    hope_light_seekBar.setProgress(0);
//                    Date date = new Date(System.currentTimeMillis());
//                    light.setTime(date);
//                    String jsonData = gson.toJson(light);
//                    LatteMessage lmsg = new LatteMessage("Control", "light", jsonData);
//                    lmsg.setClientNo(vo.getRoomNo());
//                    sendToService("toService", "Control", gson.toJson(lmsg));
            }
        });

        // 알람 On/Off 서버로 전송
//        sendEvent(alarmOnOff, "onOffState", "" + alarmOnOff.isChecked());
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
                sensorBlind.setText("OPEN");
                SensorData sensorData = new SensorData();
                Date date = new Date(System.currentTimeMillis());
                sensorData.setTime(date);
                sensorData.setDataNo(vo.getRoomNo());
                sensorData.setStates("OPEN");
                sensorData.setSensorNo(sensorTypeID.get("BLIND"));
                LatteMessage lmsg = new LatteMessage(vo.getRoomNo(), "Control", "blind", gson.toJson(sensorData));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
                sendToService("toService", "blindSet", gson.toJson(lmsg));
//                Log.i("blinddddd", "inFrag] " + gson.toJson(lmsg));
            }
        });

        blindCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorBlind.setText("CLOSE");
                blindOPEN.setChecked(false);
                SensorData sensorData = new SensorData();
                Date date = new Date(System.currentTimeMillis());
                sensorData.setTime(date);
                sensorData.setDataNo(vo.getRoomNo());
                sensorData.setStates("CLOSE");
                sensorData.setSensorNo(sensorTypeID.get("BLIND"));
                LatteMessage lmsg = new LatteMessage(vo.getRoomNo(), "Control", "blind", gson.toJson(sensorData));
                sendToService("toService", "blindSet", gson.toJson(lmsg));
            }
        });
        alarmOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatteMessage lmsg = new LatteMessage();
                lmsg.setClientNo(vo.getUserNo());
                lmsg.setCode1("Alarm");
                lmsg.setCode2("Update");

                Alarm alarm = vo.getAlarm();

                if (alarmOnOff.isChecked()) {
//                    alarm.setFlag(true);
                    alarmState.setText("ON");
                    alarm.setFlag("Y");
                } else {
                    alarmState.setText("OFF");
                    alarm.setFlag("N");
//                    alarm.setFlag(false);
                }
                Log.i("setAlarm", alarm.toString());
                lmsg.setJsonData(gson.toJson(alarm));

                sendToService("toService", "alarmUpdate", gson.toJson(lmsg));
            }
        });
//        blindOPEN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                blindState = blindOPEN.getTextOn().toString();
//                sensorBlind.setText(blindState);
//                blindCLOSE.setChecked(false);
//            }
//        });
//        blindCLOSE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                blindState = blindCLOSE.getTextOn().toString();
//                sensorBlind.setText(blindState);
//                blindOPEN.setChecked(false);
//            }
//        });


        //MAC 비교해서 UI 제어

//        if (vo.getAuthority()) {
//            thermo_seekBar.setEnabled(true);
//            hope_light_seekBar.setEnabled(true);
//            blindOPEN.setEnabled(true);
//            blindCLOSE.setEnabled(true);
//            lightOnOff.setEnabled(true);
//        } else {
//            thermo_seekBar.setEnabled(false);
//            hope_light_seekBar.setEnabled(false);
//            blindOPEN.setEnabled(false);
//            blindCLOSE.setEnabled(false);
//            lightOnOff.setEnabled(false);
//        }

        // 초기셋팅 위한 요청 메세지 작성

        Room room = new Room();
//        vo.setRoomNo("R000123");
//        vo.setRoomNo();
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
        Log.i("sendToService", "sendToService -finish] code: " + code + " data: " + data);
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
