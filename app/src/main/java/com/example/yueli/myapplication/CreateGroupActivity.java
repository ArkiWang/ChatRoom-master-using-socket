package com.example.yueli.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Util.ApplicationUtil;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText GroupName;
    private Button submit;
    private PrintWriter pw;
    private BufferedReader br;
    private void CreateGroup(String name){
        ApplicationUtil appUtil=(ApplicationUtil)CreateGroupActivity.this.getApplication();
        pw=appUtil.getPw();
        br=appUtil.getBr();
        String msg="CreateGroup-"+name;
        pw.println(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        GroupName=findViewById(R.id.GroupName);
        submit=findViewById(R.id.sendG);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CreateGroup(GroupName.getText().toString());
                    }
                }).start();
            }
        });
    }
}
