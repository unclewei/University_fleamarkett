package com.uncle.DTO;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

public class shopGoods extends BmobObject {
    private String title;
    private String text;
    private String price;
    private String variety;
    private String time;

    private int zanNub;
    private int pictureNub;

    private String owner;
    private String college;
    private String organization;
    private String avatar;
    private String name;
    private ArrayList<String> commentList;
    private ArrayList<String> imgFileList;

    public shopGoods() {
    }

    public shopGoods(ArrayList<String> imgFileList, String title, String text, String price, String variety,
                     int zanNub, int pictureNub, String owner, String college,
                     String organization, String avatar, String name) {
        this.imgFileList = imgFileList;
        this.title = title;
        this.text = text;
        this.price = price;
        this.variety = variety;
        this.zanNub = zanNub;
        this.pictureNub = pictureNub;
        this.owner = owner;
        this.college = college;
        this.organization = organization;
        this.avatar = avatar;
        this.name = name;
        commentList = null;
    }

    public ArrayList<String> getImgFileList() {
        return imgFileList;
    }

    public void setImgFileList(ArrayList<String> imgFileList) {
        this.imgFileList = imgFileList;
    }

    public ArrayList<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<String> commentList) {
        this.commentList = commentList;
    }

    public String getTime() {
        return getCreatedAt();
    }

    public int getPictureNub() {
        return pictureNub;
    }

    public void setPictureNub(int pictureNub) {
        this.pictureNub = pictureNub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getZanNub() {
        return zanNub;
    }

    public void setZanNub(int zanNub) {
        this.zanNub = zanNub;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
}
