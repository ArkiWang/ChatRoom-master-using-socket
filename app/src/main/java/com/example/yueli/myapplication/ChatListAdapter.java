package com.example.yueli.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yueli.myapplication.bean.myMessage;

import java.util.List;

/**
 * Created by yueli on 2018/4/22.
 */

public class ChatListAdapter extends BaseAdapter {

    private Context context;
    private List<myMessage> myMessages;
    public ChatListAdapter(Context context,List<myMessage> myMessages){//
        this.context=context;
        this.myMessages = myMessages;
    }
    @Override
    public int getCount() {
        return myMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return myMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        myMessage m= myMessages.get(position);
        TextView mText;
        if(m.isSelf()){
            convertView=View.inflate(context,R.layout.list_item_msg_right,null);
            mText=(TextView)convertView.findViewById(R.id.tv_msg_right);
            mText.setText(m.getMessage());
            Log.v("arki","right "+m.getMessage());

        }else{
            convertView=View.inflate(context,R.layout.list_item_msg_left,null);
            mText=(TextView)convertView.findViewById(R.id.tv_msg_left);
            mText.setText(m.getMessage());
            Log.v("arki","left "+m.getMessage());
        }
        return convertView;
    }
}
