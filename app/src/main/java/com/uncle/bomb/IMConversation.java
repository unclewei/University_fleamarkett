package com.uncle.bomb;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class IMConversation extends BmobObject {

    private String target;//目标，哪两个用户交流，两个object的结合
    private String context;//聊天内容
    private String state;//谁说的，自己说的写自己的object
    private int name_b;//a对b聊天，b的值就加一，并通知b有消息
    private int name_a;//b对a聊天，a的值就加一，并通知a有消息
    private String conversation_target;//通知的objectid，两个object的结合

    public String getConversation_target() {
        return conversation_target;
    }

    public void setConversation_target(String conversation_target) {
        this.conversation_target = conversation_target;
    }

    public int getName_a() {
        return name_a;
    }

    public void setName_a(int name_a) {
        this.name_a = name_a;
    }

    public int getName_b() {
        return name_b;
    }

    public void setName_b(int name_b) {
        this.name_b = name_b;
    }



    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


}
