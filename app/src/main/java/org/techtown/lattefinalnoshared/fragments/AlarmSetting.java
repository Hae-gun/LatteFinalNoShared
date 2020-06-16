package org.techtown.lattefinalnoshared.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.Alarm;
import org.techtown.lattefinalnoshared.VO.AlarmData;
import org.techtown.lattefinalnoshared.VO.LatteMessage;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;

import java.util.HashMap;
import java.util.LinkedList;


public class AlarmSetting extends Fragment {

    private BroadcastReceiver alarmBroadcastReceiver;

    private ToggleButton bedHopeDegree0;
    private ToggleButton bedHopeDegree45;
    private ToggleButton bedHopeDegree90;
    private SeekBar hope_light_seekBar;
    private ToggleButton blindHopeOPEN;
    private ToggleButton blindHopeCLOSE;

    private Button sendToServer;
    private TimePicker timePicker;


    private SingletoneVO vo = SingletoneVO.getInstance();
    private Gson gson = new Gson();
    //    private HashMap<String,ToggleButton> toggleButtonHashMap = new HashMap<>();
    private HashMap<ToggleButton, String> toggleButtonHashMap;
    private HashMap<String, ToggleButton> setButtonHashMap;
    private String lightPower = "";
    private String bedDegree = "";
    private String blindState = "";

    private int[] ButtonIdSets = {
            R.id.toggle_sun, R.id.toggle_mon, R.id.toggle_tue, R.id.toggle_wed, R.id.toggle_thu,
            R.id.toggle_fri, R.id.toggle_sat
    };
    private String[] weeksString = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    private HashMap<String, ToggleButton> bedButtonSets;
    private HashMap<String, ToggleButton> blindButtonSets;


    public void setWeeks(ViewGroup rootView) {
        toggleButtonHashMap = new HashMap<>();
        setButtonHashMap = new HashMap<>();
        Log.i("isChecked()","do");
        for (int i = 0; i < ButtonIdSets.length; i++) {
            ToggleButton setButton = rootView.findViewById(ButtonIdSets[i]);

            String day = weeksString[i];
            if (toggleButtonHashMap.get(setButton) == null) {
                toggleButtonHashMap.put(setButton, day);
                setButtonHashMap.put(day, setButton);
            }

        }

//        Log.i("isChecked()",gson.toJson(setButtonHashMap.keySet()));
//        Log.i("isChecked()",gson.toJson(setButtonHashMap.values()));
//

    }
    public void setReset(){
        for (int i = 0; i < setButtonHashMap.size(); i++) {
            ToggleButton button = setButtonHashMap.get(weeksString[i]);
            button.setChecked(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setReset();
        Log.i("lifeCycle","on");
//        Intent i = new Intent("toService");
//        i.putExtra("request", "AlarmSetting");
//        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);
        // 초기값 받아오기 요청


        Intent intentAlarm = new Intent("toService");
        Alarm getAlarm = new Alarm();

        LatteMessage getInit1 = new LatteMessage(vo.getUserNo(), "Alarm", "get", gson.toJson(getAlarm));
//        LatteMessage getInit1 = new LatteMessage(vo.getUserNo(), "Alarm", "get", null);
        intentAlarm.putExtra("getAlarm", gson.toJson(getInit1));
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentAlarm);

        Intent intentDetail = new Intent("toService");
        AlarmData[] getDetail = {
                new AlarmData(), new AlarmData(), new AlarmData()
        };

        LatteMessage getInit2 = new LatteMessage(vo.getUserNo(), "AlarmJob", "get", gson.toJson(getDetail));
        intentDetail.putExtra("getAlarm", gson.toJson(getInit2));
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intentDetail);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_alarm_setting,
                container, false);

        setWeeks(rootView);


