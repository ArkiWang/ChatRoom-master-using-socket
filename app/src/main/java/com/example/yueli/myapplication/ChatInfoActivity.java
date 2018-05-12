package com.example.yueli.myapplication;

import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.support.v7.widget.Toolbar;

import com.example.yueli.myapplication.Service.MyService;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Util.ApplicationUtil;

public class ChatInfoActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ArrayList<String> groupList=new ArrayList<>();
    private ArrayList<ArrayList<String>> childList=new ArrayList<ArrayList<String>>();
    private Handler loadDataHandler;
    private Toolbar toolbar;
    private MenuItem add;
    private MsgListAdapter adapter;
    private boolean flag=false;

    private Handler infoHandler;

    private void initToolbar(){
        getSupportActionBar().hide();
        toolbar.inflateMenu(R.menu.toolbar_menu);
       // add=findViewById(R.id.addFG);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addFG:
                        Intent intent=new Intent(ChatInfoActivity.this,AddActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.createG:
                        Intent intent1=new Intent(ChatInfoActivity.this,CreateGroupActivity.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("arki","info onStart");
        if(flag){
            infoHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.obj.equals("change")) {
                        adapter.notifyDataSetChanged();
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApplicationUtil appUtil = (ApplicationUtil) ChatInfoActivity.this.getApplication();
                    try {
                        appUtil.receiveData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!appUtil.getFriends().equals(childList.get(0)) || !
                            appUtil.getGroups().equals(childList.get(1))) {
                        Log.v("arki", "info onStart Changed");
                        childList.clear();
                        childList.add((ArrayList<String>)appUtil.getFriends());
                        childList.add((ArrayList<String>)appUtil.getGroups());
                        Message message = new Message();
                        message.obj = "change";
                        infoHandler.sendMessage(message);
                    }
                }
            }).start();
        }

        flag=true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);
        Intent intent=new Intent();
        intent.setClass(ChatInfoActivity.this, MyService.class);
        startService(intent);//start service receiving UDP data
        try {
            init();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        toolbar=findViewById(R.id.toolbar);
        initToolbar();
        listView=findViewById(R.id.list);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               // Toast.makeText(context,childList.get(groupPosition).get(childPosition),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ChatInfoActivity.this,ChatWndActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("group",groupList.get(groupPosition));
                bundle.putString("child",childList.get(groupPosition).get(childPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
    }
    private void init() throws SocketException, UnknownHostException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();

         loadDataHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("Finish")){
                    adapter = new MsgListAdapter(ChatInfoActivity.this, groupList, childList);
                    ExpandableListView expandableListView=(ExpandableListView)findViewById(R.id.list);
                    expandableListView.setAdapter(adapter);
                }
            }
        };

    }

    private void initData()  {
       ApplicationUtil appUtil=(ApplicationUtil)ChatInfoActivity.this.getApplication();
        try {
            appUtil.receiveData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addData("Friends",(ArrayList<String>)appUtil.getFriends());
        addData("Groups",(ArrayList<String>)appUtil.getGroups());
        if(!flag) {
            Message message = new Message();
            message.obj = "Finish";
            loadDataHandler.sendMessage(message);
        }
    }

    /**
     * 用来添加数据的方法
     */
    private void addData(String group, ArrayList<String>child) {
        groupList.add(group);
        childList.add(child);
    }
}
