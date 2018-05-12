package com.example.yueli.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Util.ApplicationUtil;

public class AddActivity extends AppCompatActivity {
    private EditText search;
    private Button addF;
    private Button addG;
    private PrintWriter pw;
    private BufferedReader br;
    private void addFG(String chioce,String key){
        ApplicationUtil appUtil=(ApplicationUtil)AddActivity.this.getApplication();
        pw=appUtil.getPw();
        br=appUtil.getBr();
        String msg=null;
        if(chioce.equals("Friend")){
            msg="ADDF";
        }else if(chioce.equals("Group")){
            msg="ADDG";
        }
        msg+="-"+key;
        pw.println(msg);
       try {
           String result= br.readLine();
            Log.v("arki",result);
           Toast.makeText(AddActivity.this, result, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        search=findViewById(R.id.search);
        addF=findViewById(R.id.add_friend);
        addG=findViewById(R.id.add_group);
        addF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText().toString()!=null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addFG("Friend", search.getText().toString());
                        }
                    }).start();
                }else {

                }
            }
        });
        addG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText().toString()!=null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addFG("Group", search.getText().toString());
                        }
                    }).start();
                }else {

                }
            }
        });

    }
}
