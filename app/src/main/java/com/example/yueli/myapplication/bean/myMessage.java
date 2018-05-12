package com.example.yueli.myapplication.bean;

/**
 * Created by yueli on 2018/4/22.
 */


public class myMessage {
    private String fromName, message;
    private boolean isSelf;
    private String group;
    private String toName;


    public myMessage(String fromName, String message, boolean isSelf, String toName, String group) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
        this.group=group;
        this.toName=toName;
    }
    public String getToName(){return toName;}
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }
    public String getGroup(){return group;}

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

}