        bedHopeDegree0 = rootView.findViewById(R.id.bedHopeDegree0);
        bedHopeDegree45 = rootView.findViewById(R.id.bedHopeDegree45);
        bedHopeDegree90 = rootView.findViewById(R.id.bedHopeDegree90);
        blindHopeOPEN = rootView.findViewById(R.id.blindHopeOPEN);
        blindHopeCLOSE = rootView.findViewById(R.id.blindHopeCLOSE);
        hope_light_seekBar = rootView.findViewById(R.id.hope_light_seekBar);
        sendToServer = rootView.findViewById(R.id.sendToServer);

        timePicker = rootView.findViewById(R.id.timePicker);

        alarmBroadcastReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onReceive(Context context, Intent intent) {
                // 서버에서 받아와서 저장된 데이터를 여기서 입력받음.
                String data = "";
                if ((data = intent.getStringExtra("setAlarmTime")) != null) {
                    LatteMessage msg = gson.fromJson(data, LatteMessage.class);
                    String jsonData = msg.getJsonData();

                    Alarm alarm = gson.fromJson(jsonData, Alarm.class);
//
//
                    String[] days = gson.fromJson(alarm.getWeeks(), String[].class);
                    // 서버에서 받아온 설정된 시간으로 timePicker 시간 설정
                    if ("Y".equals(alarm.getFlag())) {
                        int hour = Integer.valueOf(alarm.getHour());
                        int min = Integer.valueOf(alarm.getMin());
                        timePicker.setHour(hour);
                        timePicker.setMinute(min);
                    }


                    for (ToggleButton button : toggleButtonHashMap.keySet()) {
                        for (String day : days) {
                            Log.i("arraysss", "" + toggleButtonHashMap.get(button).equals(days));
                            if (toggleButtonHashMap.get(button).equals(day)) {
                                button.setChecked(true);
                            }
                        }
                    }


//}
//                    String[] array = gson.fromJson(alarm.getWeeks(),String[].class);
                    for (String s : days)
                        Log.i("arraysss", s);
//                    LinkedList<String> days = gson.fromJson(alarm.getWeeks(), LinkedList<String>.class);


                    // Alarm Job
                } else if ((data = intent.getStringExtra("setAlarmData")) != null) {
                    Log.i("inFragment", "inFragment: " + data);
                    LatteMessage lmsg = gson.fromJson(data, LatteMessage.class);
                    AlarmData[] alarmDatas = gson.fromJson(lmsg.getJsonData(), AlarmData[].class);
                    Log.i("request", gson.toJson(alarmDatas));
                    for (AlarmData aData : alarmDatas) {
                        if ("Light".equals(aData.getType()) && "On".equals(aData.getStates())) {

                            int power = Integer.valueOf(aData.getStateDetail());
                            hope_light_seekBar.setProgress(power, true);
                        } else if ("Bed".equals(aData.getType())) {
                            String value = aData.getStates();
                            for (ToggleButton bed : bedButtonSets.values()) {
                                if (bedButtonSets.get(value).equals(bed)) {
                                    bed.setChecked(true);
                                } else {
                                    bed.setChecked(false);
                                }
                            }
//                            bedButtonSets.get(value).setChecked(true);
                        } else if ("Blind".equals(aData.getType())) {
                            String value = aData.getStates();
                            for (ToggleButton blind : blindButtonSets.values()) {
                                if (blindButtonSets.get(value).equals(blind)) {
                                    blind.setChecked(true);
                                } else {
                                    blind.setChecked(false);
                                }
                            }
                            blindButtonSets.get(value).setChecked(true);
                        }
                    }

                }

            }
        };


        // Receiver 등록
        LocalBroadcastManager.getInstance((MainActivity) getActivity())
                .registerReceiver(alarmBroadcastReceiver, new IntentFilter("alarmDetail"));


        lightPower = "" + hope_light_seekBar.getProgress();
        // 조명 파워값 저장.
        hope_light_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lightPower = "" + i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 침대 각도 버튼 1개씩만 선택 & 각도값 String 객체에 저장.
        bedHopeDegree0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedDegree = bedHopeDegree0.getTextOn().toString();
                bedHopeDegree45.setChecked(false);
                bedHopeDegree90.setChecked(false);
            }
        });
        bedHopeDegree45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedDegree = bedHopeDegree45.getTextOn().toString();
                bedHopeDegree0.setChecked(false);
                bedHopeDegree90.setChecked(false);
            }
        });
        bedHopeDegree90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedDegree = bedHopeDegree90.getTextOn().toString();
                bedHopeDegree45.setChecked(false);
                bedHopeDegree0.setChecked(false);
            }
        });

        // 블라인드 버튼 1개만 선택 & 블라인드 상태값 String 객체에 저장.
        blindHopeOPEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindState = blindHopeOPEN.getTextOn().toString();
                blindHopeCLOSE.setChecked(false);
            }
        });
        blindHopeCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindState = blindHopeCLOSE.getTextOn().toString();
                blindHopeOPEN.setChecked(false);
            }
        });

        bedButtonSets = new HashMap<>();
        blindButtonSets = new HashMap<>();

        bedButtonSets.put("0", bedHopeDegree0);
        bedButtonSets.put("45", bedHopeDegree45);
        bedButtonSets.put("90", bedHopeDegree90);

        blindButtonSets.put("OPEN", blindHopeOPEN);
        blindButtonSets.put("CLOSE", blindHopeCLOSE);

        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText((MainActivity) getActivity(), "서버로 전송!", Toast.LENGTH_SHORT).show();
                Alarm alarm = new Alarm();
                LinkedList<String> days = new LinkedList<>();
                Log.i("checkedDays", gson.toJson(toggleButtonHashMap.values()));
                for (int i = 0; i < weeksString.length; i++) {
                    ToggleButton btn = (ToggleButton) rootView.findViewById(ButtonIdSets[i]);
                    if (btn.isChecked()) {
                        days.addLast(toggleButtonHashMap.get(btn));
                    }
                }

