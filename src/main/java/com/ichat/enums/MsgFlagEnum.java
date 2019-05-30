package com.ichat.enums;

/**
 * Create by glw
 * 2018/12/10 0:30
 */
public enum MsgFlagEnum {
    MY_MSG(1, "自己发送的消息"),
    FRIEND_MSG(2, "朋友发送的消息");

    public final Integer type;
    public final String content;

    MsgFlagEnum(Integer type, String content) {
        this.content = content;
        this.type = type;
    }
}
