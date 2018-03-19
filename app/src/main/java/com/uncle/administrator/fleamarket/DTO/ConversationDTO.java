package com.uncle.administrator.fleamarket.DTO;

import cn.bmob.v3.BmobUser;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class ConversationDTO extends BmobUser {

    private String avatar;
    private String lastTime;
    private String lastWord;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastWord() {
        return lastWord;
    }

    public void setLastWord(String lastWord) {
        this.lastWord = lastWord;
    }

    public ConversationDTO(){}

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
