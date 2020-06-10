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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

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

    private static final String HOST = "70.12.60.97";
    private static final int PORT = 55566;
    private String MACAddress = "";
    private static final String CHANNEL_ID = "ForeGroundServiceChannel";
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private ExecutorService executor;



    private static boolean keepConn = true;
    private boolean connected = false;

    public TCPIPConnectionService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();



        Log.i("fser","Point 4");
        start();
        createNotificationChannel();

        Log.i("fser","Point 5");
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);



        Log.i("fser","Point 6");
        getDataReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 아래서 받은 데이터를 서버로 전송한다.
                String data = intent.getStringExtra("userVO");
                Log.i("BroadcastTest", "Data from Fragment: " + data);
                UserVO vo = gson.fromJson(data, UserVO.class);
                if(intent.getStringExtra("request")!=null){
                    String request = intent.getStringExtra("request");
                    send(request);
                    return;
                }
                // 여기서 서버에서 받아온다.

                if ("Aaaa".equals(vo.getId())) {
                    Intent i = new Intent("fromService");
                    i.putExtra("LoginPermission", "correct");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                    send(data);
                } else {

                    Intent i = new Intent("fromService");
                    i.putExtra("LoginPermission", "cannotLogin");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                    send(data);
                }

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
            Log.i("MAC","Point 2");
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X",b));
                }

//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length()-1);
//                }

                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        Log.i("MAC","Point 3");
        return "";
    } //getLocalMacAddress() end.

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void start() {
        Log.i("fser","Point 7");
        executor = Executors.newFixedThreadPool(3);

        Runnable getAddr = new Runnable() {
            String macAddr= "";
            @Override
            public void run() {
                MACAddress = getLocalMacAddress();
                Log.i("fser","point: "+MACAddress);
                send(MACAddress);
            }
        };

        Runnable accept = new Runnable() {
            @Override
            public void run() {
                do {

                    if (socket != null || close()) {
                        if (connect()) {
                            executor.submit(getAddr);
                            Log.i("fser","Point 8");
                            while (socket.isConnected() && !socket.isClosed()) {
                                try {
                                    String line = "";
                                    line = input.readLine();
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
                            }
                        }
                    }
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
            Log.i("fser","Point 9");
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST,PORT));
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
            if (socket != null && socket.isClosed()) {
                socket.close();
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


}
