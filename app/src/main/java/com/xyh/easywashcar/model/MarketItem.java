package com.xyh.easywashcar.model;

/**
 * Created by 向阳湖 on 2016/5/22.
 */
public class MarketItem {
    private String shop_name;
    private int shop_img;
    private String shop_address;
    private String mark;
    private String comment;
    private String distance;
    private String type;
    private String price;

    public MarketItem(String shop_name, int shop_img, String shop_address, String mark, String comment, String distance, String type, String price) {
        this.shop_name = shop_name;
        this.shop_img = shop_img;
        this.shop_address = shop_address;
        this.mark = mark;
        this.comment = comment;
        this.distance = distance;
        this.type = type;
        this.price = price;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getShop_img() {
        return shop_img;
    }

    public void setShop_img(int shop_img) {
        this.shop_img = shop_img;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
