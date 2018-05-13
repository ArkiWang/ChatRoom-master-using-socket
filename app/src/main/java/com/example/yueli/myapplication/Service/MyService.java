package com.example.yueli.myapplication.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.yueli.myapplication.ChatInfoActivity;
import com.example.yueli.myapplication.bean.myMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Util.ApplicationUtil;

public class MyService extends Service {
   // private DatagramSocket DS;

    public MyService() throws SocketException {
        //DS=new DatagramSocket(9999);
        Log.v("arki","service init");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;//本地服务
    }



    @Override
    public void onCreate() {
        final ApplicationUtil appUtil=(ApplicationUtil)MyService.this.getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket DS=new DatagramSocket(9999);
                    while (true) {
                        DatagramPacket receiveDP=new DatagramPacket(new byte[1024],1024);
                        //接受服务器转发的消息
                        DS.receive(receiveDP);
                        String msg=new String(receiveDP.getData(),0,receiveDP.getLength());
                        // Log.v("arki","received");
                       // Log.v("arki",msg);
                        String[] str=msg.split("-");
                        myMessage m=null;
                        if(str[0].equals("Friend")){
                            m=new myMessage(str[4], str[5], false,str[2],null);
                        }else if(str[0].equals("Group")){
                            m=new myMessage(str[4], str[5], false,str[2],str[1]);
                        }
                        synchronized (appUtil.getMyMessageList()) {
                            appUtil.addToMsgList(m);
                        }
                        Intent intent=new Intent("auto");
                        intent.putExtra("auto",msg);//refresh info
                        sendBroadcast(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v("arki","destroy");
        super.onDestroy();
    }
}
