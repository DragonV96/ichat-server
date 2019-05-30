package com.ichat.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "friends_request")
public class FriendsRequest {
    /**
     * 好友请求表唯一id
     */
    @Id
    private String id;

    /**
     * 发送好友请求的目标用户id
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接收好友请求的用户id
     */
    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 请求时间
     */
    @Column(name = "request_date_time")
    private Date requestDateTime;

    /**
     * 获取好友请求表唯一id
     *
     * @return id - 好友请求表唯一id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置好友请求表唯一id
     *
     * @param id 好友请求表唯一id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发送好友请求的目标用户id
     *
     * @return send_user_id - 发送好友请求的目标用户id
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送好友请求的目标用户id
     *
     * @param sendUserId 发送好友请求的目标用户id
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接收好友请求的用户id
     *
     * @return accept_user_id - 接收好友请求的用户id
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     * 设置接收好友请求的用户id
     *
     * @param acceptUserId 接收好友请求的用户id
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     * 获取请求时间
     *
     * @return request_date_time - 请求时间
     */
    public Date getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * 设置请求时间
     *
     * @param requestDateTime 请求时间
     */
    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}