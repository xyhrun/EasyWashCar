package com.xyh.easywashcar.model;

/**
 * Created by 向阳湖 on 2016/7/23.
 */
public class MarketItem1 {
    private String name;
    private String address;
    private double distance;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    //    private String phoneNum;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
