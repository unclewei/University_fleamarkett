package com.uncle.administrator.fleamarket.DTO;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 *
 * @author Administrator
 * @date 2017/4/15 0015
 */

public class User_account extends BmobObject {

    String account;
    String head_portrait;
    String nick_name;
    String college;
    String organization;
    ArrayList zanList;
    ArrayList commentList;


    public ArrayList getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList commentList) {
        this.commentList = commentList;
    }

    public ArrayList getZanList() {
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

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
