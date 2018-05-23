package com.uncle.DTO;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * @author Administrator
 * @date 2017/4/15 0015
 */

public class Profile extends BmobObject {

    private String phoneNub;
    private String avatar;
    private String name;
    private String college;
    private String organization;
    private ArrayList<String> zanList;
    private ArrayList<String> commentList;
    private ArrayList<String> publicList;

    public Profile() {
    }

    public Profile(String phoneNub, String avatar, String name, String college,
                   String organization, ArrayList<String> zanList,
                   ArrayList<String> commentList, ArrayList<String> publicList) {
        this.phoneNub = phoneNub;
        this.avatar = avatar;
        this.name = name;
        this.college = college;
        this.organization = organization;
        this.zanList = zanList;
        this.commentList = commentList;
        this.publicList = publicList;
    }

    public ArrayList<String> getPublicList() {
        return publicList;
    }

    public void setPublicList(ArrayList publicList) {
        this.publicList = publicList;
    }

    public ArrayList<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList commentList) {
        this.commentList = commentList;
    }

    public ArrayList<String> getZanList() {
        return zanList;
    }

    public void setZanList(ArrayList zanList) {
        this.zanList = zanList;
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

    public String getPhoneNub() {
        return phoneNub;
    }

    public void setPhoneNub(String phoneNub) {
        this.phoneNub = phoneNub;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
