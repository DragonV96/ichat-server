package com.ichat.common.enums;

/**
 * Create by glw
 * 2018/12/8 17:20
 */
public enum MsgActionEnum {
    CONNECT(1, "第一次（或重连）初始化连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIEND(5, "重新拉取通讯录");

    public final Integer type;
    public final String content;

    MsgActionEnum(Integer type, String content) {
        this.content = content;
        this.type = type;
    }
}
