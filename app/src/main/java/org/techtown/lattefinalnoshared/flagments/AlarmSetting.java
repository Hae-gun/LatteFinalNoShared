package org.techtown.lattefinalnoshared.flagments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;


public class AlarmSetting extends Fragment {

    private BroadcastReceiver alarmBroadcastReceiver;

    private ToggleButton bedHopeDegree0;
    private ToggleButton bedHopeDegree45;
    private ToggleButton bedHopeDegree90;

    private ToggleButton blindHopeOPEN;
    private ToggleButton blindHopeCLOSE;
    private Button sendToServer;
    private TimePicker timePicker;
    @Override
    public void onStart() {
        super.onStart();
        Intent i = new Intent("toService");
        i.putExtra("request", "AlarmSetting");
        LocalBroadcastManager.getInstance((MainActivity) getActivity()).sendBroadcast(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_alarm_setting,
                container, false);

        bedHopeDegree0 = rootView.findViewById(R.id.bedHopeDegree0);
        bedHopeDegree45 = rootView.findViewById(R.id.bedHopeDegree45);
        bedHopeDegree90 = rootView.findViewById(R.id.bedHopeDegree90);
        blindHopeOPEN = rootView.findViewById(R.id.blindHopeOPEN);
        blindHopeCLOSE = rootView.findViewById(R.id.blindHopeCLOSE);

        sendToServer = rootView.findViewById(R.id.sendToServer);

        timePicker = rootView.findViewById(R.id.timePicker);

        alarmBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        bedHopeDegree0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedHopeDegree45.setChecked(false);
                bedHopeDegree90.setChecked(false);
            }
        });
        bedHopeDegree45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedHopeDegree0.setChecked(false);
                bedHopeDegree90.setChecked(false);
            }
        });
        bedHopeDegree90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedHopeDegree45.setChecked(false);
                bedHopeDegree0.setChecked(false);
            }
        });
        blindHopeOPEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindHopeCLOSE.setChecked(false);
            }
        });
        blindHopeCLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindHopeOPEN.setChecked(false);
            }
        });
        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText((MainActivity)getActivity(), "서버로 전송!", Toast.LENGTH_SHORT).show();
            }
        });






        // Inflate the layout for this fragment
        return rootView;
    }
}
