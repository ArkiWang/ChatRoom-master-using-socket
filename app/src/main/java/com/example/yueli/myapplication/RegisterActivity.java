package com.example.yueli.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import Util.ApplicationUtil;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText Rpw;
    private EditText ensure_pw;
    private Button submit;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.Register_mail);
        Rpw = (EditText) findViewById(R.id.Register_pw);
        ensure_pw = (EditText) findViewById(R.id.Ensure_pw);
        submit=(Button)findViewById(R.id.Register_sub);
        final ApplicationUtil appUtil = (ApplicationUtil) RegisterActivity.this.getApplication();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText()!=null&&Rpw.getText().toString().equals(ensure_pw.getText().toString())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appUtil.init();
                                socket = appUtil.getSocket();
                                pw = appUtil.getPw();
                                br = appUtil.getBr();
                                String msg="Register-"+email.getText().toString()+"-"+Rpw.getText().toString();
                                pw.println(msg);
                                Intent intent=new Intent(RegisterActivity.this,ChatInfoActivity.class);
                                appUtil.setUser(email.getText().toString());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(!Rpw.getText().equals(ensure_pw.getText())){
                    Toast.makeText(RegisterActivity.this,"ensure password error!",Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        });
    }
}
