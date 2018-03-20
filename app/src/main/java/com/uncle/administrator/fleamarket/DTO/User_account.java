package com.uncle.administrator.fleamarket.DTO;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * @author Administrator
 * @date 2017/4/15 0015
 */

public class User_account extends BmobObject {

    private String account;
    private String avatar;
    private String name;
    private String college;
    private String organization;
    private ArrayList<String> zanList;
    private ArrayList<String> commentList;
    private ArrayList<String> scanList;

    public User_account() {
    }

    public User_account( String account,
                        String avatar, String name, String college,
                        String organization, ArrayList<String> zanList,
                        ArrayList<String> commentList, ArrayList<String> scanList) {
        this.account = account;
        this.avatar = avatar;
        this.name = name;
        this.college = college;
        this.organization = organization;
        this.zanList = zanList;
        this.commentList = commentList;
        this.scanList = scanList;
    }

    public ArrayList<String> getScanList() {
        return scanList;
    }

    public void setScanList(ArrayList scanList) {
        this.scanList = scanList;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
