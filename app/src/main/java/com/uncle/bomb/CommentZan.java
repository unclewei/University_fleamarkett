package com.uncle.bomb;

import cn.bmob.v3.BmobObject;

public class comment_zan extends BmobObject {

    private String comment;

    private String taget_object;
    private String blogger;
    private String uesr;
    private int comment_reply;

    public String getBlogger() {
        return blogger;
    }

    public void setBlogger(String blogger) {
        this.blogger = blogger;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getComment_reply() {
        return comment_reply;
    }

    public void setComment_reply(int comment_reply) {
        this.comment_reply = comment_reply;
    }



    public String getTaget_object() {
        return taget_object;
    }

    public void setTaget_object(String taget_object) {
        this.taget_object = taget_object;
    }

    public String getUesr() {
        return uesr;
    }

    public void setUesr(String uesr) {
        this.uesr = uesr;
    }





}
