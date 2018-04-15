package com.uncle.DTO;

import cn.bmob.v3.BmobObject;

/**好友表
 * @author smile
 * @project Friend
 * @date 2016-04-26
 */
//TODO 好友管理：9.1、创建好友表
public class Friend extends BmobObject {

    private ConversationDTO user;
    private ConversationDTO friendUser;

    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public ConversationDTO getUser() {
        return user;
    }

    public void setUser(ConversationDTO user) {
        this.user = user;
    }

    public ConversationDTO getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(ConversationDTO friendUser) {
        this.friendUser = friendUser;
    }
}
