package org.techtown.lattefinalnoshared;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.techtown.lattefinalnoshared.VO.Guest;
import org.techtown.lattefinalnoshared.VO.LatteMessage;
import org.techtown.lattefinalnoshared.VO.SingletoneVO;
import org.techtown.lattefinalnoshared.VO.UserVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPIPConnectionService extends Service {


    private BroadcastReceiver getDataReceiver;
    private Gson gson = MainActivity.gson;


//      private static final String HOST = "70.12.60.97";
//      private static final String HOST = "70.12.60.99";
    private static final String HOST = "192.168.35.103";

    private static final int PORT = 55577;
    private static String MACAddress = "";
    private static final String CHANNEL_ID = "ForeGroundServiceChannel";
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private SingletoneVO singletoneVO = SingletoneVO.getInstance();
    private ExecutorService executor;


    private static boolean keepConn = true;
    private boolean connected = false;

    public TCPIPConnectionService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();


        Log.i("fser", "Point 4");
        start();
        createNotificationChannel();

        Log.i("fser", "Point 5");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        Log.i("fser", "Point 6");
        getDataReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 각 Fragment 에서 받은 데이터를 전송.
                // json 처리 진행.
                // 아래서 받은 데이터를 서버로 전송한다.
                if (intent.getStringExtra("request") != null) {
                    String request = intent.getStringExtra("request");
                    send(request);
                } else if (intent.getStringExtra("current") != null) {
                    String current = intent.getStringExtra("current");
//                    send(current);
                } else if (intent.getStringExtra("guestVO") != null) {
                    String jsonString = intent.getStringExtra("guestVO");
                    Guest guest = gson.fromJson(jsonString, Guest.class);

                    LatteMessage msg = new LatteMessage(null,
                            "LOGIN", null, jsonString);

                    String data = gson.toJson(msg, LatteMessage.class);

                    Log.i("LatteMessage", data);

                    Log.i("BroadcastTest", "guest.toString(): " + guest.toString());
                    Log.i("BroadcastTest", "data: " + jsonString);
                    //UserVO vo = gson.fromJson(data, UserVO.class);
                    send(data);
//                    if ("A".equals(guest.getLoginID())) {
//                        Intent i = new Intent("fromService");
//                        i.putExtra("LoginPermission", "correct");
//                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
//                        send(data);
//                    } else {
//                        Intent i = new Intent("fromService");
//                        i.putExtra("LoginPermission", "cannotLogin");
//                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
//                        send(data);
//                    }
                } else if (intent.getStringExtra("light_seekBar") != null) {
                    String light_progress = intent.getStringExtra("light_seekBar");
                    light_progress = "Light: " + light_progress;
                    send(light_progress);
                } else if (intent.getStringExtra("blindState") != null) {
                    String message = intent.getStringExtra("blindState");
                    message = "Blind:" + message;
                    send(message);
                }else if(intent.getStringExtra("setAlarm") != null){
                    String message = intent.getStringExtra("setAlarm");
                    send(message);
                }else if(intent.getStringExtra("getAlarm")!=null){
                    String message = intent.getStringExtra("getAlarm");
                    send(message);
                }else if(intent.getStringExtra("Control")!=null){
                    String message = intent.getStringExtra("Control");
                    send(message);
                }else if(intent.getStringExtra("alarmUpdate")!=null){
                    String message = intent.getStringExtra("alarmUpdate");
                    send(message);
                }else if(intent.getStringExtra("blindSet")!=null){
                    String message = intent.getStringExtra("blindSet");
                    send(message);
                }

                // 여기서 서버에서 받아온다.


            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(getDataReceiver, new IntentFilter("toService"));

    } // onCreate() end.


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        Log.i("chkMAC",getLocalMacAddress());
        return super.onStartCommand(intent, flags, startId);
    }


    public String getLocalMacAddress() {
//        Log.i("MAC","Point 2");
//        String result = "";
//        InetAddress ip;
//
//        try {
//            ip = InetAddress.getLocalHost();
//            Log.i("MAC","Point 3");
//            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//            byte[] mac = network.getHardwareAddress();
//            Log.i("MAC","Point 4");
//            StringBuilder sb = new StringBuilder();
//            Log.i("MAC","Point 5");
//
//            Log.i("MAC",""+mac.length);
//            for (int i = 0; i < mac.length; i++) {
//                Log.i("MAC","inLoop");
//                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "" : ""));
//            }
//            result = sb.toString();
//            Log.i("MAC",result);
//        } catch (UnknownHostException e) {
//            Log.i("MAC","Point 6 : "+e);
//        } catch (SocketException e) {
//            Log.i("MAC","Point 7 : "+e);
//        }
//        Log.i("MAC","Point 8");
//        return result;
        try {
            Log.i("MAC", "Point 2");
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X", b));
                }

//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length()-1);
//                }

                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        Log.i("MAC", "Point 3");
        return "";
    } //getLocalMacAddress() end.

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void start() {
        Log.i("fser", "Point 7");
        executor = Executors.newFixedThreadPool(3);

        Runnable getAddr = new Runnable() {
            String macAddr = "";

            @Override
            public void run() {
                MACAddress = getLocalMacAddress();
                singletoneVO.setMacaddress(MACAddress);
                Log.i("fser", "point: " + MACAddress);
                send(MACAddress);
            }
        };

        Runnable accept = new Runnable() {
            @Override
            public void run() {
                do {
                    Log.i("connection","connecting");
                    if (socket != null || close()) {
                        if (connect()) {
                            executor.submit(getAddr);
                            Log.i("fser", "Point 8");
                            while (socket.isConnected() && !socket.isClosed()) {
                                Log.i("connection","doReadLine");
                                try {
                                    String line = "";
                                    line = input.readLine();
                                    Log.i("fromServer", line);
//                                    Toast.makeText(TCPIPConnectionService.this, line, Toast.LENGTH_SHORT).show();

                                    // 서버에서 받은 Message 객체분해.
//                                      Intent i = new Intent("fromService");
//                                      i.putExtra("LoginPermission", "correct");
//                                      LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                                    // LatteMessage 완성하면 아래 try catch 문 삭제 예정.
                                    try {
                                        LatteMessage msg = gson.fromJson(line, LatteMessage.class);
                                        String code1 = msg.getCode1();
                                        String code2 = msg.getCode2().toUpperCase();

                                        if ("LOGIN".equals(code1)) {
                                            Guest guest = gson.fromJson(msg.getJsonData(),Guest.class);
//                                            Log.i("LOGIN","1111111111");
                                            if("SUCCESS".equals(code2)){
                                                singletoneVO.setUserNo(guest.getUserNo());
                                                singletoneVO.setId(guest.getLoginID());
                                                singletoneVO.setRole(guest.getRole());
                                            }
//                                            Log.i("LOGIN","2222222222");
                                            // 2020-06-13 17:38 유저정보 화면에 표시......
                                            //
                                            //

                                            //
                                            //
                                            //
                                            msg.setJsonData(gson.toJson(guest));


//                                            if ("SUCCESS".equals(code2)) {
                                                Intent i = new Intent("fromService");
                                                i.putExtra("LoginPermission", gson.toJson(msg));
                                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
//                                            }else if("FAIL".equals(code2)){
//                                                Intent i = new Intent("fromService");
//                                                i.putExtra("LoginPermission", gson.toJson(msg));
//                                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
//                                            }
                                        }else if("ROOMDETAIL".equals(code1)){
                                            if("SUCCESS".equals(code2)){
                                                Log.i("inRoomCurrentSetting","inService"+line);
                                                makeIntent("currentRoomSetting", "Setting", line);
                                            }
                                        }else if("RESERVELIST".equals(code1)&&code2!=null){

                                            Log.i("inRoomCurrentSetting","inService"+line);
                                            makeIntent("roomListSetting", "Setting", line);
                                        }else if("UPDATE".equals(code1)){
                                            makeIntent("currentRoomSetting", "Setting", line);
                                            if("blind".equals(code2)){
                                                makeIntent("currentRoomSetting", "Setting", line);
                                            }
                                        }else if("CONTROL".equals(code1)){
                                                makeIntent("currentRoomSetting", "Setting", line);
                                        }

                                    } catch (Exception e) {
                                        Log.i("error", e.toString());
                                    }
                                    if (line.contains("authCode")) {
                                        LatteMessage msg = gson.fromJson(line,LatteMessage.class);
                                        String code = gson.fromJson(msg.getJsonData(), Guest.class).getAuthCode();
                                        Log.i("AuthCode",code);
                                        if (code.equals(singletoneVO.getMacaddress())) {
                                            singletoneVO.setAuthority(true);
                                        } else {
                                            singletoneVO.setAuthority(false);
                                        }
                                        Log.i("Authority", "" + singletoneVO.getAuthority());
                                    }


                                    if ("RoomCurrentSetting".equals(line)) {

                                        makeIntent("currentRoomSetting"
                                                , "current", line);
                                    } else if (line.contains("lightPowerLight:")) {
                                        // 전등파워 Fragment에 전송
                                        makeIntent("currentRoomSetting"
                                                , "lightPower", line);
                                    } else if (line.contains("BlindState:")) {
                                        // 블라인드 상태 Fragment에 전송
                                        StringBuilder sb = new StringBuilder(line);
                                        sb.delete(0, 11);
                                        line = sb.toString();
                                        makeIntent("currentRoomSetting", "blindState", line);
                                        Log.i("BBBBB", line);
                                    } else if (line.contains("userId:")) {
                                        // 유저 아이디 Fragment에 전송
                                        StringBuilder sb = new StringBuilder(line);
                                        sb.delete(0, 7);
                                        line = sb.toString();
                                        makeIntent("currentRoomSetting", "userId", line);
                                    } else if(line.contains("Alarm")&&!line.contains("AlarmJob")&&line.contains("get")){
                                        Log.i("alarmmm","setting");
                                        makeIntent("alarmDetail","setAlarmTime",line);
                                    } else if(line.contains("AlarmJob")&&line.contains("get")){
                                        makeIntent("alarmDetail","setAlarmData",line);
                                    }

                                    if (line == null) throw new IOException();
                                    else {
                                        Intent sender = new Intent("fromService");
                                        sender.putExtra("Data", line);
                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sender);
                                    }
                                } catch (IOException e) {
                                    Log.i("connect/TCP", "Service - Runnable accept() : " + e);
                                    close();
                                }
                                Log.i("connection","connecting1");
                            }
                            Log.i("connection","connecting2");
                        }
                        Log.i("connection","connecting3");
                    }
                    Log.i("connection","connecting4");
                    notifyAll();
                } while (keepConn);
                stop();

            }
        };


        executor.submit(accept);
    } // start() end.

    private void stop() {
        keepConn = false;
        close();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    } // stop() end.

    private boolean connect() {
        try {
            Log.i("fser", "Point 9");
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());

            connected = true;
            Log.i("TCP/IP", "Service - connect() : success");
        } catch (IOException e) {
            Log.i("TCP/IP", "Service - connect() : " + e);
            return false;
        }
        return true;
    } // connect() end.

    private boolean close() {
        try {
            Log.i("TCP/IP", "Service - close() : start");
            if (socket != null && !socket.isClosed()) {
                socket.close();

                Log.i("TCP/IP", "Service - close() :" + socket.isClosed());
                if (input != null) input.close();
                if (output != null) output.close();
                if (input != null) {
                    Log.i("TCP/IP", "Service - connect() : success");
                    connected = false;
                }
            }
        } catch (IOException e) {
            Log.i("TCP/IP", "Service - connect() : " + e);
            return false;
        }
        return true;
    } // close() end.

    private void send(String data) {
        Runnable sender = new Sender(data);
        executor.submit(sender);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    } // createNotificationChannel() end.

    class Sender implements Runnable {
        String data = "";

        Sender(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            if (socket.isConnected() && !socket.isClosed()) {
                try {
                    output.println(data);
                    output.flush();
                } catch (Exception e) {
                    Log.i("TCP/IP", "Service - send() : " + e);
                }
            }
        }
    } // Sender.class end.

    public void makeIntent(String broadName, String code, String data) {
        Intent i = new Intent(broadName);
        i.putExtra(code, data);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

}
