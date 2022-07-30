package com.laapata.findmissingperson.ModelClasses;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String Img_message;
    private String time_stamp;
    private String type;
    private boolean isseen;


    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String img_message, String time_stamp, String type, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        Img_message = img_message;
        this.time_stamp = time_stamp;
        this.type = type;
        this.isseen = isseen;
    }



    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg_message() {
        return Img_message;
    }

    public void setImg_message(String img_message) {
        Img_message = img_message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
