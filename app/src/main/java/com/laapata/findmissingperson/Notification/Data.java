package com.laapata.findmissingperson.Notification;

public class Data {
    private String sender_Id;
    private int icon;
    private String body;
    private String title;
    private String receiver_Id;
    private String receiver_name;
    private String type;
    private long milis;


    public Data() {
    }


    public Data(String sender_Id, int icon, String body, String title, String receiver_Id, String receiver_name, String type, long milis) {
        this.sender_Id = sender_Id;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.receiver_Id = receiver_Id;
        this.receiver_name = receiver_name;
        this.type = type;
        this.milis = milis;
    }

    public String getSender_Id() {
        return sender_Id;
    }

    public void setSender_Id(String sender_Id) {
        this.sender_Id = sender_Id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiver_Id() {
        return receiver_Id;
    }

    public void setReceiver_Id(String receiver_Id) {
        this.receiver_Id = receiver_Id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMilis() {
        return milis;
    }

    public void setMilis(long milis) {
        this.milis = milis;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }
}
