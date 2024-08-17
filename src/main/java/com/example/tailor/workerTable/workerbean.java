package com.example.tailor.workerTable;

public class workerbean {

    private String wname;
    private String wmobile;
    private String waddress;
    private String spl;

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWmobile() {
        return wmobile;
    }

    public void setWmobile(String wmobile) {
        this.wmobile = wmobile;
    }

    public String getWaddress() {
        return waddress;
    }

    public void setWaddress(String waddress) {
        this.waddress = waddress;
    }

    public String getSpl() {
        return spl;
    }

    public void setSpl(String spl) {
        this.spl = spl;
    }

    public workerbean(String wname, String waddress, String wmobile, String spl) {
        this.wmobile = wmobile;
        this.wname = wname;
        this.waddress = waddress;
        this.spl = spl;
    }

    public workerbean(){

    }
}
