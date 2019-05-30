package com.ichat.pojo.vo;

/**
 *  用来对前端展示用的实体类(好友请求发送方信息)，可扩展
 */
public class FriendRequestVO {
    private String sendUserId;
    private String sendUsername;
    private String sendheadImage;
    private String sendNickname;

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendheadImage() {
        return sendheadImage;
    }

    public void setSendheadImage(String sendheadImage) {
        this.sendheadImage = sendheadImage;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }
}