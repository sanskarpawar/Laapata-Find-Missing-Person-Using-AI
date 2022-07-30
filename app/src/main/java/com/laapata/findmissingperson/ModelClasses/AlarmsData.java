package com.laapata.findmissingperson.ModelClasses;

public class AlarmsData {

    private String idPush ;
    private String userId ;
    private String creted_date ;
    private String alarm_time ;
    private String alarm_hour ;
    private String alarm_mints ;
    private String alarm_AM_PM ;
    private String medicine_name ;
    private String alarm_status ;
    private int alarm_Num ;


    public AlarmsData() {
    }


    public AlarmsData(String idPush, String userId, String creted_date, String alarm_time, String alarm_hour, String alarm_mints, String alarm_AM_PM, String medicine_name, String alarm_status, int alarm_Num) {
        this.idPush = idPush;
        this.userId = userId;
        this.creted_date = creted_date;
        this.alarm_time = alarm_time;
        this.alarm_hour = alarm_hour;
        this.alarm_mints = alarm_mints;
        this.alarm_AM_PM = alarm_AM_PM;
        this.medicine_name = medicine_name;
        this.alarm_status = alarm_status;
        this.alarm_Num = alarm_Num;
    }

    public String getAlarm_hour() {
        return alarm_hour;
    }

    public void setAlarm_hour(String alarm_hour) {
        this.alarm_hour = alarm_hour;
    }

    public String getAlarm_mints() {
        return alarm_mints;
    }

    public void setAlarm_mints(String alarm_mints) {
        this.alarm_mints = alarm_mints;
    }

    public String getAlarm_AM_PM() {
        return alarm_AM_PM;
    }

    public void setAlarm_AM_PM(String alarm_AM_PM) {
        this.alarm_AM_PM = alarm_AM_PM;
    }

    public String getIdPush() {
        return idPush;
    }

    public void setIdPush(String idPush) {
        this.idPush = idPush;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreted_date() {
        return creted_date;
    }

    public void setCreted_date(String creted_date) {
        this.creted_date = creted_date;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public int getAlarm_Num() {
        return alarm_Num;
    }

    public void setAlarm_Num(int alarm_Num) {
        this.alarm_Num = alarm_Num;
    }

    public String getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(String alarm_status) {
        this.alarm_status = alarm_status;
    }
}
