package com.laapata.findmissingperson.ModelClasses;

public class UsersData {

    public UsersData(String idPush, String accounnt_created, String full_name, String email, String phone, String city, String address, String imgUrl, String search, String userType, String status, double latitude, double longitude) {
        this.idPush = idPush;
        this.accounnt_created = accounnt_created;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.imgUrl = imgUrl;
        this.search = search;
        this.userType = userType;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getIdPush() {
        return idPush;
    }

    public void setIdPush(String idPush) {
        this.idPush = idPush;
    }

    public String getAccounnt_created() {
        return accounnt_created;
    }

    public void setAccounnt_created(String accounnt_created) {
        this.accounnt_created = accounnt_created;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String idPush;
    private String accounnt_created;
    private String full_name;
    private String email;
    private String phone;
    private String city;
    private String address;
    private String imgUrl;
    private String search;
    private String userType;
    private String status;
    private double latitude;
    private double longitude;

    public UsersData() { }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}