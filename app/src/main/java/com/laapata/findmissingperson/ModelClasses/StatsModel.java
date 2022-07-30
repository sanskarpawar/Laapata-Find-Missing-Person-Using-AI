package com.laapata.findmissingperson.ModelClasses;

public class StatsModel {

    String year;
    String Description;
    String pushid;
    String statcreateddate;
    public StatsModel()
    {

    }

    public StatsModel(String year, String description, String pushid, String statcreateddate) {
        this.year = year;
        Description = description;
        this.pushid = pushid;
        this.statcreateddate = statcreateddate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getStatcreateddate() {
        return statcreateddate;
    }

    public void setStatcreateddate(String statcreateddate) {
        this.statcreateddate = statcreateddate;
    }

}
