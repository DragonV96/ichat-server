package com.ichat.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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

    /**
     * 获取聊天记录表唯一id
     *
     * @return id - 聊天记录表唯一id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置聊天记录表唯一id
     *
     * @param id 聊天记录表唯一id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发送消息用户id
     *
     * @return send_user_id - 发送消息用户id
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送消息用户id
     *
     * @param sendUserId 发送消息用户id
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接收消息用户id
     *
     * @return accept_user_id - 接收消息用户id
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     * 设置接收消息用户id
     *
     * @param acceptUserId 接收消息用户id
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     * 获取消息
     *
     * @return msg - 消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取消息是否被查看
     *
     * @return sign_flag - 消息是否被查看
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置消息是否被查看
     *
     * @param signFlag 消息是否被查看
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 获取消息发送时间
     *
     * @return create_time - 消息发送时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置消息发送时间
     *
     * @param createTime 消息发送时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}