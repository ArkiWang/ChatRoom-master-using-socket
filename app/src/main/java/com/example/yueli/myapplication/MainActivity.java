package com.example.yueli.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import Util.ApplicationUtil;
import io.objectbox.BoxStore;

public class MainActivity extends AppCompatActivity {
    private Button submit;
    private EditText email;
    private EditText password;
    private BoxStore boxStore;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private TextView Register;


    public boolean checkInfo(String email,String password)throws IOException{//向服务器通信
        pw.println("Login-"+email+"-"+password+"-"+socket.getLocalAddress());
        String msg=null;
        while((msg=br.readLine())!=null) {
            if (msg.equals("error"))
                return false;
            else if(msg.equals("Login succeed!")){
                return true;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
       /* StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submit=(Button)findViewById(R.id.submit);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        Register=(TextView)findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        final ApplicationUtil appUtil=(ApplicationUtil)MainActivity.this.getApplication();
        final Handler loginHandler=new Handler(){//use handler to fresh ui
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("Succeed")){
                    appUtil.setUser(email.getText().toString());
                    Intent intent = new Intent(MainActivity.this, ChatInfoActivity.class);
                    appUtil.setUser(email.getText().toString());
                    startActivity(intent);
                }
            }
        };
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Login(appUtil)) {
                            Message message=new Message();
                            message.obj="Succeed";
                            loginHandler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
    }
    public boolean Login(ApplicationUtil appUtil){
        try {
            appUtil.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = appUtil.getSocket();
        pw = appUtil.getPw();
        br = appUtil.getBr();
        try {
            if (checkInfo(email.getText().toString(), password.getText().toString())) {
                //Toast.makeText(MainActivity.this, "Login Succeed!", Toast.LENGTH_SHORT).show();
               // Log.v("arki","Login succeed");
                return true;
            } else {
                Toast.makeText(MainActivity.this, "Login Error!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


