package com.uncle.administrator.fleamarket.DTO;

import cn.bmob.v3.BmobObject;

/**
 * @author unclewei
 */
public class CommentZan extends BmobObject {

    private String comment;
    private String targetObject;
    private String blogger;
    private String user;
    private int commentReply;

    public CommentZan() {
    }

    public CommentZan(String comment, String targetObject, String blogger, String user, int commentReply) {

        this.comment = comment;
        this.targetObject = targetObject;
        this.blogger = blogger;
        this.user = user;
        this.commentReply = commentReply;
    }

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

    public int getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(int commentReply) {
        this.commentReply = commentReply;
    }



    public String getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }





}
