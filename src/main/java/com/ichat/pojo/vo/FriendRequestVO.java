package com.ichat.pojo.vo;

import lombok.Data;

/**
 *  用来对前端展示用的实体类(好友请求发送方信息)，可扩展
 */
@Data
public class FriendRequestVO {
    private String sendUserId;
    private String sendUsername;
    private String sendheadImage;
    private String sendNickname;
}