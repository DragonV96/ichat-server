package com.ichat.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
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
}