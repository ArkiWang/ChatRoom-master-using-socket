package com.example.yueli.myapplication.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.example.yueli.myapplication.bean.myMessage;

import java.util.List;

/**
 * Created by yueli on 2018/5/2.
 */

public class MyAutoBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       // List<myMessage> messageList;
       if(intent.getAction().equals("auto")){
        String msg=intent.getStringExtra("auto");
        //Log.v("arki",msg);
        onAutoReceiveListener.onReceive(msg);}
    }
    OnAutoReceiveListener onAutoReceiveListener;
    public interface OnAutoReceiveListener{//callback
        public void onReceive(String msg);
    }
    public void setOnAutoReceiveListener(OnAutoReceiveListener onAutoReceiveListener){
        this.onAutoReceiveListener=onAutoReceiveListener;
    }
}
