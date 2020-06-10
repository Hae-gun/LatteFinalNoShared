package org.techtown.lattefinalnoshared.flagments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;


public class AlarmSetting extends Fragment {



    @Override
    public void onStart() {
        super.onStart();
        Intent i = new Intent("toService");
        i.putExtra("request","AlarmSetting");
        LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(i);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_setting, container, false);
    }
}
