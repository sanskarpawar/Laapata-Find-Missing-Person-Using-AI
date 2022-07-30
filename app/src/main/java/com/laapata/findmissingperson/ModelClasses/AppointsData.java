package com.laapata.findmissingperson.ModelClasses;

public class AppointsData {

    private String idPush ;
    private String doctor_id ;
    private String patient_id ;
    private String created_date ;
    private String patient_name ;
    private String patient_phone ;
    private String patient_age ;

    private String patient_disease ;
    private String doctor_name ;
    private String doctor_clinic ;
    private String doctor_phone ;
    private String appoint_time ;
    private String appoint_day ;
    private String appoint_fee ;
    private String appoint_status ;

    private long alarm_time ;

    public AppointsData() {
    }


    public AppointsData(String idPush, String doctor_id, String patient_id, String created_date, String patient_name, String patient_phone, String patient_age, String patient_disease, String doctor_name, String doctor_clinic, String doctor_phone, String appoint_time, String appoint_day, String appoint_fee, String appoint_status, long alarm_time) {
        this.idPush = idPush;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.created_date = created_date;
        this.patient_name = patient_name;
        this.patient_phone = patient_phone;
        this.patient_age = patient_age;
        this.patient_disease = patient_disease;
        this.doctor_name = doctor_name;
        this.doctor_clinic = doctor_clinic;
        this.doctor_phone = doctor_phone;
        this.appoint_time = appoint_time;
        this.appoint_day = appoint_day;
        this.appoint_fee = appoint_fee;
        this.appoint_status = appoint_status;
        this.alarm_time = alarm_time;
    }

    public String getIdPush() {
        return idPush;
    }

    public void setIdPush(String idPush) {
        this.idPush = idPush;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_disease() {
        return patient_disease;
    }

    public void setPatient_disease(String patient_disease) {
        this.patient_disease = patient_disease;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_clinic() {
        return doctor_clinic;
    }

    public void setDoctor_clinic(String doctor_clinic) {
        this.doctor_clinic = doctor_clinic;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public String getAppoint_day() {
        return appoint_day;
    }

    public void setAppoint_day(String appoint_day) {
        this.appoint_day = appoint_day;
    }

    public String getAppoint_fee() {
        return appoint_fee;
    }

    public void setAppoint_fee(String appoint_fee) {
        this.appoint_fee = appoint_fee;
    }

    public String getAppoint_status() {
        return appoint_status;
    }

    public void setAppoint_status(String appoint_status) {
        this.appoint_status = appoint_status;
    }

    public long getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(long alarm_time) {
        this.alarm_time = alarm_time;
    }
}
