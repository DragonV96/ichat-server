package com.ichat.pojo.vo;

/**
 * Create by glw
 * 2018/12/2 19:16
 * 用来对前端展示用的实体类（用户信息），可扩展
 */
public class MyFriendsVO {
    private String friendUserId;
    private String friendUsername;
    private String friendHeadImage;
    private String friendNickname;

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendHeadImage() {
        return friendHeadImage;
    }

    public void setFriendHeadImage(String friendHeadImage) {
        this.friendHeadImage = friendHeadImage;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}
