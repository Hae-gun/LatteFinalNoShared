package org.techtown.lattefinalnoshared.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.techtown.lattefinalnoshared.MainActivity;
import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.Guest;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;


public class Login extends Fragment{


    private Gson gson = MainActivity.gson;
    private SingletoneVO singletoneVO = SingletoneVO.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        final EditText idEdit = rootView.findViewById(R.id.idEdit);
        final EditText pwEdit = rootView.findViewById(R.id.pwEdit);
        Button sendToService = rootView.findViewById(R.id.sendToService);




        sendToService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                UserVO user = new UserVO(idEdit.getText().toString(),pwEdit.getText().toString());

                String id = idEdit.getText().toString();
                String pw = pwEdit.getText().toString();
                singletoneVO.setId(id);
                singletoneVO.setPw(pw);

                Guest guest = new Guest();
                guest.setLoginID(id);
                guest.setLoginPW(pw);
                guest.setAuthCode(singletoneVO.getMacaddress());
                Log.i("guest",guest.toString());
                Log.i("guest",gson.toJson(guest));

                String userJson = gson.toJson(guest);

                Intent i= new Intent("toService");
                i.putExtra("guestVO",userJson);
                LocalBroadcastManager.getInstance((MainActivity)getActivity()).sendBroadcast(i);
                idEdit.setText("");
                pwEdit.setText("");
            }
        });





        return rootView;
    }
}
