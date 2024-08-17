package com.example.tailor.ordercontroller;

public class orderbean {
    //order id,mobile,dress,qty,dateof delivery, bill, worker,status -> measurements table
    int order_id;
    String mob;
    String dress;
    int qty;
    String dod;
    int bill;
    String worker;
    int st;

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDress() {
        return dress;
    }

    public void setDress(String dress) {
        this.dress = dress;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public orderbean(int order_id, String mob, String dress, int qty, String dod, int bill, String worker, int st) {
        this.order_id = order_id;
        this.mob = mob;
        this.dress = dress;
        this.qty = qty;
        this.dod = dod;
        this.bill = bill;
        this.worker = worker;
        this.st = st;
    }

    public orderbean(){

    }
}
