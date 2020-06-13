package org.techtown.lattefinalnoshared;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.techtown.lattefinalnoshared.VO.Guest;
import org.techtown.lattefinalnoshared.VO.LatteMessage;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;
import org.techtown.lattefinalnoshared.fragments.AlarmSetting;
import org.techtown.lattefinalnoshared.fragments.Login;
import org.techtown.lattefinalnoshared.fragments.RoomCurrentSetting;
import org.techtown.lattefinalnoshared.fragments.RoomList;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private Login login = new Login();
    private RoomList roomList = new RoomList();
    private RoomCurrentSetting roomCurrentSetting = new RoomCurrentSetting();
    private AlarmSetting alarmSetting = new AlarmSetting();
    private BottomNavigationView bottomNavigationView;
    private LinkedList<Integer> navigationSet = new LinkedList<Integer>();
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
    private SingletoneVO vo = SingletoneVO.getInstance();
    private BroadcastReceiver checkIdReceiver;

    private static LinkedList<Fragment> fragList = new LinkedList<Fragment>();

    @Override
    protected void onStart() {
        super.onStart();

//        final Login login = new Login();
//        final RoomList roomList = new RoomList();
//        final RoomCurrentSetting roomCurrentSetting = new RoomCurrentSetting();
//        final AlarmSetting alarmSetting = new AlarmSetting();
        Intent intent = new Intent(MainActivity.this, TCPIPConnectionService.class);

        Log.i("fser", "Point 1");
        if (Build.VERSION.SDK_INT >= 26) {
            Log.i("fser", "onStart()");
            getApplicationContext().startForegroundService(intent);
        } else {
            getApplicationContext().startService(intent);
        }

        Log.i("fser", "Point 3");
    }


    private void enableBottomBar(BottomNavigationView mBottomMenu, boolean enable) {
        for (int i = 0; i < mBottomMenu.getMenu().size(); i++) {
            mBottomMenu.getMenu().getItem(i).setEnabled(enable);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.i("onCreate()","onCreate()");
//        if(login==null){
//            login= new Login();
//        }
//        if(roomCurrentSetting==null){
//            roomCurrentSetting= new RoomCurrentSetting();
//        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        enableBottomBar(bottomNavigationView, false);
        //fragList.addLast(login);
        // navigationSet.addLast(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab1:
//                        if(loginSucFlag){
                        replaceFragment(roomList);
//                            if(!fragList.getLast().equals(roomList)){
                        navigationSet.addLast(0);
                        fragList.addLast(roomList);
//                            }
//                        }
                        return true;
                    case R.id.tab2:
//                        if(loginSucFlag){
                        replaceFragment(roomCurrentSetting);
//                        if(!fragList.getLast().equals(roomCurrentSetting)) {
                        navigationSet.addLast(1);
                        fragList.addLast(roomCurrentSetting);
//                        }
//                        }
                        return true;
                    case R.id.tab3:
//                        if(loginSucFlag){
                        replaceFragment(alarmSetting);
//                        if(!fragList.getLast().equals(alarmSetting)) {
                        navigationSet.addLast(2);
                        fragList.addLast(alarmSetting);
//                        }
//                        }
                        return true;

                }
                return false;
            }
        });
//        bottomNavigationView.setEnabled(false);
//        bottomNavigationView.setFocusable(false);
//        bottomNavigationView.setFocusableInTouchMode(false);
//        bottomNavigationView.setClickable(false);
//        bottomNavigationView.setContextClickable(false);
//        bottomNavigationView.setOnNavigationItemSelectedListener(null);

//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.FragmentContainer, login).commit();

        replaceFragment(login);

        checkIdReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = "";
                if ((data = intent.getExtras().getString("LoginPermission")) != null) {
                    LatteMessage lmsg = gson.fromJson(data, LatteMessage.class);
                    String code2 = lmsg.getCode2();
                    if ("SUCCESS".equals(code2)) {


                        Log.i("loginSuc",data);
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
                        replaceFragment(roomList);
                        enableBottomBar(bottomNavigationView, true);

                        fragList.addLast(login);
                        fragList.addLast(roomList);
                        navigationSet.addLast(0);
                        navigationSet.addLast(0);

                    } else if ("cannotLogin".equals(data)) {
                        Log.i("cannotLogin",data);
                        Toast.makeText(context, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(checkIdReceiver, new IntentFilter("fromService"));


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
//        fragmentTransaction.replace(R.id.FragmentContainer, fragment).commit();
//        fragList.addLast(fragment);
        Log.i("LinkedList", "\nfragList: " + fragList.size() + "\nnagivationSet: " + navigationSet.size());
        fragmentTransaction.replace(R.id.FragmentContainer, fragment).commitAllowingStateLoss();
//        fragmentTransaction.add(R.id.FragmentContainer, fragment).commitAllowingStateLoss();
    }

    public void backFragment() {
        if (!fragList.isEmpty() || !navigationSet.isEmpty()) {
            Fragment prFragment = fragList.removeLast();
            bottomNavigationView.getMenu().getItem(navigationSet.removeLast()).setChecked(true);
            replaceFragment(prFragment);
            if (prFragment.equals(login)) {
                enableBottomBar(bottomNavigationView, false);
            }
        } else {
            Toast.makeText(this, "이전 화면이 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    public void putFragment(Fragment fragment) {
        fragList.addLast(fragment);
    }

    @Override
    public void onBackPressed() {
        backFragment();


    }
}