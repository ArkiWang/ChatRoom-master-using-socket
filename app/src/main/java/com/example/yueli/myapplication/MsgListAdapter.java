package com.example.yueli.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueli on 2018/4/22.
 */

public class MsgListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> groupList;
    private ArrayList<ArrayList<String>>childList;

    public MsgListAdapter(Context context,ArrayList<String> groupList,ArrayList<ArrayList<String>>childList){
        this.context=context;
        this.groupList=groupList;
        this.childList=childList;
    }


    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long)groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView=View.inflate(context,R.layout.item_group,null);
        TextView groupName=(TextView)groupView.findViewById(R.id.groupTvName);
        groupName.setText(groupList.get(groupPosition));
        TextView count=(TextView)groupView.findViewById(R.id.groupTvCount);
        count.setText(childList.get(groupPosition).size()+"人");
        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView=View.inflate(context,R.layout.item_child,null);
        TextView username=childView.findViewById(R.id.childTvUserName);
        TextView userState=childView.findViewById(R.id.childTvNet);

        username.setText(childList.get(groupPosition).get(childPosition));
        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
