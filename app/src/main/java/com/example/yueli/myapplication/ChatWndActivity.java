package com.example.yueli.myapplication;

import android.content.BroadcastReceiver;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yueli.myapplication.BroadCast.MyAutoBroadCastReceiver;
import com.example.yueli.myapplication.Service.MyService;
import com.example.yueli.myapplication.bean.myMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Util.ApplicationUtil;

public class ChatWndActivity extends AppCompatActivity {
    private ListView listView;
    private EditText sendText;
    private Button sendBtn;
    private List<myMessage> myMessageList;
    private DatagramSocket ds;
    private String group;
    private String child;
    private ChatListAdapter adapter;
    private String user;
    private MyAutoBroadCastReceiver autoBroadCastReceiver;
    private boolean flag=false;
    public void init(){
        try {
            ds=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        final ApplicationUtil appUtil=(ApplicationUtil)ChatWndActivity.this.getApplication();
        user=appUtil.getUser();
        myMessageList =new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();


        autoBroadCastReceiver=new MyAutoBroadCastReceiver();
        IntentFilter autoBroadFilter=new IntentFilter();
        autoBroadFilter.addAction("auto");
        registerReceiver(autoBroadCastReceiver,autoBroadFilter);
        autoBroadCastReceiver.setOnAutoReceiveListener(new MyAutoBroadCastReceiver.OnAutoReceiveListener() {
            @Override
            public void onReceive(String msg) {//String msg是传过来的
                Log.v("arki",msg);
                if(group.equals("Friends")) {
                    if(!getFriendMsg(child).equals(myMessageList)) {
                        myMessageList.clear();
                        myMessageList.addAll(getFriendMsg(child));
                        adapter.notifyDataSetChanged();
                        Log.v("arki",msg+" changed");
                    }
                }else if(group.equals("Groups")){
                    myMessageList.clear();
                    myMessageList.addAll(getGroupMsg(child));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        flag=true;
    }

    @Override
    protected void onDestroy() {
        if (flag) {
            unregisterReceiver(autoBroadCastReceiver);
            flag=false;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_wnd);
        /**********************获得对方ip*************************/
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        group=bundle.getString("group");
        child=bundle.getString("child");
        init();


       final ApplicationUtil appUtil=(ApplicationUtil)ChatWndActivity.this.getApplication();
        /************************************************/

        listView=(ListView)findViewById(R.id.chatMsg);
        sendText=(EditText)findViewById(R.id.msgText);
        sendBtn=(Button)findViewById(R.id.sendBtn);
       // final ApplicationUtil appUtil=(ApplicationUtil)ChatWndActivity.this.getApplication();
        if(group.equals("Friends")) {
            myMessageList.addAll(getFriendMsg(child));
        }else if(group.equals("Groups")){
           myMessageList.addAll(getGroupMsg(child));
        }
        adapter=new ChatListAdapter(ChatWndActivity.this, myMessageList);
        listView.setAdapter(adapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg=sendText.getText().toString();
                myMessage m=null;
                if(group.equals("Friends")) {
                    m = new myMessage(user, msg, true,child, null);
                    synchronized (appUtil.getMyMessageList()) {
                        appUtil.addToMsgList(m);
                    }
                    myMessageList.clear();
                    myMessageList.addAll(getFriendMsg(child));
                }else if(group.equals("Groups")){
                    m=new myMessage(user,msg,true,null,child);
                    synchronized (appUtil.getMyMessageList()) {
                        appUtil.addToMsgList(m);
                    }
                    myMessageList.clear();
                    myMessageList.addAll(getGroupMsg(child));
                }
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      sendMsg(msg);
                  }
              }).start();
               adapter.notifyDataSetChanged();
            }
        });

    }
    public List<myMessage>getFriendMsg(String friend){
        ApplicationUtil appUtil=(ApplicationUtil)ChatWndActivity.this.getApplication();
        List<myMessage>Msg=new ArrayList<>();
        for(int i = 0; i<appUtil.getMyMessageList().size(); i++){
            myMessage m=appUtil.getMyMessageList().get(i);
           if( m.getFromName().equals(friend)||m.getToName().equals(friend)){
               Msg.add(m);
           }
        }
        return Msg;
    }
    public List<myMessage>getGroupMsg(String group) {
        ApplicationUtil appUtil=(ApplicationUtil)ChatWndActivity.this.getApplication();
        List<myMessage>GroupMsg=new ArrayList<>();
        for (int i = 0; i < appUtil.getMyMessageList().size(); i++) {
            String g = appUtil.getMyMessageList().get(i).getGroup();
            if (g != null && g.equals(group)) {
                GroupMsg.add(appUtil.getMyMessageList().get(i));
            }
        }
        return GroupMsg;
    }
    public void sendMsg(String msg){
        String sendMsg=null;
        if(group.equals("Friends")){
            sendMsg="Friend-to-"+child+"-from-"+user+"-"+msg;
        }else if(group.equals("Groups")){
            sendMsg="Group-toGroup-"+child+"-from-"+user+"-"+msg;
        }
        DatagramPacket dp= null;//send
        try {
            //Server UDP Listener port 56788
            dp = new DatagramPacket(sendMsg.getBytes(),sendMsg.length(), InetAddress.getByName(ApplicationUtil.serverIP),56788);
            ds.send(dp);
            //Log.v("arki", " "+ds.getLocalPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