//                for(ToggleButton buttonCheck : toggleButtonHashMap.keySet()){
//                    if(buttonCheck.isChecked()){
//                        days.addLast(toggleButtonHashMap.get(buttonCheck));
//                        ((ToggleButton)rootView.findViewById(ButtonIdSets[1])).isChecked();
//                    }
//                }

                String daysJson = gson.toJson(days);

                alarm.setUserNo(vo.getUserNo());
                alarm.setFlag("Y");
                alarm.setHour("" + timePicker.getHour());
                alarm.setMin("" + timePicker.getMinute());
                alarm.setWeeks(daysJson);

                Log.i("checkedDays", daysJson);
                Log.i("alarmToString", alarm.toString());

                String jsonData = gson.toJson(alarm);
                LatteMessage msg = new LatteMessage(vo.getUserNo(), "Alarm", "update", jsonData);

                Intent alarmI = new Intent("toService");
                alarmI.putExtra("setAlarm", gson.toJson(msg));
                LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(alarmI);

                Log.i("LatteMessage", gson.toJson(msg));


                Log.i("LatteMessage", toggleButtonHashMap.values().toString());

                AlarmData[] alarmDatas = {
                        new AlarmData(vo.getUserNo(), "Light", "On", lightPower),
                        new AlarmData(vo.getUserNo(), "Bed", bedDegree, null),
                        new AlarmData(vo.getUserNo(), "Blind", blindState, null)
                };

                msg.setCode1("AlarmJob");
                msg.setJsonData(gson.toJson(alarmDatas));
                Log.i("AlarmData", gson.toJson(alarmDatas));
                Intent alarmDataI = new Intent("toService");
                alarmDataI.putExtra("setAlarm", gson.toJson(msg));
                LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(alarmDataI);


                Log.i("LatteMessage", gson.toJson(msg));
                Log.i("timeData", "hour:" + timePicker.getHour() + "min:" + timePicker.getMinute());

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
}
