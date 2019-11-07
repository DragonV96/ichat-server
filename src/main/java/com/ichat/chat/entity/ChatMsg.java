package com.ichat.chat.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "chat_msg")
public class ChatMsg {
    /**
     * 聊天记录表唯一id
     */
    @Id
    private String id;

    /**
     * 发送消息用户id
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接收消息用户id
     */
    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 消息
     */
    @Column(name = "msg")
    private String msg;

    /**
     * 消息是否被查看（1：签收  0：未签收）
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 消息发送时间
     */
    @Column(name = "create_time")
    private Date createTime;
}