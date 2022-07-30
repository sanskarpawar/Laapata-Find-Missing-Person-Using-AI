package com.laapata.findmissingperson.ModelClasses;

public class FoundPersonModel {
    private String caseid ;
    private String crid ;
    private String crname ;
    private String created_date ;
    private String mpname ;
    private String mpfathername ;
    private String mpheight ;
    private String mpage;
    private String mpplace;
    double latitude;
    double longitude;
    double platitude1;
    double plongitude1;
    private String mppermanentadress ;
    private String mpcontactnumber ;
    private String mpimage ;
    private String mpvedio ;

    public FoundPersonModel() {

    }
    public FoundPersonModel(String caseid, String crid, String crname, String created_date, String mpname, String mpfathername, String mpheight, String mpage, String mpplace, double latitude, double longitude, double platitude1, double plongitude1, String mppermanentadress, String mpcontactnumber, String mpimage, String mpvedio) {
        this.caseid = caseid;
        this.crid = crid;
        this.crname = crname;
        this.created_date = created_date;
        this.mpname = mpname;
        this.mpfathername = mpfathername;
        this.mpheight = mpheight;
        this.mpage = mpage;
        this.mpplace = mpplace;
        this.latitude = latitude;
        this.longitude = longitude;
        this.platitude1 = platitude1;
        this.plongitude1 = plongitude1;
        this.mppermanentadress = mppermanentadress;
        this.mpcontactnumber = mpcontactnumber;
        this.mpimage = mpimage;
        this.mpvedio = mpvedio;
    }

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

    public String getCrid() {
        return crid;
    }

    public void setCrid(String crid) {
        this.crid = crid;
    }

    public String getCrname() {
        return crname;
    }

    public void setCrname(String crname) {
        this.crname = crname;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMpname() {
        return mpname;
    }

    public void setMpname(String mpname) {
        this.mpname = mpname;
    }

    public String getMpfathername() {
        return mpfathername;
    }

    public void setMpfathername(String mpfathername) {
        this.mpfathername = mpfathername;
    }

    public String getMpheight() {
        return mpheight;
    }

    public void setMpheight(String mpheight) {
        this.mpheight = mpheight;
    }

    public String getMpage() {
        return mpage;
    }

    public void setMpage(String mpage) {
        this.mpage = mpage;
    }

    public String getMpplace() {
        return mpplace;
    }

    public void setMpplace(String mpplace) {
        this.mpplace = mpplace;
    }

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

    public double getPlatitude1() {
        return platitude1;
    }

    public void setPlatitude1(double platitude1) {
        this.platitude1 = platitude1;
    }

    public double getPlongitude1() {
        return plongitude1;
    }

    public void setPlongitude1(double plongitude1) {
        this.plongitude1 = plongitude1;
    }

    public String getMppermanentadress() {
        return mppermanentadress;
    }

    public void setMppermanentadress(String mppermanentadress) {
        this.mppermanentadress = mppermanentadress;
    }

    public String getMpcontactnumber() {
        return mpcontactnumber;
    }

    public void setMpcontactnumber(String mpcontactnumber) {
        this.mpcontactnumber = mpcontactnumber;
    }

    public String getMpimage() {
        return mpimage;
    }

    public void setMpimage(String mpimage) {
        this.mpimage = mpimage;
    }

    public String getMpvedio() {
        return mpvedio;
    }

    public void setMpvedio(String mpvedio) {
        this.mpvedio = mpvedio;
    }




}
