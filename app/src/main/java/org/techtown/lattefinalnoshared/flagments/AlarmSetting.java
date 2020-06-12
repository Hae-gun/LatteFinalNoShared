package org.techtown.lattefinalnoshared.flagments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import org.techtown.lattefinalnoshared.VO.SensorData;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


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
    private HashMap<ToggleButton,String> toggleButtonHashMap;

    private String lightPower="";
    private String bedDegree="";
    private String blindState="";

    private int[] ButtonIdSets = {
            R.id.toggle_sun,R.id.toggle_mon,R.id.toggle_tue,R.id.toggle_wed,R.id.toggle_thu,
            R.id.toggle_fri,R.id.toggle_sat,
    };
    private String[] weeksString = {"sun","mon","tue","wed","thu","fri","sat"};

    public void setWeeks(ViewGroup rootView){
        toggleButtonHashMap = new HashMap<>();
        for(int  i=0; i<ButtonIdSets.length;i++){
            ToggleButton setButton = rootView.findViewById(ButtonIdSets[i]);
            String day = weeksString[i];
            if(toggleButtonHashMap.get(setButton)==null)
                toggleButtonHashMap.put(setButton,day);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Intent i = new Intent("toService");
        i.putExtra("request", "AlarmSetting");
        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);

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
            @Override
            public void onReceive(Context context, Intent intent) {
                // 서버에서 받아와서 저장된 데이터를 여기서 입력받음.
            }
        };
        lightPower = ""+hope_light_seekBar.getProgress();
        // 조명 파워값 저장.
        hope_light_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lightPower = ""+i;
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


        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText((MainActivity)getActivity(), "서버로 전송!", Toast.LENGTH_SHORT).show();
                Alarm alarm = new Alarm();
                LinkedList<String> days = new LinkedList<>();
                Log.i("checkedDays",gson.toJson(toggleButtonHashMap.values()));
                for(ToggleButton buttonCheck : toggleButtonHashMap.keySet()){
                    if(buttonCheck.isChecked()){
                        days.addLast(toggleButtonHashMap.get(buttonCheck));
                    }
                }
                String daysJson = gson.toJson(days);

                alarm.setUserNo(vo.getUserNo());
                alarm.setFlag(true);
                alarm.setHour(""+timePicker.getHour());
                alarm.setMin(""+timePicker.getMinute());
                alarm.setWeeks(daysJson);

                Log.i("checkedDays",daysJson);
                Log.i("alarmToString",alarm.toString());

                String jsonData = gson.toJson(alarm);
                LatteMessage msg = new LatteMessage(vo.getUserNo(),"Alarm","update",jsonData);

                Intent alarmI = new Intent("toService");
                alarmI.putExtra("Alarm",gson.toJson(msg));
                LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(alarmI);

                Log.i("LatteMessage",gson.toJson(msg));


                AlarmData[] alarmDatas = {
                        new AlarmData(vo.getUserNo(),"Light","On",lightPower),
                        new AlarmData(vo.getUserNo(),"Bed",bedDegree,null),
                        new AlarmData(vo.getUserNo(),"Blind",blindState,null)
                };

                msg.setCode1("AlarmJob");
                msg.setJsonData(gson.toJson(alarmDatas));
                Log.i("AlarmData",gson.toJson(alarmDatas));
                Intent alarmDataI = new Intent("toService");
                alarmDataI.putExtra("Alarm",gson.toJson(msg));
                LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(alarmDataI);



                Log.i("LatteMessage",gson.toJson(msg));
                Log.i("timeData", "hour:"+ timePicker.getHour() +"min:"+ timePicker.getMinute());

            }
        });










        // Inflate the layout for this fragment
        return rootView;
    }
}
