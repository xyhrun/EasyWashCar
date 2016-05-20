package com.xyh.easywashcar.model;

/**
 * Created by 向阳湖 on 2016/5/18.
 */
public class GridItem {
    private String title;
    private int imgID;

    public GridItem(String title, int imgID) {
        this.title = title;
        this.imgID = imgID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImg(int imgID) {
        this.imgID= imgID;
    }
}
