package com.ichat.netty.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by glw
 * 2018/12/8 15:32
 * 传输消息
 */
@Data
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = -3464274653567766468L;

    private String senderId;    // 发送者的用户id
    private String receiverId;  // 接收者的用户id
    private String msg;         // 聊天内容
    private String msgId;       // 用于消息的签收
}
